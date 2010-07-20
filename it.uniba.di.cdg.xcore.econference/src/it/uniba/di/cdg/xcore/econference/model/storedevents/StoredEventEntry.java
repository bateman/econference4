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
package it.uniba.di.cdg.xcore.econference.model.storedevents;

import java.io.UnsupportedEncodingException;

import it.uniba.di.cdg.xcore.m2m.events.ConferenceOrganizationEvent;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;

/**
 * 
 */
public class StoredEventEntry implements IStoredEventEntry {

	private InvitationEvent event;

	/**
     * 
     */
	private static final String ENCODING = "iso-8859-1";

	/**
     * 
     */
	private String accountId;

	private String hash;

	/**
	 * 
	 * @param accountId
	 * @param backendId
	 * @param room
	 * @param inviter
	 * @param reason
	 * @param password
	 */
	public StoredEventEntry(String accountId, String backendId, String room,
			String inviter, String schedule, String reason, String password) {
		event = new InvitationEvent(backendId, room, inviter, schedule, reason,
				password);
		this.accountId = accountId;
	}

	/**
	 * 
	 * @param event
	 */
	public StoredEventEntry(InvitationEvent event) {
		this.event = event;
		this.accountId = "";
	}

	public StoredEventEntry(String accountid, String backendid, String room,
			String inviter, String schedule, String reason, String passwd,
			String[] invitees, String[] items) {
		event = new ConferenceOrganizationEvent(backendid, room, inviter,
				schedule, reason, passwd, invitees, items);
		this.accountId = accountid;
	}

	public InvitationEvent getEvent() {
		return event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry
	 * #getAccountId()
	 */
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.InvitationEvent#toString()
	 */
	@Override
	public String toString() {
		return String.format("Invitation to eConference %s",
				getRoom().split("@")[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry
	 * #getHash()
	 */
	public String getHash() {
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry
	 * #setHash(java.lang.String)
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry
	 * #getBytes()
	 */
	public byte[] getBytes() throws UnsupportedEncodingException {
		StringBuffer bf = new StringBuffer(accountId);
		bf.append(getReason()).append(getRoom()).append(getBackendId())
				.append(getInviter());
		return bf.toString().getBytes(ENCODING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object eentry) {
		if (eentry instanceof IStoredEventEntry) {
			IStoredEventEntry storedEntry = (IStoredEventEntry) eentry;
			return hash.equals(storedEntry.getHash());
		} else
			return false;

	}

	@Override
	public String getBackendId() {		
		return event.getBackendId();
	}

	@Override
	public String getInviter() {		
		return event.getInviter();
	}

	@Override
	public String getPassword() {
		return event.getPassword();
	}

	@Override
	public String getReason() {
		return event.getReason();
	}

	@Override
	public String getRoom() {
		return event.getRoom();
	}

	@Override
	public String getSchedule() {
		return event.getSchedule();
	}

	@Override
	public InvitationEvent getInvitationEvent() {		
		return event;
	}
}
