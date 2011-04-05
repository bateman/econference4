package it.uniba.di.cdg.xcore.m2m;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.m2m.internal.MultiChatHelper;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MultiChatHelperBehaviorTest {
    // SUT
    private MultiChatHelper helper;
    
    // DOCs
    private IUIHelper uiHelper;
    private INetworkBackendHelper backendHelper;
   

    @Before
    public void setUp() throws Exception {
        uiHelper = mock(IUIHelper.class);
        backendHelper = mock(INetworkBackendHelper.class);           
        helper = new MultiChatHelper(uiHelper,backendHelper);
    }
    
    @Test
    public void testOpenWithBadContext(){
        //Setup              
        IBackend backend = mock(IBackend.class);
        IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
        boolean autojoin = true;
              
        //Stub
        when(backendHelper.getRegistry()).thenReturn( backendRegistry );
        when(backendRegistry.getBackend( anyString() )).thenReturn( backend );      
        when(backendRegistry.getDefaultBackend()).thenReturn( backend );
                    
        //Installation
        // There are no setter methods so we put the installation in the setUP to use the SUT constructor
        
        //Exercise
        // This will print an exception anyway because it is fired and handled in MultiChatManager
        IMultiChatManager manager = this.helper.open( null, autojoin );      
        
        //Verify       
        verify(uiHelper).showErrorMessage( anyString() );
        verify(uiHelper).closeCurrentPerspective();       
        
      }     
    
    @Ignore
    /*
     * We can't test MultiChatManager interaction further because Mock object installation is not possible
     * even if we try to extend both the manager and the helper, there's no way to install the Mock object
     * As in EConferenceHelper this test will always fail because uiHelper and backendHelper will be overridden
     * by the real objects in UIPlugin and NetworkPlugin 
     * */
    public void testOpenWithGoodContext(){        
      //Setup        
      EConferenceContext context = mock(EConferenceContext.class);      
      IBackend backend = mock(IBackend.class);
      IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
      IMultiChatServiceActions multichatServiceActions = mock(IMultiChatServiceActions.class);
      boolean autojoin = true;
     
      //Stub
      //These stubs are not working because the manager uses the Helpers in NetworkPlugin and UiPlugin 
      //Since we cannot mock the manager, the entire test will fail throwing a NullPointerException
      when(backendHelper.getRegistry()).thenReturn( backendRegistry );
      when(backendRegistry.getBackend( anyString() )).thenReturn( backend );
      when(backend.getMultiChatServiceAction()).thenReturn( multichatServiceActions );
            
      //Exercise
      // This will print an exception anyway because it is fired and handled by MultiChatManager
      IMultiChatManager manager = this.helper.open( context, autojoin );
                  
      //Verify
      //There will be some interaction for uiHelper because a NullPointerException will be thrown due to the non-mocked backend in the manager 
      //For this, the test will fail      
      verifyNoMoreInteractions(uiHelper);
    } 

}
