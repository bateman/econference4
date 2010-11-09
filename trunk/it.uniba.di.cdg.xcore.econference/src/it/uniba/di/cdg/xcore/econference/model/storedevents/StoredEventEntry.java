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

import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.m2m.events.ConferenceOrganizationEvent;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;

import java.io.UnsupportedEncodingException;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */
public class StoredEventEntry implements IStoredEventEntry {

	private InvitationEvent invitationEvent;

	/**
     * 
     */
	private static final String ENCODING = "iso-8859-1";

	/**
     * 
     */
	private String accountId;

	private String hash;

	private String receivedOn;

	/**
	 * 
	 * @param accountId
	 * @param backendId
	 * @param room
	 * @param inviter
	 * @param reason
	 * @param password
	 * @param time 
	 */
	public StoredEventEntry(String accountId, String backendId, String room,
			String inviter, String schedule, String reason, String password, String time) {
		invitationEvent = new InvitationEvent(backendId, room, inviter, schedule, reason,
				password);
		this.accountId = accountId;
		this.setReceivedOn(time);
	}

	/**
	 * 
	 * @param event
	 */
	public StoredEventEntry(InvitationEvent event) {
		this.invitationEvent = event;
		this.accountId = "";
		this.receivedOn = "";
	}

	public StoredEventEntry(String accountid, String backendid, String room,
			String inviter, String schedule, String reason, String passwd,
			String[] invitees, String[] items, String time) {
		invitationEvent = new ConferenceOrganizationEvent(backendid, room, inviter,
				schedule, reason, passwd, invitees, items);
		this.accountId = accountid;
		this.setReceivedOn(time);
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
		String room = getRoom();
		if (room.contains("@")) {
			room = room.substring(0, room.indexOf("@"));
		}
		String type = null;
		switch (invitationEvent.getEventType()) {
		case InvitationEvent.INVITATION_EVENT_TYPE:
			type = "Invitation to event";
			if (room.contains("$") && room.startsWith("#")) {
				room = "from " + invitationEvent.getInviter();
			}
			break;
		case ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE:
			type = "Organization of event";
			if (room.contains("$")) {
				room = room.substring(0, room.indexOf("$"));
			}
			break;
		default:
			type = "Error: ";
			break;
		} 
		  
		return String.format("%s %s", type, room);
	}
	
	public Image getImage() {
		Image image = null;
		String imagePath = "";
		String EVENT_LOADER_ID = "it.uniba.di.cdg.xcore.econference.eventloader";
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EVENT_LOADER_ID);
		try {
			for (IConfigurationElement e : config) {
				String loaderService = e.getAttribute("service");
				if (invitationEvent.getReason().equals(loaderService)) {
					
					switch (invitationEvent.getEventType()) {
					case InvitationEvent.INVITATION_EVENT_TYPE:
						imagePath = e.getAttribute("invitationIcon");
						break;
						
					case ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE:
						imagePath = e.getAttribute("organizationIcon");
						break;
						
					default:
						break;
					} 
					
					if (!imagePath.isEmpty())
						image = EConferencePlugin.imageDescriptorFromPlugin(e.getAttribute("pluginID"), imagePath).createImage(false);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (image == null) {
				switch (invitationEvent.getEventType()) {
				case InvitationEvent.INVITATION_EVENT_TYPE:
					imagePath = "icons/view_stored_events_join_action_enabled.png";
					break;
					
				case ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE:
					imagePath = "icons/view_stored_events_join_from_file.png";
					break;
					
				default:
					imagePath = "icons/view_stored_events_join_action_enabled.png";
					break;
				}
				
				image = EConferencePlugin.imageDescriptorFromPlugin(
						EConferencePlugin.ID,
						imagePath).createImage(
						true);
			} 
		}
		
		return image;
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
		return invitationEvent.getBackendId();
	}

	@Override
	public String getInviter() {		
		return invitationEvent.getInviter();
	}

	@Override
	public String getPassword() {
		return invitationEvent.getPassword();
	}

	@Override
	public String getReason() {
		return invitationEvent.getReason();
	}

	@Override
	public String getRoom() {
		return invitationEvent.getRoom();
	}

	@Override
	public String getSchedule() {
		return invitationEvent.getSchedule();
	}

	@Override
	public InvitationEvent getInvitationEvent() {		
		return invitationEvent;
	}

	public void setReceivedOn(String receivedOn) {
		this.receivedOn = receivedOn;
	}

	public String getReceivedOn() {
		return receivedOn;
	}
}
