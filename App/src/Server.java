import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
   public static void main(String argv[]) {
      if(System.getSecurityManager() == null) {
         System.setSecurityManager(new SecurityManager()); //create new security manager if it does not exist already
      }
      try {
         RemoteInterface fi = (RemoteInterface) UnicastRemoteObject.exportObject(new Services("Server"), 0); //export services object

         Registry reg = LocateRegistry.getRegistry(); // get registry

         reg.rebind("Server",fi); //bind registry with name "Server"

         System.out.println("Server is running...");


      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}