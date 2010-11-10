/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
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
package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.ConferenceContextLoader;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * UI for the dialog: edit using the Visual Editor.
 */
public class LoadConferenceFileDialogUI extends Composite {
	/**
	 * EConference Xml file extension.
	 */
	public static final String[] FILE_EXT = new String[] { "*.ecx" };
	public static final String FILE_PATH = System.getProperty("user.home")
			+ System.getProperty("file.separator") + ".econference"
			+ System.getProperty("file.separator");

	protected Composite fileNameComposite = new Composite(this, SWT.NONE);
	protected GridData gridData11 = new org.eclipse.swt.layout.GridData(250, 15);
	protected Group conferenceOptionsGroup = new Group(this, SWT.NONE);
	protected CLabel labelFileName = null;
	protected Button selectFileButton = null;
	protected Text selectedFileNameText = null;
	protected Button sendInvitationsCheckBox = null;
	protected CLabel cLabel = null;
	protected Text nickNameText = null;

	protected EConferenceContext context = null;	
	protected String fileName = "";
	
	protected SelectionAdapter listener = new SelectionAdapter() {
		
		public void widgetSelected(
				org.eclipse.swt.events.SelectionEvent e) {
			final String fileName = UiPlugin.getUIHelper()
					.requestFile(FILE_EXT, FILE_PATH);
			if (fileName == null) // CANCEL pressed
				return;

			setupContext(fileName);
		}
	};
	
	
	public LoadConferenceFileDialogUI(Composite parent, int style) {
		super(parent, style);
	}
	
	public LoadConferenceFileDialogUI(Composite parent, int style,
			String fileName) {
		super(parent, style);
		this.fileName = fileName;		

		context = new EConferenceContext();
		IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
		context.setBackendId(b.getBackendId());
		
		initialize();
	}

	private void initialize() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = org.eclipse.swt.SWT.VERTICAL;
		createComposite();
		this.setLayout(fillLayout);
		createGroup();
		setSize(new org.eclipse.swt.graphics.Point(390, 133));
		if (fileName != null && !fileName.equals(""))
			setupContext(fileName);
	}

	/**
	 * This method initializes composite
	 * 
	 */
	protected void createComposite() {		
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData(250, 15);
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData = new org.eclipse.swt.layout.GridData(250, 15);
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
		gridLayout.verticalSpacing = 2;

		fileNameComposite.setLayout(gridLayout);
		labelFileName = new CLabel(fileNameComposite, SWT.NONE);
		labelFileName.setText("Filename:");
		selectedFileNameText = new Text(fileNameComposite, SWT.BORDER
				| SWT.READ_ONLY);
		selectedFileNameText.setLayoutData(gridData1);
		selectedFileNameText.setText(fileName);
		selectFileButton = new Button(fileNameComposite, SWT.NONE);
		selectFileButton.setText("Browse...");
		selectFileButton.addSelectionListener(listener);
		cLabel = new CLabel(fileNameComposite, SWT.NONE);
		cLabel.setText("Nickname:");
		nickNameText = new Text(fileNameComposite, SWT.BORDER);
		nickNameText.setLayoutData(gridData11);
		new Label(fileNameComposite, SWT.NONE);
	}

	private void setupContext(String fileName) {		
		ConferenceContextLoader loader = new ConferenceContextLoader(context);
		try {
			loader.load(fileName);
			selectedFileNameText.setText(fileName);
		} catch (Exception ex) {
			UiPlugin.getUIHelper().showErrorMessage(
					"Sorry, the specified file seems invalid: "
							+ ex.getMessage());
			return;
		}

		updateNickName();

		// Check if we can enable, disable the UI
		boolean isModerator = checkUserCanSendInvitations();
		sendInvitationsCheckBox.setEnabled(isModerator);
		// Avoid leaving the checkbox checked ;)
		sendInvitationsCheckBox.setSelection(isModerator);
	}

	/**
	 * @return <code>true</code> if the currently connected user is the
	 *         moderator for conference file, <code>false</code> otherwise
	 */
	protected boolean checkUserCanSendInvitations() {
		String moderatorId = getContext().getModerator().getId();

		IBackend b = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend();
		String currentUserId = b.getUserId();

		boolean eq = moderatorId.equals(currentUserId);
		// Avoid sending an invitation to ourselves if we are moderators!!
		if (eq) {
			for (Iterator<Invitee> it = getContext().getInvitees().iterator(); it
					.hasNext();) {
				Invitee i = (Invitee) it.next();
				if (moderatorId.equals(i.getId())) {
					it.remove();
					break;
				}
			}
		}

		return eq;
	}

	/**
	 * Replace our id with the fullname found in the invitation.
	 */
	protected void updateNickName() {
		IBackend b = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend();
		String myId = b.getUserAccount().getId();

		String myNickName = myId;

		for (Iterator<Invitee> it = getContext().getInvitees().iterator(); it
				.hasNext();) {
			Invitee i = (Invitee) it.next();
			if (i.getId().equals(myId)) {
				myNickName = i.getFullName();
				break;
			}
		}

		nickNameText.setText(myNickName);
	}

	/**
	 * This method initializes group
	 */
	protected void createGroup() {
		GridData gridData3 = new org.eclipse.swt.layout.GridData();
		gridData3.grabExcessVerticalSpace = false;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = true;
		
		conferenceOptionsGroup.setText("Options");
		conferenceOptionsGroup.setLayout(new GridLayout());
		sendInvitationsCheckBox = new Button(conferenceOptionsGroup, SWT.CHECK);
		sendInvitationsCheckBox.setText("Send invitations");
		sendInvitationsCheckBox.setEnabled(false);
		sendInvitationsCheckBox.setLayoutData(gridData3);
	}

	public String getFileName() {
		return fileName = selectedFileNameText.getText();
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getNickName() {
		return nickNameText.getText();
	}

	public boolean sendInvitations() {
		return sendInvitationsCheckBox.getSelection();
	}

	public EConferenceContext getContext() {
		return context;
	}

} // @jve:decl-index=0:visual-constraint="61,47"
