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
package it.uniba.di.cdg.xcore.econference.service;

import java.util.HashMap;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.IEConferenceService;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.hr.HandRaisingModel;
import it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.model.hr.Question;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.SpecialPrivilegesAction;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatService;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;


/**
 * Implementation of the E-Conference service.  
 */
public class EConferenceService extends MultiChatService implements IEConferenceService {
	
	private static final String STATUS = "Status";
	private static final String STATUS_CHANGE = "StatusChange";
	private static final String ITEM_LIST = "ItemList";
	private static final String ITEMS = "Items";
	private static final String CURRENT_AGENDA_ITEM = "CurrentAgendaItem";
	private static final String ITEM_ID = "ItemId";
	private final static String FROM = "From";
	private final static String QUESTION = "Question";
	private final static String RISE_HAND = "RiseHand";
	private final static String QUESTION_UPDATE = "QuestionUpdate";
	private final static String QUESTION_ID = "QuestionId";
	private final static String QUESTION_STATUS = "QuestionStatus";
	private final static String CHANGE_SPECIAL_PRIVILEGE = "ChangedSpecialPrivilege";
	private final static String SPECIAL_ROLE = "SpecialRole";
	private final static String ROLE_ACTION = "RoleAction";
	private final static String USER_ID = "UserId";
	private final static String WHITE_BOARD_CHANGED = "WhiteBoardChanged";
	private final static String WHITE_BOARD_TEXT = "WhiteBoardText";

    /**
     * Moderators receive hand raises: when creating an answer they need to assign a unique ID to
     * each.
     */
    private static int s_questionId;

    /**
     * Computes and returns the next question id.
     * 
     * @return a new unique question id
     */
    private synchronized int nextQuestionId() {
        return ++s_questionId;
    }

    /**
     * The collection of pending, approved and rejected questions are grouped in an ad-hoc
     * model. Clients might register as listeners of this model.
     */
    private IHandRaisingModel hrModel;

    /**
     * @param conferenceContext 
     * @param context
     * @param backend
     */
    public EConferenceService( EConferenceContext conferenceContext, IBackend backend ) {
        super( conferenceContext, backend );
        this.hrModel = new HandRaisingModel();
    }

    public EConferenceService() {
		super();
		 this.hrModel = new HandRaisingModel();
	}

    @Override
    protected IConferenceModel createModel() {
        IConferenceModel model = new ConferenceModel();
        // Use the item list provided by the context if it is present: typically who is invited 
        // has a context with no item list.
        if (getContext().getItemList() != null)
            model.setItemList( getContext().getItemList() );

        return model;
    }

    @Override
    public void leave() {
        super.leave();
        getHandRaisingModel().dispose();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#getModel()
     */
    public IConferenceModel getModel() {
        return (IConferenceModel) super.getModel();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#getHandRaisingModel()
     */
    public IHandRaisingModel getHandRaisingModel() {
        return hrModel;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#getConferenceContext()
     */
    public EConferenceContext getContext() {
        return (EConferenceContext) super.getContext();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyStatusChange(it.uniba.di.cdg.xcore.econference.IEConferenceService.ConferenceStatus)
     */
    public void notifyStatusChange( ConferenceStatus status ) {
        System.out.println( String.format( "notifyStatusChange( %s )", status ) );
        HashMap<String, String> param = new HashMap<String, String>();
        param.put(STATUS, status.toString());
        getMultiChatServiceActions().SendExtensionProtocolMessage(
        		STATUS_CHANGE, param);  
        
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyWhiteBoardChanged(java.lang.String)
     */
    public void notifyWhiteBoardChanged( String text ) {
        System.out.println( String.format( "notifyWhiteBoardChange( %s )", text ) );
        HashMap<String, String> param = new HashMap<String, String>();
        
        param.put(WHITE_BOARD_TEXT, text);
        param.put(FROM, getLocalUserJid());
        
        getMultiChatServiceActions().SendExtensionProtocolMessage(WHITE_BOARD_CHANGED, param);

    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyChangedSpecialPrivilege(it.uniba.di.cdg.xcore.m2m.model.IParticipant, java.lang.String)
     */
    public void notifyChangedSpecialPrivilege( IParticipant participant, String specialRole, String action ) {
        System.out.println( String.format( "notifyChangedSpecialPrivilege( %s, %s %s )", participant.getId(),
        		action, specialRole ) );
        
        HashMap<String, String> param = new HashMap<String, String>();
        param.put(SPECIAL_ROLE, specialRole);
        param.put(ROLE_ACTION, action);
        param.put(USER_ID, participant.getId());
        
        getMultiChatServiceActions().SendExtensionProtocolMessage(CHANGE_SPECIAL_PRIVILEGE, param);

    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyChangedSpecialPrivilege(it.uniba.di.cdg.xcore.m2m.model.IParticipant, java.lang.String)
     */
    public void notifyChangedMUCPersonalPrivilege( IParticipant participant, String personalStatus ) {
        System.out.println( String.format( "notifyChangedMUCPersonalStatus( %s, %s )", participant.getId(),
        		personalStatus ) );

    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyRaiseHand(java.lang.String, java.lang.String)
     */
    public void notifyRaiseHand( String question, String moderatorId ) {
        System.out.println( String.format( "notifyRaiseHand( %s, %s )", question, moderatorId ) );
        String localId = getModel().getLocalUser().getId();
        HashMap<String, String> param = new HashMap<String, String>();
        
        param.put(QUESTION, question);
        param.put(FROM, localId);
        
        getMultiChatServiceActions().SendExtensionProtocolMessage(RISE_HAND, param);
        
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyQuestionUpdate(it.uniba.di.cdg.xcore.econference.model.hr.IQuestion)
     */
    public void notifyQuestionUpdate( IQuestion q ) {
        System.out.println( String.format( "notifyQuestionUpdate( %s )", q.getText() ) );
        
        HashMap<String, String> param = new HashMap<String, String>();
        param.put(FROM, q.getWho());
        param.put(QUESTION, q.getText());
        param.put(QUESTION_ID, new Integer(q.getId()).toString());
        param.put(QUESTION_STATUS, q.getStatus().toString());
        
        getMultiChatServiceActions().SendExtensionProtocolMessage(QUESTION_UPDATE, param);

    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyCurrentAgendaItemChanged(java.lang.String)
     */
    public void notifyCurrentAgendaItemChanged( String itemId ) {
        System.out.println( String.format( "notifyCurrentAgendaItemChanged( %s )", itemId ) );
    	HashMap<String, String> param = new HashMap<String, String>();
    	param.put(ITEM_ID, itemId);
    	
    	getMultiChatServiceActions().SendExtensionProtocolMessage(CURRENT_AGENDA_ITEM, param);
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.IEConferenceService#notifyItemListToRemote(java.lang.String)
     */
    public void notifyItemListToRemote() {
        System.out.println( "notifyItemListToRemote" );
        HashMap<String, String> param = new HashMap<String, String>();
        
        param.put(ITEMS, getModel().getItemList().encode());
        getMultiChatServiceActions().SendExtensionProtocolMessage(ITEM_LIST, param);

    }
    
	@Override
	public void onBackendEvent(IBackendEvent event) {
		super.onBackendEvent(event);
		
		if(event instanceof MultiChatExtensionProtocolEvent){
			MultiChatExtensionProtocolEvent mcepe = (MultiChatExtensionProtocolEvent)event;
			
			if(mcepe.getExtensionName().equals(STATUS_CHANGE)){
				ConferenceStatus status = ConferenceStatus.valueOf((String)mcepe.getExtensionParameter(STATUS));
				
                if (status != null)
                    getModel().setStatus( status );
                else
                    System.out.println( "Invalid status received: " + status );
            }
			
			else if(mcepe.getExtensionName().equals(ITEM_LIST)){
				getModel().getItemList().decode((String)mcepe.getExtensionParameter(ITEMS));
			}
			
			else if(mcepe.getExtensionName().equals(CURRENT_AGENDA_ITEM)){
				String strItemId = (String)mcepe.getExtensionParameter(ITEM_ID); 
                if (!ITalkModel.FREE_TALK_THREAD_ID.equals( strItemId )) {
                    int itemId = Integer.parseInt( strItemId );
                    // Note that we update *** independently ***  the agenda item list 
                    // and talk model (which handles the discussion threads)
                    try {
                        getModel().getItemList().setCurrentItemIndex( itemId );
                        String newSubject = ((IDiscussionItem)getModel().getItemList().getItem( itemId )).getText();
                        getModel().setSubject( newSubject, "[System]" );
                    } catch (IllegalArgumentException e) {
                        // the item list will protest if we set an out-of-range index:
                        // here it is just safe ignoring its cries ...
                    	//e.printStackTrace();
                    	System.out.println("Agenda list empty (" + getModel().getItemList().size() + 
                    			") or item index (" + itemId + ") out of range");
                    }
                }
                getTalkModel().setCurrentThread( strItemId );
			}
			
			else if(mcepe.getExtensionName().equals(RISE_HAND)){
				String from = (String)mcepe.getExtensionParameter(FROM);
				String strQuestion = (String)mcepe.getExtensionParameter(QUESTION);
                final IParticipant p = getLocalUserOrParticipant( from );
                // We rely on UI to check against our roles: so if the client asks us a question
                // we just accept it: if stronger checks are needed then this is a good place
                // to enforce them.
                if (!IParticipant.Role.MODERATOR.equals( getModel().getLocalUser().getRole() ))
                	return;

                if (p == null)
                    return;

                // This user is the moderator someone has asked to raise hand 
                final IQuestion question = new Question( null, nextQuestionId(), strQuestion,
                		from, QuestionStatus.PENDING );

                notifyQuestionUpdate( question );
			}
			
			else if(mcepe.getExtensionName().equals(QUESTION_UPDATE)){
				
				String from = (String)mcepe.getExtensionParameter(FROM);
				String text = (String)mcepe.getExtensionParameter(QUESTION);
				Integer id = new Integer((String)mcepe.getExtensionParameter(QUESTION_ID));
				QuestionStatus status = QuestionStatus.valueOf((String)mcepe.getExtensionParameter(QUESTION_STATUS));
				
				final IParticipant p = getLocalUserOrParticipant(from);

                if (p == null)
                    return;

                

                IQuestion existing = getHandRaisingModel().getQuestion(id);
                // Unknown question? Just add it to the model. A question with the same id as an
                // old one? Replace the previous one too!
                if (existing == null
                        || (existing != null && QuestionStatus.PENDING.equals( status ))) {
                    // This user is the moderator someone has asked to raise hand
                    final IQuestion q = new Question( getHandRaisingModel(), id,
                    		text, from, status );
                    getHandRaisingModel().addQuestion( q );
                }
                // Otherwise rejected or approved questions are simply removed from model:
                // the controller is responsible for performing additional functions, like
                // freezing or unfreezing.
                else if (QuestionStatus.REJECTED.equals( status )) {
                    notifyLocalSystemMessage( String.format(
                            "Moderator has rejected the following question from %s:\n\"%s\"", p
                                    .getNickName(), existing.getText() ) );
                    getHandRaisingModel().removeQuestion( existing );
                } else if (QuestionStatus.APPROVED.equals( status )) {
                    notifyLocalSystemMessage( String.format(
                            "Moderator has approved the following question from %s:\n\"%s\"", p
                                    .getNickName(), existing.getText() ) );
                    getHandRaisingModel().removeQuestion( existing );
                }
			}
			
			else if(mcepe.getExtensionName().equals(CHANGE_SPECIAL_PRIVILEGE)){
				String who = (String) mcepe.getExtensionParameter(USER_ID);
                String privilegeAction = (String) mcepe.getExtensionParameter(ROLE_ACTION);
                String specialPrivilegesAction = (String)mcepe.getExtensionParameter(SPECIAL_ROLE);
                IParticipant p = getLocalUserOrParticipant( who );

                if (p == null) {
                    System.out.println( "Received privilege change for unknown participant id: " + who );
                    return;
                }
                // The participant will fire an event
                if(privilegeAction.equals(SpecialPrivilegesAction.GRANT))
                	p.addSpecialPriviliges( specialPrivilegesAction) ;
                else if(privilegeAction.equals(SpecialPrivilegesAction.REVOKE))
                	p.removeSpecialPrivileges( specialPrivilegesAction );
                else{
                	System.out.println( "Received unknown privilege action for participant id: " + who );
                    return;
                }
			}
			
			else if(mcepe.getExtensionName().equals(WHITE_BOARD_CHANGED)){
				String text = mcepe.getExtensionParameter(WHITE_BOARD_TEXT);
                String who = mcepe.getExtensionParameter(FROM);
                // Try to use a nickname (friendlier to see), if available
                IParticipant p = getLocalUserOrParticipant( who );
                if (p != null)
                    who = p.getNickName();

                getModel().setWhiteBoardText( text == null? "" : text );
			}
			
		}
				
	}
    
    
}
