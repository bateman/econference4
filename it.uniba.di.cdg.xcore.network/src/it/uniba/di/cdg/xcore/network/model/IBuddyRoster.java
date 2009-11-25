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

import it.uniba.di.cdg.xcore.network.IBackend;

import java.util.Collection;

/**
 * Ok, this is an abstracted clone of SMACK's Roster ... It should be noted that a buddy 
 * may belong to more than one group but this seems one of the XMPP-only features and must
 * be treated carefully.
 */
public interface IBuddyRoster extends IEntry, Iterable<IBuddy> {
    /**
     * Returns the buddy with the specified unique id.
     * 
     * @param buddyId a string representing the buddy identifier
     * @return a buddy or <code>null</code> if no such id is present
     */
    IBuddy getBuddy( String buddyId );
    
    /**
     * Returns the groups(!) this buddy is registered as belonging to.
     *  
     * @param buddy the buddy 
     * @return the groups this buddy belongs to or an empty collection otherwise.
     */
    Collection<IBuddyGroup> getGroups( IBuddy buddy );

    /**
     * Returns all the buddies present in this roster.
     * 
     * @return the buddies
     */
    Collection<IBuddy> getBuddies();

    /**
     * Get all the groups registered in this roster.
     * 
     * @return the groups
     */
    Collection<IBuddyGroup> getAllGroups();

    /**
     * Clear this roster removing all buddies and groups. Listeners should be notified about
     * the {@link IBuddyRosterListener#rosterChanged()} event.
     */
    void clear();

    /**
     * Check if this roster has groups.
     * 
     * @return <code>true</code> if there are groups, <code>false</code> otherwise
     */
    boolean hasGroups();
    
    /**
     * Check if this roster has buddies.
     * 
     * @return <code>true</code> if there are groups, <code>false</code> otherwise
     */
    boolean hasBuddies();

    /**
     * Check if this roster contains the specified buddy identifier.
     * 
     * @param buddyId
     * @return <code>true</code> if the buddy is present, <code>false</code> otherwise
     */
    boolean contains( String buddyId );
    
    /**
     * Add an event listener for roster events. Notifications will happen in arbitrary order.
     * 
     * @param listener the new listener
     */
    void addListener( IBuddyRosterListener listener );

    /**
     * Remove the specified listener. Nothing will happen if the listener is unknown.
     * 
     * @param listener the listener to remove
     */
    void removeListener( IBuddyRosterListener listener );
    
    /**
     * Returns the network backend implementation that this roster belongs to.
     * 
     * @return the network backend
     */
    IBackend getBackend();
}
