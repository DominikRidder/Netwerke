import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {

	public static void main(String[] argv) throws Exception {
		startclient();
	}

	public static void startclient() throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(13337);
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			Connection connect = new Connection(connectionSocket);
			connect.start();
		}
	}

}