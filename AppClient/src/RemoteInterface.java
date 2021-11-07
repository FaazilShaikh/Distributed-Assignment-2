import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
   public Double TriangleArea(Double Height, Double Base) throws RemoteException;
   public byte[] downloadImage(String fileName) throws RemoteException;
   public void uploadImage(String fileName, byte[] content) throws RemoteException;
   public String[] sort(String[] array) throws RemoteException;
   public String encrypt(String str) throws Exception;
   public String decrypt(String str) throws Exception;
}