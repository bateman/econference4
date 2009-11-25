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
package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.events.ITypingEventListener;
import it.uniba.di.cdg.xcore.network.events.ITypingListener;
import it.uniba.di.cdg.xcore.network.messages.IMessage;
import it.uniba.di.cdg.xcore.network.services.Capability;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.network.services.INetworkServiceContext;
import it.uniba.di.cdg.xcore.network.services.NetworkServiceException;

/**
 * An implementation-dependant chat service: user interfaces are expected to wrap and use
 * it.
 * <p>
 * Typical life cycle is:
 * <ol>
 * <li>create the chat</li>
 * <li>... use the chat ...</li>
 * <li>close the chat by calling <b>close()</b></li>
 * </ol>
 */
public interface IChatService extends INetworkService, ITypingListener {
    /**
     * Constant indicating that the backend supports peer-to-peer chat.
     */
    public static final ICapability CHAT_SERVICE = new Capability( "chat" );

    /**
     * The chat service context: needs only the buddy to chat with.
     */
    public static class ChatContext implements INetworkServiceContext {
        /**
         * The buddy we are chatting with.
         */
        private String buddyId;
        
        /**
         * A chat may be initiated by another user, sending a message to us. We store
         * that message here: it will probably be null if this local user has initiated
         * the chat, instead.
         */
        private IMessage helloMessage;
        
        /**
         * Creates a new chat context regarding the specified buddy
         * 
         * @param buddyId
         */
        public ChatContext( String buddyId ) {
            this( buddyId, null );
        }

        /**
         * Create a new context for a chat, which includes a buddy an the hello message.
         * This constructor is useful for letting the chat service to know that a remote
         * user wants to chat with us.
         * 
         * @param buddyId
         * @param helloMessage
         */
        public ChatContext( String buddyId, IMessage helloMessage ) {
            this.buddyId = buddyId;
            this.helloMessage = helloMessage;
        }

        /**
         * @return Returns the buddy.
         */
        public String getBuddyId() {
            return buddyId;
        }

        /**
         * @return Returns the helloMessage.
         */
        public IMessage getHelloMessage() {
            return helloMessage;
        }
    }

    /**
     * Interface for listeners of this chat service.
     */
    public interface IChatListener {
        /**
         * Notified when the other buddy sends a text message.
         * 
         * @param message the receivied text.
         */
        void messageReceived( IMessage message );
        
        /**
         * Notified when the buddy's presence status changes.
         */
        void presenceChanged();
    }
    
    /**
     * Returns the context associated to the chat service. 
     * 
     * Design note: we are using Java 5+ covariance to upcast an <code>INetworkServiceContext</code>
     * return type to needed <code>ChatContext</code>: clients won't need any cast themselves in this 
     * way. 
     * 
     * @return the context
     */
    ChatContext getContext();
    
    /**
     * Initialize the chat.
     */
    void open();

    /**
     * Set the helper to use. This method is intended to be used by the manager for IoC.
     * 
     * @param helper
     */
    void setHelper( INetworkBackendHelper helper );
    
    /**
     * Returns the network helper.
     * 
     * @return the network helper
     */
    INetworkBackendHelper getHelper();
    
    /**
     * Send a chat message to the buddy.
     * 
     * @param message the chat message  
     */
    void sendMessage( IChatMessage message ) throws NetworkServiceException;
    
    /**
     * Add a listener for chat events.
     * 
     * @param listener
     */
    void addChatListener( IChatListener listener );
    
    /**
     * Remove a listener fro chat events.
     * 
     * @param listener
     */
    void removeChatListener( IChatListener listener );

    String getLocalUserId();
    
    /**
     * Close the chat (the event listeners will be deregistered). 
     */
    void close();
    
    void addTypingEventListener( ITypingEventListener listener );
    
    void removeTypingEventListener( ITypingEventListener listener );
}
