package org.apertium.api.translate;

import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class TranslateHelper {
	private static Map<String, LanguagePair> pairs = null;
	//private static Languages languages = null;
	//private static ISO639 iso639 = null;
	
	public TranslateHelper() {
		//iso639 = new ISO639();
		
		/*
		try {
			if (languages == null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setNamespaceAware(true);

				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse("iso_639.xml");

				languages = new Languages(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		pairs = new HashMap<String, LanguagePair>();
		prepareLangPairs();
		
		if (pairs == null) {
			pairs = new HashMap<String, LanguagePair>();
			prepareLangPairs();
		} else if (pairs.size() == 0) {
			prepareLangPairs();
		}
	}

	private Language codeToLanguage(String code) {
		//String name = iso639.getLanguage(code);
		return new Language(code);
	}
	
	private LanguagePair codesToLanguagePair(String srcCode, String destCode) {
		Language srcLanguage = codeToLanguage(srcCode);
		Language destLanguage = codeToLanguage(destCode);
		return new LanguagePair(srcLanguage, destLanguage);
	}
	
	public void prepareLangPairs() {
		LanguagePair en_es = codesToLanguagePair("en", "es");
		pairs.put(en_es.toString(), en_es);
	}

	public Map<String, LanguagePair> getLangPairs() {
		return pairs;
	}

	public String translate(String request, LanguagePair langPair) throws Exception {
		return "test (" + request + ")";
	}
}
