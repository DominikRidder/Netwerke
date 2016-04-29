package generated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Main {
	
	public static void main(String[] args) throws JAXBException, FileNotFoundException {
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext = JAXBContext.newInstance(Beispielobjekt.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		Beispielobjekt beispielobjekt = objectFactory.createBeispielobjekt();
		beispielobjekt.setData1("hallo");
		beispielobjekt.setData2(1337);
		beispielobjekt.setType(BeispielAttribut.TYPE_1);
		
		marshaller.marshal(beispielobjekt, System.out);
		marshaller.marshal(beispielobjekt, new FileOutputStream(new File("test.xml")));
	}
}
