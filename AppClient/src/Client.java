import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class Client {
   public static void main(String arg[]) {

      try {
         Registry reg = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostName()); //automatically get host name so user doesn't have to with command line arguments

         RemoteInterface remoteInterface = (RemoteInterface) reg.lookup("Server"); //look up reference to the remote object

         Scanner scan = new Scanner(System.in); //scanner object to scan for input
         String input;

         System.out.println("Enter from commands from list: ");
         System.out.println("'upload (name)' - upload an image with name if file is in current directory");
         System.out.println("'download (name)' - download an image with given name");
         System.out.println("'sort (values)' - sorts given values");
         System.out.println("'encrypt (text)' - encrypts given text");
         System.out.println("'decrypt (text)' - decrypts encrypted text");
         System.out.println("'triarea (height) (base)' - calculates area of a triangle given height and base");
         System.out.println("'e' - exit from program\n");

         while ( (input = scan.nextLine()) !=null) {

            String[] args = input.split(" "); //split string by spaces so we can get the command followed by the arguments
            //check for commands
            if (args[0].equalsIgnoreCase("triarea")) {
               if (args.length < 3) {
                  System.out.println( "Enter both a base and a height!");
               }
               else {
                  Area(remoteInterface, args[1], args[2]);
               }
            } else if (args[0].equalsIgnoreCase("upload")) {
               if (args.length == 1) {
                  System.out.println( "Enter a file name!");
               }
               else{
                  uploadImg(remoteInterface,args[1]);
               }
            }
            else if (args[0].equalsIgnoreCase("download")) {
               if (args.length == 1) {
                  System.out.println( "Enter a file name!");
               }
               else{
                  downloadImg(remoteInterface,args[1]);
               }
            }
            else if (args[0].equalsIgnoreCase("sort")) {
               sort(remoteInterface,args);
            }
            else if (args[0].equalsIgnoreCase("encrypt")) {
               if (args.length < 2) {
                  System.out.println( "Enter a string to encrypt!");
               }
               else{
                  encrypt(remoteInterface,args[1]);
               }
            }
            else if (args[0].equalsIgnoreCase("decrypt")) {
               if (args.length < 2) {
                  System.out.println( "Enter a string to decrypt!");
               }
               else{
                  decrypt(remoteInterface,args[1]);
               }
            }
            else if (args[0].equalsIgnoreCase("e")){
               break;
            }
            else {
               System.out.println("Invalid input!"); //if its none of the commands then it is invalid input
            }
            System.out.println("Enter another command or enter 'e' to exit");
         }
      } catch(Exception e) {
         System.err.println("Exception: "+ e.getMessage());
         e.printStackTrace();
      }
   }



   public static void downloadImg(RemoteInterface remoteInterface, String name) throws IOException {
      //check to see if file extension is PNG or JPG
      System.out.println(name);
      if (name == null){
         System.out.println("Enter a file name!");
         return;
      }
      String extension = getFileExtension(name);
      if (!(extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg"))){
         System.out.println("Only png and jpg extensions are allowed. " + extension);
         return; //if its neither png or jpg do not go further
      }
      try{
         byte[] bytedata = remoteInterface.downloadImage(name); //call remote method to get file data

         BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(name)); //creates new file if it doesn't already exist, and use buffered output to write to file

         output.write(bytedata,0,bytedata.length); //write data to file

         output.close(); //close file

         System.out.println("Downloaded " + name);
      }
      catch(Exception e) {
         System.out.println("File not found!");
      }

   }

   public static void uploadImg(RemoteInterface remoteInterface, String name) throws IOException {
      //check to see if file extension is PNG or JPG
      String extension = getFileExtension(name);
      if (!(extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg"))){
         System.out.println("Only png and jpg extensions are allowed. " + extension);
         return; //if its neither png or jpg do not go further
      }
      try {
         System.out.println("Uploading file " + name);

         File file = new File(name); //create new file instance for the file given by pathname

         byte[] bytes = Files.readAllBytes(file.toPath()); //read file bytes

         remoteInterface.uploadImage(name, bytes); //call remote method

         System.out.println("Uploaded " + name); //print out some confirmation
      }
      catch(Exception e){
         System.out.println("File not found!");
      }
   }

   public static void Area(RemoteInterface remoteInterface, String height, String base) throws IOException {
      try { //call remote method to compute the answer
         System.out.println("Area of the triangle is : " + remoteInterface.TriangleArea(Double.parseDouble(height),Double.parseDouble(base)));
      }
      catch (Exception e){
         System.out.println("Invalid input, please enter number values!");
      }
   }

   public static void sort(RemoteInterface remoteInterface, String[] array) throws RemoteException {

      if (array.length == 1){
         System.out.println("Enter values to sort!");
         return;
      }
      String[] ArrayToSort = new String[array.length-1];
      for(int i = 0; i < array.length-1;i++){
         ArrayToSort[i] = array[i+1];
      }

      ArrayToSort = remoteInterface.sort(ArrayToSort);

      System.out.println("Sorted Array:");
      for(int i = 0; i < ArrayToSort.length;i++){
         System.out.print(ArrayToSort[i] + " ");
      }
      System.out.println();

   }
   public static void encrypt(RemoteInterface remoteInterface, String input) throws Exception {
      input = remoteInterface.encrypt(input);
      System.out.println("Encrypted Message: \n" + input);

   }
   public static void decrypt(RemoteInterface remoteInterface, String input) throws Exception{
      input = remoteInterface.decrypt(input);
      System.out.println("Decrypted Message: \n" + input);

   }
   public static String getFileExtension(String file) {
      int Index = file.lastIndexOf("."); //get the index of the last period
      if (Index == -1) { //if no extension
         return "";
      }
      return file.substring(Index+1); //return the file extension name
   }

}