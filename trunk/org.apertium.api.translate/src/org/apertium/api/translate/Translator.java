package org.apertium.api.translate;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;

public class Translator {
	
	private TranslateConfiguration lastConfiguration = null;

	private ISO639 iso639 = null;
	private Services services = null;
	
	public Translator() {
		System.out.println("Translator()");
		
		iso639 = new ISO639();
		services = new Services();
	}
	
	private void refresh(TranslateConfiguration c) {
		if (lastConfiguration == null || !c.equals(lastConfiguration)) {
			
		}
		
		lastConfiguration = c.clona();
	}
	
	public String translate(String text) {
		System.out.println("Translator.translate()");
		
		String ret = text;
		
		TranslateConfiguration configuration = TranslateConfigurationAction.getInstance().getConfiguration();
		
		System.out.println("Translator.translate() 1");
		
		refresh(configuration);
		
		System.out.println("Translator.translate() 2");
		
		IQuery q = new GoogleMTConnector();
		
		System.out.println("Translator.translate() 3 ");
		
		q.setLanguages(
				LocaleId.fromString(configuration.getLangPair().getSrcLang().getCode()), 
				LocaleId.fromString(configuration.getLangPair().getDestLang().getCode()));

		System.out.println("Translator.translate() 4");
		
		q.open();
		
		System.out.println("Translator.translate() 5");
		
		q.query(text);
		
		System.out.println("Translator.translate() 6");
		
		if (q.hasNext()) {
			ret = q.next().target.toString();
		}
		
		return ret;
	}
}
