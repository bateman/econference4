package org.apertium.api.translate.actions;

import org.apertium.api.translate.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigTranslateAction extends ActionDelegate implements IEditorActionDelegate {
	
	private LanguagePair langPair = null;
	private String apertiumServerURL = null;

	private static ConfigTranslateAction instance = null;

	public static ConfigTranslateAction getInstance() {
		if (instance == null) {
			try {
				instance = new ConfigTranslateAction();
			} catch (Exception e) {
				e.printStackTrace();
			}

			instance.readProperties();
		}
		return instance;
	}

	private void readProperties() {
		String userDir = System.getProperty("user.dir");
		Properties props = new Properties();
		File file = new File(userDir + "/" + ".translate");
		if (file.exists()) {
			try {
				props.load(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Language src = new Language((String)props.get("translate.srcLang.name"), 
				(String)props.get("translate.srcLang.code"));
		
		Language dest = new Language((String)props.get("translate.destLang.name"), 
				(String)props.get("translate.destLang.code"));
		
		langPair = new LanguagePair(src, dest);
		
		apertiumServerURL = (String) props.get("translate.apertium.serverUrl");
	}

	private void writeProperties() {
		String userDir = System.getProperty("user.dir");

		Properties props = new Properties();
		
		if (langPair != null) {
			props.put("translate.srcLang.name", langPair.getSrcLang().getName());
			props.put("translate.srcLang.code", langPair.getSrcLang().getCode());
			
			props.put("translate.destLang.name", langPair.getDestLang().getName());
			props.put("translate.destLang.code", langPair.getDestLang().getCode());
		}
		
		if (apertiumServerURL != null) {
			props.put("translate.apertium.serverUrl", apertiumServerURL);
		}
		
		File file = new File(userDir + "/" + ".translate");
		
		try {
			props.store(new FileOutputStream(file), "translate properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ConfigTranslateAction() throws Exception {
		Properties systemProps = System.getProperties();

		if (apertiumServerURL != null && apertiumServerURL.trim().length() > 0) {
			systemProps.put("apertiumServerSet", "true");
			systemProps.put("apertiumServerURL", apertiumServerURL);
		}

		systemProps.put("apertiumServerSet", "true");
		systemProps.put("apertiumServerURL", "http://www.neuralnoise.com:6173/RPC2");
	}

	public void run(IAction action) {
		readProperties();

		final TranslateConfigDialog dialog = new TranslateConfigDialog(null);
		
		dialog.setLangPair(langPair);
		dialog.setApertiumServerURL(apertiumServerURL);
		dialog.loadProperties();

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				if (dialog.isAnswer()) {
					langPair = dialog.getLangPair();
					apertiumServerURL = dialog.getApertiumServerURL();

					writeProperties();
				}
			}
		});

		dialog.setVisible(true);
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	}

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

}
