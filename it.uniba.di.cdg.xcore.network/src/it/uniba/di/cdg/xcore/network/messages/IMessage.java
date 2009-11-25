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
package it.uniba.di.cdg.xcore.network.messages;

/**
 * An abstract message.
 */
public interface IMessage {
    /**
     * Normal chat message (usually associated to a thread id).
     */
    public static final MessageType CHAT = new MessageType( "chat" );  
    
    /**
     * Single shop message (email type).
     */
    public static final MessageType NORMAL = new MessageType( "normal" );  
    
    /**
     * System message are sent by server to notify non-conversation events.
     */
    public static final MessageType SYSTEM = new MessageType( "system" );  

    /**
     * Multi-chat related messages.
     */
    public static final MessageType GROUP_CHAT = new MessageType( "group-chat" );
    
    public static final String LOCAL_USER = new String( "__LOCAL_USER__" );
    
    /**
     * Returns the message type of this message so clients can query it.
     * 
     * @return the message type
     */
    MessageType getType();

    /**
     * Returns he thread id, if present.
     * 
     * @return the thread id or <code>null</code> if no thread id is present / necessary
     */
    String getThreadId();

    /**
     * The message body.
     * 
     * @return the message body.
     */
    String getText();
    
    /**
     * The message subject, if 
     * @return
     */
    String getSubject();

    /**
     * The originator of the conversation
     * 
     * @return the id of the message originator or <code>null</code> if not present
     */
    String getFrom();
    
    /**
     * Set the originator of this message.
     * 
     * @param from
     */
    void setFrom( String from );
}
