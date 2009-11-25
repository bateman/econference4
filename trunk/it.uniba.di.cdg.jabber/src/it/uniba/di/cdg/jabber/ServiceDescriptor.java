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
package it.uniba.di.cdg.jabber;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.services.INetworkService;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.network.IBackendDescriptor}.
 */
public class ServiceDescriptor implements IServiceDescriptor {
    /**
     * Returns a new backend descriptor initialized with fields values from the configuration
     * element. 
     * 
     * @param member the configuration element to process
     * @return a new backend descriptor
     */
    public static ServiceDescriptor createFromElement( IConfigurationElement member ) {
        ServiceDescriptor descriptor = new ServiceDescriptor( member );
        
        String name = member.getAttribute( NAME_ATTR );
        String className = member.getAttribute( CLASS_ATTR );

        descriptor.setName( name );
        descriptor.setClassName( className );
     
        return descriptor;
    }

    
    /**
     * Backend's name.
     */
    private String name;
    
    /**
     * Backend's implementation fully qualified class name.
     */
    private String className;
    

    private IConfigurationElement cfgElement;
    
    /**
     * Construct a blank descriptor.
     */
    private ServiceDescriptor( IConfigurationElement cfgElement ) {
        this.cfgElement = cfgElement;
    }


    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendDescriptor#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendDescriptor#getClassName()
     */
    public String getClassName() {
        return className;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (other == null || !(other instanceof ServiceDescriptor)) 
            return false;
        final IServiceDescriptor that = (IServiceDescriptor) other;
        
        return this.getName().equals( that.getName() );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }


    public void setName( String name ) {
        this.name = name;
    }
    
    public void setClassName( String className ) {
        this.className = className;
    }

    /**
     * Create the concrete extension: this is used as lazy instantiation mechanism by
     * {@see BackendRegistry}.
     *  
     * @return an instance of the backend 
     */
    INetworkService createExecutableExtension() {
        try {
            return (INetworkService) cfgElement.createExecutableExtension( getClassName() );
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }
    }
}
