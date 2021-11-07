
import java.io.*;
import java.nio.file.Files;
import java.rmi.*;
import java.rmi.server.RemoteServer;
import java.util.Arrays;
import java.util.Base64;


public class Services extends RemoteServer implements RemoteInterface {

   private String name;

   public Services(String s) throws RemoteException {
      super();
      name = s;
   }

   public byte[] downloadImage(String name){
      try {
         File file = new File(name); //create new file instance for the file given by pathname

         byte[] bytes = Files.readAllBytes(file.toPath()); //read file bytes

         return(bytes); //return byte data
      } catch(Exception e){
         System.out.println("File not found");
         return(null); //if there is an exception the file was not found, so return null
      }
   }


   public void uploadImage(String name, byte[] bytedata) throws RemoteException {
      try {

         BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(name)); //creates new file if it doesn't already exist, and use buffered output to write to file

         output.write(bytedata, 0, bytedata.length); //write data to file

         output.close(); //close file

      }
      catch(Exception e){

      }

   }




   public Double TriangleArea(Double Height, Double Base) throws RemoteException {
      try{
         return (Base*Height/2d);
      }
      catch(Exception e){

      }
      return null;
   }
   public String[] sort(String[] Unsorted) throws RemoteException{

      Arrays.sort(Unsorted);

      return Unsorted;
   }


   public String encrypt(String input) throws  Exception {
      byte[] encryptArray = Base64.getEncoder().encode(input.getBytes());

      return new String(encryptArray,"UTF-8");
   }


   public String decrypt(String input) throws Exception {
      byte[] encryptArray = Base64.getDecoder().decode(input.getBytes());

      return new String(encryptArray,"UTF-8");
   }


}