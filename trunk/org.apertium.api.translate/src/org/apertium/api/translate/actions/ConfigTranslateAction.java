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
		
	private Services.ServiceType service = null;
	private LanguagePair langPair = null;
	private String url = null;

	private static Services services = null;
	private static ConfigTranslateAction instance = null;

	public static ConfigTranslateAction getInstance() {
		System.out.println("ConfigTranslateAction.getInstance()");
		
		if (instance == null) {
			try {
				instance = new ConfigTranslateAction();
				services = new Services();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance.readProperties();
		}
		return instance;
	}

	private void readProperties() {
		System.out.println("ConfigTranslateAction.readProperties()");
		
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
		
		System.out.println("ConfigTranslateAction.readProperties() 2");
		
		String tservice = (String)props.get("translate.service");
		
		System.out.println("ConfigTranslateAction.readProperties() 2.5 " + tservice);
		
		if (tservice != null) {
			service = services.getServiceType(tservice);
		}
		
		System.out.println("ConfigTranslateAction.readProperties() 3");
		
		String tsrc = (String)props.get("translate.srcLang");
		String tdest = (String)props.get("translate.destLang");
		
		if (tsrc != null && tdest != null) {
			Language src = new Language(tsrc);
			Language dest = new Language(tdest);

			System.out.println("ConfigTranslateAction.readProperties() 4");

			langPair = new LanguagePair(src, dest);
		}
		
		System.out.println("ConfigTranslateAction.readProperties() 5");
		
		String turl = (String) props.get("translate.url");
		
		if (turl != null) {
			url = (String) props.get("translate.url");
		}
	}

	private void writeProperties() {
		String userDir = System.getProperty("user.dir");

		Properties props = new Properties();
		
		if (service != null) {
			props.put("translate.service", services.getService(service));
		}
		
		if (langPair != null) {
			props.put("translate.srcLang", langPair.getSrcLang().getCode());
			props.put("translate.destLang", langPair.getDestLang().getCode());
		}
		
		if (url != null) {
			props.put("translate.url", url);
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

		if (url != null && url.trim().length() > 0) {
			systemProps.put("urlSet", "true");
			systemProps.put("url", url);
		}

		systemProps.put("urlSet", "true");
		systemProps.put("url", "http://www.neuralnoise.com:6173/RPC2");
	}

	public void run(IAction action) {
		System.out.println("ConfigTranslateAction.run()");
		
		readProperties();

		System.out.println("ConfigTranslateAction.run() 2");
		
		final TranslateConfigDialog dialog = new TranslateConfigDialog(null);
		
		System.out.println("ConfigTranslateAction.run() 3");
		
		dialog.setService(service);
		dialog.setLangPair(langPair);
		dialog.setUrl(url);
		
		System.out.println("ConfigTranslateAction.run() 4");
		
		dialog.loadProperties();

		System.out.println("ConfigTranslateAction.run() 5");
		
		dialog.pack();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e) {
				if (dialog.isAnswer()) {
					
					service = dialog.getService();
					langPair = dialog.getLangPair();
					url = dialog.getUrl();

					writeProperties();
				}
			}
		});

		System.out.println("ConfigTranslateAction.run() 6");
		
		dialog.setVisible(true);
		
		System.out.println("ConfigTranslateAction.run() 7");
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) { }

	public Services.ServiceType getService() {
		return service;
	}

	public void setService(Services.ServiceType s) {
		this.service = s;
	}
	
	public LanguagePair getLangPair() {
		return langPair;
	}

	public void setLangPair(LanguagePair langPair) {
		this.langPair = langPair;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
