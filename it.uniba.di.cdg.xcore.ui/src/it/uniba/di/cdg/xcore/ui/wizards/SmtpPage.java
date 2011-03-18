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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

/**
 * Wizard page for SMTP server data to use to send invitations
 * 
 * @see WizardPage
 */
public class SmtpPage extends WizardPage {
	
	private String gmailUsername;			//GMail username added to the previous page

	private String gmailPassword;			//GMail password added to the previous page

	private Button[] choiceRadios;			//Radio button for choice between GMail or another SMTP Server

	private Text server;					//SMTP server name text line

	private Text port;						//SMTP server port text line

	private Text username;					//SMTP server username text line

	private Text password;					//SMTP server password text line

	private Combo secure;					//SMTP secure connection combo

	private ArrayList<String> securityProt;	//Security protocol

	/**
	 * The constructor
	 */
	protected SmtpPage() {
		super("SMTP server");
		setTitle("SMTP server");
		setDescription("Insert SMTP server data");
		securityProt = new ArrayList<String>();
		securityProt.add("None");
		securityProt.add("SSL/TLS");
		securityProt.add("STARTTLS");
	}

	/**
	 * Show the form
	 * 
	 * @see WizardPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		/* Setup container layout */
		Composite container = new Composite(parent, SWT.NULL);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		container.setLayout(layout);
		setControl(container);

		/* Setup first group layout */
		Group serverGroup = new Group(container, SWT.SHADOW_IN);
		GridLayout serverLayout = new GridLayout();
		serverLayout.numColumns = 2;
		serverGroup.setText("SMTP server data");
		serverGroup.setLayout(serverLayout);

		/* Add button for choice between GMail or another SMTP server */
		choiceRadios = new Button[2];

		/* Add Gmail SMTP server button */
		choiceRadios[0] = new Button(serverGroup, SWT.RADIO);
		choiceRadios[0].setText("GMail");
		GridData choiceGmailGrid = new GridData();
		choiceGmailGrid.horizontalIndent = 20;
		choiceRadios[0].setLayoutData(choiceGmailGrid);

		/* Add other SMTP server button */
		choiceRadios[1] = new Button(serverGroup, SWT.RADIO);
		choiceRadios[1].setText("other...");
		GridData choiceOtherGrid = new GridData();
		choiceOtherGrid.horizontalIndent = 20;
		choiceRadios[1].setLayoutData(choiceOtherGrid);

		/* Add a spacer */
		Label spacer1Label = new Label(serverGroup, SWT.NONE);
		GridData spacer1Grid = new GridData();
		spacer1Grid.horizontalSpan = 2;
		spacer1Label.setLayoutData(spacer1Grid);

		/* Add server name label */
		Label serverLabel = new Label(serverGroup, SWT.NONE);
		GridData serverGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		serverGrid.horizontalIndent = 20;
		serverLabel.setLayoutData(serverGrid);
		serverLabel.setText("Server:");

		/* Add server name text line */
		server = new Text(serverGroup, SWT.BORDER);
		server.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});
		server.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/* Add a spacer */
		Label spacer2Label = new Label(serverGroup, SWT.NONE);
		GridData spacer2Grid = new GridData();
		spacer2Grid.horizontalSpan = 2;
		spacer2Label.setLayoutData(spacer2Grid);

		/* Add server port label */
		Label portLabel = new Label(serverGroup, SWT.NONE);
		GridData portGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		portGrid.horizontalIndent = 20;
		portLabel.setLayoutData(portGrid);
		portLabel.setText("Port:");

		/* Add server port text line */
		port = new Text(serverGroup, SWT.BORDER);
		port.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});
		port.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/* Setup second group layout */
		Group secureGroup = new Group(container, SWT.SHADOW_IN);
		GridLayout secureLayout = new GridLayout();
		secureLayout.numColumns = 2;
		secureGroup.setText("Authentication and security");
		secureGroup.setLayout(secureLayout);

		/* Add username label */
		Label usernameLabel = new Label(secureGroup, SWT.NONE);
		GridData usernameGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		usernameGrid.horizontalIndent = 20;
		usernameLabel.setLayoutData(usernameGrid);
		usernameLabel.setText("Username:");

		/* Add username text line */
		username = new Text(secureGroup, SWT.BORDER);
		username.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});
		username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		username.addListener(SWT.FocusOut, new Listener() {

			/* Add @gmail.com suffix to username if necessary */
			@Override
			public void handleEvent(Event event) {
				if(choiceRadios[0].getSelection()){
					String usernameRegex = "[a-zA-Z0-9._%-]+";
					Pattern usernamePattern = Pattern.compile(usernameRegex);
					Matcher usernameMatcher = usernamePattern.matcher(username.getText());
	
					if (usernameMatcher.matches()) {
						username.setText(username.getText() + "@gmail.com");
					}
				}
			}
		});

		/* Add a spacer */
		Label spacer3Label = new Label(secureGroup, SWT.NONE);
		GridData spacer3Grid = new GridData();
		spacer3Grid.horizontalSpan = 2;
		spacer3Label.setLayoutData(spacer3Grid);

		/* Add password label */
		Label passwordLabel = new Label(secureGroup, SWT.NONE);
		GridData passwordGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		passwordGrid.horizontalIndent = 20;
		passwordLabel.setLayoutData(passwordGrid);
		passwordLabel.setText("Password:");

		/* Add password text line */
		password = new Text(secureGroup, SWT.BORDER);
		password.setEchoChar('*');
		password.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});
		password.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/* Add a spacer */
		Label spacer4Label = new Label(secureGroup, SWT.NONE);
		GridData spacer4Grid = new GridData();
		spacer4Grid.horizontalSpan = 2;
		spacer4Label.setLayoutData(spacer4Grid);
		
		/* Add secure label */
		Label secureLabel = new Label(secureGroup, SWT.NONE);
		GridData secureGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		secureGrid.horizontalIndent = 20;
		secureLabel.setLayoutData(secureGrid);
		secureLabel.setText("Security:");
		
		/* Add secure connection combo */
		secure = new Combo(secureGroup, SWT.READ_ONLY);
		secure.setText("Secure connection");
		for(int i=0; i<securityProt.size(); i++){
			secure.add(securityProt.get(i));
		}
		secure.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});
		secure.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		dataControl();
	}

	/**
	 * Checks the validity of user entered data and, if necessary, prevents from
	 * continuing in the wizard
	 */
	private void dataControl() {
		setPageComplete(false);
		setErrorMessage(null);
		setMessage(null);

		if (server.getText().length() == 0
				|| port.getText().length() == 0
				|| username.getText().length() == 0
				|| password.getText().length() == 0
				|| secure.getSelectionIndex() == -1){
			/* If one text line is empty, prevents from continuing in the wizard */
			return;
		}

		String serverRegex = "([a-zA-Z0-9.-]+\\.){2,}[a-zA-Z]{2,4}";
		Pattern serverPattern = Pattern.compile(serverRegex);
		Matcher serverMatcher = serverPattern.matcher(server.getText());

		if (!serverMatcher.matches()) {
			/* If server name isn't valid, show error message and prevents from continuing in the wizard */
			setErrorMessage("Server name not valid");
			return;
		}

		String portRegex = "[0-9]*";
		Pattern portPattern = Pattern.compile(portRegex);
		Matcher portMatcher = portPattern.matcher(port.getText());

		if (!portMatcher.matches()) {
			/* If server port isn't valid, show error message and prevents from continuing in the wizard */
			setErrorMessage("Port number not valid");
			return;
		}

		if(choiceRadios[0].getSelection()){
			/* If user select GMail SMTP server, shecks the validity of username */
			String usernameRegex = "[a-zA-Z0-9._%-]+";
			Pattern usernamePattern = Pattern.compile(usernameRegex);
			Matcher usernameMatcher = usernamePattern.matcher(username.getText());
	
			if (usernameMatcher.matches()) {
				/* Suggest to add @gmail.com suffix to username if necessary */
				setMessage("Complete username with @gmail.com suffix");
				return;
			}
	
			String completeUsernameRegex = "[a-zA-Z0-9._%-]+@gmail\\.com";
			Pattern completeUsernamePattern = Pattern.compile(completeUsernameRegex);
			Matcher completeUsernameMatcher = completeUsernamePattern.matcher(username.getText());
	
			if (!completeUsernameMatcher.matches()) {
				/* If username isn't valid, show error message and prevents from continuing in the wizard */
				setErrorMessage("Username not valid");
				return;
			}
		}
		
		setPageComplete(true);
		setErrorMessage(null);
		setMessage(null);
	}

	/**
	 * Update the content before becoming visible
	 * 
	 * @see WizardPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (visible) {
			/* Search data in preferences */
			Preferences preferences = new ConfigurationScope().getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
			Preferences smtpPref = preferences.node(IConfigurationConstant.SMTP);
			final String serverPref = smtpPref.get(IConfigurationConstant.SERVER, "");
			final String portPref = smtpPref.get(IConfigurationConstant.PORT, "");
			final String usernamePref = smtpPref.get(IConfigurationConstant.USERNAME, "");
			final String passwordPref = smtpPref.get(IConfigurationConstant.PASSWORD, "");
			final String securePref = smtpPref.get(IConfigurationConstant.SECURE, "");

			/* Search data GMail account page */
			gmailUsername = ((ConfigurationWizard) getWizard()).getGMailUsername();
			gmailPassword = ((ConfigurationWizard) getWizard()).getGMailPassword();
			if (!gmailUsername.isEmpty()) {
				username.setText(gmailUsername);
				password.setText(gmailPassword);
			}

			choiceRadios[0].addListener(SWT.Selection, new Listener() {
				/* Tasks at GMail SMTP server chosen */
				@Override
				public void handleEvent(Event event) {
					server.setEnabled(false);
					server.setEditable(false);
					server.setText("smtp.googlemail.com");
					port.setEnabled(false);
					port.setEditable(false);
					port.setText("465");
					if (serverPref.compareTo("smtp.googlemail.com") == 0
							&& portPref.compareTo("465") == 0) {
						username.setText(usernamePref);
						password.setText(passwordPref);
					}else{
						username.setText(gmailUsername);
						password.setText(gmailPassword);
					}
					secure.select(securityProt.indexOf("SSL/TLS"));
					secure.setEnabled(false);
				}
			});

			choiceRadios[1].addListener(SWT.Selection, new Listener() {

				/* Tasks at other SMTP server chosen */
				@Override
				public void handleEvent(Event event) {
					server.setEnabled(true);
					server.setEditable(true);
					port.setEnabled(true);
					port.setEditable(true);
					if ((serverPref.compareTo("smtp.googlemail.com") == 0
							&& portPref.compareTo("465") == 0)
						|| (serverPref.isEmpty() && portPref.isEmpty())){
						server.setText("");
						port.setText("");
						username.setText("");
						password.setText("");
						secure.select(securityProt.indexOf("None"));
					} else {
						server.setText(serverPref);
						port.setText(portPref);
						username.setText(usernamePref);
						password.setText(passwordPref);
						secure.select(securityProt.indexOf(securePref));
					}
					secure.setEnabled(true);
				}
			});
			
			if ((serverPref.compareTo("smtp.googlemail.com") == 0 && portPref.compareTo("465") == 0)
					|| (serverPref.isEmpty() && portPref.isEmpty())) {
				/* If there's GMail SMTP server data or no data in preferences, set GMail data in form */
				server.setEditable(false);
				server.setEnabled(false);
				server.setText("smtp.googlemail.com");
				port.setEditable(false);
				port.setEnabled(false);
				port.setText("465");
				username.setText(gmailUsername);
				password.setText(gmailPassword);
				if (serverPref.compareTo("smtp.googlemail.com") == 0
						&& portPref.compareTo("465") == 0) {
					username.setText(usernamePref);
					password.setText(passwordPref);
				}
				secure.select(securityProt.indexOf("SSL/TLS"));
				secure.setEnabled(false);
				choiceRadios[1].setSelection(false);
				choiceRadios[0].setSelection(true);
			} else {
				/* If there's other SMTP server in preferences, set them in form */
				server.setEditable(true);
				server.setEnabled(true);
				server.setText(serverPref);
				port.setEditable(true);
				port.setEnabled(true);
				port.setText(portPref);
				username.setText(usernamePref);
				password.setText(passwordPref);
				secure.select(securityProt.indexOf(securePref));
				choiceRadios[0].setSelection(false);
				choiceRadios[1].setSelection(true);
			}
		}
		super.setVisible(visible);
	}

	/**
	 * Return the SMTP server name
	 * 
	 * @return SMTP server name
	 */
	public String getServer() {
		return server.getText();
	}

	/**
	 * Return the SMTP server port
	 * 
	 * @return SMTP server port
	 */
	public int getPort() {
		return Integer.parseInt(port.getText());
	}

	/**
	 * Return the SMTP server username
	 * 
	 * @return SMTP server username
	 */
	public String getUsername() {
		return username.getText();
	}

	/**
	 * Return the SMTP server password
	 * 
	 * @return SMTP server password
	 */
	public String getPassword() {
		return password.getText();
	}

	/**
	 * Return the SMTP server secure connection
	 * 
	 * @return SMTP server secure connection
	 */
	public String getSecure() {
		return securityProt.get(secure.getSelectionIndex());
	}
}
