/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2011 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package it.uniba.di.cdg.xcore.ui.wizards;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Main class of the configuration wizard
 * 
 * @see Wizard
 */
public class ConfigurationWizard extends Wizard {
	
	private boolean isFirst;			//Denotes whether this is first access wizard
	
	private Preferences preferences;	//Preferences resulting from wizard

	private WelcomePage welcome;		//Welcome wizard page

	private PathPage path;				//Wizard page for ECX file path configuration

	private GMailPage gmail;			//Wizard page for GMail account configuration

	private SkypePage skype;			//Wizard page for Skype account configuration

	private SmtpPage smtp;				//Wizard page for SMTP server data configuration

	/**
	 * The constructor
	 */
	public ConfigurationWizard(boolean first) {
		super();
		setWindowTitle("Configuration Wizard");
		preferences = new ConfigurationScope().getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
		isFirst = first;
	}

	/**
	 * Pages inclusion in wizard
	 * 
	 * @see Wizard#addPages()
	 */
	public void addPages() {

		if (isFirst) {
			/* If wizard is launched for the first time, add welcome page */
			welcome = new WelcomePage();
			addPage(welcome);
		}
		
		/* Add ECX file path configuration page */
		path = new PathPage();
		addPage(path);
		
		/* Add GMail account configuration page */
		gmail = new GMailPage();
		addPage(gmail);
		
		/* Add Skype account configuration page */
		skype = new SkypePage();
		addPage(skype);
		
		/* Add SMTP server data configuration page */
		smtp = new SmtpPage();
		addPage(smtp);
	}

	/**
	 * Tasks in event of acceptance in the wizard
	 * 
	 * @see Wizard#performFinish()
	 */
	public boolean performFinish() {		
		/* Add ECX file path to preferences */
		Preferences pathPref = preferences.node(IConfigurationConstant.PATH);
		if(path.getChange()){
			String preferredFilePath = path.getPath();
			if(preferredFilePath.lastIndexOf(System.getProperty("file.separator")) != preferredFilePath.length()-1){
				preferredFilePath = preferredFilePath + System.getProperty("file.separator");
			}
			pathPref.put(IConfigurationConstant.DIR, preferredFilePath);
		}
		
		/* Add GMail account data to preferences if any */
		Preferences gmailPref = preferences.node(IConfigurationConstant.GMAIL);
		if (!gmail.getUsername().isEmpty()) {
			/* If user enters GMail account data, add them to preferences */
			gmailPref.put(IConfigurationConstant.USERNAME, gmail.getUsername());
			gmailPref.put(IConfigurationConstant.PASSWORD, gmail.getPassword());
		} else {
			/* If user doesn't enters GMail account data, try to delete them from preferences */
			try {
				gmailPref.clear();
			} catch (BackingStoreException e) {
				System.out.println("No GMail data");
			}
		}

		/* Add Skype account data to preferences if any */
		Preferences skypePref = preferences.node(IConfigurationConstant.SKYPE);
		if (!skype.getUsername().isEmpty()) {
			/* If user enters Skype account data, add them to preferences */
			skypePref.put(IConfigurationConstant.USERNAME, skype.getUsername());
			skypePref.put(IConfigurationConstant.PASSWORD, skype.getPassword());
		} else {
			/* If user doesn't enters Skype account data, try to delete them from preferences */
			try {
				skypePref.clear();
			} catch (BackingStoreException e) {
				System.out.println("No Skype data");
			}
		}

		/* Add SMTP server data to preferences */
		Preferences smtpPref = preferences.node(IConfigurationConstant.SMTP);
		smtpPref.put(IConfigurationConstant.SERVER, smtp.getServer());
		smtpPref.putInt(IConfigurationConstant.PORT, smtp.getPort());
		smtpPref.put(IConfigurationConstant.USERNAME, smtp.getUsername());
		smtpPref.put(IConfigurationConstant.PASSWORD, smtp.getPassword());
		smtpPref.put(IConfigurationConstant.SECURE, smtp.getSecure());
		
		/* Delete wizard preferences to simulate first-time launch of eConference */
		/*
		try {
			pathPref.removeNode();
			gmailPref.removeNode();
			skypePref.removeNode();
			smtpPref.removeNode();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		*/
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
		/* If the path was changed, restart application */
		if(path.getChange()){
			PlatformUI.getWorkbench().restart();
		}
		
		return true;
	}
	
	/**
	 * Returns GMail Username for reuse by SMTP Page
	 * 
	 * @return GMail Username
	 */
	protected String getGMailUsername() {
		return gmail.getUsername();
	}

	/**
	 * Returns GMail Password for reuse by SMTP Page
	 * 
	 * @return GMail Password
	 */
	protected String getGMailPassword() {
		return gmail.getPassword();
	}
}
