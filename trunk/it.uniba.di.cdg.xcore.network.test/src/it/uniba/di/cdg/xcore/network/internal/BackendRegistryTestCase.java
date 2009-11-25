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

import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;
import it.uniba.di.cdg.xcore.util.IExtensionVisitor;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * jUnit test case for the {@see it.uniba.di.cdg.xcore.network.internal.BackendRegistry}.
 */
public class BackendRegistryTestCase extends MockObjectTestCase {
    private static final String BACKEND_ID = "com.acme.dummy.dummyBackend";
    private static final String BACKEND_NAME = "ACME Service";
    private static final String BACKEND_CLASS = "com.acme.dummy.DummyBackend";
    private static final String BACKEND_SECURE = "false";
    private static final String UNEXISTING_BACKEND_ID = "do.not.exist.backendId";

    private IBackendRegistry registry;
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
    }

    /**
     * TODO Check that the backend successfully registers a backend which is correctly
     * configured.
     */
    public void testProcessGoodBackendExtension() {
        // 1. create a configuration element
    }

    /**
     * Check that retrieving a descriptor for an existing backend works.
     * 
     * @throws Exception if failed something 
     */
    public void testGetDescriptor() throws Exception {
        Mock mock = mock( INetworkBackendHelper.class );
        INetworkBackendHelper helper = (INetworkBackendHelper) mock.proxy();
        
        registry = new BackendRegistry( helper );
        IExtensionProcessor xprocessor = new IExtensionProcessor() {
            public void process( String xpid, IExtensionVisitor visitor ) throws Exception {
                assertEquals( IBackendRegistry.XP_BACKENDS, xpid );
                assertNotNull( visitor );
                
                Mock mock = mock( IExtension.class );
                IExtension extension = (IExtension) mock.proxy();
                
                // Note that we do not stub createExecutableExtension() since the processExtensions()
                // method will use virtual proxies.
                mock = mock( IConfigurationElement.class );
                mock.stubs().method( "getAttribute" ).with( eq( IBackendDescriptor.ID_ATTR ))
                    .will( returnValue( BACKEND_ID ) );
                mock.stubs().method( "getAttribute" ).with( eq( IBackendDescriptor.NAME_ATTR ))
                    .will( returnValue( BACKEND_NAME ) );
                mock.stubs().method( "getAttribute" ).with( eq( IBackendDescriptor.CLASS_ATTR ))
                    .will( returnValue( BACKEND_CLASS ) );
                mock.stubs().method( "getAttribute" ).with( eq( IBackendDescriptor.SECURE_ATTR ))
                    .will( returnValue( BACKEND_SECURE ) );
                IConfigurationElement member = (IConfigurationElement) mock.proxy();
                
                visitor.visit( extension, member );
            }
        };
        // Do service discovery 
        registry.processExtensions( xprocessor );

        // Now check that it worked ...
        IBackendDescriptor descriptor = registry.getDescriptor( BACKEND_ID );
        
        // 2. Get the descriptor back
        assertNotNull( descriptor );
    }
}
