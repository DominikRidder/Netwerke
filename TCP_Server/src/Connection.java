import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connection extends Thread {
	static List<Statistic> allstatistic = Collections.synchronizedList(new ArrayList<Statistic>());

	private Socket connectionSocket;
	private Statistic statistic;

	public Connection(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
		statistic = new Statistic();

		allstatistic.add(statistic);
	}

	public void run() {
		String clientSentence = "";
		String answer = "";
		String trailer = null;

		try {
			DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			while (clientSentence.equals("exit")) {
				try {
					clientSentence = inFromClient.readUTF();
				} catch (EOFException e) {
					Thread.sleep(100);
					continue;
				}
				
				switch(clientSentence){
				case "showstat":
					answer = getShowStat();
					break;
				case "showallstat":
					answer = getShowAllStat();
					break;
			    default:
					trailer = "(IP = " + connectionSocket.getInetAddress().getHostAddress() + ", Port = "
							+ connectionSocket.getPort() + ") ";
					answer = trailer+clientSentence.toUpperCase() + '\n';
			    	break;
				}

				
				outToClient.writeUTF(answer);
				statistic.record(clientSentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getShowStat() {
		return statistic.toString();
	}
	
	public String getShowAllStat() {
		StringBuilder msg = new StringBuilder();
		for (Statistic stat : allstatistic) {
			msg.append("IP: "+connectionSocket.getInetAddress().getHostAddress());
			msg.append("\n");
			msg.append("Port: "+connectionSocket.getPort());
			msg.append("\n");
			msg.append(stat);
			msg.append("\n\n\n");
		}
		return msg.toString();
	}
}
