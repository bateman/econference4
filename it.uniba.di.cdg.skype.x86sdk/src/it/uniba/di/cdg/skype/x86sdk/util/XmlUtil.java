package it.uniba.di.cdg.skype.x86sdk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;

public class XmlUtil {

	
	public static HashMap<String, String> readXmlExtension(String xmlString){
		HashMap<String, String> extension = new HashMap<String, String>();
		try {
		    // First create a new XMLInputFactory
		    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		    // Setup a new eventReader
		    InputStream in = new ByteArrayInputStream( xmlString.getBytes() );
		    XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "ISO-8859-1");
		    // Read the XML document
		    while (eventReader.hasNext()) {
	
				XMLEvent event = eventReader.nextEvent();
	
				if (event.isStartElement()) {
					String name;
					String value;
					name = event.asStartElement().getName().getLocalPart();
					event = eventReader.nextEvent();
					if(event.isCharacters()){
						value = event.asCharacters().getData();
						extension.put(name, value);
					}
					/*value = event.asCharacters().getData();
					if(!value.equals("\n\t"))
						extension.put(name, value);*/
				}
		    }   
		} catch (XMLStreamException e) {
		    e.printStackTrace();
		}
		extension.remove(ExtensionConstants.EXTENSION_NAME);
		return extension;
	}
	
	public static String writeXmlExtension(String extensionName,
			HashMap<String, String> param){

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter
		XMLEventWriter eventWriter;
		try {
			eventWriter = outputFactory.createXMLEventWriter(outStream);
			// Create a EventFactory
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			// Create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
			eventWriter.add(eventFactory.createDTD("\n"));
			eventWriter.add(eventFactory.createStartElement("","", ExtensionConstants.EXTENSION));
			eventWriter.add(eventFactory.createDTD("\n"));
			eventWriter.add(eventFactory.createDTD("\t"));
			eventWriter.add(eventFactory.createStartElement("","", ExtensionConstants.EXTENSION_NAME));
			eventWriter.add(eventFactory.createCharacters(extensionName));
			eventWriter.add(eventFactory.createEndElement("","", ExtensionConstants.EXTENSION_NAME));
			eventWriter.add(eventFactory.createDTD("\n"));
			
			for(String s: param.keySet()){
				writeXmlNode(eventWriter, s, param.get(s));
			}
			
			eventWriter.add(eventFactory.createEndElement("","", ExtensionConstants.EXTENSION));
			
			
			
			eventWriter.close();	
			return outStream.toString();
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String getValueByElement(String xmlString, String element){
		String result = null;
		try {
		    // First create a new XMLInputFactory
		    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		    // Setup a new eventReader
		    InputStream in = new ByteArrayInputStream( xmlString.getBytes() );
		    XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "ISO-8859-1");
		    // Read the XML document
		    while (eventReader.hasNext()) {
	
				XMLEvent event = eventReader.nextEvent();
	
				if (event.isStartElement()) {
				    if (event.asStartElement().getName().getLocalPart() == (element)) {
				    	event = eventReader.nextEvent();
				    	result = event.asCharacters().getData();
						break;
				    }
				}
		    }   
		} catch (XMLStreamException e) {
			System.err.println("Non xml skype msg received");
			//non è presente l'elemento
		}
		
		return result;
	}


	private static void writeXmlNode(XMLEventWriter eventWriter, String element,
			String value) throws XMLStreamException {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		eventWriter.add(eventFactory.createDTD("\t"));
		eventWriter.add(eventFactory.createStartElement("","", element));
		if (element.equals(ExtensionConstants.MESSAGE)) {
			eventWriter.add(eventFactory.createCData(value));
		} else {
			eventWriter.add(eventFactory.createCharacters(value));
		}
		eventWriter.add(eventFactory.createEndElement("","", element));
		eventWriter.add(eventFactory.createDTD("\n"));
	}

	public static String extensionName(String xmlString) {
		return getValueByElement(xmlString, ExtensionConstants.EXTENSION_NAME);
	}
	
	public static String chatType(String xmlString){
		return getValueByElement(xmlString, ExtensionConstants.CHAT_TYPE);
	}
	
	public static boolean isSkypeXmlMessage(String xmlString){
		return (getValueByElement(xmlString, "partlist") != null);
	}	

}
