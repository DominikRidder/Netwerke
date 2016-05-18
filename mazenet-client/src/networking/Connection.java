package networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import generated.LoginMessageType;
import generated.ObjectFactory;

public class Connection {
	
	int id;
	
	public static void main(String[] agrs) {
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext;
		Marshaller marshaller = null;
		Socket serverSocket = null;
		
		try {
			jaxbContext = JAXBContext.newInstance(LoginMessageType.class);
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		try {
			serverSocket = new Socket("localhost", 5123);
			
			UTFOutputStream outToServer = new UTFOutputStream(
					serverSocket.getOutputStream());
			LoginMessageType loginMsg = objectFactory.createLoginMessageType();
			loginMsg.setName("Zall");
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			marshaller.marshal(loginMsg, byteArrayOutputStream);
			String out = new String(byteArrayOutputStream.toByteArray());
			outToServer.writeUTF8(out);
			
			ServerListener serverListener = new ServerListener(serverSocket);
			serverListener.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		
	}

	
	
}
