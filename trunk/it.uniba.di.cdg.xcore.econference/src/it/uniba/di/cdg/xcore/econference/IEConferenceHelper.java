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
package it.uniba.di.cdg.xcore.econference;

import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.preferences.EConferencePreferencePage;

/**
 * E-Conference helper provides some useful methods to access e-conferencing functions (basicly
 * starting a conference).
 */
public interface IEConferenceHelper {
    /**
     * To respond to the right invitations we use discriminating upong the reason filed associated to each invitation.
     * In case of the e-conferences, the coded reason is <b>e-conference</b>.
     */
    public static final String ECONFERENCE_REASON = "e-conference";
    
    public static final String ECONFERENCE_PREFS_NODE = "it.uniba.di.cdg.xcore.ui";
    
    public static final String AUTO_SAVE_LOGS = EConferencePreferencePage.AUTO_SAVE_LOGS;
    
    public static final String AUTO_SAVE_LOGS_DIR = EConferencePreferencePage.AUTO_SAVE_LOGS_DIR;
    
    public static final String URL_SHORTENER = EConferencePreferencePage.URL_SHORTENER;

    /**
     * Perform helper initialization.
     */
    void init();

    /**
     * Release all resources and listeners.
     */
    void dispose();

    /**
     * Open an e-conference perspective.
     * 
     * @param context
     * @return the create econference.
     */
    IEConferenceManager open( EConferenceContext context, boolean autojoin );

    /**
     * Open an e-conference loading its configuration from a file. The helper will show the dialog
     * and fill the context.
     */
    void openFromFile();
    
    void openFromFile(String filepath);
    
    void openInviteWizard();
    /**
     * Ask the user whether to accept the invitation. Note that this method will automatically rejects invitation
     * which reason is not {@see IEConferenceHelper#ECONFERENCE_REASON}.
     * 
     * @param invitation
     * @return <code>true</code> if the user said ok, <code>false</code> otherwise
     */
     EConferenceContext askUserAcceptInvitation( InvitationEvent invitation );
     
     /**
      * 
      * @param prefName
      * @return
      */
     boolean getBooleanPreference( final String prefName );
     
     /**
      * 
      * @param prefName
      * @return
      */
     String getStringPreference( final String prefName );
     
     
 	 public void setUIHelper(IUIHelper uihelper);
	
	 public void setBackendHelper(INetworkBackendHelper backendHelper);
	 
	 public IEConferenceManager getManager();
}
