import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connection extends Thread {
	static List<Connection> connections = Collections
			.synchronizedList(new ArrayList<Connection>());

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
		String trailer = null;

		try {
			inFromClient = new DataInputStream(
					connectionSocket.getInputStream());
			outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			while (!clientSentence.equals("exit")) {
				try {
					clientSentence = inFromClient.readUTF();
				} catch (EOFException e) {
					Thread.sleep(100);
					continue;
				}

				switch (clientSentence) {
				case "showstat":
					answer = getShowStat();
					break;
				case "showallstat":
					answer = getShowAllStat();
					break;
				default:
					if (clientSentence.startsWith("broadcast")) {
						answer = clientSentence.substring("broadcast".length());
						for (Connection c : connections) {
							if (!this.equals(c)) {
								c.outToClient.writeUTF(answer);
							}
						}
					} else {
						trailer = "(IP = "
								+ connectionSocket.getInetAddress()
										.getHostAddress() + ", Port = "
								+ connectionSocket.getPort() + ") ";
						answer = trailer + clientSentence.toUpperCase() + '\n';
					}
					break;
				}

				outToClient.writeUTF(answer);
				statistic.record(clientSentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		connections.remove(this);
	}

	public String getShowStat() {
		return statistic.toString();
	}

	public String getShowAllStat() {
		StringBuilder msg = new StringBuilder();
		for (Connection c : connections) {
			msg.append("IP: "
					+ c.connectionSocket.getInetAddress().getHostAddress());
			msg.append("\n");
			msg.append("Port: " + c.connectionSocket.getPort());
			msg.append("\n");
			msg.append(c.getShowStat());
			msg.append("\n\n\n");
		}
		return msg.toString();
	}
}
