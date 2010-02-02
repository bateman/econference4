package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;

public class TranslateConfiguration {
	private LanguagePair langPair = null;
	private Services.ServiceType service = null;
	private String url = null;
	
	public TranslateConfiguration() {
		langPair = new LanguagePair("it", "en");
		service = Services.ServiceType.APERTIUM;
		url = "http://www.neuralnoise.com:6173/RPC2";
	}

	public LanguagePair getLangPair() {
		return langPair;
	}

	public void setLangPair(LanguagePair langPair) {
		//if (langPair != null)	
		this.langPair = langPair;
	}

	public Services.ServiceType getService() {
		return service;
	}
	
	public void setService(Services.ServiceType service) {
		//if (service != null)	
		this.service = service;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		//if (url != null)	
		this.url = url;
	}
	
	@Override
	public String toString() {
		String ret = "";
		Services s = new Services();
		ret += "service: " + s.getService(service) + " url: " + url + " pair: " + langPair;
		return ret;
	}
	
	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof TranslateConfiguration))
			return false;
		
		boolean ret = true;
		TranslateConfiguration that = (TranslateConfiguration) aThat;
		
		ret &= url.equals(that.getUrl());
		ret &= langPair.equals(that.getLangPair());
		ret &= service.equals(that.getService());
		
		return ret;
	}

}
