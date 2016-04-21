import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {

	public static void main(String[] argv) {
		startServer();
	}

	public static void startServer() {
		boolean started = false;
		
		try (ServerSocket welcomeSocket = new ServerSocket(24800)) {
			started = true;
			System.out.println("Server wurde gestartet");
			
			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				Connection connect = new Connection(connectionSocket);
				connect.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (started) {
				System.out.println("Server wurde beendet");
			}
		}
	}
}