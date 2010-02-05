package org.apertium.api.translate;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;

public class Translator {
	
	private TranslateConfiguration lastConfiguration = null;
	
	private IQuery connector = null;
	
	public Translator() {
		System.out.println("Translator()");
	}
	
	private void refresh(TranslateConfiguration c) {
		if (!c.equals(lastConfiguration)) {
			if (connector != null) {
				connector.close();
			}
			
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
			
			connector.open();
		}

		lastConfiguration = c.clona();
	}
	
	private String _translate(String text, TranslateConfiguration c) {
		String ret = text;
		
		connector.setLanguages(
				LocaleId.fromString(c.getLangPair().getSrcLang().getCode()), 
				LocaleId.fromString(c.getLangPair().getDestLang().getCode()));
		connector.query(text);
		
		if (connector.hasNext()) {
			ret = connector.next().target.toString();
		}
		
		return ret;
	}
	
	public String translate(String text) {
		System.out.println("Translator.translate()");
		
		TranslateConfiguration c = TranslateConfigurationAction.getInstance().getConfiguration();
		refresh(c);

		String ret = _translate(text, c);

		return ret;
	}
}
