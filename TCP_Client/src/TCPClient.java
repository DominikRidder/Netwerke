
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class TCPClient {

	boolean alreadyrunning = false;

	public static void main(String[] argv) throws Exception {
		TCPClient client = new TCPClient();
		client.start(argv[0], Integer.parseInt(argv[1]));
	}

	public void start(String ip, int port) {
		String sentence = "";
		Socket serverSocket = null;
		DataOutputStream outToServer = null;

		if (alreadyrunning) {
			System.out.println("The Client is already started.");
			return;
		}

		try (Scanner inFromUser = new Scanner(System.in)) {
			alreadyrunning = true;
			System.out.println("Neuen Client gestartet");
			
			serverSocket = new Socket(ip, port);
			outToServer = new DataOutputStream(serverSocket.getOutputStream());

			ServerListener serverl = new ServerListener(serverSocket);
			serverl.start();

			while (!sentence.toLowerCase().contains("exit")) { // not the wanted solution
				sentence = inFromUser.nextLine();

				outToServer.writeUTF(sentence);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Client beendet");
			alreadyrunning = false;
		}
	}
}