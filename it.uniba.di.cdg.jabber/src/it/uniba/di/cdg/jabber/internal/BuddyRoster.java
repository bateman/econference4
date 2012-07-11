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
import it.uniba.di.cdg.aspects.SwtSyncExec;
import it.uniba.di.cdg.xcore.network.model.AbstractBuddyRoster;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddy.Status;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRosterListener;
import it.uniba.di.cdg.xcore.network.model.IEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 * An implementation of the <code>IBuddyRoster</code>. 
 */
public class BuddyRoster extends AbstractBuddyRoster implements RosterListener {

    private Roster jroster;

    /**
     * Creates a new empty buddy roster.
     * 
     * @param backend
     */
    public BuddyRoster( JabberBackend backend ) {
        super();
        this.backend = backend;
        this.jroster = null;
        this.buddies = new HashMap<String, IBuddy>();
        this.listeners = new HashSet<IBuddyRosterListener>();
        this.groups = new HashMap<String, IBuddyGroup>();
    }

    /**
     * A convenience method used by the jabber backend: smack provides its own roster 
     * implementation which we wrap.
     * 
     * @param roster the smack roster
     */
    public void setJabberRoster( Roster roster ) {
        // Unregister from old roster, if present
        if (jroster != null)
            jroster.removeRosterListener( this );
        // Register to this new roster 
        this.jroster = roster;

        roster.addRosterListener( this );
        rosterModified();
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyRoster#getBuddy(java.lang.String)
     */
    public IBuddy getBuddy( String buddyId ) {
        Buddy buddy = (Buddy) buddies.get( XMPPUtils.cleanJid( buddyId ) );

        return buddy;
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.RosterListener#rosterModified()
     */
    public void rosterModified() {
        //        System.out.println( "rosterModified()" );
        // "Called when a roster entry is added or removed."
        refreshBuddies();
        refreshGroups();

        for (IBuddyRosterListener l : listeners)
            l.rosterChanged();
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.RosterListener#presenceChanged(java.lang.String)
     */
    public void presenceChanged( Presence presence ) {
    	String jid = presence.getFrom();
        System.out.println( String.format( "presenceChanged( %s ): %s", jid, presence ) );              
        //final Presence presence = jroster.getPresence( XMPPUtils.cleanJid( jid ) );
        final RosterEntry entry = jroster.getEntry( XMPPUtils.cleanJid( jid ) );
        Buddy buddy;
        if (presence != null && !Presence.Type.unavailable.equals(presence.getType())) {
            // Buddy is online: remove the old offline (without resource) presence if there was any
            buddy = (Buddy) getBuddy( jid );

            if (buddy == null) // Double notification guard ...
                return;
            // Update its status
            buddy.setId( jid );
            buddy.setName( entry.getName() );
            buddy.setStatusMessage(presence.getStatus());
            buddy.setOnline( true );
            Presence.Mode mode = presence.getMode();
            if (Presence.Mode.available.equals( mode ) || (mode==null&& Presence.Type.available.equals(presence.getType())))
                buddy.setStatus( Status.AVAILABLE );
            else if (Presence.Mode.away.equals( mode ))
                buddy.setStatus( Status.AWAY );
            else if (Presence.Mode.dnd.equals( mode ))
                buddy.setStatus( Status.BUSY ); 
            else if (Presence.Mode.chat.equals( mode ))
                buddy.setStatus(Status.CHAT);
            else if (Presence.Mode.xa.equals( mode ))
                buddy.setStatus(Status.EXTENDED_AWAY);
        } else {
            // Buddy is offline ... remove the old online record (with resource) and replace with one without resource
            buddy = (Buddy) getBuddy( jid );
            // It seems that a second notification is sent bu the "jid/resource" has already been removed so we just flee away.
            if (buddy == null)
                return;

            buddies.remove( jid );
            buddy.setId( XMPPUtils.cleanJid( jid ) );
            buddy.setName( entry.getName() );
            buddy.setOnline( false );
            buddy.setStatus( Status.OFFLINE );
        }
        
     // check if we should add back the buddy because he has other presences online
		Presence rpresence = jroster.getPresence(entry.getUser());
        Buddy rbuddy = new Buddy( this, entry.getUser(), entry.getName(), rpresence,"", rpresence.getStatus());
        buddies.put( rbuddy.getCleanJid(), rbuddy );

        for (IBuddyRosterListener l : listeners)
            l.presenceChanged( buddy );
    }

    private void refreshBuddies() {
        buddies.clear();
        // Get all the buddies ...
        for (Iterator<RosterEntry> it = jroster.getEntries().iterator(); it.hasNext();) {
            RosterEntry entry = (RosterEntry) it.next();
            Presence presence = jroster.getPresence(entry.getUser());
            Buddy buddy = new Buddy( this, entry.getUser(), entry.getName(), presence,"", presence.getStatus());
            //Buddy buddy = new Buddy( this, entry.getUser(), entry.getName(), presence.getStatus());
            buddies.put( buddy.getCleanJid(), buddy ); 
        }
    }

    /**
     * Scan the roster's groups: for each roster group get the entries, find a buddy for each entry and 
     * add the buddy to buddygroup.
     * <p>
     * Note that this method needs the buddy list already filled, so it must be called before of
     * <code>refreshBuddies()</code>
     */
    private void refreshGroups() {
        groups.clear();
        for (Iterator<RosterGroup> itGroups = jroster.getGroups().iterator(); itGroups.hasNext();) {
            RosterGroup rosterGroup = (RosterGroup) itGroups.next();

            BuddyGroup buddyGroup = new BuddyGroup( rosterGroup.getName() );

            for (Iterator<RosterEntry> itBuddy = rosterGroup.getEntries().iterator(); itBuddy.hasNext();) {
                RosterEntry entry = (RosterEntry) itBuddy.next();

                if (buddies.containsKey( entry.getUser() )) {
                    IBuddy knownBuddy = buddies.get( entry.getUser() );
                    buddyGroup.addBuddy( knownBuddy );
                    // System.out.println( String.format( "Adding buddy %s to group %s", knownBuddy, buddyGroup ) );
                }
            }

            groups.put( buddyGroup.getName(), buddyGroup );
        }
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyRoster#clear()
     */
    public void clear() {
        buddies.clear();
        groups.clear();

        for (IBuddyRosterListener l : listeners)
            l.rosterChanged();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (IBuddy buddy : buddies.values()) {
            sb.append( buddy.getName() );
            sb.append( " : " );
            sb.append( getGroups( buddy ) );
            sb.append( "\n" );
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.RosterListener#entriesAdded(java.util.Collection)
     */
    public void entriesAdded( Collection<String> entries ) {
        System.out.println( "Added entries: " + entries );
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.RosterListener#entriesUpdated(java.util.Collection)
     */
    @Override
    public void entriesUpdated( Collection<String> entries ) {
        System.out.println( "Updated entries: " + entries );
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.RosterListener#entriesDeleted(java.util.Collection)
     */
    public void entriesDeleted( Collection<String> entries ) {
        System.out.println( "Deleted entries: " + entries );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IEntry#getParent()
     */
    public IEntry getParent() {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IEntry#getChilds()
     * FIXME generates a java.util.ConcurrentModificationException
     */
    @SwtSyncExec
    public IEntry[] getChilds() {
        // Collect all groups and buddies without a group
        final Collection<IEntry> all = new HashSet<IEntry>( groups.values() );
        
        for (IBuddy buddy : getBuddies()) {
            if (getGroups( buddy ).isEmpty()) {
                //System.out.println( "Adding orphan : " + buddy );
                all.add( buddy );
            }
        }
        
        return all.toArray( new IEntry[all.size()] );
    }
    
    @Override
    public void removeBuddy(String id)  {
        try{
        RosterEntry entry = jroster.getEntry( id );
        jroster.removeEntry( entry ); 
        Thread.sleep(1000);
        }
        catch(XMPPException e){
            e.getMessage();
        }
        catch (InterruptedException e) {
			e.printStackTrace();
		}
        rosterModified();

    }
    
    @Override
    public void addBuddy(String id, String name, String[] gruppi){
        try{
        	if(gruppi[0].equals("None")){
        		jroster.createEntry(id, name, null);
        	}
        	else{
        		jroster.createEntry(id, name, gruppi );
        	}
        Thread.sleep(1000);
        }
        catch(XMPPException e){
            e.getMessage();
        }        
        catch (InterruptedException e) {
			e.printStackTrace();
		}
        rosterModified();
    }
   
    @Override
    public void renameBuddy(String user, String name)  {
        RosterEntry entry = jroster.getEntry( user );
        entry.setName( name );
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(entry.getGroups().isEmpty())refreshBuddies();
		else rosterModified();
        
    }
    
    public void renameGroup(String old_name, String new_name){
        
        RosterGroup group = jroster.getGroup( old_name );
        group.setName( new_name );
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        rosterModified();
    }

    public void moveToGroup(String user, String nameNewGroup){
        try{
            RosterEntry entry = jroster.getEntry( user );
            if(!entry.getGroups().isEmpty()){
            RosterGroup oldgroup = entry.getGroups().iterator().next();
            oldgroup.removeEntry(entry);
            }
            if(!nameNewGroup.equals("None")){
            RosterGroup newGroup = jroster.getGroup( nameNewGroup );
            if(newGroup == null){
            	addGroup(nameNewGroup);
            	newGroup = jroster.getGroup( nameNewGroup );
            	
            }
            newGroup.addEntry( entry ); }
            Thread.sleep(1000);
           }
        catch (XMPPException e) {
            
        }catch (InterruptedException e) {
			e.printStackTrace();
		}
        rosterModified();
    }

	@Override
	public void addGroup(String name) {
		jroster.createGroup(name);
		rosterModified();
	}

	@Override
	public void removeGroup(String namegroup, String newGroup ) {
		RosterGroup group = jroster.getGroup( namegroup );
		RosterGroup nGroup = null;
		if(!newGroup.equals("None")){nGroup = jroster.getGroup( newGroup );}
		Iterator<RosterEntry> buddy = group.getEntries().iterator();
		RosterEntry entry;
		while (buddy.hasNext()) {
			entry = buddy.next();
			try {
				group.removeEntry(entry);
				if(!newGroup.equals("None")){nGroup.addEntry(entry);}
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			
		}
		rosterModified();		
	}

	@Override
	public void reload() {
		jroster.reload();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rosterModified();
		refreshBuddies();
		
		
	}
	
	@Override
	public ArrayList<String> getPresencesStr (String user) {
    	Iterator<Presence> presences = jroster.getPresences(user);
    	ArrayList<String> presencesStr = new ArrayList<String>();
    	while(presences.hasNext()) {
    		Presence p = presences.next();
    		presencesStr.add(p.getFrom());
    	}
    	return presencesStr;
    }
}
