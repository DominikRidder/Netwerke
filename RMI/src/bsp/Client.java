package bsp;
import java.rmi.Naming;
 
public class Client {
 
  public static void main(String[] args) throws Throwable {
    OwnStackInterface ownStack = (OwnStackInterface) Naming.lookup("rmi://localhost/queue");
    Integer integer = new Integer(42);
    ownStack.push(integer);
    System.out.println(ownStack.pop());
  }
}