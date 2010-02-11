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
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.model.IEntry;

/**
 * Implementation of a Jabber/XMPP buddy. Note that this implements <code>IAdaptable</code> 
 * to provide an <code>IActionFilter</code> for GUI actions. 
 */
public class Buddy extends AbstractBuddy {
    /**
     * Buddy's unique id.
     */
    private String id;

    /**
     * The visible name of this buddy.
     */
    private String name;
    
    /**
     * The online status.
     */
    private boolean online;
    
    /**
     * The roster this buddy belongs to.
     */
    private final IBuddyRoster roster;
    
    /**
     * The message the buddy's current activity (away, do not disturb, ...)
     */
    private Status status;

    /**
     * The message the remote buddy has put ("I'm gone", "Need food", "Whatever", ...).
     */
    private String statusMessage;
    
    
    public Buddy( IBuddyRoster roster, String jid, String name, boolean online, String status, String statusMessage ) {
        this.roster = roster;
        this.id = jid;
        this.name = (name.equals("")) ? jid : name;
        this.online = online;
        this.statusMessage = statusMessage;
        this.status = Status.OFFLINE;
    }

    /**
     * Super-compact form: provides only the id. The id itself may contain the "/Resource" format: this 
     * constructor will separate them and initialize them accordingly.
     * 
     * @param roster
     * @param jid
     */
    public Buddy( IBuddyRoster roster, String jid) {
        this( roster, jid, null, false, "", "" );
    }
    
    public Buddy( IBuddyRoster roster, String jid, String name, String statusMessage ) {
        this( roster, jid, name , false, "", statusMessage );
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddy#getName()
     */
    public String getName() {
    	if(name=="" || name==null )
    		return getCleanJid();
    	else
    		return name;
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddy#setName(java.lang.String)
     */
    public void setName( String name ) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddy#getUser()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddy#setId(java.lang.String)
     */
    public void setId( String user ) {
        this.id = user;
    }
    
    /**
     * Returns the Jabber id, without any resource specification
     *  
     * @return the jid, without any resource specification
     */
    public String getCleanJid() {
        return XMPPUtils.cleanJid( id );
    }

    /**
     * @return Returns the resource.
     */
    public String getResource() {
        int i = id.indexOf( "/" );
        if (i == -1) // No resource
            return "";
        else
            return id.substring( i + 1, id.length() );
    }
    
    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddy#isOnline()
     */
    public boolean isOnline() {
        return online;
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddy#setOnline(boolean)
     */
    public void setOnline( boolean online ) {
        this.online = online;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getStatus()
     */
    public Status getStatus() {
        return status;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddy#setStatus(it.uniba.di.cdg.xcore.network.model.IBuddy.Status)
     */
    public void setStatus( Status status ) {
        this.status = status;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format( "%s [%s]", name, id );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (other == null || !(other instanceof IBuddy) )
            return false;
        IBuddy that = (IBuddy) other;
        // Note that we ignore the "Resource field"
        return this.getId().equals( that.getId() );
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public int compareTo( IBuddy that ) {
        return String.CASE_INSENSITIVE_ORDER.compare( this.getName(), that.getName() );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getRoster()
     */
    public IBuddyRoster getRoster() {
        return roster;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddy#getPrintableLabel()
     */
    public String getPrintableLabel() {
        String s = getName();
        if (s == null)
            s = getId();
        
        if (!Status.OFFLINE.equals( getStatus() ) && 
        		statusMessage!=null && !statusMessage.isEmpty())
            s += " (" + statusMessage + ")";
        
        return s;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IEntry#getParent()
     */
    public IEntry getParent() {
        IBuddyGroup parentGroup = (IBuddyGroup) getRoster().getGroups( this ).toArray()[0];
        return parentGroup;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IEntry#getChilds()
     */
    public IEntry[] getChilds() {
        return new IEntry[0];
    }

	@Override
	public String getStatusMessage() {
		return statusMessage;
	}

	@Override
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;		
	}
}
