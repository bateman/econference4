import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.lib.translation.IQuery;
import net.sf.okapi.lib.translation.QueryResult;

import org.apertium.api.ApertiumXMLRPCClient;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.csvreader.*;

public class Main {

	public static String makeXML(List<Utterance> uts) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();

		Document doc = impl.createDocument(null, null, null);
		Element root = doc.createElement("meeting");
		doc.appendChild(root);

		for (Utterance u : uts) {

			Element un = doc.createElement("utterance");
			root.appendChild(un);

			un.setAttribute("turn", u.getTurn().toString());
			un.setAttribute("who", u.getWho());
			un.setAttribute("role", u.getRole().toString());
			//un.setAttribute("utterance", u.getUtterance());
			un.setAttribute("category", u.getCategory());
			
			un.appendChild(doc.createTextNode(u.getUtterance()));
		}
		
        DOMSource domSource = new DOMSource(doc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        java.io.StringWriter sw = new java.io.StringWriter();
        StreamResult sr = new StreamResult(sw);
        
        transformer.transform(domSource, sr);
        
        String xml = sw.toString();
        
        return xml;
	}
	
	public static List<Utterance> readUtterances(String path)
			throws ParserConfigurationException, SAXException, IOException {
		
		List<Utterance> ret = new LinkedList<Utterance>();

		File file = new File(path);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		doc.getDocumentElement().normalize();

		NodeList utteranceNodes = doc.getElementsByTagName("utterance");
		
		for (int i = 0; i < utteranceNodes.getLength(); ++i) {
			Node utteranceNode = utteranceNodes.item(i);
			
			NamedNodeMap attributes = utteranceNode.getAttributes();
			
			Integer turn = Integer.decode(attributes.getNamedItem("turn").getNodeValue());
			
			String who = attributes.getNamedItem("who").getNodeValue();

			Utterance.Role role = Utterance.Role.OTHER;
			
			if (attributes.getNamedItem("role").equals("Client"))
				role = Utterance.Role.CLIENT;
			else if (attributes.getNamedItem("role").equals("Developer"))
				role = Utterance.Role.DEVELOPER;
			
			String utterance = utteranceNode.getTextContent();
			
			String category = attributes.getNamedItem("category").getNodeValue();
			
			Utterance u = new Utterance(turn, who, role, utterance, category);
			
			ret.add(u);
		}
		
		return ret;
	}
	
	public static List<Utterance> readCSV(String path) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
        CsvReader csvReader = new CsvReader(isr, ';');
        
        List<Utterance> uts = new LinkedList<Utterance>();
        
        while (csvReader.readRecord()) {
        	
            Integer turn = 0;
            try {
            	turn = Integer.parseInt(csvReader.get(0));
            } catch (Exception e) {
            	//
            }
            
            String who = csvReader.get(1);
            Utterance.Role role = Utterance.Role.OTHER;
            if (csvReader.get(2).equals("client")) {
            	 role = Utterance.Role.CLIENT;
            } else if (csvReader.get(2).equals("dev")) {
            	 role = Utterance.Role.DEVELOPER;
            }
            
            String utterance = csvReader.get(3);
            String category = csvReader.get(4);
            
            if (turn > 0) {
            	Utterance u = new Utterance(turn, who, role, utterance, category);
            	uts.add(u);
            }
        }
        
        csvReader.close();
        
        return uts;
	}
	
	public static void main(String[] args) throws Exception {
		
		List<Utterance> orig = readCSV("log.csv");
		
		BufferedWriter out = new BufferedWriter(new FileWriter("log.xml"));
		out.write(makeXML(orig));
		out.close();
		
		List<Utterance> utterances = readUtterances("log.xml");
		
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://www.neuralnoise.com:6173/RPC2"));
		IQuery q = new GoogleMTConnector();
		
		q.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));
		q.open();
		
		List<Utterance> utterancesApertium = new LinkedList<Utterance>();
		List<Utterance> utterancesGoogle = new LinkedList<Utterance>();
		
		for (Utterance u : utterances) {
			
			System.out.println("Translating: " + u.getUtterance());
			
			String tradApertium = a.translate(u.getUtterance(), "en", "it").get("translation");
			String tradGoogle = "";
			int hits = q.query(u.getUtterance());
			
			System.out.println("Hits: " + hits);
			
			while (q.hasNext()) {
				QueryResult r = q.next();
				tradGoogle = r.target.toString();
				System.out.println("Hit: " + tradGoogle);
			}
			
			Utterance ua = u.clona();
			ua.setUtterance(tradApertium);
			
			Utterance ug = u.clona();
			ug.setUtterance(tradGoogle);
			
			utterancesApertium.add(ua);
			utterancesGoogle.add(ug);
		}
		
		out = new BufferedWriter(new FileWriter("log-apertium.xml"));
		out.write(makeXML(utterancesApertium));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-google.xml"));
		out.write(makeXML(utterancesGoogle));
		out.close();
	}

}
