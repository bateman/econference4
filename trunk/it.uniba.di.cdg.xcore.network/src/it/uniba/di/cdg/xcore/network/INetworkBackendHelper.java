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

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

import java.util.List;

/**
 * 
 */
public interface INetworkBackendHelper {
    /**
     * Set-up internal data structures and register listeners.
     * @throws Exception  
     */
    void initialize() throws Exception;

    /**
     * Clear internar status and remove all listeners.
     */
    void shutdown();

    /**
     * Register a new listener for a backend. The listener will be subsequently notified about 
     * backend envents.
     * 
     * @param backendId
     * @param listener
     */
    void registerBackendListener( String backendId, IBackendEventListener listener );

    /**
     * Deregister a listener for backend events.
     * 
     * @param backendId
     * @param listener
     */
    void unregisterBackendListener( String backendId, IBackendEventListener listener );

    /**
     * Convenience method for registering an event listener to all known backends.
     * 
     * @param listener
     */
    void registerBackendListener( IBackendEventListener listener );

    /**
     * Convenience method for unregistering an event listener from all known backends.
     * 
     * @param listener
     */
    void unregisterBackendListener( IBackendEventListener listener );
    
    /**
     * Query the backend status.
     * 
     * @param backendId the backend's id 
     * @return <code>true</code> if the backend is actually connected, <code>false</code> otherwise
     */
    boolean isBackendOnline( String backendId );

    /**
     * Returns the descriptors of the backends that are actually online.
     * 
     * @return the online backends' descriptors
     */
    List<IBackendDescriptor> getOnlineBackends();

    /**
     * Notify a new backend event to all registered listeners for the event's backend. This method is
     * intended to be used by backend implementations to expose events to listeners.
     * 
     * @param event
     */
    void notifyBackendEvent( IBackendEvent event );

    /**
     * Returns the current buddy roster. 
     * FIXME This buddy roster should merge all the backends' specific rosters. For now we
     * just catch the jabber backend ...
     * 
     * @return the roster
     */
    IBuddyRoster getRoster();

    /**
     * Set the registry to use.
     * 
     * @param registry
     */
    void setRegistry( IBackendRegistry registry );
    
    /**
     * Returns the registry tracking all backends.
     * 
     * @return Returns the registry.
     */
    IBackendRegistry getRegistry();

}