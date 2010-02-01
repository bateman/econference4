package org.apertium.api.translate;

public class TranslateHelper {
	
	public TranslateHelper() {
		
	}

	/*
	private Language codeToLanguage(String code) {
		return new Language(code);
	}

	private LanguagePair codesToLanguagePair(String srcCode, String destCode) {
		Language srcLanguage = codeToLanguage(srcCode);
		Language destLanguage = codeToLanguage(destCode);
		return new LanguagePair(srcLanguage, destLanguage);
	}
	*/

	public String translate(String request, LanguagePair langPair) throws Exception {
		return "test (" + request + ")";
	}
}
