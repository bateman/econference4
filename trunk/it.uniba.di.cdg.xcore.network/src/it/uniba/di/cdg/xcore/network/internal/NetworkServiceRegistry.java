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
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;
import it.uniba.di.cdg.xcore.util.IExtensionVisitor;
import it.uniba.di.cdg.xcore.util.VirtualProxyFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

/**
 * Implementation of the {@see it.uniba.di.cdg.xcore.network.INetworkServiceRegistry}.
 */
public class NetworkServiceRegistry implements INetworkServiceRegistry {

    private Map<INetworkServiceDescriptor, INetworkService> services;

    /**
     * Constructs a new registry fro tracking network services.
     */
    public NetworkServiceRegistry() {
        this.services = new HashMap<INetworkServiceDescriptor, INetworkService>();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceRegistry#processExtensions(it.uniba.di.cdg.xcore.util.IExtensionProcessor, it.uniba.di.cdg.xcore.network.IBackendRegistry)
     */
    public void processExtensions( final IExtensionProcessor xprocessor, final IBackendRegistry backendRegistry ) throws Exception {
        IExtensionVisitor visitor = new IExtensionVisitor() {
            public void visit( IExtension extension, IConfigurationElement member ) throws Exception {
                NetworkServiceDescriptor descriptor = NetworkServiceDescriptor.createFromElement( member );

                IBackend backend = backendRegistry.getBackend( descriptor.getBackendId() );
                if (backend == null) {
                    System.out.println( String.format( "Skipping descriptor %s for unexistent backend id (%s)", 
                            descriptor, descriptor.getBackendId() ) );
                    return;
                }
                
//                INetworkService service = (INetworkService) member.createExecutableExtension( NetworkServiceDescriptor.CLASS_ATTR );
                INetworkService service = VirtualProxyFactory.getProxy( INetworkService.class, member );

                services.put( descriptor, service );
            }
        };
        xprocessor.process( XP_SERVICES, visitor );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceRegistry#getServiceDescriptor(java.lang.String)
     */
    public INetworkServiceDescriptor getServiceDescriptor( String serviceId ) {
        for (INetworkServiceDescriptor d : services.keySet()) {
            if (serviceId.equals( d.getId() )) 
                return d;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceRegistry#getService(it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor)
     */
    public INetworkService getService( INetworkServiceDescriptor descriptor ) {
        return services.get( descriptor );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceRegistry#getServices()
     */
    public Collection<INetworkService> getServices() {
        return Collections.unmodifiableCollection( services.values() );
    }
}
