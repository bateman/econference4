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
package it.uniba.di.cdg.jabber.internal;

import it.uniba.di.cdg.jabber.JabberBackend;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.network.messages.IMessage;

import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * We customize the invitation event providing custom code for invitation
 * rejectal.
 */
public class CustomInvitationEvent extends InvitationEvent {
	
	private static final int CUSTOM_EVENT_TYPE = 0x03;

	private JabberBackend backend;

	/**
	 * (Just pass the parameters to the base constructor)
	 * 
	 * @param backend
	 * @param backendId
	 * @param room
	 * @param inviter
	 * @param moderator
	 * @param reason
	 * @param password
	 * @param message
	 */
	public CustomInvitationEvent(JabberBackend backend, String backendId,
			String room, String schedule, String inviter, String reason,
			String password, IMessage message) {		
		super(backendId, room, inviter, schedule, reason, password);
		this.backend = backend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.InvitationEvent#decline(java.lang.String)
	 */
	@Override
	public void decline(String reason) {
		MultiUserChat.decline(backend.getConnection(), getRoom(), getInviter(),
				reason);
	}
	
	public int getEventType() {
    	return CUSTOM_EVENT_TYPE;
    }
}