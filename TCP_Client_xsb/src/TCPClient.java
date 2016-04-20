import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import generated.EchoMessage;
import generated.EchoMessageType;
import generated.ObjectFactory;

class TCPClient {

	public static void main(String[] argv) throws Exception {
		new TCPClient().start(argv[0], Integer.parseInt(argv[1]));
	}

	public void start(String ip, int port) throws Exception {
		String sentence;
		EchoMessage msgOut;
		String out;

		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext;
		Marshaller marshaller;

		try {
			jaxbContext = JAXBContext.newInstance(EchoMessage.class);
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
			return;
		}

		msgOut = objectFactory.createEchoMessage();

		Scanner inFromUser = new Scanner(System.in);
		Socket serverSocket = new Socket(ip, port);
		DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());

		ServerListener serverl = new ServerListener(serverSocket);
		serverl.start();

		System.out.println(
				"Geben sie eine nachricht ein, die an den Server geschickt werden soll. (Exit beenden den Client)");

		while (true) {
			if (inFromUser.hasNextLine()) {
				msgOut = objectFactory.createEchoMessage();
				sentence = inFromUser.nextLine();
				msgOut.setType(EchoMessageType.DEFAULT);
				String command = sentence.split("[ \n]")[0];
				for (EchoMessageType next : EchoMessageType.values()) {
					if (next.value().toLowerCase().equals(command.toLowerCase())) {
						msgOut.setType(next);
						sentence = sentence.substring(next.value().length());
						break;
					}
				}

				msgOut.setContent(sentence);
			} else {
				if (serverSocket.isClosed()) {
					break;
				} else {
					Thread.sleep(30);
					continue;
				}
			}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			marshaller.marshal(msgOut, byteArrayOutputStream);
			out = new String(byteArrayOutputStream.toByteArray());
			outToServer.writeUTF(out);
		}

		inFromUser.close();
	}
}