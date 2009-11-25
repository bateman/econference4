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

import it.uniba.di.cdg.xcore.m2m.model.IParticipant.IParticipantListener;

/**
 * This is the model shown by the {@see it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView}.
 */
public interface IChatRoomModel extends IParticipantListener {
    /**
     * Returns the participant.
     * 
     * @param participantId
     * @return the participant or <code>null</code> if the id is unknown
     */
    IParticipant getParticipant( String participantId );
    
    /**
     * Add a new participant to this chat room.
     * 
     * @param participant
     */
    void addParticipant( IParticipant participant );
    
    /**
     * Remove the participant from the chat room.
     * 
     * @param participant
     */
    void removeParticipant( IParticipant participant );
 
    /**
     * Returns the participants of this chatroom as an array.
     * 
     * @return an array (maybe empty) of participants
     */
    IParticipant[] getParticipants();
    
    /**
     * Add a new event listener.
     * 
     * @param listener
     */
    void addListener( IChatRoomModelListener listener );

    /**
     * Remove an existing event listener.
     * 
     * @param listener
     */
    void removeListener( IChatRoomModelListener listener );

    /**
     * Returns the actual subject for the chatroom.
     * 
     * @return the chatroom subject 
     */
    String getSubject();

    /**
     * Change the subject of this chatroom.
     * 
     * @param subject The subject to set.
     * @param from 
     */
    void setSubject( String subject, String from );
    
    void setLocalUser( IParticipant p );
    
    IParticipant getLocalUser();
    
    IParticipant getLocalUserOrParticipant( String id );
}
