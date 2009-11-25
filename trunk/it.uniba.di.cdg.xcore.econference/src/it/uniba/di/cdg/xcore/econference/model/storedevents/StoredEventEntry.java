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

import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;

/**
 * 
 */
public class StoredEventEntry extends InvitationEvent implements IStoredEventEntry {

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
    public StoredEventEntry( String accountId, String backendId, String room, String inviter,
            String reason, String password ) {
        super( backendId, room, inviter, reason, password, null );
        this.accountId = accountId;
    }

    /**
     * 
     * @param event
     */
    public StoredEventEntry( InvitationEvent event ) {
        super( event.getBackendId(), event.getRoom(), event.getInviter(), event.getReason(), event
                .getPassword(), event.getMessage() );
        this.accountId = "";
    }

    /* (non-Javadoc) 
     * @see it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry#getAccountId()
     */
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId( String accountId ) {
        this.accountId = accountId;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.InvitationEvent#toString()
     */
    @Override
    public String toString() {
        return String.format( "Invitation to eConference %s", getRoom().split("@")[0] );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry#getHash()
     */
    public String getHash() {
        return hash;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry#setHash(java.lang.String)
     */
    public void setHash( String hash ) {
        this.hash = hash;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry#getBytes()
     */
    public byte[] getBytes() throws UnsupportedEncodingException {
        StringBuffer bf = new StringBuffer( accountId );
        bf.append( getReason() ).append( getRoom() ).append( getBackendId() ).append( getInviter() );
        return bf.toString().getBytes(ENCODING);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object eentry ) {
        if (eentry instanceof IStoredEventEntry) {
            IStoredEventEntry storedEntry = (IStoredEventEntry) eentry;           
            return hash.equals( storedEntry.getHash() );
        } else
            return false;

    }
}
