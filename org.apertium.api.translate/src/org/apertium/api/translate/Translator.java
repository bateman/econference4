package org.apertium.api.translate;

import org.apertium.api.translate.actions.TranslateConfigurationAction;

public class Translator {
	public Translator() {
		System.out.println("Translator()");
	}
	
	public String translate(String text) {
		System.out.println("Translator.translate()");
		String ret = TranslateConfigurationAction.getInstance().getConfiguration().getLangPair().toString();
		System.out.println("Translator.translate() 2");
		return ret;
	}
}
