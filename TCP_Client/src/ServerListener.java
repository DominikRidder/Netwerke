import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener extends Thread {
	private Socket serverSocket;

	public ServerListener(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void run() {
		String modifiedSentence = "";

		try {
			DataInputStream inFromServer = new DataInputStream(serverSocket.getInputStream());
			while (!modifiedSentence.contains("EXIT")) {
				modifiedSentence = inFromServer.readUTF();
				System.out.println("FROM SERVER: " + modifiedSentence);
			}
		} catch (Exception e) {

		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
