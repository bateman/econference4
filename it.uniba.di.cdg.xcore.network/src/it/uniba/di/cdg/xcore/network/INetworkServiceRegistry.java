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

import java.util.Collection;

import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;

/**
 * A network service registry tracks all the services implemented in a per-backend criteria.
 */
public interface INetworkServiceRegistry {
    /**
     * Extension point for contributing new services.
     */
    public static final String XP_SERVICES = "it.uniba.di.cdg.xcore.network.services";
    
    /**
     * Process the <code>it.uniba.di.cdg.xcore.network.services</code>.
     * 
     * @param xprocessor
     * @param backendRegistry
     * @throws Exception 
     */
    void processExtensions( IExtensionProcessor xprocessor, IBackendRegistry backendRegistry ) throws Exception;

    /**
     * @param service_id
     * @return
     */
    INetworkServiceDescriptor getServiceDescriptor( String service_id );

    /**
     * 
     * @param descriptor
     * @return
     */
    INetworkService getService( INetworkServiceDescriptor descriptor );

    /**
     * @return
     */
    Collection<INetworkService> getServices();
}
