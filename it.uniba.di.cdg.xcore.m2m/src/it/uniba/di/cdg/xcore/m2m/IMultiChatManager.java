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
package it.uniba.di.cdg.xcore.m2m;

import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.service.IMultiChatService;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.IServiceManager;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.views.ITalkView;

import java.util.List;

import org.eclipse.ui.IWorkbenchWindow;

/**
 * A multichat represents the controller of a multi user chat: it provides access to the views,
 * models and network service. In addition it implements a set of functionalities that are called by
 * the defined actions.
 */
public interface IMultiChatManager extends IServiceManager, IRoleProvider {
    /**
     * Listeners interested to open and close events of this multichat.
     */
    public static interface IMultiChatListener {
        void open();

        void closed();
    }

    /**
     * Open the multichat: views are created, connection established, listeners registered and the
     * perspective is switched as needed. Ultimately <code>IMultiChatService.join()</code> will be
     * called if nothing bad happens.
     * 
     * @param context
     *        the context for initializing the chat room
     * @return <code>true</code> if the chat was successfully open, <code>false</code> otherwise
     * @throws Exception
     *         if something bad happens
     */
    public void open( MultiChatContext context, boolean autojoin ) throws Exception;

    /**
     * Close the multichat.
     */
    public void close();

    /**
     * Returns the view displaying the ongoing chat.
     * 
     * @return the view
     */
    IChatRoomView getChatRoomView();

    /**
     * Returns the ongoing talk view.
     * 
     * @return the talk view or <code>null</code> if none is set
     */
    ITalkView getTalkView();

    /**
     * The network service that implement communication with the remote clients.
     * 
     * @return the multi chat service
     */
    IMultiChatService getService();

    /**
     * Change the subject of the current chatroom: nothing will happen if the user hasn't privileges
     * needed.
     */
    void changeSubject( String subject );

    /**
     * Switch a participant to frozen state if it wasn't and unfreezes frozen participants.
     */
    void toggleFreezeUnfreeze( List<IParticipant> participants );

    /**
     * Send a single-shot message to the specified participants.
     * 
     * @param text
     */
    void sendPrivateMessage( List<IParticipant> participants, String text );

    /**
     * Send an invitatation to the specified user. If the invitation is accepted the recipient user
     * will log on the chat room otherwise all {@see
     * it.uniba.di.cdg.xcore.m2m.service.IInvitationRejectedListener}s will be notified about
     * rejection.
     * 
     * @param participantId
     */
    void inviteNewParticipant( String participantId );

    /**
     * Notify remote clients that the a view must be set as read-only.
     * 
     * @param viewId
     * @param readOnly
     */
    void notifyViewReadOnly( String viewId, boolean readOnly );

    /**
     * Add a new listener for multichat events.
     * 
     * @param listener
     */
    void addListener( IMultiChatListener listener );

    /**
     * Remove a listener for multichat events.
     * 
     * @param listener
     */
    void removeListener( IMultiChatListener listener );

    /**
     * Returns the helper for manipulating backends.
     * 
     * @return the backend helper
     */
    INetworkBackendHelper getBackendHelper();

    /**
     * Set the backend helper to use.
     * 
     * @param backendHelper
     *        The backendHelper to set.
     */
    void setBackendHelper( INetworkBackendHelper backendHelper );

    /**
     * Returns the UI helper which provides user interaction features.
     * 
     * @return the UI helper
     */
    IUIHelper getUihelper();

    /**
     * Changes the UI helper to use for interaction with the user.
     * 
     * @param uihelper
     *        The uihelper to set.
     */
    void setUihelper( IUIHelper uihelper );

    /**
     * Returns the workbench windows this manager is using.
     * 
     * @return Returns the workbenchWindow.
     */
    IWorkbenchWindow getWorkbenchWindow();

    /**
     * Changes the workbench window. Clients are not expected to use this method: it is used by the
     * helper to initialize the manager.
     * 
     * @param workbenchWindow
     *        The workbenchWindow to set.
     */
    void setWorkbenchWindow( IWorkbenchWindow workbenchWindow );
}
