package bsp;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
 
public class Server {
 
  public static void main(String[] args) throws Throwable {
    LocateRegistry.createRegistry(1099);
    Naming.bind("rmi://localhost/queue", new OwnStackImplementation());
  }
 
}