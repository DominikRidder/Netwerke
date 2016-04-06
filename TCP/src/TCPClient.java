import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

class TCPClient {

	public static void main(String[] argv) throws Exception {
		start(argv[0], Integer.parseInt(argv[1]));
	}

	public static void start(String ip, int port) throws Exception {
		String sentence;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket serverSocket = new Socket(ip, port);
		DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());

		ServerListener serverl = new ServerListener(serverSocket);
		serverl.start();

		while (true) {
			sentence = inFromUser.readLine();
			outToServer.writeUTF(sentence);
			if (sentence.equals("exit")) {
				break;
			}
		}
	}
}