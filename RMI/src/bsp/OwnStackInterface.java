package bsp;
import java.rmi.Remote;
import java.rmi.RemoteException;
 
public interface OwnStackInterface extends Remote {
  void push(Integer value) throws RemoteException;
 
  Integer pop() throws RemoteException;
 
}