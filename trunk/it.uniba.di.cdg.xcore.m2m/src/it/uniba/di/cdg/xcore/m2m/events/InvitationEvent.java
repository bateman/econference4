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
package it.uniba.di.cdg.xcore.m2m.events;

import it.uniba.di.cdg.xcore.network.events.AbstractBackendEvent;
import it.uniba.di.cdg.xcore.network.messages.IMessage;

/**
 * An invitation event is a a remote request from someone to participate to a 
 * multichat conversation. 
 */
public class InvitationEvent extends AbstractBackendEvent {
    /**
     * The room name (id) to join to.
     */
    private final String room;

    /**
     * Who is inviting.
     */
    private final String inviter;

    /**
     * The reason the remote use is sending to explain why the user should join.
     */
    private final String reason;

    /**
     * The (optional) password that should be used when joining the chat room.
     */
    private final String password;

    /**
     * The first message to display on the chat.
     */
    private final IMessage message;

    /**
     * Construct a new invitation event.
     * 
     * @param room
     * @param inviter
     * @param reason
     * @param password
     * @param message
     */
    public InvitationEvent( String backendId, String room, String inviter, String reason,
            String password, IMessage message ) {
        super( backendId );
        this.room = room;
        this.inviter = inviter;
        this.reason = reason;
        this.password = (password == null ? "" : password);
        this.message = message;
    }

    /**
     * @return Returns the inviter.
     */
    public String getInviter() {
        return inviter;
    }

    /**
     * @return Returns the message.
     */
    public IMessage getMessage() {
        return message;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return Returns the reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return Returns the room.
     */
    public String getRoom() {
        return room;
    }

    /**
     * Decline the invitation: this method is left to implementation for performing custom
     * declination.
     * 
     * @param reason the reason the user is declining the invitation
     */
    public void decline( String reason ) {
        // By default it does nothing: implementation should implement this
    }
}
