package org.apertium.api.translate;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;

public class Translator {
	
	private TranslateConfiguration lastConfiguration = null;
	
	public Translator() {
		System.out.println("Translator()");
	}
	
	private void refresh(TranslateConfiguration c) {
		lastConfiguration = c.clona();
	}
	
	public String translate(String text) {
		System.out.println("Translator.translate()");

		TranslateConfiguration configuration = TranslateConfigurationAction.getInstance().getConfiguration();
		if (!configuration.equals(lastConfiguration)) {
			refresh(configuration);
		}
		
		String ret = configuration.getLangPair().toString();
		return ret;
	}
}
