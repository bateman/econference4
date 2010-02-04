package org.apertium.api.translate;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;

public class Translator {
	
	private TranslateConfiguration lastConfiguration = null;

	//private ISO639 iso639 = null;
	private Services services = null;
	
	IQuery googleConnector = null;
	
	public Translator() {
		System.out.println("Translator()");
		
		//iso639 = new ISO639();
		services = new Services();
		googleConnector = new GoogleMTConnector();
	}
	
	private void refresh(TranslateConfiguration c) {
		if (lastConfiguration == null || !c.equals(lastConfiguration)) {
			if (c.getService() == Services.ServiceType.GOOGLE) {
				googleConnector.open();
			} else {
				if (lastConfiguration != null && lastConfiguration.getService() == Services.ServiceType.GOOGLE) {
					googleConnector.close();
				}
			}
		}
		
		lastConfiguration = c.clona();
	}
	
	private String translateGoogle(String text, TranslateConfiguration c) {
		String ret = text;
		googleConnector.setLanguages(
				LocaleId.fromString(c.getLangPair().getSrcLang().getCode()), 
				LocaleId.fromString(c.getLangPair().getDestLang().getCode()));
		googleConnector.query(text);
		if (googleConnector.hasNext()) {
			ret = googleConnector.next().target.toString();
		}
		return ret;
	}
	
	private String translateApertium(String text, TranslateConfiguration c) {
		String ret = text;
		return "<<<" + ret + ">>>";
	}
	
	public String translate(String text) {
		System.out.println("Translator.translate()");
		
		TranslateConfiguration c = TranslateConfigurationAction.getInstance().getConfiguration();
		refresh(c);
		
		String ret = text;
		
		if (c.getService() == Services.ServiceType.GOOGLE) {
			ret = translateGoogle(text, c);
		} else if (c.getService() == Services.ServiceType.APERTIUM) {
			ret = translateApertium(text, c);
		}

		return ret;
	}
}
