package org.apertium.api.translate;

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
	private IQuery connector = null;
	
	public Translator() {
		System.out.println("Translator()");
		lastConfiguration = new TranslateConfiguration();
	}
	
	private void refresh(TranslateConfiguration c) {
		System.out.println("Translator.refresh()");
		
		if (!c.equals(lastConfiguration)) {
			if (connector != null) {
				connector.close();
				connector = null;
			}
			
			System.out.println("Translator.refresh() 2");
			
			switch (c.getService()) {
			case GOOGLE:
				connector = new GoogleMTConnector();
				break;
			case MICROSOFT:
				connector = new MicrosoftMTConnector();	
				net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
				p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
				connector.setParameters(p);
				break;
			}
			
			System.out.println("Translator.refresh() 3");
			
			connector.open();
		}

		System.out.println("Translator.refresh() 4");
		
		lastConfiguration = c.clona();
	}
	
	private static String _translate(String text, String src, String dest, Object c) throws InterruptedException, ApertiumXMLRPCClientException {
		String ret = text;
		
		if (c instanceof IQuery) {
			IQuery q = (IQuery) c;
			if (c instanceof MicrosoftMTConnector) {
				net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
				p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
				q.setParameters(p);
			}

			q.setLanguages(LocaleId.fromString(src), LocaleId.fromString(dest));

			int hits = q.query(text);

			//System.out.println("Hits: " + hits);

			if (q.hasNext()) {
				ret = q.next().target.toString();
			}
		} else if (c instanceof ApertiumXMLRPCClient) {
			ApertiumXMLRPCClient a = (ApertiumXMLRPCClient)c;
			ret = a.translate(text, src, dest).get("translation");
		}
		
		return ret;
	}

	public String translate(String text) throws InterruptedException, ApertiumXMLRPCClientException {
		System.out.println("Translator.translate()");
		
		TranslateConfiguration c = TranslateConfigurationAction.getInstance().getConfiguration();
		refresh(c);

		String ret = _translate(text, "en", "it", connector);

		return ret;
	}
}
