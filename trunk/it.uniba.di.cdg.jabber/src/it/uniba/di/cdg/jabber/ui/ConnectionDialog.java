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
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.dialogs.DialogAccount;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Create a new connection dialog.
 */
public class ConnectionDialog extends Dialog {

    private static final String CONFIGURATION_NODE_QUALIFIER = UiPlugin.ID;

    public static final String USERID = "userid";

    private static final String PASSWORD = "password";

    private static final String SERVER = "server";

    private static final String SECURE = "secure";
    
    private static final String NAME = "name";

    private static final String EMAIL = "email";

    private static final String USE_DEFAULT_PORT = "default-port";

    private static final String PORT = "port";

    private static final String SAVED_PROFILES = "saved-connection-profiles";

    private static final String LAST_USER = "last-connection-profile";

    private ConnectionDialogUI ui;
    
    private Shell shell;
    
    private Composite parentcomposite;

    public ProfileContext profileContext;

    public Map<String, ProfileContext> savedProfileContexts;

    public ConnectionDialog( Shell parentShell ) {
        super( parentShell );
        shell = parentShell;
        savedProfileContexts = new HashMap<String, ProfileContext>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar( Composite parent ) {
        Button deleteUser = createButton( parent, IDialogConstants.CLIENT_ID, "&Delete", false );
        deleteUser.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected( SelectionEvent e ) {
                String profileID = ui.getProfileContext().getProfileName();
                savedProfileContexts.remove( profileID );

                Preferences preferences = new ConfigurationScope()
                        .getNode( CONFIGURATION_NODE_QUALIFIER );
                Preferences connections = preferences.node( SAVED_PROFILES );
                try {
                    if (connections.nodeExists( profileID )) {
                        connections.node( profileID ).removeNode();

                        connections = preferences.node( LAST_USER );

                        if (connections.nodeExists( profileID )) {
                            connections.node( profileID ).removeNode();
                        }
                        // FIXME
                        // I think it is right to simply call flush here
                        // instead of storing back again all the profiles
                        //savePreferences();
                        connections.flush();
                    }
                } catch (BackingStoreException bse) {
                    bse.printStackTrace();
                }

                ui.initializeUsers( "" );
            }
        } );

        super.createButtonsForButtonBar( parent );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea( Composite parent ) {

        Composite composite = (Composite) super.createDialogArea( parent );
        FillLayout layout = new FillLayout( SWT.VERTICAL );
        composite.setLayout( layout );

        loadPreferences();

        if (null == profileContext) {
            // create an empty profile
            profileContext = new ProfileContext();
        }

        this.ui = new ConnectionDialogUI( composite, SWT.NONE, savedProfileContexts, profileContext
                .getProfileName() );
        parentcomposite = composite;
        return composite;
    }

    protected void loadPreferences() {
        try {
            Preferences preferences = new ConfigurationScope()
                    .getNode( CONFIGURATION_NODE_QUALIFIER );
            Preferences connections = preferences.node( SAVED_PROFILES );
            String[] userNames = connections.childrenNames();

            for (int i = 0; i < userNames.length; i++) {
                String userName = userNames[i];
                Preferences node = connections.node( userName );
                UserContext ua = new UserContext( node.get( USERID, "" ), node.get( PASSWORD, "" ),node.get( NAME, "" ),node.get( EMAIL, "" ) );
                ServerContext sc = new ServerContext( node.get( SERVER, "jabber.org" ), Boolean
                        .parseBoolean( node.get( USE_DEFAULT_PORT, "true" ) ), Boolean
                        .parseBoolean( node.get( SECURE, "false" ) ), Integer.parseInt( node.get(
                        PORT, "5222" ) ) );
                savedProfileContexts.put(userName, new ProfileContext( userName, ua, sc,false ) );
            }
            profileContext = savedProfileContexts.get( preferences.get( LAST_USER, "" ) );
            System.out.println( "Last profile:" + profileContext );
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        // Save the user input before this dialog is disposed!
    	
        profileContext = ui.getProfileContext();
        if (profileContext.isValid()) {
        	if (profileContext.isNewAccount()){
        		//Display display = Display.getDefault();
    			//Shell shell = new Shell(display);
    			DialogAccount inst = new DialogAccount(shell, SWT.NULL,profileContext);
    			inst.open();
            } else {
            savedProfileContexts.put( profileContext.getProfileName(), profileContext );
            savePreferences();
            super.okPressed();
            }
        }  else {
            // FIXME doesn't ever show up
            UiPlugin.getUIHelper().showErrorMessage(
                    "The information you provided are either invalid or incomplete!" );
        }
    }

    /**
     * Stores the connection details preferences.
     */
    protected void savePreferences() {
        Preferences preferences = new ConfigurationScope().getNode( CONFIGURATION_NODE_QUALIFIER );
        preferences.put( LAST_USER, profileContext.getProfileName() );
        Preferences connections = preferences.node( SAVED_PROFILES );
        for (String profileId : savedProfileContexts.keySet()) {
            ProfileContext profile = savedProfileContexts.get( profileId );
            Preferences connection = connections.node( profileId );
            connection.put( SERVER, profile.getServerContext().getServerHost() );
            connection.put( SECURE, String.valueOf( profile.getServerContext().isSecure() ) );
            connection.put( PORT, String.valueOf( profile.getServerContext().getPort() ) );
            connection.put( USERID, profile.getUserContext().getId() );
            connection.put( PASSWORD, profile.getUserContext().getPassword() );
            connection.put( NAME, profile.getUserContext().getName() );
            connection.put( EMAIL, profile.getUserContext().getEmail() );
            connection.put( USE_DEFAULT_PORT, String.valueOf( profile.getServerContext()
                    .isUsingDefaultPort() ) );
        }
        try {
            connections.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the account context.
     * 
     * @return the account context or <code>null</code> if the user pressed <code>CANCEL</code>.
     */
    public ProfileContext getProfileContext() {
        return profileContext;
    }
}
