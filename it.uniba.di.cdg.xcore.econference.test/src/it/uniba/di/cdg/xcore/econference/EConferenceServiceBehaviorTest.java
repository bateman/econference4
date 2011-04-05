package it.uniba.di.cdg.xcore.econference;


import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;
import it.uniba.di.cdg.xcore.econference.service.EConferenceService;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.SpecialPrivilegesAction;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserJoinedEvent;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.services.JoinException;
import it.uniba.di.cdg.xcore.network.services.NetworkServiceException;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class EConferenceServiceBehaviorTest {
    
    // SUT
    private EConferenceService service;    
    
    protected static final String STATUS = "Status";
    protected static final String STATUS_CHANGE = "StatusChange";
    protected static final String ITEM_LIST = "ItemList";
    protected static final String ITEMS = "Items";
    protected static final String CURRENT_AGENDA_ITEM = "CurrentAgendaItem";
    protected static final String ITEM_ID = "ItemId";
    protected final static String FROM = "From";
    protected final static String QUESTION = "Question";
    protected final static String RISE_HAND = "RiseHand";
    protected final static String QUESTION_UPDATE = "QuestionUpdate";
    protected final static String QUESTION_ID = "QuestionId";
    protected final static String QUESTION_STATUS = "QuestionStatus";
    protected final static String CHANGE_SPECIAL_PRIVILEGE = "ChangedSpecialPrivilege";
    protected final static String SPECIAL_ROLE = "SpecialRole";
    protected final static String ROLE_ACTION = "RoleAction";
    protected final static String USER_ID = "UserId";
    protected final static String WHITE_BOARD_CHANGED = "WhiteBoardChanged";
    protected final static String WHITE_BOARD_TEXT = "WhiteBoardText";

    @Before
    public void setUp() throws Exception {
        service = new EConferenceService();
    }
    
    /* These tests are about the interactions with the MultiChatServiceActions component
     * Note that they will be focused on the specific interactions in EConferenceService
     * Other interactions are tested in MultiChatService behavior test
     * */    
    
    @Test
    public void testServiceActionsNotifyStatusChange(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);
        // ConferenceStatus cannot be mocked because it is an enum type, 
        // We don't care about the specific value because we just check it keeps unchanged during the interaction tested  
        ConferenceStatus status = ConferenceStatus.STARTED;
        
        HashMap<String, String> stParam = new HashMap<String, String>();
        stParam.put(STATUS, status.toString());    
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.notifyStatusChange( status );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(STATUS_CHANGE, stParam); 
    }
    
    @Test
    public void testServiceActionsNotifyWhiteboardChanged(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);
       
        String whiteBoardText = "decision 1";
        String userId = "userid";
        
        HashMap<String, String> wbParam = new HashMap<String, String>();        
        wbParam.put(WHITE_BOARD_TEXT, whiteBoardText);
        wbParam.put(FROM, userId);
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(backend.getUserId()).thenReturn( userId );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.notifyWhiteBoardChanged( whiteBoardText );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(WHITE_BOARD_CHANGED, wbParam);
    }
    
    @Test
    public void testServiceActionsNotifyChangedSpecialPrivilege(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);     
        IParticipant participant = mock(IParticipant.class);   
       
        String specialRole = "newSpecialRole";
        String action = "newAction";
        String userId = "userid";

        HashMap<String, String> spParam = new HashMap<String, String>();
        spParam.put(SPECIAL_ROLE, specialRole);
        spParam.put(ROLE_ACTION, action);
        spParam.put(USER_ID, userId);
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(participant.getId()).thenReturn( userId );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.notifyChangedSpecialPrivilege( participant, specialRole, action );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(CHANGE_SPECIAL_PRIVILEGE, spParam);
    }
    
    @Test
    public void testServiceActionsNotifyRaiseHand(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);     
        IParticipant participant = mock(IParticipant.class);   
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
       
        String questionText = "question";
        String moderatorId = "moderatorId";
        String userId = "userid";

        HashMap<String, String> rhParam = new HashMap<String, String>();        
        rhParam.put(QUESTION, questionText);
        rhParam.put(FROM, userId);
                
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(participant.getId()).thenReturn( userId );
        when(chatRoomModel.getLocalUser()).thenReturn( participant );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );    
        service.setChatRoomModel( chatRoomModel );      
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        // we could have used an anyString() matcher for the moderatorId but the Mock framework
        // needs that all arguments must be matchers if one is
        service.notifyRaiseHand( questionText, moderatorId );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(RISE_HAND, rhParam);
    }
    
    @Test
    public void testServiceActionsNotifyQuestionUpdate(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);     
        IParticipant participant = mock(IParticipant.class);        
        IQuestion question = mock(IQuestion.class);
       
        String userId = "userid";
        // QuestionStatus cannot be mocked because it is an enum type, 
        // We don't care about the specific value because we just check it keeps unchanged during the interaction tested           
        QuestionStatus questionStatus = QuestionStatus.APPROVED;

        HashMap<String, String> quParam = new HashMap<String, String>();
        quParam.put(FROM, question.getWho());
        quParam.put(QUESTION, question.getText());
        quParam.put(QUESTION_ID, new Integer(question.getId()).toString());
        quParam.put(QUESTION_STATUS, questionStatus.toString());
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(participant.getId()).thenReturn( userId );
        when(question.getStatus()).thenReturn( questionStatus );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.notifyQuestionUpdate( question );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(QUESTION_UPDATE, quParam);
    }    
    
    @Test
    public void testServiceActionsNotifyCurrentAgendaItemChanged(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);     
        IParticipant participant = mock(IParticipant.class);   
       
        String userId = "userid";
        String itemId = "itemId";
        

        HashMap<String, String> aiParam = new HashMap<String, String>();
        aiParam.put(ITEM_ID, itemId);
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(participant.getId()).thenReturn( userId );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.notifyCurrentAgendaItemChanged( itemId );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(CURRENT_AGENDA_ITEM, aiParam);
    }
    
    @Test
    public void testServiceActionsNotifyItemListToRemote(){
        // Setup
        IBackend backend = mock(IBackend.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        EConferenceContext context = mock(EConferenceContext.class);     
        IParticipant participant = mock(IParticipant.class);   
        IItemList itemList = mock(IItemList.class);
       
        String userId = "userid";
        String encodedItem = "encodeditem";

        HashMap<String, String> irParam = new HashMap<String, String>();        
        irParam.put(ITEMS, encodedItem);
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(participant.getId()).thenReturn( userId );
        when(chatRoomModel.getItemList()).thenReturn( itemList );
        when(itemList.encode()).thenReturn( encodedItem );
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.notifyItemListToRemote();   
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(ITEM_LIST, irParam);
    }
       
    
    // onBackendEvent tests
    // Note that the listeners interactions can't be tested because the listeners lists are final
    // so even if we subclass the SUT and build a setter method they are unreachable.
    
    @Test
    public void testServiceOnMultiChatUserJoinedEvent(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatUserJoinedEvent event = mock(MultiChatUserJoinedEvent.class);
        ITalkModel talkModel = mock(ITalkModel.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        IParticipant participant = mock(IParticipant.class);
        IItemList itemList = mock(IItemList.class);        
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);  

        Role participantRole = Role.MODERATOR;
        Map<String, IParticipant> participants = new HashMap<String, IParticipant>();
        participants.put( participant.getId(), participant );

        String specialRole = "newSpecialRole";
        String action = "newAction";

        HashMap<String, String> param = new HashMap<String, String>();
        param.put(SPECIAL_ROLE, specialRole);
        param.put(ROLE_ACTION, action);
        param.put(USER_ID, participant.getId());
        
        
        // Stub
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions ); 
        when(chatRoomModel.getLocalUser()).thenReturn( participant );
        when(chatRoomModel.getItemList()).thenReturn( itemList );
        when(chatRoomModel.getParticipants()).thenReturn(  
                participants.values().toArray( new IParticipant[participants.size()] ) );
        when(participant.getRole()).thenReturn( participantRole );
        when(chatRoomModel.getLocalUserOrParticipant( anyString() )).thenReturn( participant );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( talkModel ); 
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(multiChatServiceActions, never()).SendExtensionProtocolMessage( CHANGE_SPECIAL_PRIVILEGE, param );
    }
    
    @Test

    public void testMultiChatExtensionProtocolEventStatusChangeWithGoodStatus(){
        // Setup
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ConferenceStatus status = ConferenceStatus.STARTED;
        // Stub
        when(event.getExtensionParameter( STATUS )).thenReturn( status.toString() );
        when(event.getExtensionName()).thenReturn( STATUS_CHANGE );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(chatRoomModel).setStatus( status );
    }
    
    @Test
    public void testMultiChatExtensionProtocolEventStatusChangeWithBadStatus(){
        // Setup
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        // Stub
        when(event.getExtensionParameter( STATUS )).thenReturn(null);
        when(event.getExtensionName()).thenReturn( STATUS_CHANGE );       
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        try{
        service.onBackendEvent( (IBackendEvent) event );        
        } catch (IllegalArgumentException e) {}
          catch (NullPointerException e) {}
          catch (Exception e){fail("Only IllegalArgumentException || NullPointerException");}
        
        // Verify
        verify(chatRoomModel, never()).setStatus( (ConferenceStatus)anyObject() );
    }

    @Test
    public void testMultiChatExtensionProtocolEventItemListDecoding(){
        // Setup
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        IItemList itemList = mock(IItemList.class);
        // Stub
        //when(event.getExtensionParameter( ITEMS )).thenReturn( "" );
        when(event.getExtensionName()).thenReturn( ITEM_LIST );
        when(chatRoomModel.getItemList()).thenReturn( itemList );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(itemList).decode( event.getExtensionParameter( ITEMS ));
    }
    
    @Test
    public void testMultiChatExtensionProtocolEventCurrentAgendaItemSetThread(){
        // Setup
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class);
        IItemList itemList = mock(IItemList.class);
        IDiscussionItem discussionItem = mock(IDiscussionItem.class);
        String threadId = "1";
        // Stub
        when(event.getExtensionParameter( ITEM_ID )).thenReturn( threadId );
        when(event.getExtensionName()).thenReturn( CURRENT_AGENDA_ITEM );
        when(chatRoomModel.getItemList()).thenReturn( itemList );
        when(itemList.getItem( Integer.parseInt(threadId) )).thenReturn(discussionItem);
    
        // Installation
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(chatRoomModel).setSubject( anyString(), anyString() );
        verify(model).setCurrentThread( threadId );
    }
    

    @Test
    public void testMultiChatExtensionProtocolEventRiseHandAsModerator(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class); 
        IParticipant participant = mock(IParticipant.class);
                        
        // Stub
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(chatRoomModel.getLocalUser()).thenReturn( participant );
        when(participant.getRole()).thenReturn( IParticipant.Role.MODERATOR );
        when(event.getExtensionName()).thenReturn( RISE_HAND );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // We don't check the arguments further because this is already tested in the notifyQuestionUpdate() interaction
        verify(multiChatServiceActions, never()).SendExtensionProtocolMessage(anyString(), (HashMap)anyObject());
    }
    

    @Test
    public void testMultiChatExtensionProtocolEventRiseHandAsNullParticipant(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class); 
        IParticipant participant = mock(IParticipant.class);
                        
        // Stub
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(chatRoomModel.getLocalUser()).thenReturn( participant );
        when(chatRoomModel.getLocalUserOrParticipant( anyString())).thenReturn( null );
        when(participant.getRole()).thenReturn( IParticipant.Role.PARTICIPANT );
        when(event.getExtensionName()).thenReturn( RISE_HAND );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // We don't check the arguments further because this is already tested in the notifyQuestionUpdate() interaction
        verify(multiChatServiceActions, never()).SendExtensionProtocolMessage(anyString(), (HashMap)anyObject());
    }
    
    @Test
    public void testMultiChatExtensionProtocolEventRiseHandAsGoodParticipant(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class); 
        IParticipant participant = mock(IParticipant.class);
                        
        // Stub
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(chatRoomModel.getLocalUser()).thenReturn( participant );
        when(chatRoomModel.getLocalUserOrParticipant( anyString())).thenReturn( participant );
        when(participant.getRole()).thenReturn( IParticipant.Role.PARTICIPANT );
        when(event.getExtensionName()).thenReturn( RISE_HAND );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // We don't check the arguments further because this is already tested in the notifyQuestionUpdate() interaction
        verify(multiChatServiceActions, never()).SendExtensionProtocolMessage(anyString(), (HashMap)anyObject());
    }
    
    /* This test can't be done until a refactoring process make the handRaisingModel mockable.
     * 
    @Test
    public void testMultiChatExtensionProtocolEventQuestionUpdate(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class); 
        IParticipant participant = mock(IParticipant.class);
                        
        // Stub
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(chatRoomModel.getLocalUser()).thenReturn( participant );
        when(chatRoomModel.getLocalUserOrParticipant( anyString())).thenReturn( participant );
        when(participant.getRole()).thenReturn( IParticipant.Role.PARTICIPANT );
        when(event.getExtensionName()).thenReturn( QUESTION_UPDATE );
        when(event.getExtensionParameter( QUESTION_ID )).thenReturn( "1" );
        when(event.getExtensionParameter( QUESTION_STATUS )).thenReturn( QuestionStatus.PENDING.toString() );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // We can't do this test because the handRaisingModel can't be mocked.
        
    }*/
    
    @Test
    public void testMultiChatExtensionProtocolEventChangeSpecialPrivilegeGrant(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class); 
        IParticipant participant = mock(IParticipant.class);
        String privilege = "privilege"; 
            
        // Stub
        when(event.getExtensionName()).thenReturn( CHANGE_SPECIAL_PRIVILEGE );
        when(event.getExtensionParameter( ROLE_ACTION )).thenReturn( SpecialPrivilegesAction.GRANT );
        when(event.getExtensionParameter( SPECIAL_ROLE )).thenReturn( privilege );
        when(chatRoomModel.getLocalUserOrParticipant( anyString())).thenReturn( participant );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant).addSpecialPriviliges( privilege );
    }
    
    @Test
    public void testMultiChatExtensionProtocolEventChangeSpecialPrivilegeRevoke(){
        // Setup
        IBackend backend = mock(IBackend.class);
        MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
        IConferenceModel chatRoomModel = mock(IConferenceModel.class);
        ITalkModel model = mock(ITalkModel.class); 
        IParticipant participant = mock(IParticipant.class);
        String privilege = "privilege"; 
            
        // Stub
        when(event.getExtensionName()).thenReturn( CHANGE_SPECIAL_PRIVILEGE );
        when(event.getExtensionParameter( ROLE_ACTION )).thenReturn( SpecialPrivilegesAction.REVOKE );
        when(event.getExtensionParameter( SPECIAL_ROLE )).thenReturn( privilege );
        when(chatRoomModel.getLocalUserOrParticipant( anyString())).thenReturn( participant );
    
        // Installation
        service.setBackend( backend );
        service.setTalkModel( model );
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant).removeSpecialPrivileges( privilege );
    }
    
   @Test
   public void testMultiChatExtensionProtocolEventChangeWhiteBoardChanged(){
       // Setup
       IBackend backend = mock(IBackend.class);
       MultiChatExtensionProtocolEvent event = mock(MultiChatExtensionProtocolEvent.class);
       IConferenceModel chatRoomModel = mock(IConferenceModel.class);
       String whiteboardText = "whiteboard text";
       // Stub
       when(event.getExtensionName()).thenReturn( WHITE_BOARD_CHANGED );
       when(event.getExtensionParameter( WHITE_BOARD_TEXT )).thenReturn( whiteboardText );
   
       // Installation
       service.setBackend( backend );
       service.setChatRoomModel( chatRoomModel );
       
       // Exercise
       service.onBackendEvent( (IBackendEvent) event );
       
       // Verify
       verify(chatRoomModel).setWhiteBoardText( whiteboardText );
   }
}
