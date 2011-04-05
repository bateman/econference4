package it.uniba.di.cdg.xcore.m2m;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.m2m.events.IManagerEventListener;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.m2m.service.IInvitationRejectedListener;
import it.uniba.di.cdg.xcore.m2m.service.IMultiChatService;
import it.uniba.di.cdg.xcore.m2m.service.IUserStatusListener;
import it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.MultiChatTalkView;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.services.JoinException;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import static org.junit.Assert.fail;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;
import org.eclipse.ui.IPerspectiveListener;

import org.junit.Before;
import org.junit.Test;

public class MultiChatManagerBehaviorTest {
    //SUT
    private MultiChatManagerSubClass manager;

    @Before
    public void setUp() throws Exception {
        manager = new MultiChatManagerSubClass();
    }
    
    @Test
    /* This test will open a new conference from the MultiChatManager with a Mocked context
     * */
    public void testOpenManagerWithGoodContext(){
      //Setup        
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
      IUIHelper uiHelper = mock(IUIHelper.class);
      IWorkbenchWindow workbenchWindow = mock(IWorkbenchWindow.class);
      IMultiChatService service = mock(IMultiChatService.class);
      
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      EConferenceContext context = mock(EConferenceContext.class);
      IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
      IWorkbenchPage workbenchPage = mock(IWorkbenchPage.class);
      
      MultiChatTalkView talkViewPart = mock(MultiChatTalkView.class);
      ChatRoomView chatRoomViewPart = mock(ChatRoomView.class);
      
      boolean autojoin = true;
      
      //Stub      
      try{ // Exception may be raised in the stubbed methods so we need to put the stubbing phase in a try/catch block.          
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
      when(backendRegistry.getBackend(anyString())).thenReturn( backend );
      when(backendRegistry.getDefaultBackend()).thenReturn( backend );      
      when(backend.getHelper()).thenReturn( backendHelper );
      when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
      when(context.getBackendId()).thenReturn( "backendID" );
      when(context.getRoom()).thenReturn( "room" );
      when(workbenchWindow.getActivePage()).thenReturn( workbenchPage );
      when(workbenchPage.showView( MultiChatTalkView.ID )).thenReturn( (IViewPart)talkViewPart );      
      when(workbenchPage.findView( ChatRoomView.ID )).thenReturn( (IViewPart)chatRoomViewPart );
                              
      //Installation
      manager.setChatService( service );
      manager.setBackendHelper( backendHelper );
      manager.setUihelper( uiHelper );
      manager.setWorkbenchWindow( workbenchWindow );
      
      //Exercise
      manager.open( context, autojoin );
      manager.close();          
      }
      catch (Exception e) {}
      //Verify
      try {
        verify(service).join();
      } catch (Exception e) {}
      verify(talkViewPart).setTitleText( anyString() );
      verify(talkViewPart).setModel((ITalkModel)anyObject());
      verify(talkViewPart).setFocus();
      verify(talkViewPart).addTypingListener((IMultiChatService)anyObject());
      verify(talkViewPart).setManager(manager);
      verify(workbenchWindow).addPerspectiveListener( (IPerspectiveListener) anyObject() );
      verify(chatRoomViewPart).setManager(manager);
      verify(backendHelper, times(2)).registerBackendListener( (IBackendEventListener)anyObject() );
      verify(service).leave();      
      verify(workbenchWindow).removePerspectiveListener( (IPerspectiveListener) anyObject() );      
    }
    
    @Test
    /* This test will open a new conference from the MultiChatManager with a Mocked context
     * and will focus on the listeners setup
     * */
    public void testOpenManagerWithGoodContextSetupListener(){
      //Setup        
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
      IUIHelper uiHelper = mock(IUIHelper.class);
      IWorkbenchWindow workbenchWindow = mock(IWorkbenchWindow.class);
      IMultiChatService service = mock(IMultiChatService.class);
      
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      EConferenceContext context = mock(EConferenceContext.class);
      IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
      IWorkbenchPage workbenchPage = mock(IWorkbenchPage.class);
      
      MultiChatTalkView talkViewPart = mock(MultiChatTalkView.class);
      ChatRoomView chatRoomViewPart = mock(ChatRoomView.class);
      
      boolean autojoin = true;
      
      //Stub      
      try{ // Exception may be raised in the stubbed methods so we need to put the stubbing phase in a try/catch block.          
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
      when(backendRegistry.getBackend(anyString())).thenReturn( backend );
      when(backendRegistry.getDefaultBackend()).thenReturn( backend );      
      when(backend.getHelper()).thenReturn( backendHelper );
      when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );
      when(context.getBackendId()).thenReturn( "backendID" );
      when(context.getRoom()).thenReturn( "room" );
      when(workbenchWindow.getActivePage()).thenReturn( workbenchPage );
      when(workbenchPage.showView( MultiChatTalkView.ID )).thenReturn( (IViewPart)talkViewPart );      
      when(workbenchPage.findView( ChatRoomView.ID )).thenReturn( (IViewPart)chatRoomViewPart );
                              
      //Installation
      manager.setChatService( service );
      manager.setBackendHelper( backendHelper );
      manager.setUihelper( uiHelper );
      manager.setWorkbenchWindow( workbenchWindow );
      
      //Exercise
      manager.open( context, autojoin );
      manager.close();          
      }
      catch (Exception e) {}
      // Verify
      verify(talkViewPart).addListener((ISendMessagelListener)anyObject());
      verify(service).addMessageReceivedListener(talkViewPart);
      verify(talkViewPart).addTypingListener(service);
      verify(service).addTypingEventListener( talkViewPart );
      verify(service).addUserStatusListener( (IUserStatusListener)anyObject());
      verify(service).addInvitationRejectedListener( (IInvitationRejectedListener)anyObject());
      verify(service).addManagerEventListener( (IManagerEventListener)anyObject() );
      verify(backendHelper, times(2)).registerBackendListener( (IBackendEventListener)anyObject() );      
    }
    
    @Test
    /* This test will open a new conference from the MultiChatManager with a null context
     * */
    public void testOpenManagerWithBadContext(){
      //Setup        
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
      IUIHelper uiHelper = mock(IUIHelper.class);
      IWorkbenchWindow workbenchWindow = mock(IWorkbenchWindow.class);
      IMultiChatService service = mock(IMultiChatService.class);
      
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
      IWorkbenchPage workbenchPage = mock(IWorkbenchPage.class);
      
      MultiChatTalkView talkViewPart = mock(MultiChatTalkView.class);
      ChatRoomView chatRoomViewPart = mock(ChatRoomView.class);
      
      boolean autojoin = true;
      
      //Stub      
      try{ // Exception may be raised in the stubbed methods so we need to put the stubbing phase in a try/catch block.          
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
      when(backendRegistry.getBackend(anyString())).thenReturn( backend );
      when(backendRegistry.getDefaultBackend()).thenReturn( backend );      
      when(backend.getHelper()).thenReturn( backendHelper );
      when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );     
      when(workbenchWindow.getActivePage()).thenReturn( workbenchPage );
            
      when(workbenchPage.showView( MultiChatTalkView.ID )).thenReturn( (IViewPart)talkViewPart );      
      when(workbenchPage.findView( ChatRoomView.ID )).thenReturn( (IViewPart)chatRoomViewPart ); 
                        
      //Installation
      manager.setChatService( service );
      manager.setBackendHelper( backendHelper );
      manager.setUihelper( uiHelper );
      manager.setWorkbenchWindow( workbenchWindow );
      
      //Exercise
      manager.open( null, autojoin );
      fail("Context null: Exception not thrown");      
      }
      catch (Exception e) { }
      // Verify
      // We forced an exception, if the "open" behavior works there will be a sure fail
      
    }
    
    @Test
    /* This test will open a new conference from the MultiChatManager with a service join failure
     * */
    public void testOpenManagerWithServiceFailure(){
      //Setup        
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
      IUIHelper uiHelper = mock(IUIHelper.class);
      IWorkbenchWindow workbenchWindow = mock(IWorkbenchWindow.class);
      IMultiChatService service = mock(IMultiChatService.class);
      EConferenceContext context = mock(EConferenceContext.class);
      
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      IMultiChatServiceActions multiChatServiceActions = mock(IMultiChatServiceActions.class);
      IWorkbenchPage workbenchPage = mock(IWorkbenchPage.class);
      
      MultiChatTalkView talkViewPart = mock(MultiChatTalkView.class);
      ChatRoomView chatRoomViewPart = mock(ChatRoomView.class);
      
      boolean autojoin = true;
      
      //Stub      
      try{ // Exception may be raised in the stubbed methods so we need to put the stubbing phase in a try/catch block.          
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
      when(backendRegistry.getBackend(anyString())).thenReturn( backend );
      when(backendRegistry.getDefaultBackend()).thenReturn( backend );      
      when(backend.getHelper()).thenReturn( backendHelper );
      when(backend.getMultiChatServiceAction()).thenReturn( multiChatServiceActions );     
      when(workbenchWindow.getActivePage()).thenReturn( workbenchPage );
            
      when(workbenchPage.showView( MultiChatTalkView.ID )).thenReturn( (IViewPart)talkViewPart );      
      when(workbenchPage.findView( ChatRoomView.ID )).thenReturn( (IViewPart)chatRoomViewPart );
      
      doThrow(new JoinException("Service Failure Exception")).when(service).join();
                        
      //Installation
      manager.setChatService( service );
      manager.setBackendHelper( backendHelper );
      manager.setUihelper( uiHelper );
      manager.setWorkbenchWindow( workbenchWindow );
      
      //Exercise
      manager.open( context, autojoin );      
      }
      catch (JoinException e) {}
      catch (Exception e) { fail("Only JoinException should be thrown with a service join failure.");}
      // Verify
      verify(service).leave();
      verify(uiHelper).showErrorMessage(anyString());      
    }
    
    /* These tests are pretty useless because the interaction is very, very simple and the test 
     * is harder to maintain than the tested code. 
     */
    @Test
    public void testToggleFreezeUnfreeze(){
        IMultiChatService service = mock(IMultiChatService.class);
        IParticipant p1 = mock(IParticipant.class);
        IParticipant p2 = mock(IParticipant.class);
        IParticipant p3 = mock(IParticipant.class);
        IParticipant p4 = mock(IParticipant.class);
        IParticipant p5 = mock(IParticipant.class);
        
        final List<String> frozen = new ArrayList<String>();
        final List<String> unfrozen = new ArrayList<String>();        
        final List<IParticipant> participants = new ArrayList<IParticipant>();

        participants.add( p1 );
        participants.add( p2 );
        participants.add( p3 );
        participants.add( p4 );
        participants.add( p5 );

        frozen.add( p1.getNickName() );
        frozen.add( p2.getNickName() );
        frozen.add( p3.getNickName() );

        unfrozen.add( p4.getNickName() );
        unfrozen.add( p5.getNickName() );
        
        // Stub
        when(p1.getStatus()).thenReturn( Status.JOINED );
        when(p2.getStatus()).thenReturn( Status.JOINED );
        when(p3.getStatus()).thenReturn( Status.JOINED );
        when(p4.getStatus()).thenReturn( Status.FROZEN );
        when(p5.getStatus()).thenReturn( Status.FROZEN );
        
        // Installation
        manager.setChatService( service );
        // Exercise
        manager.toggleFreezeUnfreeze( participants );        
        // Verify
        verify(service).revokeVoice( frozen );
        verify(service).grantVoice( unfrozen );               
    }
    
    @Test
    public void testSendPrivateMessage(){
        IMultiChatService service = mock(IMultiChatService.class);
        IParticipant p1 = mock(IParticipant.class);
        IParticipant p2 = mock(IParticipant.class);
        IParticipant p3 = mock(IParticipant.class);
        String message = "hello"; 
               
        final List<IParticipant> participants = new ArrayList<IParticipant>();

        participants.add( p1 );
        participants.add( p2 );
        participants.add( p3 );
                
        // Installation
        manager.setChatService( service );
        // Exercise
        manager.sendPrivateMessage( participants, message );        
        // Verify
        verify(service).sendPrivateMessage( p1, message );
        verify(service).sendPrivateMessage( p2, message );
        verify(service).sendPrivateMessage( p3, message );           
    }
    
    @Test
    public void testChangeSubject(){
        IMultiChatService service = mock(IMultiChatService.class);
        String subject = "something";
        // Installation
        manager.setChatService( service );
        // Exercise
        manager.changeSubject( subject );
        // Verify
        verify(service).changeSubject( subject );
    }
    
    @Test
    public void testInviteNewParticipant(){
        IMultiChatService service = mock(IMultiChatService.class);
        String participantId = "something";
        // Installation
        manager.setChatService( service );
        // Exercise
        manager.inviteNewParticipant( participantId );
        // Verify
        verify(service).invite( participantId, IMultiChatHelper.MULTICHAT_REASON );
    }
}
