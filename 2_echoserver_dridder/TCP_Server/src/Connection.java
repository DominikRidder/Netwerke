import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connection extends Thread {
	private static List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());

	private Socket connectionSocket;
	private Statistic statistic;
	private DataInputStream inFromClient;
	private DataOutputStream outToClient;

	public Connection(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
		statistic = new Statistic();

		connections.add(this);
	}

	public void run() {
		String clientSentence = "";
		String answer = "";
		String command = "";

		try {
			inFromClient = new DataInputStream(connectionSocket.getInputStream());
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			outToClient.writeUTF(welcomeMessage());
			
			while (!clientSentence.equals("exit")) {
				try {
					clientSentence = inFromClient.readUTF();
				} catch (EOFException e) {
					Thread.sleep(100);
					continue;
				}

				System.out.println("FROM USER: \n\t"+clientSentence.replace("\n", "\n\t"));
				
				command = clientSentence.split("[ \n]")[0].toLowerCase();

				// Create message
				switch (command) {
				case "showstat":
					answer = getShowStat();
					break;
				case "showallstat":
					answer = getShowAllStat();
					break;
				case "broadcast":
					answer = clientSentence.substring("broadcast".length() + 1);
					for (Connection c : connections) {
						if (!this.equals(c)) {
							c.outToClient.writeUTF(answer);
						}
					}
					break;
				default:
					answer = getConnectionInfo() + clientSentence + '\n';
				}

				// Send message
				outToClient.writeUTF(answer);
				statistic.record(clientSentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		connections.remove(this);
	}

	private String welcomeMessage() {
		return "Welcome to the Server! Usefull commands:\no showstat\no showallstat\no broadcast";
	}
	
	private String getConnectionInfo() {
		return "(IP = " + connectionSocket.getInetAddress().getHostAddress() + ", Port = "
				+ connectionSocket.getPort() + ") ";
	}
	
	public String getShowStat() {
		return statistic.toString();
	}

	public String getShowAllStat() {
		StringBuilder msg = new StringBuilder();
		for (Connection c : connections) {
			msg.append(c.getConnectionInfo());
			msg.append("\n");
			msg.append(c.getShowStat());
			msg.append("\n\n\n");
		}
		return msg.toString();
	}
}
