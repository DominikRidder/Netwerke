import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import generated.EchoMessage;
import generated.EchoMessageType;

public class ServerListener extends Thread {
	private Socket serverSocket;
	private JAXBContext jaxbContext;
	private Unmarshaller unmarshaller;

	public ServerListener(Socket serverSocket) throws JAXBException {
		this.serverSocket = serverSocket;

		jaxbContext = JAXBContext.newInstance(EchoMessage.class);
		unmarshaller = jaxbContext.createUnmarshaller();
	}

	public void run() {
		String modifiedSentence = "";
		EchoMessage msgIn = null;

		try {
			DataInputStream inFromServer = new DataInputStream(
					serverSocket.getInputStream());
			while (msgIn == null || !msgIn.getType().equals(EchoMessageType.EXIT)) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						inFromServer.readUTF().getBytes());
				msgIn = (EchoMessage) unmarshaller
						.unmarshal(byteArrayInputStream);
				modifiedSentence = msgIn.getContent();

				System.out.println("FROM SERVER: \n\t"
						+ modifiedSentence.replace("\n", "\n\t"));
			}
		} catch (Exception e) {

		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
