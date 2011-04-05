package it.uniba.di.cdg.xcore.one2one;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

import it.uniba.di.cdg.xcore.one2one.ChatManager;
import it.uniba.di.cdg.xcore.one2one.ChatManager.IChatStatusListener;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;

public class ChatManagerBehaviorTest {
    // SUT
    private ChatManager manager;    

    @Before
    public void setUp() throws Exception {
        manager = new ChatManager();
    }
    /*
     * Note that manager.close() is not exercised because there is a listener registered 
     * to close the chat when the view is closed. The partClosed() event is fired when JUnit 
     * closes the dummy workbench when testing so manager.close() is automatically fired.
     * For this, mocking framework doesn't realize the interaction with chatServiceAction
     * and backendHelper.
     * */
    @Test
    public void testOpenWithGoodBackendHelper(){
        // Setup 
        ChatContext chatContext = mock(ChatContext.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IBuddyRoster roster = mock(IBuddyRoster.class);
        IBackend backend = mock(IBackend.class);
        IChatServiceActions chatServiceAction = mock(IChatServiceActions.class);
        IBuddy buddy = mock(IBuddy.class);
        IChatStatusListener chatlistener = mock(IChatStatusListener.class);
        
        String buddyId = "buddyId";
        String buddyName = "buddyname";
        
        // STUB        
        when(backendHelper.getRoster()).thenReturn( roster );
        when(roster.getBuddy(buddyId)).thenReturn( buddy );
        when(buddy.getName()).thenReturn( buddyName ); 
        when(roster.getBackend()).thenReturn( backend );
        when(backend.getChatServiceAction()).thenReturn( chatServiceAction );
        when(chatContext.getBuddyId()).thenReturn( buddyId );
        
        // Installation
        this.manager.setBackendHelper( backendHelper );
        this.manager.addChatStatusListener(chatlistener);
        
        // Exercise
        try{
        if (!this.manager.open( chatContext ))
           fail("Open failed, but backend helper was ok");
        } catch (NullPointerException e){fail("NullPointerException: you probably run this test as a JUnit Test (Not JUnit Plug-In Test)");}        
        //this.manager.close();
        
       
        // Verify
        // We cannot test WorkbenchWindow and TalkView further because a mock can't be installed
        verify(backendHelper).registerBackendListener( this.manager );
        verify(chatServiceAction).OpenChat( buddyId );    
        //verify(chatServiceAction).CloseChat( anyString() );
        //verify(backendHelper).unregisterBackendListener( this.manager );
        
    }

    @Test
    public void testOpenWithBadBackendHelper(){
        // Setup 
        ChatContext chatContext = mock(ChatContext.class);
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        IBuddyRoster roster = mock(IBuddyRoster.class);
        IBackend backend = mock(IBackend.class);
        IChatServiceActions chatServiceAction = mock(IChatServiceActions.class);
        IBuddy buddy = mock(IBuddy.class);
        IUIHelper uihelper = mock(IUIHelper.class);
        
        String buddyId = "buddyId";
        String buddyName = "buddyname";
        
        // STUB
        when(backendHelper.getRoster()).thenReturn( roster );
        when(roster.getBuddy(buddyId)).thenReturn( buddy );
        when(roster.getBackend()).thenReturn( backend );
        when(buddy.getName()).thenReturn( buddyName ); 
        when(chatContext.getBuddyId()).thenReturn( buddyId );
        when(backend.getChatServiceAction()).thenReturn( chatServiceAction );
        
        // Installation
        this.manager.setBackendHelper( null );
        this.manager.setUihelper( uihelper );
        
        // Exercise
        if (this.manager.open( chatContext ))
            fail("Open successed, but backend helper was null");        
       
        // Verify
        // We cannot test WorkbenchWindow and TalkView further because a mock can't be installed
        // User should be warned:
        verify(uihelper).showErrorMessage( anyString() );      
        
    }

}
