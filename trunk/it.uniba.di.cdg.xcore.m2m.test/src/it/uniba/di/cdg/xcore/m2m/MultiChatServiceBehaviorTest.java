package it.uniba.di.cdg.xcore.m2m;

import static org.mockito.Mockito.*;
import java.util.HashMap;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatService;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatNameChangedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatSubjectUpdatedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserJoinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceRevokedEvent;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.services.JoinException;
import it.uniba.di.cdg.xcore.network.services.NetworkServiceException;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import it.uniba.di.cdg.xcore.network.model.tv.Entry;

public class MultiChatServiceBehaviorTest {
    // SUT
    private MultiChatService service;
    
    protected final static String PRIVATE_MESSAGE = "PrivateMessage";
    protected final static String MESSAGE = "Message";
    protected final static String TO = "To";
    protected final static String FROM = "From";    
    protected final static String VIEW_READ_ONLY = "ViewReadOnly";
    protected final static String VIEW_ID = "viewId";
    protected final static String READ_ONLY = "readOnly";
    
    @Before
    public void setUp() throws Exception {
        service = new MultiChatService();
    }
    
    @Test
    public void testServiceJoinWithNoNickname(){
        // Setup
        IBackend backend = mock(IBackend.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);
        MultiChatContext context = mock(MultiChatContext.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
                
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );       
        
        // Installation        
        service.setBackend( backend );
        service.setChatRoomModel( chatRoomModel );
        service.setContext( context );    
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {} 
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.leave();
        
        // Verify        
        verify(backendHelper).registerBackendListener( (IBackendEventListener) anyObject());
        try {
            verify(multiChatServiceActions).join( anyString(), anyString(), anyString(), anyString(), anyBoolean() );
        } catch (JoinException e) { }
        // Since we provided no NickName, the join will set a standard nickname for us
        verify(context).setNickName(anyString());
        // In this test we focus on the chatRoomModel interaction with our MultiChatService
        // so we just check that a new participant is always added        
        verify(chatRoomModel).addParticipant((IParticipant) anyObject() );
        verify(chatRoomModel).setLocalUser( (IParticipant) anyObject() );
        verify(multiChatServiceActions).leave();
    }
    
    @Test
    public void testServiceJoinWithNickname(){
        // Setup
        IBackend backend = mock(IBackend.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);
        MultiChatContext context = mock(MultiChatContext.class);
        //ITalkModel talkModel = mock(ITalkModel.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
                
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );      
        when(context.getNickName()).thenReturn( "pippo" );
        
        // Installation        
        service.setBackend( backend );
        service.setChatRoomModel( chatRoomModel );
        service.setContext( context );        
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {} 
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        service.leave();
        
        // Verify        
        verify(backendHelper).registerBackendListener( (IBackendEventListener) anyObject());
        try {
            verify(multiChatServiceActions).join( anyString(), anyString(), anyString(), anyString(), anyBoolean() );
        } catch (JoinException e) { }
        // Since we provided a NickName, the join will not set a standard nickname for us
        verify(context, never()).setNickName(anyString());
        // In this test we focus on the chatRoomModel interaction with our MultiChatService
        // so we just check that a new participant is always added
        verify(chatRoomModel).addParticipant((IParticipant) anyObject() );
        verify(chatRoomModel).setLocalUser( (IParticipant) anyObject() );
        verify(multiChatServiceActions).leave();
    }
    
    /* MultiChatServiceActions behavior tests are straightforward, every service operation has a corresponding
     * action that will be called.
     */    
    @Test 
    public void testServiceActionsGetRoomInfo(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
          
        service.getRoomInfo( room );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).getRoomInfo( "room" );        
    }
    
    @Test 
    public void testServiceActionsInvite(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        String userId = "userid";
        String reason = "reason";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
          
        service.invite( userId, reason );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).invite( context.getRoom(), userId, reason );       
    }
    
    @Test 
    public void testServiceActionsDeclineInvitation(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);
        InvitationEvent invitationEvent = mock(InvitationEvent.class);

        String room = "room";
        String nickName = "pippo";
        String reason = "reason";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
          
        service.declineInvitation( invitationEvent, reason );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).declineInvitation( invitationEvent.getRoom(), invitationEvent.getInviter(), reason );        
    }
    
    @Test 
    public void testServiceActionsSendPrivateMessage(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);
        IParticipant participant = mock(IParticipant.class);

        String room = "room";
        String nickName = "pippo";
        String message = "hello";
        String userId = "userid";
        

        HashMap<String, String> pmParam = new HashMap<String, String>();
        pmParam.put(MESSAGE, message);
        pmParam.put(TO, userId);
        pmParam.put(FROM, backend.getUserId());  
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room ); 
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

        service.sendPrivateMessage( participant, message );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(PRIVATE_MESSAGE, pmParam);        
    }
    
    @Test 
    public void testServiceActionsSendMessage(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        String message = "hello";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
          
        service.sendMessage( message );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).sendMessage( room, message );        
    }
    
    @Test 
    public void testServiceActionsGrantVoice(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
          
        service.grantVoice( nickName );
        service.leave();
        
        // Verify   
        verify(multiChatServiceActions).grantVoice( room, nickName );     
    }
    
    @Test 
    public void testServiceActionsRevokeVoice(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}
        
        // Note that we don't grant the voice permission first because the revoke action is called anyway  
        service.revokeVoice( nickName );
        service.leave();
        
        // Verify   
        verify(multiChatServiceActions).revokeVoice( room, nickName );    
    }
    
    @Test 
    public void testServiceActionsChangeSubject(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        String subject = "subject";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}

        service.changeSubject( subject );
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).changeSubject( room, subject );     
    }
    
    @Test 
    public void testServiceActionsTypingNotification(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);
        IParticipant participant = mock(IParticipant.class);

        String room = "room";
        String nickName = "pippo";
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
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
          
        service.typing();
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).sendTyping( participant.getNickName() );        
    }
    
    @Test 
    public void testServiceActionsNotifyViewReadOnly(){
        // Setup
        IBackend backend = mock(IBackend.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
        MultiChatContext context = mock(MultiChatContext.class);

        String room = "room";
        String nickName = "pippo";
        String viewId = "viewid";
        // it doesn't matter if it's true or false because it will be notified anyway, we just check it keeps unchanged
        boolean readOnly = true; 
        
        HashMap<String, String> roParam = new HashMap<String, String>();
        roParam.put(VIEW_ID, viewId);
        roParam.put(READ_ONLY, new Boolean(readOnly).toString());
        
        // Stub
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );   
        when(context.getNickName()).thenReturn( nickName );
        when(context.getRoom()).thenReturn( room );    
        
        // Installation    
        service.setBackend( backend );  
        service.setContext( context );          
        
        // Exercise
        try {
            service.join();
        } catch (JoinException e) {}
          catch (NetworkServiceException e) {}
          catch (Exception e){fail("Only JoinException || NetworkServiceException");}

        service.notifyViewReadOnly( viewId, readOnly );   
        service.leave();
        
        // Verify
        verify(multiChatServiceActions).SendExtensionProtocolMessage(VIEW_READ_ONLY, roParam);  
    }
          
    
    // onBackendEvent tests
    // Note that the listeners interactions can't be tested because the listeners lists are final
    // so even if we subclass the SUT and build a setter method they are unreachable.
    
    @Test
    public void testServiceOnMultiChatMessageEvent(){
        // Setup
        MultiChatMessageEvent event = mock(MultiChatMessageEvent.class);
        ITalkModel talkModel = mock(ITalkModel.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);
        IParticipant participant = mock(IParticipant.class);
        
        // Stub
        when(chatRoomModel.getLocalUserOrParticipant( anyString() )).thenReturn( participant );
    
        // Installation
        service.setTalkModel( talkModel ); 
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // We can't check the entry object in detail because his creation method is not visible
        // and if we build it manually we'll get an over-specified test
        verify(talkModel).addEntry( (Entry)anyObject() );
    }
    
    
    @Test
    public void testServiceOnMultiChatSubjectUpdatedEvent(){
        // Setup
        MultiChatSubjectUpdatedEvent event = mock(MultiChatSubjectUpdatedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);       
        
        // Stub
        // None
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(chatRoomModel).setSubject( event.getSubject(), event.getFrom() );
    }
    

    @Test
    public void testServiceOnMultiChatUserJoinedEventWithExistingParticipant(){
        // Setup
        MultiChatUserJoinedEvent event = mock(MultiChatUserJoinedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);
        IParticipant participant = mock(IParticipant.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( participant );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant).setStatus( Status.JOINED );
    }
    
    @Test
    public void testServiceOnMultiChatUserJoinedEventWithNotExistingParticipant(){
        // Setup
        MultiChatUserJoinedEvent event = mock(MultiChatUserJoinedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class);        
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( null );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(chatRoomModel).addParticipant( (IParticipant)anyObject() );
    }
    

    @Test
    public void testServiceOnMultiChatUserLeftEventWithExistingParticipant(){
        // Setup
        MultiChatUserLeftEvent event = mock(MultiChatUserLeftEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        
        // Stub
        when(chatRoomModel.getParticipantByNickName(anyString())).thenReturn( participant );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(chatRoomModel).removeParticipant( participant );
    }
    

    @Test
    public void testServiceOnMultiChatUserLeftEventWithNotExistingParticipant(){
        // Setup
        MultiChatUserLeftEvent event = mock(MultiChatUserLeftEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        
        // Stub
        when(chatRoomModel.getParticipantByNickName(anyString())).thenReturn( null );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(chatRoomModel, never()).removeParticipant( participant );
    }
    

    @Test
    public void testServiceOnMultiChatNameChangedEventWithExistingParticipant(){
        // Setup
        MultiChatNameChangedEvent event = mock(MultiChatNameChangedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( participant );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // The server will throw a new joined event so the current participant must be removed
        verify(chatRoomModel).removeParticipant( participant );
    }
    

    @Test
    public void testServiceOnMultiChatNameChangedEventWithNotExistingParticipant(){
        // Setup
        MultiChatNameChangedEvent event = mock(MultiChatNameChangedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( null );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        // The server will fire a new joined event so the current participant must be removed
        verify(chatRoomModel, never()).removeParticipant( participant );
    }
    

    @Test
    public void testServiceOnMultiChatVoiceGrantedEventWithExistingParticipant(){
        // Setup
        MultiChatVoiceGrantedEvent event = mock(MultiChatVoiceGrantedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        IBackend backend = mock(IBackend.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( participant );
        String userid = "userid";
        // we can't use a anyString() matcher because mock framework doesn't mock "equals" method
        when(event.getUserId()).thenReturn( userid );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        service.setBackend( backend );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant).setStatus( Status.JOINED );
    }
    

    @Test
    public void testServiceOnMultiChatVoiceGrantedEventWithNotExistingParticipant(){
        // Setup
        MultiChatVoiceGrantedEvent event = mock(MultiChatVoiceGrantedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        IBackend backend = mock(IBackend.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( null );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        service.setBackend( backend );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant, never()).setStatus( Status.JOINED );
    }
    


    @Test
    public void testServiceOnMultiChatVoiceRevokedEventWithExistingParticipant(){
        // Setup
        MultiChatVoiceRevokedEvent event = mock(MultiChatVoiceRevokedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        IBackend backend = mock(IBackend.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( participant );
        String userid = "userid";
        // we can't use a anyString() matcher because mock framework doesn't mock "equals" method
        when(event.getUserId()).thenReturn( userid );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        service.setBackend( backend );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant).setStatus( Status.FROZEN );
    }
    


    @Test
    public void testServiceOnMultiChatVoiceRevokedEventWithNotExistingParticipant(){
        // Setup
        MultiChatVoiceRevokedEvent event = mock(MultiChatVoiceRevokedEvent.class);
        IChatRoomModel chatRoomModel = mock(IChatRoomModel.class); 
        IParticipant participant = mock(IParticipant.class);
        IBackend backend = mock(IBackend.class);
        
        // Stub
        when(chatRoomModel.getParticipant(anyString())).thenReturn( null );
    
        // Installation
        service.setChatRoomModel( chatRoomModel );
        service.setBackend( backend );
        
        // Exercise        
        service.onBackendEvent( (IBackendEvent) event );
        
        // Verify
        verify(participant, never()).setStatus( Status.FROZEN );
    }
}
