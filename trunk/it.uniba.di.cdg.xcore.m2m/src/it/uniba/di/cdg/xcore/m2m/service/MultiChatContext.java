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
package it.uniba.di.cdg.xcore.m2m.service;

import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.network.services.INetworkServiceContext;

import java.util.List;

/**
 * The context for each multi-chat: at the minimum we need the room's unique id on server-side.
 */
public class MultiChatContext implements INetworkServiceContext {
    /**
     * The chat room id.
     */
    String room;

    /**
     * The nickname the user wants to be known as.
     */
    String nickName;
    
    /**
     * The personal status the user wants.
     */
    String personalStatus;
    
    /**
     * The password if we want to use a password protected chat
     */
    String password;
    
    /**
     * The backend id identifies the implementation that provides the service we want to 
     * use for multichat.
     */
    String backendId;

    /**
     * A user may be invited to participate to a chat: this attribute provides the information
     * about the the invitation.
     */
    InvitationEvent invitation;

    /**
     * A multichat context may includes the invitees for this chat. Clients are expected to dispatch
     * invitations to them.
     */
    private List<Invitee> invitees;

    /**
     * The moderator of the room
     * 
     */
    private Invitee moderator;

	
    
    /**
     * Create a new context for a chat room that has been created by the user (not invited).
     * 
     * @param backendId the id of the backend to use
     * @param room the room id on the server
     * @param nickName the wanted nickname 
     * @param password 
     */
    public MultiChatContext( String backendId, String room, String nickName, String password ) {
        this.backendId = backendId;
        this.room = room;
        this.nickName = nickName;
        this.invitation = null;
        this.password = password;
        this.invitees = null;
    }

    /**
     * Create a new chat context based on invitation.
     *
     * @param chatServer
     * @param nickName
     * @param personalStatus 
     * @param invitation
     */
    public MultiChatContext( String nickName, String personalStatus, InvitationEvent invitation ) {
        this.backendId = invitation.getBackendId();
        this.room = invitation.getRoom();
        this.nickName = nickName;
        this.personalStatus = personalStatus;
        this.invitation = invitation;
        this.password = invitation.getPassword();
        this.invitees = null;
    }
    
    /**
     * Default constructor which initializes nothing (all properties return <code>null</code>).
     */
    public MultiChatContext() {
        super();
    }
    
    /**
     * @return Returns the room.
     */
    public String getRoom() {
        return room;
    }
    
    /**
     * @return Returns the nickName.
     */
    public String getNickName() {
        return nickName;
    }
    
    /**
     * @return Returns the personal status.
     */
	public String getPersonalStatus() {
		return personalStatus;
	}

    /**
     * @return Returns the backendId.
     */
    public String getBackendId() {
        return backendId;
    }

    /**
     * Returns the invitation if any.
     * 
     * @return Returns the invitation or <code>null</code> if the user is the chat creator
     */
    public InvitationEvent getInvitation() {
        return invitation;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param invitees The invitees to set.
     */
    public void setInvitees( List<Invitee> invitees ) {
        this.invitees = invitees;
    }

    /**
     * @return Returns the invitees.
     */
    public List<Invitee> getInvitees() {
        return invitees;
    }
    
    /**
     * Check that the password field is not empty.
     * 
     * @return <code>true</code> if there is a password set, <code>false</code> otherwise
     */
    public boolean hasPassword() {
        return password != null && password.length() > 0;
    }

    /**
     * @param backendId The backendId to set.
     */
    public void setBackendId( String backendId ) {
        this.backendId = backendId;
    }

    /**
     * @param invitation The invitation to set.
     */
    public void setInvitation( InvitationEvent invitation ) {
        this.invitation = invitation;
    }

    /**
     * @param nickName The nickName to set.
     */
    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }
    
    /**
     * @param personalStatus The personal status to set.
     */
    public void setPersonalStatus( String personalStatus ) {
        this.personalStatus = personalStatus;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword( String password ) {
        this.password = password;
    }

    /**
     * @param room The room to set.
     */
    public void setRoom( String room ) {
        this.room = room;
    }
    
    /**
     * Set the moderator of this conference.
     * 
     * @return
     */
    public Invitee getModerator() {
        return moderator;
    }

    /**
     * Set the moderator of the conference.
     * 
     * @param moderator
     */
    public void setModerator( Invitee moderator ) {
        this.moderator = moderator;
    }


    
    
}