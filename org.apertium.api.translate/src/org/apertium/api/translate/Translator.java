package org.apertium.api.translate;

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;
import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;

public class Translator {
	
	private TranslateConfiguration lastConfiguration = null;
	private Object connector = null;
	
	public Translator() {
		System.out.println("Translator()");
		lastConfiguration = new TranslateConfiguration();
	}
	
	private void refresh(TranslateConfiguration c) throws MalformedURLException {
		System.out.println("Translator.refresh()");
		
		boolean ref = true;
		if (c != null) {
			if (c.equals(lastConfiguration)) {
				ref = false;
			}
		}
		
		if (ref) {
			if (connector instanceof IQuery) {
				((IQuery)connector).close();
			}
			
			System.out.println("Translator.refresh() 2");
			
			switch (c.getService()) {
			case MICROSOFT:
				connector = new MicrosoftMTConnector();	
				net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
				p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
				((MicrosoftMTConnector)connector).setParameters(p);
				break;
			case APERTIUM:
				connector = new ApertiumXMLRPCClient(new URL(c.getUrl()));
				break;
			case GOOGLE:
				default:
					connector = new GoogleMTConnector();
					break;
			}
			
			if (connector instanceof IQuery) {
				((IQuery)connector).open();
			}
			
			System.out.println("Translator.refresh() 3");
		}

		System.out.println("Translator.refresh() 4");
		
		lastConfiguration = c.clona();
	}
	
	private static String _translate(String text, String src, String dest, Object c) throws InterruptedException, ApertiumXMLRPCClientException {
		String ret = text;
		
		if (c instanceof IQuery) {
			((IQuery)c).setLanguages(LocaleId.fromString(src), LocaleId.fromString(dest));
			((IQuery)c).query(text);
			if (((IQuery)c).hasNext()) {
				ret = ((IQuery)c).next().target.toString();
			}
		} else if (c instanceof ApertiumXMLRPCClient) {
			ret = ((ApertiumXMLRPCClient)c).translate(text, src, dest).get("translation");
		}
		
		return ret;
	}

	public String translate(String text) throws InterruptedException, ApertiumXMLRPCClientException, MalformedURLException {
		System.out.println("Translator.translate()");
		
		TranslateConfiguration c = TranslateConfigurationAction.getInstance().getConfiguration();
		refresh(c);
		
		String ret = _translate(text, 
				c.getLangPair().getSrcLang().getCode(), 
				c.getLangPair().getDestLang().getCode(), 
				connector);

		return ret;
	}
}
