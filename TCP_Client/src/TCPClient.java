import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

class TCPClient {
	
	public static void main(String[] argv) throws Exception {
		new TCPClient().start(argv[0], Integer.parseInt(argv[1]));
	}

	public void start(String ip, int port) throws Exception {
		String sentence;

		Scanner inFromUser = new Scanner(System.in);
		Socket serverSocket = new Socket(ip, port);
		DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());

		ServerListener serverl = new ServerListener(serverSocket);
		serverl.start();

		System.out.println("Geben sie eine nachricht ein, die an den Server geschickt werden soll. (Exit beenden den Client)");
		
		while (true) {
			System.out.println("start loop");
			if (inFromUser.hasNextLine()){
				sentence = inFromUser.nextLine();
			}else{
				if(serverSocket.isClosed()){
					break;
				}else {
					System.out.println("wait");
					Thread.sleep(30);
					continue;
				}
			}

			
			outToServer.writeUTF(sentence);
		}
		
		inFromUser.close();
		System.out.println("client dead");
	}
}