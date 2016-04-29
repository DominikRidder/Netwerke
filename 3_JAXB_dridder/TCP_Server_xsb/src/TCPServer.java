import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {

	public static void main(String[] argv) throws Exception {
		startclient();
	}

	public static void startclient() throws Exception {
		try (ServerSocket welcomeSocket = new ServerSocket(24801)) {
			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				Connection connect = new Connection(connectionSocket);
				connect.start();
			}
		}
	}

}