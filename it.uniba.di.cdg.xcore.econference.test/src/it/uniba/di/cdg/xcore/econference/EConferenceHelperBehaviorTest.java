package it.uniba.di.cdg.xcore.econference;

import static org.mockito.Mockito.*;
import it.uniba.di.cdg.xcore.econference.internal.EConferenceHelper;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EConferenceHelperBehaviorTest {
    //SUT
    private EConferenceHelper helper;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        this.helper = new EConferenceHelper();
    }
    
    @Test
    public void testAskUserAcceptInvitationUsernameValid(){
      //Setup        
      IUIHelper uiHelper = mock(IUIHelper.class);
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
      InvitationEvent invitationEvent = mock(InvitationEvent.class);
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      UserContext userContext = mock(UserContext.class);
      
      //Stub
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
       when(backendRegistry.getBackend( anyString() )).thenReturn( backend );      
        when(backend.getUserAccount()).thenReturn( userContext );
      when(invitationEvent.getReason()).thenReturn( "e-conference" );
      when(uiHelper.askFreeQuestion( anyString(), anyString(), anyString() )).thenReturn( "Username" );
            
      //Installation
      this.helper.setUIHelper( uiHelper );
      this.helper.setBackendHelper( backendHelper );      
      
      //Exercise
      EConferenceContext context = this.helper
                                  .askUserAcceptInvitation(invitationEvent);
      //Verify
      verify(uiHelper).askFreeQuestion( anyString(), anyString(), anyString() );
      
    }
    
    @Test
    public void testAskUserAcceptInvitationUsernameNull(){
      //Setup        
      IUIHelper uiHelper = mock(IUIHelper.class);
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
      InvitationEvent invitationEvent = mock(InvitationEvent.class);
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      UserContext userContext = mock(UserContext.class);
      
      //Stub
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
       when(backendRegistry.getBackend( anyString() )).thenReturn( backend );      
        when(backend.getUserAccount()).thenReturn( userContext );
      when(invitationEvent.getReason()).thenReturn( "e-conference" );
      when(uiHelper.askFreeQuestion( anyString(), anyString(), anyString() )).thenReturn( null );
            
      //Installation
      this.helper.setUIHelper( uiHelper );
      this.helper.setBackendHelper( backendHelper );      
      
      //Exercise
      EConferenceContext context = this.helper
                                  .askUserAcceptInvitation(invitationEvent);
      //Verify
      verify(uiHelper).askFreeQuestion( anyString(), anyString(), anyString() );
      verify(invitationEvent).decline( anyString() );
      
    } 
   
    @Test
    /*
     * Can't test IEConferenceManager further because Mock object installation is not possible
     * Note that this test will have success even if run as a JUnit Test (Not JUnit Plug-in Test)
     * the reason though will be different
     * */
    public void testOpenWithBadContext(){
      //Setup        
      IUIHelper uiHelper = mock(IUIHelper.class);
      INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);      
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      boolean autojoin = true;
            
      //Stub
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
      when(backendRegistry.getBackend( anyString() )).thenReturn( backend );      
      when(backendRegistry.getDefaultBackend()).thenReturn( backend );
                  
      //Installation
      this.helper.setUIHelper( uiHelper );
      this.helper.setBackendHelper( backendHelper );      
      
      //Exercise
      // This will print an exception anyway because it is fired and handled in MultiChatManager
      IEConferenceManager manager = this.helper.open( null, autojoin );      
      
      //Verify       
      verify(uiHelper).showErrorMessage( anyString() );
      verify(uiHelper).closeCurrentPerspective();       
      
    } 
}