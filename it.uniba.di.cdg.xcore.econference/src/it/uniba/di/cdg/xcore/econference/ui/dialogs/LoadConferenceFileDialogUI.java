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
import it.uniba.di.cdg.xcore.econference.model.internal.ConferenceContextLoader;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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

    private Composite fileNameComposite = null;
    private Group conferenceOptionsGroup = null;
    private CLabel labelFileName = null;
    private Button selectFileButton = null;
    private Text selectedFileNameText = null;
    private Button sendInvitationsCheckBox = null;
    private CLabel cLabel = null;
    private Label label = null;
    private Text nickNameText = null;

    private EConferenceContext context;
    
    public LoadConferenceFileDialogUI( Composite parent, int style ) {
        super( parent, style );
        initialize();
        
        context = new EConferenceContext();
        IBackend b = NetworkPlugin.getDefault().getHelper().getRoster().getBackend();
        context.setBackendId(b.getBackendId());
    }

    private void initialize() {
        FillLayout fillLayout = new FillLayout();
        fillLayout.type = org.eclipse.swt.SWT.VERTICAL;
        createComposite();
        this.setLayout(fillLayout);
        createGroup();
        setSize(new org.eclipse.swt.graphics.Point(390,133));
    }

    /**
     * This method initializes composite	
     *
     */
    private void createComposite() {
        GridData gridData11 = new org.eclipse.swt.layout.GridData();
        gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData11.grabExcessHorizontalSpace = true;
        gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.horizontalSpacing = 5;
        gridLayout.marginWidth = 1;
        gridLayout.marginHeight = 1;
        gridLayout.verticalSpacing = 2;
        fileNameComposite = new Composite( this, SWT.NONE );
        fileNameComposite.setLayout(gridLayout);
        labelFileName = new CLabel(fileNameComposite, SWT.NONE);
        labelFileName.setText("Filename:");
        labelFileName.setLayoutData(gridData);
        selectedFileNameText = new Text(fileNameComposite, SWT.BORDER | SWT.READ_ONLY);
        selectedFileNameText.setLayoutData(gridData1);
        selectFileButton = new Button(fileNameComposite, SWT.NONE);
        selectFileButton.setText("Select ...");
        selectFileButton.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected( org.eclipse.swt.events.SelectionEvent e ) {
                final String fileName = UiPlugin.getUIHelper().requestFile( FILE_EXT );
                if (fileName == null) // CANCEL pressed
                    return;
                ConferenceContextLoader loader = new ConferenceContextLoader( context );
                try {
                    loader.load( fileName );
                    selectedFileNameText.setText( fileName );
                } catch (Exception ex) {
                    UiPlugin.getUIHelper().showErrorMessage( "Sorry, the specified file seems invalid: " 
                            + ex.getMessage() );
                    return;
                } 
                
                updateNickName();
                
                // Check if we can enable, disable the UI
                boolean isModerator = checkUserCanSendInvitations();
                sendInvitationsCheckBox.setEnabled( isModerator );
                // Avoid leaving the checkbox checked ;)
                sendInvitationsCheckBox.setSelection( isModerator );
            }
        } );
        cLabel = new CLabel(fileNameComposite, SWT.NONE);
        cLabel.setText("Nickname:");
        nickNameText = new Text(fileNameComposite, SWT.BORDER);
        nickNameText.setLayoutData(gridData11);
        label = new Label(fileNameComposite, SWT.NONE);
    }

    /**
     * @return <code>true</code> if the currently connected user is the moderator for conference file,
     *         <code>false</code> otherwise
     */
    private boolean checkUserCanSendInvitations() {
        String moderatorId = getContext().getModerator().getId();

        // XXX We should let the user to choice its own backend
        IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
        String currentUserId = b.getUserAccount().getId();
        
        boolean eq = moderatorId.indexOf( currentUserId ) > -1;
        // XXX Avoid sending an invitation to ourselves if we are moderators!!
        if (eq) {
            for (Iterator it = getContext().getInvitees().iterator(); it.hasNext(); ) {
                Invitee i = (Invitee) it.next();
                if (moderatorId.equals( i.getId() )) {
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
    private void updateNickName() {
        IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
        String myId = b.getUserAccount().getId();
        
        String myNickName = myId;
        
        for (Iterator it = getContext().getInvitees().iterator(); it.hasNext(); ) {
            Invitee i = (Invitee) it.next();
            if (i.getId().indexOf( myId ) > -1) {
                myNickName = i.getFullName();
                break;
            }
        }
        
        nickNameText.setText( myNickName );
    }
    
    /**
     * This method initializes group	
     *
     */
    private void createGroup() {
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.grabExcessVerticalSpace = false;
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData3.grabExcessHorizontalSpace = true;
        conferenceOptionsGroup = new Group( this, SWT.NONE );
        conferenceOptionsGroup.setText("Options");
        conferenceOptionsGroup.setLayout(new GridLayout());
        sendInvitationsCheckBox = new Button(conferenceOptionsGroup, SWT.CHECK);
        sendInvitationsCheckBox.setText("Send invitations");
        sendInvitationsCheckBox.setEnabled(false);
        sendInvitationsCheckBox.setLayoutData(gridData3);
    }
    
    public String getFileName() {
        return selectedFileNameText.getText();
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
    
}  //  @jve:decl-index=0:visual-constraint="61,47"
