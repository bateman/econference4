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
package it.uniba.di.cdg.xcore.m2m.service;

import it.uniba.di.cdg.xcore.m2m.events.IManagerEventListener;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.network.events.ITypingEventListener;
import it.uniba.di.cdg.xcore.network.events.ITypingListener;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.services.Capability;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.network.services.IRoomInfo;
import it.uniba.di.cdg.xcore.network.services.NetworkServiceException;

import java.util.List;

/**
 * A service which knows how to handle a conversation between serveral participants.
 * Implementations should define this and add the <code>MULTI_CHAT_SERVICE</code> capability.
 */
public interface IMultiChatService extends INetworkService, ITypingListener {
    /**
     * Constant indicating that the backend supports multi-peers chat.
     */
    public static final ICapability MULTI_CHAT_SERVICE = new Capability( "multi-chat" );

    /**
     * Listener for service local events (those which involves this local user only).
     */
    public static interface IMultiChatServiceListener {
        /**
         * We just joined the chat room.
         */
        void joined();

        /**
         * We just left the chat room.
         */
        void left();
    }
    
    /**
     * Returns the context associated to the chat service. 
     * 
     * Design note: we are using Java 5+ covariance to upcast an <code>INetworkServiceContext</code>
     * return type to needed <code>MultiChatContext</code>: clients won't need any cast themselves in this 
     * way. 
     * 
     * @return the context of this multi-chat
     */
    MultiChatContext getContext();

    /**
     * Returns the chat room object model: clients may want to use this for registering listeners.
     * 
     * @return the chat model
     */
    IChatRoomModel getModel();
    
    /**
     * Set the model to use.
     * 
     * @param model
     */
    void setChatRoomModel( IChatRoomModel model );
    
    /**
     * Changes the talk model to use.
     * 
     * @param model
     */
    void setTalkModel( ITalkModel model );
    
    /**
     * Returns the talk model storing the message entries.
     * 
     * @return the talk model
     */
    ITalkModel getTalkModel();
    
    /**
     * Returns the information for a chat room.
     * 
     * @param room the room name
     * @return the information requested about the room or <code>null</code> if the room is not existing
     */
    IRoomInfo getRoomInfo( String room );

    /**
     * Invite a new user to join the chatroom
     * 
     * @param userId
     * @param reason "Dear user, please come in ..."
     */
    void invite( String userId, String reason );

    /**
     * Decline an invitation.
     * 
     * @param event the invitation event we declining 
     * @param reason the reason we are declining the invitation (maybe <code>null</code>)
     */
    void declineInvitation( InvitationEvent event, String reason );
    
    /**
     * Join the chat room. After successfull join, all {@see IMultiChatServiceListener}s will be 
     * notified about the <code>joined()</code> event.
     */
    void join() throws NetworkServiceException;
    
    /**
     * Leave the chat room. Before leaving is performed, all {@see IMultiChatServiceListener}s will be 
     * notified about the <code>left()</code> event.
     */
    void leave();

    /**
     * Change the current discussion subject.
     * 
     * @param subject the new subject 
     */
    void changeSubject( String subject );
    
    /**
     * Send a private message to the specified user.
     * 
     * @param p
     * @param message
     */
    void sendPrivateMessage( IParticipant p, String message );

    /**
     * Let the user the ability to speak.
     * 
     * @param nickNames
     */
    void grantVoice( List<String> nickNames );

    /**
     * Convenient constructor for giving voice to the remote participant.
     * 
     * @param nickName
     */
    void grantVoice( String nickName );
    
    /**
     * Deny the ability to speak to an user.
     * 
     * @param nickNames
     */
    void revokeVoice( List<String> nickNames );

    /**
     * Revoke the ability to speak to the specified remote user.
     * 
     * @param nickName
     */
    void revokeVoice( String nickName );

    /**
     * Notify to remote clients that the specified view must be set as read-only/read-write.
     * 
     * @param viewId
     * @param readOnly
     */
    void notifyViewReadOnly( String viewId, boolean readOnly );
    
    /**
     * Broadcast a message to all participants of the chat: this is the normal usage of this service.
     * 
     * @param message
     */
    void sendMessage( String message );
    
    void addTypingEventListener( ITypingEventListener listener );
    
    void removeTypingEventListener( ITypingEventListener listener );
    
    void addServiceListener( IMultiChatServiceListener listener );
    
    void removeServiceListener( IMultiChatServiceListener listener );

    void addMessageReceivedListener( IMessageReceivedListener listener );

    void removeMessageReceivedListener( IMessageReceivedListener listener );

    void addInvitationRejectedListener( IInvitationRejectedListener listener );

    void removeInvitationRejectedListener( IInvitationRejectedListener listener );

    void addUserStatusListener( IUserStatusListener listener );

    void removeUserStatusListener( IUserStatusListener listener );
    
    void addManagerEventListener( IManagerEventListener listener );

    void removeManagerEventListener( IManagerEventListener listener );
}
