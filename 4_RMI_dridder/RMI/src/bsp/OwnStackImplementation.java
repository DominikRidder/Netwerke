package bsp;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;
 
public class OwnStackImplementation extends UnicastRemoteObject implements OwnStackInterface {
 
  private static final long serialVersionUID = 1L;
 
  private Stack<Integer> stack = new Stack<Integer>();
 
  protected OwnStackImplementation() throws RemoteException {
    super();
  }
 
  @Override
  public void push(Integer value) throws RemoteException {
    stack.push(value);
  }
 
  @Override
  public Integer pop() throws RemoteException {
    return stack.pop();
  }
 
}