package server;

import java.rmi.*;
 
import client.IChatClientCallback;
 
public interface IChatServer extends Remote {
 
  /**
  * Ein neuer Benutzer betritt das System. Zusammen mit dem Benutzer erhalten
  * wir auch die Callback-Information
  */
  boolean login(String userID, IChatClientCallback receiver)
  throws RemoteException;
 
  /**
  * Benutzer verlaesst das System
  */
  void logout(String userID) throws RemoteException;
 
  /**
  * Benutzer verschickt eine Nachricht
  */
  void chat(String userID, String message) throws RemoteException;
  
  /**
   * Schickt nur einem Benutzer eine Nachricht
   */
  boolean pn(String fromID, String toID, String message) throws RemoteException;
}