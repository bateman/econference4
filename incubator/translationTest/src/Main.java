import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;
import net.sf.okapi.lib.translation.QueryResult;

import org.apertium.api.ApertiumXMLRPCClient;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.csvreader.*;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

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
			
			if (u.getRole() != null)
				un.setAttribute("role", u.getRole().toString());
			//un.setAttribute("utterance", u.getUtterance());
			
			if (u.getCategory() != null)
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

			Integer turn = Integer.decode(attributes.getNamedItem("turn")
					.getNodeValue());

			String who = attributes.getNamedItem("who").getNodeValue();

			Utterance.Role role = null;

			if (attributes.getNamedItem("role") != null) {
				if (attributes.getNamedItem("role").equals("Client"))
					role = Utterance.Role.CLIENT;
				else if (attributes.getNamedItem("role").equals("Developer"))
					role = Utterance.Role.DEVELOPER;
				else
					role = Utterance.Role.OTHER;
			}

			String utterance = utteranceNode.getTextContent();

			String category = null;
			if (attributes.getNamedItem("category") != null) {
				category = attributes.getNamedItem("category").getNodeValue();
			}
			Utterance u = new Utterance(turn, who, role, utterance, category);

			ret.add(u);
		}
		
		return ret;
	}
	
	public static List<Utterance> readCSVnew(String path) throws IOException {
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

            
            String utterance = csvReader.get(2);
            
            if (turn > 0) {
            	Utterance u = new Utterance(turn, who, null, utterance, null);
            	uts.add(u);
            }
        }
        
        csvReader.close();
        
        return uts;
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
	private static String _translate(String text, IQuery q) throws InterruptedException {
		String ret = text;
		
		if (q instanceof MicrosoftMTConnector) {
			net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
			p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
			q.setParameters(p);
		}
		
		q.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));
		
		int tries = 2;
		int hits = 0;
		
		do {
			try {
				if (hits == 0) {
					Thread.sleep(100);
				}
				hits = q.query(text);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep(5000);
			}
		} while (--tries > 0 && hits == 0);
		
		
		if (q.hasNext()) {
			ret = q.next().target.toString();
		}
		
		return ret;
	}

	
	public static void main(String[] args) throws Exception {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://localhost:6173/RPC2"));

		IQuery qm = new MicrosoftMTConnector();
		IQuery qg = new GoogleMTConnector();
		
		qm.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));
		qg.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));

		qm.open();
		qg.open();
		
		for (int i = 1; i <= 5; ++i) {
			List<Utterance> orig = readCSVnew("testset/testset_log_" + i + ".csv");

			BufferedWriter out = new BufferedWriter(new FileWriter("testset/testset_log " + i + ".xml"));
			out.write(makeXML(orig));
			out.close();

			List<Utterance> utterances = readUtterances("testset/testset_log " + i	+ ".xml");

			List<Utterance> newutterancesa = new LinkedList<Utterance>();
			List<Utterance> newutterancesm = new LinkedList<Utterance>();
			List<Utterance> newutterancesg = new LinkedList<Utterance>();
			
			for (Utterance u : utterances) {
				Utterance nua = u.clona();
				Utterance num = u.clona();
				Utterance nug = u.clona();
				
				System.out.println(u.getUtterance());
				
				nua.setUtterance(a.translate(u.getUtterance(), "en", "it").get("translation"));
				num.setUtterance(_translate(u.getUtterance(), qm));
				nug.setUtterance(_translate(u.getUtterance(), qg));
				
				newutterancesa.add(nua);
				newutterancesm.add(num);
				newutterancesg.add(nug);
			}

			out = new BufferedWriter(new FileWriter("testset/testset_log " + i	+ ".trans.apertium.xml"));
			out.write(makeXML(newutterancesa));
			out.close();
			
			out = new BufferedWriter(new FileWriter("testset/testset_log " + i	+ ".trans.microsoft.xml"));
			out.write(makeXML(newutterancesm));
			out.close();
			
			out = new BufferedWriter(new FileWriter("testset/testset_log " + i	+ ".trans.google.xml"));
			out.write(makeXML(newutterancesg));
			out.close();
		}
	}
	
	public static void _main(String[] args) throws Exception {
		
		List<Utterance> orig = readCSV("log.csv");
		
		BufferedWriter out = new BufferedWriter(new FileWriter("log.xml"));
		out.write(makeXML(orig));
		out.close();
		
		List<Utterance> utterances = readUtterances("log.xml");
		
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://www.neuralnoise.com:6173/RPC2"));
		
		//IQuery qes = new GoogleMTConnector();
		//IQuery qit = new GoogleMTConnector();
		
		//qes.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("es"));
		//qit.setLanguages(LocaleId.fromString("en"), LocaleId.fromString("it"));

		//qes.open();
		//qit.open();
		
		Translate.setHttpReferrer("http://www.neuralnoise.com");
		
		List<Utterance> utterancesApertiumEs = new LinkedList<Utterance>();
		List<Utterance> utterancesApertiumIt = new LinkedList<Utterance>();
		
		List<Utterance> utterancesGoogleEs = new LinkedList<Utterance>();
		List<Utterance> utterancesGoogleIt = new LinkedList<Utterance>();
		
		for (Utterance u : utterances) {
			
			System.out.println("Translating: " + u.getUtterance());
			
			String tradApertiumEs = a.translate(u.getUtterance(), "en", "es").get("translation");
			System.out.println("Apertium EN -> ES: " + tradApertiumEs);
			String tradApertiumIt = a.translate(u.getUtterance(), "en", "it").get("translation");
			System.out.println("Apertium EN -> IT: " + tradApertiumIt);
			
			String tradGoogleEs = Translate.execute(u.getUtterance(), Language.ENGLISH, Language.SPANISH);
			System.out.println("Google EN -> ES: " + tradGoogleEs);
			String tradGoogleIt = Translate.execute(u.getUtterance(), Language.ENGLISH, Language.ITALIAN);
			System.out.println("Google EN -> IT: " + tradGoogleIt);
			
			//String tradGoogleEs = invokeGoogle(u.getUtterance(), qes);
			//String tradGoogleIt = invokeGoogle(u.getUtterance(), qit);
			
			Utterance uaes = u.clona();
			Utterance uait = u.clona();
			
			uaes.setUtterance(tradApertiumEs);
			uait.setUtterance(tradApertiumIt);
			
			Utterance uges = u.clona();
			Utterance ugit = u.clona();
			
			uges.setUtterance(tradGoogleEs);
			ugit.setUtterance(tradGoogleIt);
			
			utterancesApertiumEs.add(uaes);
			utterancesApertiumIt.add(uait);
			
			utterancesGoogleEs.add(uges);
			utterancesGoogleIt.add(ugit);
		}
		
		out = new BufferedWriter(new FileWriter("log-apertium-es.xml"));
		out.write(makeXML(utterancesApertiumEs));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-apertium-it.xml"));
		out.write(makeXML(utterancesApertiumIt));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-google-es.xml"));
		out.write(makeXML(utterancesGoogleEs));
		out.close();
		
		out = new BufferedWriter(new FileWriter("log-google-it.xml"));
		out.write(makeXML(utterancesGoogleIt));
		out.close();
	}

}
