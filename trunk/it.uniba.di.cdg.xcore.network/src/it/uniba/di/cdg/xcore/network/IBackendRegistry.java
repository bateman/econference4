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
package it.uniba.di.cdg.xcore.network;

import it.uniba.di.cdg.xcore.util.IExtensionProcessor;

import java.util.Collection;

/**
 * A Registry which tracks down every backend connected to the <code>it.uniba.di.cdg.xcore.network.backends</code>
 * extension point.
 */
public interface IBackendRegistry {
    /**
     * The extension point that this registry handles.
     */
    public static final String XP_BACKENDS = NetworkPlugin.ID + ".backends";
    
    public IBackend getDefaultBackend();
    
    /**
     * Returns the descriptors for the currently known backends.
     * 
     * @return the backends' descriptors
     */
    Collection<IBackendDescriptor> getDescriptors();

    /**
     * Returns all the registered backends.
     * 
     * @return the registry.
     */
    Collection<IBackend> getBackends();

    /**
     * Returns the descriptor associated to a specific id.
     * 
     * @param id the backend id
     * @return the descriptor or <code>null</code> if the id is invalid or unknown
     */
    IBackendDescriptor getDescriptor( String id );
    
    IBackendDescriptor getDefaultDescriptor();
    
    /**
     * Returns the backend object identified by the identifier.
     * 
     * @param backendId the identifier of the wanted backend
     * @return the backend object or <code>null</code> if the id is unknown
     */
    IBackend getBackend( String backendId );
    
    /**
     * Process the extension point's extensions. 
     * @throws Exception 
     */
    void processExtensions( IExtensionProcessor processor ) throws Exception;

    /**
     * Dispose all backends (close open connections and perform clean-ups).  
     */
    void dispose();
    
    public String getDefaultBackendId();
}
