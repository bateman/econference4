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
package it.uniba.di.cdg.xcore.network.internal;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor;
import it.uniba.di.cdg.xcore.network.INetworkServiceRegistry;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;
import it.uniba.di.cdg.xcore.util.IExtensionVisitor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * jUnit test-case for the {@see it.uniba.di.cdg.xcore.network.internal.NetworkServiceRegistry}.
 */
public class NetworkServiceRegistryStateTest {
    private static final String SERVICE_ID = "com.acme.dummy.service";
    private static final String SERVICE_NAME = "ACME Service";
    private static final String SERVICE_CAPABILITY = "do-something";
    private static final String SERVICE_CLASS = "com.acme.dummy.ServiceImpl";
    private static final String BACKEND_ID = "com.acme.dummy.dummyBackend";
    private static final String UNEXISTING_BACKEND_ID = "do.not.exist.backendId";
    
    /**
     * Check that if registration of a service for an existing backend works while
     * for unexisting backend the service is simply ignored (not registered).
     * 
     * @throws Exception
     */
    @Test
    public void testAddService() throws Exception {
        INetworkServiceRegistry registry = new NetworkServiceRegistry();

        IBackend backend = mock( IBackend.class );
        IBackendRegistry backendRegistry = mock(IBackendRegistry.class);
        // return null for all other cases
        when(backendRegistry.getBackend( eq( UNEXISTING_BACKEND_ID ) )).thenReturn( null );
        // Return the mock !null backend if asked for the right backend
        when(backendRegistry.getBackend( eq( BACKEND_ID ) )).thenReturn( backend );
        
        IExtensionProcessor xprocessor = new IExtensionProcessor() {
            public void process( String xpid, IExtensionVisitor visitor ) throws Exception {
                assertEquals( INetworkServiceRegistry.XP_SERVICES, xpid );
                assertNotNull( visitor );
                
                IExtension extension = mock(IExtension.class);
                IConfigurationElement member = mock(IConfigurationElement.class);

                when(member.getAttribute( eq( INetworkServiceDescriptor.ID_ATTR ) )).thenReturn( SERVICE_ID );
                when(member.getAttribute( eq( INetworkServiceDescriptor.NAME_ATTR ) )).thenReturn( SERVICE_NAME );
                when(member.getAttribute( eq( INetworkServiceDescriptor.CLASS_ATTR ) )).thenReturn( SERVICE_CLASS );
                when(member.getAttribute( eq( INetworkServiceDescriptor.CAPABILITY_ATTR ) )).thenReturn( SERVICE_CAPABILITY );
                when(member.getAttribute( eq( INetworkServiceDescriptor.BACKEND_ID_ATTR ) )).thenReturn( BACKEND_ID );
                                
                visitor.visit( extension, member );
            }
        };
        // Do service discovery 
        registry.processExtensions( xprocessor, backendRegistry );
        
        // Now check that it worked ...
        INetworkServiceDescriptor descriptor = registry.getServiceDescriptor( SERVICE_ID );

        assertNotNull( descriptor );
        assertEquals( SERVICE_ID, descriptor.getId() );
        assertEquals( SERVICE_NAME, descriptor.getName() );
        assertEquals( SERVICE_CAPABILITY, descriptor.getCapability().getName() );
        assertEquals( SERVICE_CLASS, descriptor.getClassName() );
        assertNotNull( registry.getService( descriptor ) );
        
        // Ensure that the other backend is unknown
        assertNull( registry.getServiceDescriptor( UNEXISTING_BACKEND_ID ) );
    }
}
