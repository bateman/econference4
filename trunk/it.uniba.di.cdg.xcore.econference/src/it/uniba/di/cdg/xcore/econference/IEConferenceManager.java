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
package it.uniba.di.cdg.xcore.econference;

import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.SpecialPrivilegesAction;

/**
 * Manger for handling e-conferences.
 */
public interface IEConferenceManager extends IMultiChatManager {  
	    
    /**
     * Change the conference status. 
     * @param status 
     */
    void setStatus( ConferenceStatus status );

    /**
     * Change the conference status.
     * @return the conference status
     */
    ConferenceStatus getStatus();
    
    /**
     * Covariance enforcement.
     * 
     * @return the conference model
     */
    IEConferenceService getService();

    /**
     * Notify remote clients about the changed white board.
     * 
     * @param text the updated whitboard text
     */
    void notifyWhiteBoardChanged( String text );
    
    /**
     * Notify remote client about the changed special role for a participant (this action
     * is usually performed by moderators).
     *  
     * @param p
     * @param newPrivilege
     * @param action see {@link SpecialPrivilegesAction}
     */
    void notifySpecialPrivilegeChanged( IParticipant p, String newPrivilege, String action );

    /**
     * Notify remote client about the changed of participant's personal status in the MUC.
     *  
     * @param p
     * @param personalStatus
     */
    void notifyChangedMUCPersonalPrivilege( IParticipant p, String personalStatus );
    
    /**
     * Notify that this client wants to ask a question and requests to do this to the specified
     * moderator.
     * 
     * @param moderator
     * @param question
     */
    void notifyRaiseHand( IParticipant moderator, String question );
    
    /**
     * Notify other clients that the specified question has changes that needs to be propagated
     * to the other clients. 
     * 
     * @param q
     */
    void notifyQuestionUpdate( IQuestion q );

    /**
     * The manager will notify remote clients about the changed item id.
     * 
     * @param itemId
     */
    void notifyCurrentAgendaItemChanged( String itemId );
    
    /**
     * Send the current agenda's items list to remote clients.
     */
    void notifyItemListToRemote();

}
