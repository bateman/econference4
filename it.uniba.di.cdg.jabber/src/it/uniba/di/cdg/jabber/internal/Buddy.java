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

import it.uniba.di.cdg.xcore.network.model.AbstractBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;

/**
 * Implementation of a Jabber/XMPP buddy. 
 */
public class Buddy extends AbstractBuddy {

	public Buddy(IBuddyRoster roster, String jid, String name, Boolean online,
			String status, String statusMessage) {
		this.roster = roster;
		this.id = jid;
		this.name = (name.equals("")) ? jid : name;
		this.online = online;
		this.statusMessage = statusMessage;
		this.status = Status.OFFLINE;

	}

	public Buddy(IBuddyRoster roster, String jid, String name,
			Presence presence, String status, String statusMessage) {
		this.roster = roster;
		this.id = jid;
		this.name = (name == null || name.equals("")) ? jid : name;
		;
		this.statusMessage = statusMessage;
		if (presence != null) {
			Mode mode = presence.getMode();
			if (Presence.Mode.available.equals(mode)
					|| (mode == null && Presence.Type.available.equals(presence
							.getType()))) {
				this.status = Status.AVAILABLE;
				this.online = true;
			} else if (Presence.Mode.away.equals(mode)) {
				this.status = Status.AWAY;
				this.online = true;
			} else if (Presence.Mode.dnd.equals(mode)) {
				this.status = Status.BUSY;
				this.online = true;
			} else if (Presence.Mode.chat.equals(mode)) {
				this.status = Status.CHAT;
				this.online = true;
			} else if (Presence.Mode.xa.equals(mode)) {
				this.status = Status.EXTENDED_AWAY;
				this.online = true;
			} else {
				this.status = Status.OFFLINE;
				this.online = false;
			}
		} else {
			this.status = Status.OFFLINE;
			this.online = false;
		}
	}

	/**
	 * Super-compact form: provides only the id. The id itself may contain the
	 * "/Resource" format: this constructor will separate them and initialize
	 * them accordingly.
	 * 
	 * @param roster
	 * @param jid
	 */
	public Buddy(IBuddyRoster roster, String jid) {
		this(roster, jid, null, false, "", "");
	}

	public Buddy(IBuddyRoster roster, String jid, String name,
			String statusMessage) {
		this(roster, jid, name, false, "", statusMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.osslabs.jabber.client.model.IBuddy#getName()
	 */
	public String getName() {
		if (name == "" || name == null)
			return getCleanJid();
		else
			return name;
	}

	/**
	 * Returns the Jabber id, without any resource specification
	 * 
	 * @return the jid, without any resource specification
	 */
	public String getCleanJid() {
		return XMPPUtils.cleanJid(id);
	}

	/**
	 * @return Returns the resource.
	 */
	public String getResource() {
		int i = id.indexOf("/");
		if (i == -1) // No resource
			return "";
		else
			return id.substring(i + 1, id.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s [%s]", name, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof IBuddy))
			return false;
		IBuddy that = (IBuddy) other;
		// Note that we ignore the "Resource field"
		return this.getId().equals(that.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}

}
