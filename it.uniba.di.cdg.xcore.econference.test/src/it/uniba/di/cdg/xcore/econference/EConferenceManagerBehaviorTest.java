package it.uniba.di.cdg.xcore.econference;
import it.uniba.di.cdg.xcore.econference.internal.EConferenceHelper;
import it.uniba.di.cdg.xcore.econference.internal.EConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.ui.views.AgendaView;
import it.uniba.di.cdg.xcore.econference.ui.views.HandRaisingView;
import it.uniba.di.cdg.xcore.econference.ui.views.WhiteBoardView;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.services.JoinException;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModelListener;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.MultiChatTalkView;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;

public class EConferenceManagerBehaviorTest {
    //SUT
    private EConferenceManagerSubClass manager;
    //DOCs
    private IEConferenceService service;    
    private INetworkBackendHelper backendHelper;
    private IUIHelper uiHelper;
    private IWorkbenchWindow workbenchWindow;
    private IBackend backend;
    private IBackendRegistry backendRegistry;
    private EConferenceContext context;
    private IMultiChatServiceActions multiChatServiceActions;
    private IWorkbenchPage workbenchPage;
    private boolean autojoin = true;
    private WhiteBoardView whiteBoardView;
    private AgendaView agendaView;
    private HandRaisingView handRaisingView; 
    private MultiChatTalkView talkViewPart;
    private ChatRoomView chatRoomViewPart; 
    private IConferenceModel conferenceModel; 
    private IItemList itemList;
    
    /* Since main interaction of this SUT requires a lot of mock configuration and all of the tests
     * require that behavior, many Mock instantiations and Stub have been moved in setUp
     */
    @Before
    public void setUp() throws Exception {        
        service = mock(IEConferenceService.class);
        manager = new EConferenceManagerSubClass();
        manager.setChatService( service );
        
        backendHelper = mock(INetworkBackendHelper.class);
        uiHelper = mock(IUIHelper.class);
        workbenchWindow = mock(IWorkbenchWindow.class);        
        backend = mock(IBackend.class);
        backendRegistry = mock(IBackendRegistry.class);
        context = mock(EConferenceContext.class);
        multiChatServiceActions = mock(IMultiChatServiceActions.class);
        workbenchPage = mock(IWorkbenchPage.class);
        whiteBoardView = mock(WhiteBoardView.class);
        agendaView = mock(AgendaView.class);
        handRaisingView = mock(HandRaisingView.class);
        talkViewPart = mock(MultiChatTalkView.class);
        chatRoomViewPart = mock(ChatRoomView.class);
        conferenceModel = mock(IConferenceModel.class);
        itemList = mock(IItemList.class);
        
        when(backendHelper.getRegistry()).thenReturn( backendRegistry );
        when(backendRegistry.getBackend(anyString())).thenReturn( backend );
        when(backendRegistry.getDefaultBackend()).thenReturn( backend );      
        when(backend.getHelper()).thenReturn( backendHelper );
        when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
        when(context.getBackendId()).thenReturn( "backendID" );
        when(context.getRoom()).thenReturn( "room" );
        when(workbenchWindow.getActivePage()).thenReturn( workbenchPage );  
        when(workbenchPage.findView( ChatRoomView.ID )).thenReturn( (IViewPart)chatRoomViewPart ); 
        try {
            when(workbenchPage.showView( MultiChatTalkView.ID )).thenReturn( (IViewPart)talkViewPart ); 
            when(workbenchPage.showView(AgendaView.ID)).thenReturn( (IViewPart)agendaView);
            when(workbenchPage.showView(WhiteBoardView.ID)).thenReturn( (IViewPart)whiteBoardView);
            when(workbenchPage.showView(HandRaisingView.ID)).thenReturn( (IViewPart)handRaisingView);
        } catch (Exception e) { }
        when(service.getModel()).thenReturn( conferenceModel );
        when(conferenceModel.getItemList()).thenReturn( itemList );
        
        manager.setBackendHelper( backendHelper );
        manager.setUihelper( uiHelper );
        manager.setWorkbenchWindow( workbenchWindow );        
    }
    
    @Test
    /* This test will open a new conference from the EConferenceManager with a Mocked context
     * Note that the exercising has been done on a method of the MultiChatManager that is extended by EConferenceManager
     * This test will focus on the interaction of EConferenceManager, the rest will be verified in the MultiChatManager tests 
     * */
    public void testOpenWithGoodService(){
      //Setup      
      IParticipant participant = mock(IParticipant.class);
      Role role = Role.PARTICIPANT;
            
      //Stub                           
      when(conferenceModel.getLocalUser()).thenReturn( participant );
      when(participant.getRole()).thenReturn( role );
      //Exercise      
      try{    
      manager.open( context, autojoin );          
      }
      catch (Exception e) {fail("Exception should not be thrown");}
      //Verify
      // registration is done in setupListeners() and in the service.join() methods
      verify(backendHelper, atLeast(1)).registerBackendListener( (IBackendEventListener)anyObject() );
      verify(agendaView).setManager( manager );
      // The agenda readOnly property is set checking the user role.
      // This attribute is saved in the running service but the whole service interaction 
      // is tested in MultiChatManager behavior Test so we just verify that the agenda property is configured.
//      verify(agendaView).setReadOnly( !Role.MODERATOR.equals(role) );
      verify(whiteBoardView).setManager( manager );
      verify(whiteBoardView).setReadOnly( true );
      verify(handRaisingView).setManager( manager );
      verify(talkViewPart).setReadOnly( false );
      verify(talkViewPart, atLeast(1)).setTitleText( anyString() );      
      verify(conferenceModel, atLeast(1)).addListener( (IChatRoomModelListener )anyObject());
      
    }
    
    @Test
    /* This test will open a new conference from the EConferenceManager with a null context
     * Note that the exercising has been done on a method of the MultiChatManager that is extended by EConferenceManager
     * This test will focus on the interaction of EConferenceManager, the rest will be verified in the MultiChatManager tests 
     * */
    public void testOpenWithServiceFailure(){
      // Stub     
      try {
        doThrow(new JoinException("JoinException: IEConferenceService could not join")).when(service).join();
      } catch (Exception e) { }
      
      // Installation
      // Done in setUp()
      
      // Exercise
      try{ 
      manager.open( null, autojoin );         
      }
      catch (Exception e) {fail("Only JoinException should be catched by the manager");}
      // Verify
      verify(service).leave();
      // An error message should be notified
      verify(uiHelper).showErrorMessage( anyString() ); 
    }
    
    @Test
    public void testStatusNotification(){
        // Setup
        ConferenceStatus status = ConferenceStatus.STARTED;
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.setStatus(status);
        // Verify
        verify(whiteBoardView).setReadOnly(ConferenceStatus.STOPPED.equals( status ));
        verify(service).notifyStatusChange( status );
    }
    
    @Test
    public void testInviteNewParticipant(){
        // Setup
        String participantId = "something";
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.inviteNewParticipant( participantId );
        // Verify
        verify(service).invite( participantId, IEConferenceHelper.ECONFERENCE_REASON );
    }
    
    @Test
    public void testNotifyWhiteBoardChanged(){
        // Setup
        String whiteboardText = "something";
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyWhiteBoardChanged( whiteboardText );
        // Verify
        verify(service).notifyWhiteBoardChanged( whiteboardText );
    }
    
    @Test
    public void testNotifySpecialPrivilegeChanged(){
        // Setup
        IParticipant p = mock(IParticipant.class);
        String newPrivilege = "MODERATOR";
        String action = "action";
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifySpecialPrivilegeChanged( p, newPrivilege, action );
        // Verify
        verify(service).notifyChangedSpecialPrivilege( p, newPrivilege, action );    
    }
    
    @Test
    public void testNotifyChangedMUCPersonalPrivilege(){
        // Setup
        IParticipant p = mock(IParticipant.class);
        String personalStatus = "status";
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyChangedMUCPersonalPrivilege( p, personalStatus );
        // Verify
        verify(service).notifyChangedMUCPersonalPrivilege( p, personalStatus );    
    }
    
    @Test
    public void testNotifyRaiseHand(){
        // Setup
        IParticipant moderator = mock(IParticipant.class);
        String question = "To be or not to be?";
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyRaiseHand( moderator, question );
        // Verify
        verify(service).notifyRaiseHand( question, moderator.getId() );    
    }
    
    @Test
    public void testNotifyQuestionUpdateGoodParticipant( ){
        // Setup
        IQuestion question = mock(IQuestion.class);
        IParticipant participant = mock(IParticipant.class);
        when(conferenceModel.getParticipant( anyString() )).thenReturn( participant );
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyQuestionUpdate( question );
        // Verify
        verify(service).notifyQuestionUpdate( question );  
    }
    
    @Test
    public void testNotifyQuestionUpdateUnknownParticipant( ){
        // Setup
        IQuestion question = mock(IQuestion.class);
        when(conferenceModel.getParticipant( anyString() )).thenReturn( null );
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyQuestionUpdate( question );
        // Verify
        verify(service, never()).notifyQuestionUpdate( question );  
    }
    
    @Test
    public void testNotifyCurrentAgendaItemChanged( ){
        // Setup
        String itemId = "id";
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyCurrentAgendaItemChanged( itemId );
        // Verify
        verify(service).notifyCurrentAgendaItemChanged( itemId );  
    }
    
    @Test
    public void testNotifyItemListToRemote( ){
        // Exercise
        try {
            manager.open(context, autojoin);
        } catch (Exception e) { fail("Exception should not be thrown");}
        manager.notifyItemListToRemote( );
        // Verify
        verify(service).notifyItemListToRemote( );  
    }
    
}
