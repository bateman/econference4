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

import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
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
 * Implementation of {@see it.uniba.di.cdg.xcore.network.IBackendRegistry}.
 */
public class BackendRegistry implements IBackendRegistry {
    
    private INetworkBackendHelper helper;
    
    private String defaultBackendId;
    
    /**
     * Descriptors mapped by backend id.
     */
    private Map<String, IBackendDescriptor> descriptors;

    /**
     * The cached backends, mapped by their descriptors. They are created in lazy way.
     */
    private Map<IBackendDescriptor, IBackend> backends;
    
    /**
     * Creates an empty registry.
     */
    public BackendRegistry( INetworkBackendHelper helper ) {
        this.helper = helper;
        this.descriptors = new HashMap<String, IBackendDescriptor>();
        this.backends = new HashMap<IBackendDescriptor, IBackend>();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#processExtensions()
     */
    public void processExtensions( IExtensionProcessor processor ) throws Exception {
        IExtensionVisitor visitor = new IExtensionVisitor() {
            public void visit( IExtension extension, IConfigurationElement member ) throws Exception {
                BackendDescriptor descriptor = BackendDescriptor.createFromElement( member );

                defaultBackendId = descriptor.getId();
                
                if (getBackend( descriptor.getId() ) != null )
                    throw new BackendException( String.format( "Backend %s is already registered!", descriptor.getName() ) );

                descriptors.put( descriptor.getId(), descriptor );
                
                IBackend backend = VirtualProxyFactory.getProxy( IBackend.class, member );
//                IBackend backend = (IBackend) member.createExecutableExtension( BackendDescriptor.CLASS_ATTR );
                backends.put( descriptor, backend );
            }
        };
        processor.process( XP_BACKENDS, visitor );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#dispose()
     */
    public void dispose() {
        backends.clear();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getDescriptors()
     */
    public Collection<IBackendDescriptor> getDescriptors() {
        return Collections.unmodifiableSet( backends.keySet() );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getBackend(java.lang.String)
     */
    public IBackend getBackend( String backendId ) {
        BackendDescriptor descriptor = (BackendDescriptor) descriptors.get( backendId );
        if (descriptor != null) {
            IBackend backend = backends.get( descriptor );
            backend.setHelper( helper );
            return backend;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getBackends()
     */
    public Collection<IBackend> getBackends() {
        return Collections.unmodifiableCollection( backends.values() );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getDescriptor(java.lang.String)
     */
    public IBackendDescriptor getDescriptor( String id ) {
        return descriptors.get( id );
    }

	@Override
	public IBackend getDefaultBackend() {
		return getBackend(defaultBackendId);
	}

	@Override
	public IBackendDescriptor getDefaultDescriptor() {
		return getDescriptor(defaultBackendId);
	}

	@Override
	public String getDefaultBackendId() {
		return defaultBackendId;
	}
}
