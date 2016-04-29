package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClientCallback extends Remote {

	/**
	 * Ein Benutzer hat eine Nachricht an den Server geschickt
	 */
	void receiveChat(String userID, String message) throws RemoteException;

	/**
	 * Ein neuer Benutzer hat sich beim Server angemeldet
	 */
	void receiveUserLogin(String userID, Object[] users) throws RemoteException;

	/**
	 * Ein Benutzer hat sich beim Server abgemeldet
	 */
	void receiveUserLogout(String userID, Object[] users) throws RemoteException;

	/**
	 * Ein Benutzer schreibt eine Nachricht an nur einen Benutzer gesendet
	 */
	void whisper(String fromID, String message) throws RemoteException;
}