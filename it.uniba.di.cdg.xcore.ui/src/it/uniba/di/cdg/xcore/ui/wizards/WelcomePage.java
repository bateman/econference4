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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Welcome wizard page
 * 
 * @see WizardPage
 */
public class WelcomePage extends WizardPage {

	/**
	 * The constructor
	 */
	protected WelcomePage() {
		super("Welcome");
		setTitle("Welcome to eConference");
		setDescription("Start the configuration wizard");
	}

	/**
	 * Show the form
	 * 
	 * @see WizardPage#createControl(Composite)
	 */
	public void createControl(final Composite parent) {
		/* Setup container layout */
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		setControl(container);

		/* Add title label */
		Label title = new Label(container, SWT.NULL);
		title.setText("Welcome to eConference");
		title.setAlignment(SWT.CENTER);
		FontData titleFontData = new FontData(container.getFont().toString(),
				24, SWT.BOLD);
		Font titleFont = new Font(container.getDisplay(), titleFontData);
		title.setFont(titleFont);
		GridData titleGrid = new GridData(GridData.FILL_HORIZONTAL);
		title.setLayoutData(titleGrid);

		/* Add a spacer */
		Label spacer1Label = new Label(container, SWT.NONE);
		GridData spacer1Grid = new GridData();
		spacer1Label.setLayoutData(spacer1Grid);

		/* Add subtitle label */
		Label subtitle = new Label(container, SWT.NONE);
		subtitle.setText("Configuration wizard is about to start");
		FontData subtitleFontData = new FontData(
				container.getFont().toString(), 10, SWT.BOLD);
		Font subtitleFont = new Font(container.getDisplay(), subtitleFontData);
		subtitle.setFont(subtitleFont);
		GridData subtitleGrid = new GridData();
		subtitle.setLayoutData(subtitleGrid);

		/* Add a spacer */
		Label spacer2Label = new Label(container, SWT.NONE);
		GridData spacer2Grid = new GridData();
		spacer2Label.setLayoutData(spacer2Grid);

		/* Add requests label */
		Label requests = new Label(container, SWT.NONE);
		requests.setText("You must provide the following information:");
		GridData requestsGrid = new GridData();
		requests.setLayoutData(requestsGrid);

		/* Add path request label */
		Label pathRequest = new Label(container, SWT.NONE);
		pathRequest.setText(" - location of the event folder");
		GridData pathRequestGrid = new GridData();
		pathRequestGrid.horizontalIndent = 10;
		pathRequest.setLayoutData(pathRequestGrid);

		/* Add gmail request label */
		Label gmailRequest = new Label(container, SWT.NONE);
		gmailRequest.setText(" - gmail account data");
		GridData gmailRequestGrid = new GridData();
		gmailRequestGrid.horizontalIndent = 10;
		gmailRequest.setLayoutData(gmailRequestGrid);

		/* Add skype request label */
		Label skypeRequest = new Label(container, SWT.NONE);
		skypeRequest.setText(" - skype account data");
		GridData skypeRequestGrid = new GridData();
		skypeRequestGrid.horizontalIndent = 10;
		skypeRequest.setLayoutData(skypeRequestGrid);

		/* Add smtp request label */
		Label smtpRequest = new Label(container, SWT.NONE);
		smtpRequest.setText(" - data of the SMTP server used to send the invitations");
		GridData smtpRequestGrid = new GridData();
		smtpRequestGrid.horizontalIndent = 10;
		smtpRequest.setLayoutData(smtpRequestGrid);
		
		/* Add a spacer */
		Label spacer3Label = new Label(container, SWT.NONE);
		GridData spacer3Grid = new GridData();
		spacer3Label.setLayoutData(spacer3Grid);
		
		/* Add hint label */
		Label hint = new Label(container, SWT.NONE);
		hint.setText("You can run the wizard again using menu: Options > Configuration wizard");
		GridData hintGrid = new GridData();
		hint.setLayoutData(hintGrid);
	}
}
