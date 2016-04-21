import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import generated.EchoMessage;
import generated.EchoMessageType;
import generated.ObjectFactory;

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
		EchoMessageType command;
		String trailer = null;
		EchoMessage msgIn;
		EchoMessage msgOut;
		boolean exitrequest = false;

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext;
		Marshaller marshaller;
		Unmarshaller unmarshaller;

		try {
			jaxbContext = JAXBContext.newInstance(EchoMessage.class);
			marshaller = jaxbContext.createMarshaller();
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
			return;
		}

		try {
			inFromClient = new DataInputStream(connectionSocket.getInputStream());
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			while (!exitrequest) {
				msgOut = objectFactory.createEchoMessage();
				msgOut.setSender("IP = " + connectionSocket.getInetAddress().getHostAddress() + "\nPort = "
						+ connectionSocket.getPort());
				
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inFromClient.readUTF().getBytes());
				msgIn = (EchoMessage) unmarshaller.unmarshal(byteArrayInputStream);
				clientSentence = msgIn.getContent();
				msgOut.setType(msgIn.getType());

				// Create message
				command = msgIn.getType();
				switch (command) {
				case SHOWSTAT:
					answer = getShowStat();
					break;
				case SHOWALLSTAT:
					answer = getShowAllStat();
					break;
				case BROADCAST:
					answer = clientSentence.substring("broadcast".length() + 1);
					for (Connection c : connections) {
						if (!this.equals(c)) {
							c.outToClient.writeUTF(answer);
						}
					}
					break;
				case EXIT:
					exitrequest = true;
					break;
				case SHUTDOWN:
					// ugly right now
					System.exit(1);
					break;
				default:
					trailer = "(IP = " + connectionSocket.getInetAddress().getHostAddress() + ", Port = "
							+ connectionSocket.getPort() + ") ";
					answer = trailer + clientSentence.toUpperCase() + '\n';
					break;
				}

				// Send message
				msgOut.setContent(answer);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				marshaller.marshal(msgOut, byteArrayOutputStream);
				answer = new String(byteArrayOutputStream.toByteArray());
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
			msg.append("IP: " + c.connectionSocket.getInetAddress().getHostAddress());
			msg.append("\n");
			msg.append("Port: " + c.connectionSocket.getPort());
			msg.append("\n");
			msg.append(c.getShowStat());
			msg.append("\n\n\n");
		}
		return msg.toString();
	}
}
