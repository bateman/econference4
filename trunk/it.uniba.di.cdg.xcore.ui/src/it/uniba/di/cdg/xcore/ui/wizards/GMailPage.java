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

import it.uniba.di.cdg.xcore.ui.service.Auth;
import it.uniba.di.cdg.xcore.ui.service.Plusclass;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import org.eclipse.swt.widgets.Canvas;

/**
 * Wizard page for GMail account configuration
 * 
 * @see WizardPage
 * 
 *      Modified from Malerba Francesco in order to support the google+ account associated with the
 *      gmail mail.
 */

public class GMailPage extends WizardPage {

    private Text username; // GMail username text line

    private Text password; // GMail password text line

    private String imageurl;

    private String profileurl;

    private String plusid;

    private String name;

    // needed for gplus account info
    private Text token;

    boolean utenteloggato = false;

    boolean tokeninserito = false;

    private int codiceerrore;

    private Image im;

    /**
     * The constructor
     */
    protected GMailPage() {
        super( "GMail account" );
        setTitle( "GMail account" );
        setDescription( "Enter username and password, or leave blank and click next to skip this step" );
    }

    /**
     * Show the form
     * 
     * @see WizardPage#createControl(Composite)
     */
    public void createControl( final Composite parent ) {
        /* Search data in preferences */
        Preferences preferences = ConfigurationScope.INSTANCE
                .getNode( IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER );
        Preferences gmailPref = preferences.node( IConfigurationConstant.GMAIL );
        String usernamePref = gmailPref.get( IConfigurationConstant.USERNAME, "" );
        String passwordPref = gmailPref.get( IConfigurationConstant.PASSWORD, "" );

        /* Setup container layout */
        Composite container = new Composite( parent, SWT.NULL );
        org.eclipse.swt.layout.GridLayout g = new GridLayout();
        g.numColumns = 9;
        container.setLayout( g );
        setControl( container );

        /* Setup group layout */
        Group gmailGroup = new Group( container, SWT.SHADOW_IN );
        gmailGroup.setText( "GMail account data" );

        /* Add username label */
        GridLayout gl_gmailGroup = new GridLayout( 2, false );
        gmailGroup.setLayout( gl_gmailGroup );
        Label usernameLabel = new Label( gmailGroup, SWT.NONE );
        usernameLabel.setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, false, false, 1, 1 ) );
        usernameLabel.setText( "Username:" );

        /* Add username text line */
        username = new Text( gmailGroup, SWT.BORDER );
        GridData gd_username = new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 );
        gd_username.widthHint = 179;
        username.setLayoutData( gd_username );
        username.setText( usernamePref );
        username.addListener( SWT.Modify, new Listener() {

            /* Checks the validity of the data if it changes */
            @Override
            public void handleEvent( Event event ) {
                dataControl();
            }
        } );
        username.addListener( SWT.FocusOut, new Listener() {

            /* Add @gmail.com suffix to username if necessary */
            @Override
            public void handleEvent( Event event ) {
                String usernameRegex = "[a-zA-Z0-9._%-]+";
                Pattern usernamePattern = Pattern.compile( usernameRegex );
                Matcher usernameMatcher = usernamePattern.matcher( username.getText() );

                if (usernameMatcher.matches()) {
                    username.setText( username.getText() + "@gmail.com" );
                }
            }
        } );

        /* Add a spacer */
        new Label( gmailGroup, SWT.NONE );
        new Label( gmailGroup, SWT.NONE );
        Label spacer1Label = new Label( gmailGroup, SWT.NONE );
        spacer1Label.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );

        /* Add password label */
        Label passwordLabel = new Label( gmailGroup, SWT.NONE );
        passwordLabel.setText( "Password:" );

        /* Add password text line */
        password = new Text( gmailGroup, SWT.BORDER );
        password.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 ) );
        password.setEchoChar( '*' );
        password.setText( passwordPref );
        password.addListener( SWT.Modify, new Listener() {

            /* Checks the validity of the data if it changes */
            @Override
            public void handleEvent( Event event ) {
                dataControl();
            }
        } );

        new Label( gmailGroup, SWT.NONE );
        new Label( gmailGroup, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );

        // creating profile info layout

        Composite composite = new Composite( container, SWT.NONE );
        GridData gd_composite = new GridData( SWT.LEFT, SWT.CENTER, false, false, 4, 2 );
        gd_composite.heightHint = 105;
        gd_composite.widthHint = 179;
        composite.setLayoutData( gd_composite );

        final Label lblLoggedAs = new Label( composite, SWT.NONE );
        lblLoggedAs.setBounds( 0, 59, 64, 15 );
        lblLoggedAs.setText( "" );

        final Canvas canvas = new Canvas( composite, SWT.NONE );
        canvas.setBounds( 115, 0, 64, 64 );

        final Label lblName = new Label( composite, SWT.NONE );
        lblName.setBounds( 0, 80, 169, 15 );
        lblName.setText( "" );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        if (im != null) {
            GC gc = new GC( canvas );
            gc.drawImage( im, 0, 0 );
            gc.dispose();
        }
        // end creating profile info layout

        // creating validate account layout
        Group validateGroup = new Group( container, SWT.SHADOW_IN );
        GridData gd_validateGroup = new GridData( SWT.LEFT, SWT.CENTER, false, false, 1, 1 );
        gd_validateGroup.widthHint = 264;
        validateGroup.setLayoutData( gd_validateGroup );
        validateGroup.setText( "Validate GoogleAccount" );
        validateGroup.setLayout( new GridLayout( 1, false ) );

        final Button validateButton = new Button( validateGroup, SWT.NONE );
        validateButton.setText( "Connect Google Account" );
        new Label( validateGroup, SWT.NONE );
        new Label( validateGroup, SWT.NONE );

        Label tokenLabel = new Label( validateGroup, SWT.NONE );
        tokenLabel.setText( "Autentication token" );

        token = new Text( validateGroup, SWT.BORDER );
        token.setText( "" );
        GridData gd_token = new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 );
        gd_token.widthHint = 244;
        token.setLayoutData( gd_token );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );
        new Label( container, SWT.NONE );

        Listener buttonListener = new Listener() {
            @Override
            public void handleEvent( Event event ) {
                Auth autorizzazione = new Auth();
                if (!(tokeninserito))
                    try {
                        autorizzazione.askauthorize();

                    } catch (IOException e) {
                        codiceerrore = 1;
                        e.printStackTrace();
                        dataControl();

                    }

                else {
                    System.out.print( "Trying to autorize with token" );
                    String s = token.getText();
                    autorizzazione.authorize( s );

                    // use the token
                    String accesstoken = autorizzazione.getAccessToken();
                    if (!(accesstoken.equals( "" ))) {
                        try {
                            System.out.print( "Autorized, getAccessToken not empty" );
                            Plus plusdausare = Plusclass.getPlus( autorizzazione );
                            Person mePerson = null;
                            mePerson = plusdausare.people().get( "me" ).execute();

                            Plusclass.show( mePerson );
                            if (!(mePerson == null)) {
                                imageurl = mePerson.getImage().getUrl();
                                profileurl = mePerson.getUrl();
                                plusid = mePerson.getId();
                                name = mePerson.getDisplayName();
                                lblLoggedAs.setText( "Logged as:" );
                                lblName.setText( name );
                                ImageDescriptor i = ImageDescriptor.createFromURL( new URL(
                                        imageurl ) );
                                if (i == null)
                                    System.out.println( "image null" );
                                im = i.createImage();
                                if (im == null)
                                    System.out.println( "immage null" );

                                GC gc = new GC( canvas );
                                gc.drawImage( im, 0, 0 );
                                gc.dispose();
                                setMessage( "Autantication succeeded." );
                                validateButton.setEnabled( false );
                                token.setEnabled( false );
                                utenteloggato = true;
                                dataControl();
                            }
                            // gooogleplus profile is ok

                        } catch (Exception e) {

                            codiceerrore = 2;
                            validateButton.setText( "Connect Google Account" );
                            tokeninserito = false;
                            e.printStackTrace();
                            dataControl();
                        }
                    } else {
                        // validation of token failed, re execute the connection of the account
                        codiceerrore = 3;
                        tokeninserito = false;
                        validateButton.setText( "Connect Google Account" );
                        dataControl();
                    }

                }

            }

        };

        Listener tokenlistener = new Listener() {
            @Override
            public void handleEvent( Event event ) {

                try {
                    String contenutocopy;
                    // read what's inside ctrl c
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Clipboard clipboard = tk.getSystemClipboard();
                    Transferable t = clipboard.getContents( null );
                    contenutocopy = (String) t.getTransferData( DataFlavor.stringFlavor );

                    String contenutotoken = token.getText();

                    // System.out.println("Contenct of ctrl + c: " + contenutocopy);
                    if (!(contenutotoken.equals( contenutocopy ))) {
                        token.setText( contenutocopy );
                    }
                    validateButton.setText( "Validate Google Account" );
                    tokeninserito = true;
                    setMessage( "Now click on validate google account" );

                } catch (UnsupportedFlavorException e) {
                    setMessage( "Autentication failed.\nCopy the token from the Web Image and than select the Authorizazion token field" );
                    e.printStackTrace();
                } catch (IOException e) {
                    setMessage( "Autentication failed.\nCopy the token from the Web Image and than select the Authorizazion token field" );
                    e.printStackTrace();
                }

            }
        };

        token.addListener( SWT.FOCUSED, tokenlistener );
        validateButton.addListener( SWT.Selection, buttonListener );

        // end validate account layout

        dataControl();
    }

    /**
     * Checks the validity of user entered data and, if necessary, prevents from continuing in the
     * wizard
     */
    private void dataControl() {
        setPageComplete( false );
        setMessage( null );
        setErrorMessage( null );

        if (username.getText().length() == 0 && password.getText().length() == 0) {
            /* If there are no data allows you to skip this step */
            setPageComplete( true );
            setMessage( null );
            setErrorMessage( null );
            return;
        }

        if (username.getText().length() != 0 && password.getText().length() == 0) {
            /*
             * If there's username but password text line is empty, prevents from continuing in the
             * wizard
             */
            setMessage( "Complete data by entering password or delete username to skip this step" );
            return;
        }

        if (username.getText().length() == 0 && password.getText().length() != 0) {
            /*
             * If there's password but username text line is empty, prevents from continuing in the
             * wizard
             */
            setMessage( "Complete data by entering username or delete password to skip this step" );
            return;
        }

        String usernameRegex = "[a-zA-Z0-9._%-]+";
        Pattern usernamePattern = Pattern.compile( usernameRegex );
        Matcher usernameMatcher = usernamePattern.matcher( username.getText() );

        if (usernameMatcher.matches()) {
            /* Suggest to add @gmail.com suffix to username if necessary */
            setMessage( "Complete username with @gmail.com suffix" );
            return;
        }

        String completeUsernameRegex = "[a-zA-Z0-9._%-]+@gmail\\.com";
        Pattern completeUsernamePattern = Pattern.compile( completeUsernameRegex );
        Matcher completeUsernameMatcher = completeUsernamePattern.matcher( username.getText() );

        if (!completeUsernameMatcher.matches()) {
            /*
             * If username isn't valid, show error message and prevents from continuing in the
             * wizard
             */
            setErrorMessage( "Username not valid" );
            return;
        }

        if (codiceerrore == 1) {
            setErrorMessage( "Failed to ask authorization\nCopy the token from the Web Image and than select the Authorizazion token field" );
            return;
        }

        if (codiceerrore == 2) {
            setErrorMessage( "Autentication failed.\nCopy the token from the Web Image and than select the Authorizazion token field" );
            return;
        }

        if (codiceerrore == 3) {
            setErrorMessage( "Autentication failed.\nThe autantication token is empty or not valid, copy the token from the web image and than select the Authorizazion token field" );
            return;
        }

        // if he doesn't want to connect a google profile than
        // we can't connect him to googleplus
        if ((utenteloggato == false) && (username.getText().length() != 0)
                && (password.getText().length() != 0)) {
            return;

        }
        setMessage( "Account configuration complete.\nClick next to continue" );
        setPageComplete( true );
        setMessage( null );
        setErrorMessage( null );
    }

    /**
     * Return the GMail password
     * 
     * @return GMail password
     */
    public String getPassword() {
        return password.getText();
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
     * 
     * 
     * @return the GMAIL PROFILE INFO
     */
    public String getPlusId() {
        return plusid;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public String getProfileUrl() {
        return profileurl;
    }

    public String getName() {
        return name;
    }
}
