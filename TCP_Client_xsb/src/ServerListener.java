import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import generated.EchoMessage;
import generated.ObjectFactory;

public class ServerListener extends Thread {
	private Socket serverSocket;

	public ServerListener(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void run() {
		String modifiedSentence = "";
		EchoMessage msgIn;

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext;
		Unmarshaller unmarshaller;

		try {
			jaxbContext = JAXBContext.newInstance(EchoMessage.class);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			DataInputStream inFromServer = new DataInputStream(serverSocket.getInputStream());
			while (!modifiedSentence.contains("EXIT")) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inFromServer.readUTF().getBytes());
				msgIn = (EchoMessage) unmarshaller.unmarshal(byteArrayInputStream);
				modifiedSentence = msgIn.getContent(); 
				System.out.println("FROM SERVER: " + modifiedSentence);
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
