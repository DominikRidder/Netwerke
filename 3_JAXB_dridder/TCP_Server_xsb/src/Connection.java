import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	private static List<Connection> connections = Collections
			.synchronizedList(new ArrayList<Connection>());

	// For the Connection
	private String sender;
	private Statistic statistic;
	private Socket connectionSocket;
	private DataInputStream inFromClient;
	private DataOutputStream outToClient;

	// JAXB content
	private final ObjectFactory objectFactory = new ObjectFactory();
	private JAXBContext jaxbContext;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	public Connection(Socket connectionSocket) throws JAXBException {
		this.connectionSocket = connectionSocket;
		this.statistic = new Statistic();
		this.jaxbContext = JAXBContext.newInstance(EchoMessage.class);
		this.marshaller = jaxbContext.createMarshaller();
		this.unmarshaller = jaxbContext.createUnmarshaller();
		this.sender = "(IP = "
				+ connectionSocket.getInetAddress().getHostAddress()
				+ "\nPort = " + connectionSocket.getPort()+")\n";

		Connection.connections.add(this);
	}

	public void run() {
		String clientSentence = "";
		String answer = "";
		EchoMessageType command;
		EchoMessage msgIn;
		boolean exitrequest = false;
		boolean sendMessage = true;

		try {
			inFromClient = new DataInputStream(
					connectionSocket.getInputStream());
			outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			while (!exitrequest) {
				if (inFromClient.available() > 0) { // Prevents EOFException by inFromClient
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							inFromClient.readUTF().getBytes());
					msgIn = (EchoMessage) unmarshaller
							.unmarshal(byteArrayInputStream);
					clientSentence = msgIn.getContent();
				} else {
					Thread.sleep(100);
					continue;
				}

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
					broadcast(msgIn.getContent(), msgIn.getType());
					sendMessage = false;
					break;
				case EXIT:
					exitrequest = true;
					break;
				case SHUTDOWN:
					// ugly right now
					sendEchoMessage(answer, EchoMessageType.EXIT, getSender(),
							outToClient);
					System.exit(1);
					break;
				default:
					answer = getSender() + clientSentence.toUpperCase() + '\n';
					break;
				}

				// Send message
				if (sendMessage) {
					sendEchoMessage(answer, msgIn.getType(), getSender(),
							outToClient);
				} else {
					sendMessage = true;
				}

				statistic.record(clientSentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Connection closed");
		connections.remove(this);
	}

	/**
	 * Sends a EchoMessage to every Client.
	 */
	private void broadcast(String content, EchoMessageType type)
			throws IOException, JAXBException {
		for (Connection c : connections) {
			sendEchoMessage(content, type, getSender(), c.outToClient);
		}
	}

	/**
	 * Sends a EchoMessage with the given content and type to the given
	 * OutputStream.
	 */
	private void sendEchoMessage(String content, EchoMessageType type,
			String sender, DataOutputStream outToClient) throws IOException,
			JAXBException {
		String answer;

		EchoMessage msgOut = objectFactory.createEchoMessage();
		msgOut.setContent(content);
		msgOut.setType(type);
		msgOut.setSender(sender);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(msgOut, byteArrayOutputStream);
		answer = new String(byteArrayOutputStream.toByteArray());
		outToClient.writeUTF(answer);
	}

	// GETTER

	public String getShowStat() {
		return statistic.toString();
	}

	public String getShowAllStat() {
		StringBuilder msg = new StringBuilder();
		for (Connection c : connections) {
			msg.append(c.getSender());
			msg.append(c.getShowStat());
			msg.append("\n\n\n");
		}
		return msg.toString();
	}

	private String getSender() {
		return sender;
	}
}
