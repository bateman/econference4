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
package it.uniba.di.cdg.xcore.m2m.model;

import org.eclipse.core.runtime.IAdaptable;

/**
 * A participant to a conference, identified by its id and with additional informations.
 * <p>
 * Note that this interface suppose that participants information do not change.
 * <p>
 * Note also that it happens that <b>both</b> the id and nickName fields must be unique.
 */
public interface IParticipant extends IAdaptable {
    /**
     * The role each participant can cover. 
     * See {@link http://java.sun.com/j2se/1.5.0/docs/guide/language/enums.html} for enums info.
     */
    public enum Role { VISITOR, PARTICIPANT, MODERATOR };
    
    /**
     * The status of each participant.
     */
    public enum Status { JOINED, NOT_JOINED, FROZEN }; 
    
    /**
     * Observer interface for participants.
     */
    public static interface IParticipantListener {
        /**
         * The participant status has changed.
         * 
         * @param participant the participant that has changed
         */
        void changed( IParticipant participant );
    }
    
    /**
     * Set the model this participant should belongs to.
     * 
     * @param room
     */
    void setChatRoom( IChatRoomModel room );
    
    /**
     * Returns the chatroom where this participant is present.
     * 
     * @return
     */
    IChatRoomModel getChatRoom();
    
    /**
     * The id of the participant.
     * 
     * @return
     */
    String getId();
    
    /**
     * 
     * @return
     */
    String getNickName();
    
    /**
     * Returns the nick name for this participant.
     * 
     * @param nickName
     */
    void setNickName( String nickName );
    
    /**
     * The participant role.
     * 
     * @return the actual role of this participant
     */
    Role getRole();
    
    /**
     * Changes the role for this participant.
     * 
     * @param role
     */
    void setRole( Role role );
    
    /**
     * Returns the status of the participant.
     * 
     * @return the participant status.
     */
    Status getStatus();

    /**
     * Change the status for this participant.
     * 
     * @param status
     */
    void setStatus( Status status );

    /**
     * Returns the special role used here.
     * 
     * @return the special role
     */
    String getPersonalStatus();
    
    /**
     * Change the current special role for this participant.
     * 
     * @param personalStatus
     */
    void setPersonalStatus( String personalStatus );
    
    /**
     * Returns special privileges of the participant.
     * 
     * @return an array of special privileges
     */
    String[] getSpecialPrivileges();
    
    /**
     * Add a special privilege for this participant.
     * 
     * @param privilege 
     *     
     */
    void addSpecialPriviliges( String privilege );
    
    /**
     * Remove the special privilege for this participant.
     * 
     * @param privilege 
     *     
     */
    void removeSpecialPrivileges(String privilege);
    
    
    /**
     * Check if participant has the special privilege.
     * 
     * @param privilege 
     * 
     * @return <code>true</code> if participant has the privilege 
     *     
     */
    boolean hasSpecialPrivilege(String privilege);
}
