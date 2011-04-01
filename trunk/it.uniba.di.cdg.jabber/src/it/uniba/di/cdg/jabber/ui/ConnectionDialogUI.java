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

import it.uniba.di.cdg.xcore.network.ProfileContext;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;

import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
/**
 * 
 */
public class ConnectionDialogUI extends Composite {

	private Group groupServerContext = null;

	private Label serverHostLabel = null;

	private Group groupBuddy = null;

	private Text passwordField = null;

	private Label passwordFieldLabel = null;

	private Label jidLabel = null;

	private CCombo jabberIdCombo = null;

	// Label profileIdLabel = null;

	// private CCombo profileIdCombo = null;

	private Label portNumberLabel = null;

	private Text serverHostField = null;

	private Label secureCheckLabel = null;

	private Text portNumberField = null;
	private Button checkNewAccount;

	private Button secureCheck = null;

	private Label useDefaultPortCheckLabel;

	private Button useDefaultPortCheck;

	private Map<String, ProfileContext> savedProfileContexts;

	private Button checkSaveProfile;
	
    private Preferences gmailPref;

	public ConnectionDialogUI(Composite parent, int style,
			Map<String, ProfileContext> savedProfileContexts,
			String lastLoadedProfile) {
		super(parent, style);
		this.savedProfileContexts = savedProfileContexts;
        gmailPref = new ConfigurationScope()
        	.getNode( IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER )
        	.node(IConfigurationConstant.GMAIL);
		initialize(lastLoadedProfile);
	}

	private void initialize(String lastLoadedProfile) {
		GridLayout fillLayout = new GridLayout();
		fillLayout.makeColumnsEqualWidth = true;
		this.setLayout(fillLayout);
		createLoginGroup();
		createServerGroup();
		initializeUsers(lastLoadedProfile);
		pack();
	}

	protected void initializeUsers(final String lastProfile) {
		jabberIdCombo.removeAll();
		jabberIdCombo.setText("");
		passwordField.setText("");
		serverHostField.setText("");
		for (String prof : savedProfileContexts.keySet()) {
			jabberIdCombo.add(prof);
		}
		if (lastProfile != null) {
			int index = Math.max(jabberIdCombo.indexOf(lastProfile), 0);
			jabberIdCombo.select(index);
		}
	}

	/**
	 * This method initializes group
	 */
	private void createServerGroup() {
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = false;
		gridData2.horizontalIndent = 0;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData1.widthHint = 60;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		groupServerContext = new Group(this, SWT.FILL);
		groupServerContext.setText("Server:");
		groupServerContext.setLayout(gridLayout1);
		GridData groupServerContextLData = new GridData();
		groupServerContextLData.widthHint = 323;
		groupServerContextLData.heightHint = 113;
		groupServerContext.setLayoutData(groupServerContextLData);
		serverHostLabel = new Label(groupServerContext, SWT.NONE);
		serverHostLabel.setText("&Host:");
		serverHostField = new Text(groupServerContext, SWT.BORDER);
		serverHostField.setText("ugres.di.uniba.it");
		serverHostField.setLayoutData(gridData);
		serverHostField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				serverHostField.setSelection(0, serverHostField.getText()
						.length());
			}
		});
		useDefaultPortCheckLabel = new Label(groupServerContext, SWT.NONE);
		useDefaultPortCheckLabel.setText("Use &default port");
		useDefaultPortCheck = new Button(groupServerContext, SWT.CHECK);
		useDefaultPortCheck.setSelection(true);
		useDefaultPortCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selected = useDefaultPortCheck.getSelection();
				if (selected) {
					portNumberField.setEnabled(false);
				} else {
					portNumberField.setEnabled(true);
				}
			}
		});
		portNumberLabel = new Label(groupServerContext, SWT.NONE);
		portNumberLabel.setText("P&ort:");
		portNumberField = new Text(groupServerContext, SWT.BORDER);
		portNumberField.setText("5222");
		portNumberField.setEnabled(false);
		portNumberField.setLayoutData(gridData1);
		portNumberField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				portNumberField.setSelection(0, portNumberField.getText()
						.length());
			}
		});
		secureCheckLabel = new Label(groupServerContext, SWT.NONE);
		secureCheckLabel.setText("&Secure:");
		secureCheckLabel.setLayoutData(gridData2);
		secureCheck = new Button(groupServerContext, SWT.CHECK);
		secureCheck.setSelection(false);
		secureCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (useDefaultPortCheck.getSelection() == true) {
					boolean selected = secureCheck.getSelection();
					if (selected) {
						portNumberField.setText("5223");
					} else {
						portNumberField.setText("5222");
					}
				}
			}
		});
	}

	/**
	 * Returns the profile the user wants to use.
	 * 
	 * @return the profile context
	 */
	public ProfileContext getProfileContext() {

		final String serverHost = serverHostField.getText();
		final boolean defaultPort = useDefaultPortCheck.getSelection();
		final int port = Integer.parseInt(portNumberField.getText());
		final boolean secure = secureCheck.getSelection();
		ServerContext sc = new ServerContext(serverHost, defaultPort, secure,
				port);

		final String jid = jabberIdCombo.getText();
		final String password = passwordField.getText();

		UserContext ua = new UserContext(jid, password);
		final boolean newacc = checkNewAccount.getSelection();
		return new ProfileContext(jabberIdCombo.getText(), ua, sc, newacc);
	}

	public boolean isSaveProfileChecked() {
		return checkSaveProfile.getSelection();
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createLoginGroup() {
        groupBuddy = new Group( this, SWT.FILL );
        groupBuddy.setText( "Login as:" );
        groupBuddy.setLayout(new GridLayout(2,false));
        GridData groupBuddyLData = new GridData();
        groupBuddyLData.widthHint = 325;
        groupBuddyLData.heightHint = 118;
        groupBuddy.setLayoutData(groupBuddyLData);
//        {
//        	profileIdLabel = new Label(groupBuddy, SWT.NONE);
//        	profileIdLabel.setText( "&Profile:" );
//        	profileIdLabel.setBounds(8, 22, 34, 13);
//        }
//        profileIdCombo = new CCombo( groupBuddy, SWT.BORDER );
//        profileIdCombo.setBounds(85, 22, 158, 19);
//        profileIdCombo.addListener( SWT.Modify, new Listener() {
//            public void handleEvent( Event event ) {
//                ProfileContext profile = savedProfileContexts.get( profileIdCombo.getText() );
//                if (null != profile) {
//                    UserContext ua = profile.getUserContext();
//                    jabberIdField.setText( ua.getId() );
//                    passwordField.setText( ua.getPassword() );
//                    
//                    ServerContext sc = profile.getServerContext();
//                    serverHostField.setText( sc.getServerHost() );
//                    secureCheck.setSelection( sc.isSecure() );
//                    portNumberField.setText( String.valueOf( (sc.getPort()) ) );
//                    useDefaultPortCheck.setSelection(sc.isUsingDefaultPort());
//                    if(useDefaultPortCheck.getSelection() == true) {
//                        portNumberField.setEnabled(false);
//                    } else {
//                        portNumberField.setEnabled(true);
//                    }
//                }
//            }
//        } );
        
        GridData gd = new GridData();        
        jidLabel = new Label( groupBuddy, SWT.NONE );
        jidLabel.setText( "&Id:" );
        jidLabel.setLayoutData(gd);
        
        gd = new GridData(); 
        gd.widthHint= 200;
        jabberIdCombo = new CCombo(groupBuddy, SWT.BORDER );
        jabberIdCombo.setToolTipText("Enter the user id.\n john.doe for Jabber servers\njohn.doe@gmail.com for GTalk");
        jabberIdCombo.setLayoutData(gd);
        jabberIdCombo.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained( FocusEvent e ) {
                //jabberIdField.setSelection(0, jabberIdField.getText().length());
            }
            
        });
        jabberIdCombo.addListener( SWT.Modify, new Listener() {
        	public void handleEvent( Event event ) {
        		ProfileContext profile = savedProfileContexts.get( jabberIdCombo.getText() );    
        		ServerContext sc = null;
        		if (null != profile) {
        			UserContext ua = profile.getUserContext();
        			//jabberIdCombo.setText( ua.getId() );
        			checkSaveProfile.setSelection(true);
        			passwordField.setText( ua.getPassword() );

        			sc = profile.getServerContext();
        			if(jabberIdCombo.getText().compareTo(gmailPref.get(IConfigurationConstant.USERNAME, ""))==0){
        				passwordField.setEnabled(false);
        				checkSaveProfile.setEnabled(false);
        				checkNewAccount.setEnabled(false);
        				serverHostField.setEnabled(false);
        				useDefaultPortCheck.setEnabled(false);
        				secureCheck.setEnabled(false);
        				portNumberField.setEnabled(false);	
	        		}else{
        				passwordField.setEnabled(true);
        				checkSaveProfile.setEnabled(true);
        				checkNewAccount.setEnabled(true);
        				serverHostField.setEnabled(true);
        				useDefaultPortCheck.setEnabled(true);
        				secureCheck.setEnabled(true);
        				portNumberField.setEnabled(true);	
	        		}
        		}else{
        			checkSaveProfile.setSelection(false);
        			passwordField.setText("");
        			//check if is a gmail.com/googlemail.com account, if yes we must set server settings
        			if(jabberIdCombo.getText().contains("gmail.com") ||
        					jabberIdCombo.getText().contains("googlemail.com")){
        				sc = ServerContext.GOOGLE_TALK;
        				serverHostField.setEnabled(false);
        				useDefaultPortCheck.setEnabled(false);
        				secureCheck.setEnabled(false);
	        			portNumberField.setEnabled(false);			
        			} else {
        				serverHostField.setEnabled(true);
        				useDefaultPortCheck.setEnabled(true);
        				secureCheck.setEnabled(true);
        				portNumberField.setEnabled(true); 
        			}
        		}
        		if(sc!=null){
        			serverHostField.setText( sc.getServerHost() );
        			secureCheck.setSelection( sc.isSecure() );
        			portNumberField.setText( String.valueOf( (sc.getPort()) ) );
        			useDefaultPortCheck.setSelection(sc.isUsingDefaultPort());
        			if(useDefaultPortCheck.getSelection() == true) {
        				portNumberField.setEnabled(false);
        			} else {
        				portNumberField.setEnabled(true);
        			}  
        		}

        	}
        } );
        gd = new GridData();
        passwordFieldLabel = new Label( groupBuddy, SWT.NONE );
        passwordFieldLabel.setText( "Pass&word:" );
        passwordFieldLabel.setLayoutData(gd);
        
        gd = new GridData();
        gd.widthHint= 200;
         passwordField = new Text( groupBuddy, SWT.BORDER | SWT.PASSWORD );
        passwordField.setLayoutData(gd);
        passwordField.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusGained( FocusEvent e ) {
        		passwordField.setSelection(0, passwordField.getText().length());
        	}            
        });
        
        gd = new GridData();
        gd.horizontalSpan = 2;
        checkSaveProfile = new Button(groupBuddy, SWT.CHECK | SWT.LEFT);
        checkSaveProfile.setText("Save profile");
        checkSaveProfile.setLayoutData(gd);
        
        gd = new GridData();
        gd.horizontalSpan = 2;
    	checkNewAccount = new Button(groupBuddy, SWT.CHECK | SWT.LEFT);
    	checkNewAccount.setText("Register New Account (only for XMPP servers)");
    	checkNewAccount.setLayoutData(gd);
    }
} // @jve:decl-index=0:visual-constraint="10,10"
