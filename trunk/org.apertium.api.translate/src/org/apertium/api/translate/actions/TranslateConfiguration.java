package org.apertium.api.translate.actions;

import javax.swing.*;

import org.apertium.api.translate.*;

public class TranslateConfiguration {
	private TranslateConfigurationForm form;
	private LanguagePair langPair;
	
	private String apertiumServerURL;

	public LanguagePair getLangPair() {
		return langPair;
	}

	public void setLangPair(LanguagePair langPair) {
		this.langPair = langPair;
	}

	public String getApertiumServerURL() {
		return apertiumServerURL;
	}

	public void setApertiumServerURL(String apertiumServerURL) {
		this.apertiumServerURL = apertiumServerURL;
	}

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

}
