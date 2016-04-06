import java.io.DataInputStream;
import java.net.Socket;

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
			serverSocket.close();
		} catch (Exception e) {

		}
	}
}
