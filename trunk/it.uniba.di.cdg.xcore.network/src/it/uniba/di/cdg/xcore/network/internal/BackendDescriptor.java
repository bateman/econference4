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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.network.IBackendDescriptor}.
 */
public class BackendDescriptor implements IBackendDescriptor {
    /**
     * Returns a new backend descriptor initialized with fields values from the configuration
     * element. 
     * 
     * @param member the configuration element to process
     * @return a new backend descriptor
     */
    public static BackendDescriptor createFromElement( IConfigurationElement member ) {
        BackendDescriptor descriptor = new BackendDescriptor( member );
        
        String id = member.getAttribute( ID_ATTR );
        String name = member.getAttribute( NAME_ATTR );
        String className = member.getAttribute( CLASS_ATTR );
        String secure = member.getAttribute( SECURE_ATTR );
        
        descriptor.setId( id );
        descriptor.setName( name );
        descriptor.setClassName( className );
        descriptor.setSecure( Boolean.parseBoolean( secure ) );
        
        return descriptor;
    }

    /**
     * Backends id.
     */
    private String id;
    
    /**
     * Backend's name.
     */
    private String name;
    
    /**
     * Backend's implementation fully qualified class name.
     */
    private String className;
    
    /**
     * Flag indicating that the backend supports secure connections.
     */
    private boolean secure;

    private IConfigurationElement cfgElement;
    
    /**
     * Construct a blank descriptor.
     */
    private BackendDescriptor( IConfigurationElement cfgElement ) {
        this.cfgElement = cfgElement;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendDescriptor#getId()
     */
    public String getId() {
        return id;
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
     * @see it.uniba.di.cdg.xcore.network.IBackendDescriptor#isSecure()
     */
    public boolean isSecure() {
        return secure;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (other == null || !(other instanceof BackendDescriptor)) 
            return false;
        final IBackendDescriptor that = (IBackendDescriptor) other;
        
        return this.getId().equals( that );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void setId( String id ) {
        this.id = id;
    }

    public void setName( String name ) {
        this.name = name;
    }
    
    public void setClassName( String className ) {
        this.className = className;
    }

    public void setSecure( boolean secure ) {
        this.secure = secure;
    }

    /**
     * Create the concrete extension: this is used as lazy instantiation mechanism by
     * {@see BackendRegistry}.
     *  
     * @return an instance of the backend 
     */
    IBackend createExecutableExtension() {
        try {
            return (IBackend) cfgElement.createExecutableExtension( getClassName() );
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }
    }
}
