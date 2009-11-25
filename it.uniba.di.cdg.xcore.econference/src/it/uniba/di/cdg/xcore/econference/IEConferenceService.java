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

import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.SpecialPrivilegesAction;
import it.uniba.di.cdg.xcore.m2m.service.IMultiChatService;
import it.uniba.di.cdg.xcore.network.services.Capability;
import it.uniba.di.cdg.xcore.network.services.ICapability;

/**
 * Operations that must be supported by all conference implementations. 
 */
public interface IEConferenceService extends IMultiChatService {
    /**
     * Constant indicating that the backend supports multi-peers chat.
     */
    public static final ICapability ECONFERENCE_SERVICE = new Capability( "e-conference" );
    
    /**
     * We can perform several operations upon the agenda (currently only adding is supported).
     */
    public enum AgendaOperation {
        ADD, REMOVE
    }
    
    /**
     * Notify to clients the conference status change. 
     */
    void notifyStatusChange( ConferenceStatus status );

    /**
     * Returns the conference context.
     * 
     * @return the conference context
     */
    EConferenceContext getContext();
    
    /**
     * Covariance enforcement.
     * 
     * @return the conference model
     */
    IConferenceModel getModel();
    
    /**
     * Returns the hand raising model (all the quesions pending, approved or rejected).
     * 
     * @return the hand raising model
     */
    IHandRaisingModel getHandRaisingModel();

    /**
     * Notify remote peers about the changed whiteboard content.
     * 
     * @param text new whiteboard content 
     */
    void notifyWhiteBoardChanged( String text );
    
    /**
     * Notify remote clients about the change in the special privilege attribute. Note that the 
     * participant role must not be changed by clients: the server will broadcast the change 
     * back and then the model will be changed an event fired. 
     * 
     * @param participant the participant that must change the role
     * @param specialPrivilege the new special privilege (client specific)
     * @param action the privilege action see {@link SpecialPrivilegesAction}
     */
    void notifyChangedSpecialPrivilege( IParticipant participant, String specialPrivilege, String action );
    
    /**
     * Notify remote clients about the change of the personal status of a MUC Participant.
     * 
     * @param participant the participant that must change the role
     * @param personalStatus the new special role (client specific)
     */
    void notifyChangedMUCPersonalPrivilege( IParticipant participant, String personalStatus );
    
    /**
     * Signal a moderator about a question. The moderator client will receive the notification
     * and acknowledge it, notifying all clients: only after this the client's HR model will
     * be changed and a change event fired.
     * 
     * @param question the question to ask
     * @param moderatorId the id of the moderator to ask the question to
     */
    void notifyRaiseHand( String question, String moderatorId );

    /**
     * This method is for moderator-privileged clients that need to notify some change in a question
     * status.
     * 
     * @param question
     */
    public void notifyQuestionUpdate( IQuestion question );

    /**
     * Notify remote client that the currently selected agenda item has changed.
     * 
     * @param itemId
     */
    void notifyCurrentAgendaItemChanged( String itemId );
    
    /**
     * Notift the current local agenda's item list to remote clients: this method is 
     * intended to be used if the local user is a moderator and wants to synchronize 
     * the agenda of remote clients.
     */
    void notifyItemListToRemote();

}
