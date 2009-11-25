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
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * jUnit test-case for the {@see it.uniba.di.cdg.xcore.network.internal.NetworkServiceRegistry}.
 */
public class NetworkServiceRegistryTestCase extends MockObjectTestCase {
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
    public void testAddService() throws Exception {
        INetworkServiceRegistry registry = new NetworkServiceRegistry();

        Mock mock = mock( IBackend.class );
        IBackend backend = (IBackend) mock.proxy();

        mock = mock( IBackendRegistry.class );
        // Return the mock !null backend if asked for the right backend
        mock.expects( once() ).method( "getBackend" ).with( eq( BACKEND_ID ) )
            .will( returnValue( backend ) );
        // return null for all other cases
        mock.stubs().method( "getBackend" ).with( eq( UNEXISTING_BACKEND_ID ) )
            .will( returnValue( null ) );
        IBackendRegistry backendRegistry = (IBackendRegistry) mock.proxy();
        
        IExtensionProcessor xprocessor = new IExtensionProcessor() {
            public void process( String xpid, IExtensionVisitor visitor ) throws Exception {
                assertEquals( INetworkServiceRegistry.XP_SERVICES, xpid );
                assertNotNull( visitor );
                
                Mock mock = mock( IExtension.class );
                IExtension extension = (IExtension) mock.proxy();
                
                mock = mock( IConfigurationElement.class );
                mock.stubs().method( "getAttribute" ).with( eq( INetworkServiceDescriptor.ID_ATTR ))
                    .will( returnValue( SERVICE_ID ) );
                mock.stubs().method( "getAttribute" ).with( eq( INetworkServiceDescriptor.NAME_ATTR ))
                    .will( returnValue( SERVICE_NAME ) );
                mock.stubs().method( "getAttribute" ).with( eq( INetworkServiceDescriptor.CLASS_ATTR ))
                    .will( returnValue( SERVICE_CLASS ) );
                mock.stubs().method( "getAttribute" ).with( eq( INetworkServiceDescriptor.CAPABILITY_ATTR ))
                    .will( returnValue( SERVICE_CAPABILITY ) );
                mock.stubs().method( "getAttribute" ).with( eq( INetworkServiceDescriptor.BACKEND_ID_ATTR ))
                    .will( returnValue( BACKEND_ID ) );
                IConfigurationElement member = (IConfigurationElement) mock.proxy();
                
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
