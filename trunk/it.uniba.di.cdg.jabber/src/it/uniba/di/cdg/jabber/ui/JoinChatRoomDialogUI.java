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
package it.uniba.di.cdg.jabber.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * UI for creating a multichat context. 
 */
public class JoinChatRoomDialogUI extends Composite {

    private CLabel connectionLabel = null;
    protected Combo connectionCombo = null;
    private CLabel roomLabel = null;
    protected Text roomText = null;
    private CLabel serverNameLabel = null;
    protected Text serverText = null;
    private CLabel nickNameLabel = null;
    protected Text nickNameText = null;
    private CLabel passwordLabel = null;
    protected Text passwordText = null;
    
    /**
     * Create the dialog user interface.
     * 
     * @param parent
     * @param style
     */
    public JoinChatRoomDialogUI( Composite parent, int style ) {
        super( parent, style );
        initialize();
    }

    private void initialize() {
        GridData gridData7 = new org.eclipse.swt.layout.GridData();
        gridData7.grabExcessHorizontalSpace = true;
        gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData6 = new org.eclipse.swt.layout.GridData();
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData5 = new org.eclipse.swt.layout.GridData();
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData4 = new org.eclipse.swt.layout.GridData();
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData3 = new org.eclipse.swt.layout.GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        connectionLabel = new CLabel(this, SWT.NONE);
        connectionLabel.setText("Connection");
        createCombo();
        this.setLayout(gridLayout);
        this.setSize(new org.eclipse.swt.graphics.Point(320,157));
        roomLabel = new CLabel(this, SWT.NONE);
        roomLabel.setText("Room");
        roomLabel.setLayoutData(gridData5);
        roomText = new Text(this, SWT.BORDER);
        roomText.setText("testroom");
        roomText.setLayoutData(gridData1);
        serverNameLabel = new CLabel(this, SWT.NONE);
        serverNameLabel.setText("Server name");
        serverText = new Text(this, SWT.BORDER);
        serverText.setText("conference.casa");
        serverText.setLayoutData(gridData2);
        nickNameLabel = new CLabel(this, SWT.NONE);
        nickNameLabel.setText("NickName");
        nickNameLabel.setLayoutData(gridData3);
        nickNameText = new Text(this, SWT.BORDER);
        nickNameText.setText("Peppe");
        nickNameText.setLayoutData(gridData6);
        passwordLabel = new CLabel(this, SWT.NONE);
        passwordLabel.setText("Password");
        passwordLabel.setLayoutData(gridData4);
        passwordText = new Text(this, SWT.BORDER | SWT.PASSWORD);
        passwordText.setLayoutData(gridData7);
    }

    /**
     * This method initializes combo	
     */
    private void createCombo() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        connectionCombo = new Combo( this, SWT.NONE );
        connectionCombo.setLayoutData(gridData);
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
