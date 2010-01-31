package org.apertium.api.translate.actions;

//import javax.swing.*;
import org.apertium.api.translate.*;

public class TranslateConfiguration {
	
	//private TranslateConfigurationForm form;
	
	private LanguagePair langPair = null;
	
	public enum ServiceType { APERTIUM, GOOGLE };
	private ServiceType service = null;
	
	private String url = null;

	public LanguagePair getLangPair() {
		return langPair;
	}

	public void setLangPair(LanguagePair langPair) {
		this.langPair = langPair;
	}

	public ServiceType getService() {
		return service;
	}
	
	public void setService(ServiceType service) {
		this.service = service;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	/*
	public boolean isModified() {
		return form != null && form.isModified(this);
	}

	public JComponent createComponent() {
		if (form == null) {
			form = new TranslateConfigurationForm();
		}
		return form.getRootComponent();
	}

	public void apply() {
		if (form != null) {
			form.getData(this);
		}
	}

	public void reset() {
		if (form != null) {
			form.setData(this);
		}
	}
	*/

}
