package networking;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ServerListener extends Thread {
	private Socket serverSocket;
	private JAXBContext jaxbContext;
	private Unmarshaller unmarshaller;


	public ServerListener(Socket serverSocket) throws JAXBException {
		this.serverSocket = serverSocket;
//
//		jaxbContext = JAXBContext.newInstance(EchoMessage.class);
//		unmarshaller = jaxbContext.createUnmarshaller();
	}

	public void run() {
		String modifiedSentence = "";
//		EchoMessage msgIn = null;

		try {
			UTFInputStream inFromServer = new UTFInputStream(
					serverSocket.getInputStream());
			while (!serverSocket.isClosed()) {
				String infromServer = inFromServer.readUTF8();
				System.out.println(infromServer);
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(infromServer.getBytes());
				//switch(){}
//				msgIn = (EchoMessage) unmarshaller
//						.unmarshal(byteArrayInputStream);

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