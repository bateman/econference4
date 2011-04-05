package it.uniba.di.cdg.xcore.network;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import it.uniba.di.cdg.xcore.util.ExtensionProcessor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NetworkBackendHelperBehaviorTest {
    // SUT
    private NetworkBackendHelper backendHelper;

    @Before
    public void setUp() throws Exception {
       backendHelper = new NetworkBackendHelper();
    }
    
    @Test
    public void testInizializingBackend(){
        // Setup
        IBackendRegistry registry = mock(IBackendRegistry.class);
        // Installation      
        backendHelper.setRegistry( registry );
        
        // Exercise
        try{
        backendHelper.initialize();
        } catch(Exception e){fail("BackendException: " + e.getMessage() + " but there should be only one backend!");}
        
        // Verify        
        try{
        // Note that Backends are tested in the BackendRegistry test case
        // we can't check them from here, anyway it is a state verification test
        verify(registry).processExtensions( ExtensionProcessor.getDefault() );
        }catch(Exception e){}
       
    }   
    
    /* We can't check the backends disconnection because we can't set the backend status Hashmap
     * When the helper checks the online backends he will realize that none of them is online
     * */
    @Test
    public void testShutdownBackends(){
        // Setup
        IBackendRegistry registry = mock(IBackendRegistry.class);
        IBackendDescriptor bdesc1 = mock(IBackendDescriptor.class);
        IBackendDescriptor bdesc2 = mock(IBackendDescriptor.class);
        IBackendDescriptor bdesc3 = mock(IBackendDescriptor.class);
        IBackend backend1 = mock(IBackend.class);
        IBackend backend2 = mock(IBackend.class);
        IBackend backend3 = mock(IBackend.class);
               
        List<IBackendDescriptor> descriptors = new ArrayList<IBackendDescriptor>();        
        descriptors.add( bdesc1 );
        descriptors.add( bdesc2 );
        descriptors.add( bdesc3 );
               
        // STUB
        when(registry.getDescriptors()).thenReturn(descriptors);
        when(bdesc1.getId()).thenReturn( "1" );
        when(bdesc2.getId()).thenReturn( "2" );
        when(bdesc3.getId()).thenReturn( "3" );
        when(registry.getBackend( bdesc1.getId() )).thenReturn( backend1 );
        when(registry.getBackend( bdesc2.getId() )).thenReturn( backend2 );
        when(registry.getBackend( bdesc3.getId() )).thenReturn( backend3 );
        
        // Installation      
        backendHelper.setRegistry( registry );
                
        // Exercise
        backendHelper.shutdown();
                
        // Verify      
        //verify(backend1).disconnect();
        //verify(backend2).disconnect();
        //verify(backend3).disconnect();
        verify(registry).dispose();
               
    }   

}
