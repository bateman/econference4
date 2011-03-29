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
package it.uniba.di.cdg.xcore.network.model;

import org.eclipse.core.runtime.PlatformObject;

/**
 * 
 */
public abstract class AbstractBuddy extends PlatformObject implements IBuddy {

	/**
	 * The roster this buddy belongs to.
	 */
	protected IBuddyRoster roster;

	/**
	 * Buddy's unique id.
	 */
	protected String id;

	/**
	 * The visible name of this buddy.
	 */
	protected String name;

	/**
	 * The online status.
	 */
	protected boolean online;
	
	

	/**
	 * The message the buddy's current activity (away, do not disturb, ...)
	 */
	protected Status status;

	/**
	 * The message the remote buddy has put ("I'm gone", "Need food",
	 * "Whatever", ...).
	 */
	protected String statusMessage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.osslabs.jabber.client.model.IBuddy#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.osslabs.jabber.client.model.IBuddy#getUser()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.osslabs.jabber.client.model.IBuddy#setId(java.lang.String)
	 */
	public void setId(String user) {
		this.id = user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.osslabs.jabber.client.model.IBuddy#isOnline()
	 */
	public boolean isOnline() {
		return online;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.osslabs.jabber.client.model.IBuddy#setOnline(boolean)
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	@Override
	public boolean isNotOffline() {		 
		return status == Status.OFFLINE ? false : true ;
	}

	@Override
	public boolean isOffline() {		
		return !isOnline();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getStatus()
	 */
	public Status getStatus() {
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.model.IBuddy#setStatus(it.uniba.di.cdg.
	 * xcore.network.model.IBuddy.Status)
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getStatusMessage()
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.model.IBuddy#setStatusMessage(java.lang
	 * .String)
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getRoster()
	 */
	public IBuddyRoster getRoster() {
		return roster;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IEntry#getChilds()
	 */
	public IEntry[] getChilds() {
		return new IEntry[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IEntry#getParent()
	 */
	public IEntry getParent() {
		IBuddyGroup parentGroup = (IBuddyGroup) getRoster().getGroups(this)
				.toArray()[0];
		return parentGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IBuddy that) {
		return String.CASE_INSENSITIVE_ORDER.compare(this.getName(),
				that.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getPrintableLabel()
	 */
	public String getPrintableLabel() {
		String s = getName();
		if (s == null)
			s = getId();

		if (!Status.OFFLINE.equals(getStatus()) && statusMessage != null
				&& !statusMessage.isEmpty())
			s += " (" + statusMessage + ")";

		return s;
	}

}
