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

/**
 * Interface for buddies (aka contacts).
 */
public interface IBuddy extends IEntry, Comparable<IBuddy> {
    
    public enum Status { AVAILABLE, CHAT, OFFLINE, BUSY, AWAY, EXTENDED_AWAY }
    
    /**
     * Returns the id, which identifies this buddy in the roster.
     * 
     * @return the id 
     */
    String getId();

    /**
     * Changes the id of the buddy. Clients are not expected to use this method.
     * 
     * @param id
     */
    public void setId( String id );

    /**
     * @return Returns the name.
     */
    String getName();

    /**
     * @param name The name to set.
     */
    void setName( String name );

    /**
     * Check if the buddy is reported as online.
     * 
     * @return <code>true</code> if the buddy is online, <code>false</code> otherwise
     */
    boolean isOnline();
    
    /**
     * Set this buddy as online. Clients are not expected to use this method directly.
     * 
     * @param online
     */
    void setOnline( boolean online );
    
    /**
     * Returns the current status of the buddy. This is usually a message indicating the 
     * buddy's current activity (away, do not disturb, ...).
     *  
     * @return the buddy's status
     */
    Status getStatus();
    
    /**
     * Change the status of this buddy. Clients are not expected to use this method directly.
     * 
     * @param status
     */
    void setStatus( Status status );
    
    
    /**
     * Returns the current status message of the buddy if present. 
     * This is a personal message written from a participant.
     *  
     * @return the buddy's status message
     */
    String getStatusMessage();
    
    /**
     * Change the status message of this buddy. 
     * Clients are not expected to use this method directly.
     * 
     * @param statusMessage
     */
    void setStatusMessage( String statusMessage );
    
    /**
     * Returns the roster this buddy belongs to.
     * 
     * @return the roster
     */
    IBuddyRoster getRoster();
    
    /**
     * Returna a <b>not <code>null</code></b> string, containing a printable name for this buddy.
     *  
     * @return a <code>not null</code> printable label
     */
    String getPrintableLabel();
}