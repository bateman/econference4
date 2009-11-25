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

import it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor;
import it.uniba.di.cdg.xcore.network.services.Capability;
import it.uniba.di.cdg.xcore.network.services.ICapability;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor}. 
 */
public class NetworkServiceDescriptor implements INetworkServiceDescriptor {
    /**
     * Returns a new service descriptor initialized with fields values from the configuration
     * element. 
     * 
     * @param member the configuration element to process
     * @return a new backend descriptor
     */
    public static NetworkServiceDescriptor createFromElement( IConfigurationElement member ) {
        NetworkServiceDescriptor descriptor = new NetworkServiceDescriptor();
        
        String id = member.getAttribute( ID_ATTR );
        String name = member.getAttribute( NAME_ATTR );
        String className = member.getAttribute( CLASS_ATTR );
        String capability = member.getAttribute( CAPABILITY_ATTR );
        String backendId = member.getAttribute( BACKEND_ID_ATTR );
        
        descriptor.setId( id );
        descriptor.setName( name );
        descriptor.setClassName( className );
        descriptor.setCapability( new Capability( capability ) );
        descriptor.setBackendId( backendId );
        
        return descriptor;
    }

    private String id;
    
    private String name;
    
    private ICapability capability;
    
    private String className;

    private String backendId;
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor#getCapability()
     */
    public ICapability getCapability() {
        return capability;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor#getClassName()
     */
    public String getClassName() {
        return className;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkServiceDescriptor#getBackendId()
     */
    public String getBackendId() {
        return backendId;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (other == null || !(other instanceof INetworkServiceDescriptor))
            return false;
        INetworkServiceDescriptor that = (INetworkServiceDescriptor) other;
        return this.getId().equals( that.getId() );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * @param capability
     */
    private void setCapability( ICapability capability ) {
        this.capability = capability;
    }

    /**
     * @param className
     */
    private void setClassName( String className ) {
        this.className = className;
    }

    /**
     * @param name
     */
    private void setName( String name ) {
        this.name = name;
    }

    /**
     * @param id
     */
    private void setId( String id ) {
        this.id = id;
    }

    /**
     * @param backendId
     */
    private void setBackendId( String backendId ) {
        this.backendId = backendId;
    }
}
