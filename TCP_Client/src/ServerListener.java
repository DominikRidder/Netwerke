import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerListener extends Thread {
	private Socket serverSocket;

	public ServerListener(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void run() {
		String modifiedSentence = "";

		System.out.println("ServerListener gestarted");
		
		try {
			DataInputStream inFromServer = new DataInputStream(serverSocket.getInputStream());
			while (!modifiedSentence.toLowerCase().contains("exit")) {
				modifiedSentence = inFromServer.readUTF();
				System.out.println("FROM SERVER: \n\t" + modifiedSentence.replace("\n", "\n\t"));
			}
		} catch (Exception e) {

		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("ServerListener beendet");
	}
}
