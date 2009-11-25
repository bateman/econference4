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
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.util.ExtensionProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.network.INetworkBackendHelper}.
 */
public class NetworkBackendHelper implements IBackendEventListener, INetworkBackendHelper {
    /**
     * Maps the backends with their event listeners.
     */
    private final Map<String, List<IBackendEventListener>> backendEventListeners;
    
    /**
     * Track backend's ids to their online / offline status.
     */
    private final Map<String, Boolean> backendStatus;

    /**
     * Keep a reference to the backend registry.
     */
    private IBackendRegistry registry;

    /**
     * Default constructor.
     */
    public NetworkBackendHelper() {
        this.backendEventListeners = new HashMap<String, List<IBackendEventListener>>();
        this.backendStatus = new HashMap<String, Boolean>(); 
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#initialize()
     */
    public void initialize() throws Exception {
        registry.processExtensions( ExtensionProcessor.getDefault() );

        // Register this indicator to listen to all backends' events
        for (IBackendDescriptor d : registry.getDescriptors()) {
            registerBackendListener( d.getId(), this );
            backendStatus.put( d.getId(), false ); // Offline by default
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#shutdown()
     */
    public void shutdown() {
        for (IBackendDescriptor d : getOnlineBackends()) {
            registry.getBackend( d.getId() ).disconnect();
        }
        registry.dispose();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#registerBackendListener(java.lang.String, it.uniba.di.cdg.xcore.network.events.IBackendEventListener)
     */
    public void registerBackendListener( String backendId, IBackendEventListener listener ) {
        List<IBackendEventListener> listeners = backendEventListeners.get( backendId );
        if (listeners == null) { // This is a new backend ...
            listeners = new ArrayList<IBackendEventListener>();
            backendEventListeners.put( backendId, listeners );
        }
        listeners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#unregisterBackendListener(java.lang.String, it.uniba.di.cdg.xcore.network.events.IBackendEventListener)
     */
    public void unregisterBackendListener( String backendId, IBackendEventListener listener ) {
        List<IBackendEventListener> listeners = backendEventListeners.get( backendId );
        if (listeners == null) // Unknown backend
            return;
        listeners.remove( listener );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#registerBackendEventListed(it.uniba.di.cdg.xcore.network.events.IBackendEventListener)
     */
    public void registerBackendListener( IBackendEventListener listener ) {
        for (IBackendDescriptor d : getOnlineBackends())
            registerBackendListener( d.getId(), listener );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#unregisterBackendEventListed(it.uniba.di.cdg.xcore.network.events.IBackendEventListener)
     */
    public void unregisterBackendListener( IBackendEventListener listener ) {
        for (IBackendDescriptor d : getOnlineBackends())
            unregisterBackendListener( d.getId(), listener );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#isBackendOnline(java.lang.String)
     */
    public boolean isBackendOnline( String backendId ) {
        return backendStatus.containsKey( backendId ) ? backendStatus.get( backendId ) : false; 
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#getOnlineBackends()
     */
    public List<IBackendDescriptor> getOnlineBackends() {
        List<IBackendDescriptor> descriptors = new ArrayList<IBackendDescriptor>();
        for (IBackendDescriptor d : registry.getDescriptors()) {
            if (isBackendOnline( d.getId() ))
                descriptors.add( d );
        }
        return descriptors;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#notifyBackendEvent(it.uniba.di.cdg.xcore.network.events.IBackendEvent)
     */
    public void notifyBackendEvent( IBackendEvent event ) {
        List<IBackendEventListener> listeners = backendEventListeners.get( event.getBackendId() );
        // Skip unknown backends ...
        if (listeners == null)
            return;
        // Notify listeners for this backend.
        for (IBackendEventListener l : listeners)
            l.onBackendEvent( event );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#getRoster()
     */
    public IBuddyRoster getRoster() {
        // FIXME Here we should create an uber-roster and merge all the backends'. 
        IBackend backend = registry.getDefaultBackend();
        return backend.getRoster();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.events.IBackendEventListener#onBackendEvent(it.uniba.di.cdg.xcore.network.events.IBackendEvent)
     */
    public void onBackendEvent( IBackendEvent event ) {
        if (event instanceof BackendStatusChangeEvent) {
            BackendStatusChangeEvent changeEvent = (BackendStatusChangeEvent) event;
            
            backendStatus.put( changeEvent.getBackendId(), changeEvent.isOnline() );
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#setRegistry(it.uniba.di.cdg.xcore.network.IBackendRegistry)
     */
    public void setRegistry( IBackendRegistry registry ) {
        this.registry = registry;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.INetworkBackendHelper#getRegistry()
     */
    public IBackendRegistry getRegistry() {
        return registry;
    }
    
    /**
     * Provides internal thread synchronization.
     */
//    @Aspect
//    public static class OwnThreadSafety extends ThreadSafetyAspect {
//        /* (non-Javadoc)
//         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#readOperations()
//         */
//        @Override
//        @Pointcut( "execution( public * NetworkBackendHelper.get*(..) )" +
//                "|| execution( public void NetworkBackendHelper.onBackendEvent(..) )" +
//                "|| execution( public void NetworkBackendHelper.notifyBackendEvent(..) )" +
//                "|| execution( public boolean NetworkBackendHelper.is*(..) )" )
//        protected void readOperations() {}
//
//        /* (non-Javadoc)
//         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#writeOperations()
//         */
//        @Override
//        @Pointcut( "execution( public void NetworkBackendHelper.set*(..) )" +
//                "|| execution( public void NetworkBackendHelper.*register*(..) )" )
//        protected void writeOperations() {}
//    }
}
