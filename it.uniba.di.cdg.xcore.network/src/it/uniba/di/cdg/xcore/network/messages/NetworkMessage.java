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
 * Implementation of a message. 
 */
public abstract class NetworkMessage implements IMessage {
    /**
     * The message type.
     */
    private final MessageType type;
    
    /**
     * The thread id.
     */
    private String threadId;

    private String from;
    
    private String text;
    
    private String subject;

    /**
     * Create a new message.
     * 
     * @param type
     * @param threadId
     * @param from
     * @param text
     * @param subject
     */
    public NetworkMessage( MessageType type, String threadId, String from, String text, String subject ) {
        super();
        this.type = type;
        this.threadId = threadId;
        this.from = from;
        this.text = text;
        this.subject = subject;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.messages.IMessage#getThreadId()
     */
    public String getThreadId() {
        return threadId;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.messages.IMessage#getFromBuddy()
     */
    public String getFrom() {
        return from;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.messages.IMessage#setFrom(java.lang.String)
     */
    public void setFrom( String from ) {
        this.from = from;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.messages.IMessage#getSubject()
     */
    public String getSubject() {
        return subject;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.messages.IMessage#getText()
     */
    public String getText() {
        return text;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.messages.IMessage#getType()
     */
    public MessageType getType() {
        return type;
    }
}
