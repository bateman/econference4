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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

/**
 * Wizard page for GMail account configuration
 * 
 * @see WizardPage
 */
public class GMailPage extends WizardPage {
	
	private Text username;	//GMail username text line
	
	private Text password;	//GMail password text line

	/**
	 * The constructor
	 */
	protected GMailPage() {
		super("GMail account");
		setTitle("GMail account");
		setDescription("Enter username and password, or leave blank and click next to skip this step");
	}

	/**
	 * Show the form
	 * 
	 * @see WizardPage#createControl(Composite)
	 */
	public void createControl(final Composite parent) {
		/* Search data in preferences */
		Preferences preferences = new ConfigurationScope().getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
		Preferences gmailPref = preferences.node(IConfigurationConstant.GMAIL);
		String usernamePref = gmailPref.get(IConfigurationConstant.USERNAME, "");
		String passwordPref = gmailPref.get(IConfigurationConstant.PASSWORD, "");

		/* Setup container layout */
		Composite container = new Composite(parent, SWT.NULL);
	    FillLayout layout = new FillLayout(SWT.VERTICAL);
	    container.setLayout(layout);
		setControl(container);

		/* Setup group layout */
		Group gmailGroup = new Group(container, SWT.SHADOW_IN);
		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 2;
		gmailGroup.setText("GMail account data");
		gmailGroup.setLayout(groupLayout);

		/* Add username label */
		Label usernameLabel = new Label(gmailGroup, SWT.NONE);
		GridData usernameGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		usernameGrid.horizontalIndent = 20;
		usernameLabel.setLayoutData(usernameGrid);
		usernameLabel.setText("Username:");

		/* Add username text line */
		username = new Text(gmailGroup, SWT.BORDER);
		username.setText(usernamePref);
		username.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});
		username.addListener(SWT.FocusOut, new Listener() {

			/* Add @gmail.com suffix to username if necessary */
			@Override
			public void handleEvent(Event event) {
				String usernameRegex = "[a-zA-Z0-9._%-]+";
				Pattern usernamePattern = Pattern.compile(usernameRegex);
				Matcher usernameMatcher = usernamePattern.matcher(username.getText());

				if (usernameMatcher.matches()) {
					username.setText(username.getText() + "@gmail.com");
				}
			}
		});
		username.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		/* Add a spacer */
		Label spacer1Label = new Label(gmailGroup, SWT.NONE);
		GridData spacer1Grid = new GridData();
		spacer1Grid.horizontalSpan = 2;
		spacer1Label.setLayoutData(spacer1Grid);

		/* Add password label */
		Label passwordLabel = new Label(gmailGroup, SWT.NONE);
		GridData passwordGrid = new GridData();
		passwordGrid.horizontalIndent = 20;
		passwordLabel.setLayoutData(passwordGrid);
		passwordLabel.setText("Password:");

		/* Add password text line */
		password = new Text(gmailGroup, SWT.BORDER);
		password.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		password.setEchoChar('*');
		password.setText(passwordPref);
		password.addListener(SWT.Modify, new Listener() {

			/* Checks the validity of the data if it changes */
			@Override
			public void handleEvent(Event event) {
				dataControl();
			}
		});

		dataControl();
	}

	/**
	 * Checks the validity of user entered data and, if necessary, prevents from
	 * continuing in the wizard
	 */
	private void dataControl() {
		setPageComplete(false);
		setMessage(null);
		setErrorMessage(null);

		if (username.getText().length() == 0
				&& password.getText().length() == 0) {
			/* If there are no data allows you to skip this step */
			setPageComplete(true);
			setMessage(null);
			setErrorMessage(null);
			return;
		}

		if (username.getText().length() != 0
				&& password.getText().length() == 0) {
			/* If there's username but password text line is empty, prevents from continuing in the wizard */
			setMessage("Complete data by entering password or delete username to skip this step");
			return;
		}

		if (username.getText().length() == 0
				&& password.getText().length() != 0) {
			/* If there's password but username text line is empty, prevents from continuing in the wizard */
			setMessage("Complete data by entering username or delete password to skip this step");
			return;
		}

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

		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}

	/**
	 * Return the GMail username
	 * 
	 * @return GMail username
	 */
	public String getUsername() {
		return username.getText();
	}

	/**
	 * Return the GMail password
	 * 
	 * @return GMail password
	 */
	public String getPassword() {
		return password.getText();
	}
}
