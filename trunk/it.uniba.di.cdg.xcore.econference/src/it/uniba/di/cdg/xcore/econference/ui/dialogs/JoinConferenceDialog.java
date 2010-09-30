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

import static it.uniba.di.cdg.xcore.util.Misc.isEmpty;
import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Collects the ECX filename to load, the user selected nickname and if  
 */
public class JoinConferenceDialog extends Dialog {
    protected LoadConferenceFileDialogUI ui = null;
    protected Composite composite = null;
    protected FillLayout layout = null;

    protected String fileName = "";
    protected String nickName = "";
    protected boolean sendInvitations = false;
    
    /**
     * Create a new dialog.
     * 
     * @param parentShell
     */
    public JoinConferenceDialog( Shell parentShell ) {
        super( parentShell );
    }
    
    public JoinConferenceDialog(Shell parentShell, String filepath) {		
		this(parentShell);
		setFileName(filepath);
	}

	/* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea( Composite parent ) {
        composite = (Composite) super.createDialogArea( parent );

        layout = new FillLayout( SWT.VERTICAL );
        composite.setLayout( layout );

        ui = new LoadConferenceFileDialogUI( composite, SWT.NONE, fileName );

        return composite;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        fileName = ui.getFileName();
        nickName = ui.getNickName();
        sendInvitations = ui.sendInvitations();
        
        if (validate()) {
            getContext().setNickName( nickName );
            super.okPressed();
        } else {
            UiPlugin.getUIHelper().showErrorMessage( "Please fill all required fields" );
        }
    }

    /**
     * Check if the inserted data are valid or not.
     * 
     * @return <code>true</code> if they are valid, <code>false</code> otherwise
     */
    protected boolean validate() {
        return !isEmpty( getFileName() ) && !isEmpty( getNickName() ) && getContext() != null;
    }

    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

    /**
     * @return Returns the nickName.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Returns if the user wants to send invitations (if the user is not the conference moderator
     * this method will always return false).
     * 
     * @return <code>true</code> if the user is the conference moderator and wants to send invitations, 
     *         <code>false</code> otherwise
     */
    public boolean isSendInvitations() {
        return sendInvitations;
    }

    /**
     * Returns the conference context loaded from file.
     * 
     * @return the conference context
     */
    public EConferenceContext getContext() {
        return ui.getContext();
    }
}
