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
package it.uniba.di.cdg.xcore.ui.contribution;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * Track the connection status. The graphics layout is pretty simple: we add a light bulb for 
 * every backend and display an 'ON'/'OFF' picture while listening to its status.
 * <p>
 * XXX Design note: here we are replicating the same stuff as in INetworkBackendHelper for tracking
 * online / offline status: this obviously bad but we cannot rely on helper's isBackendOnline()
 * method since it is based upon the same IBackendEvents loop as this class (that is, there is no
 * guerentee that we are called _after_ the helper has handled the event).  
 */
public class OnlineStatusIndicator extends ContributionItem implements IBackendEventListener {
    /**
     * This contribution item's id.
     */
    public static final String ID = UiPlugin.ID + ".contribution.onlineIndicator";

    /**
     * Track backend's ids to their online / offline status.
     */
    private final Map<String, Boolean> backendStatus;
    
    /**
     * Create a new status line.
     */
    public OnlineStatusIndicator() {
        super( ID );
        setVisible( false ); 

        this.backendStatus = new HashMap<String, Boolean>(); 
        // Register this indicator to listen to all backends' events
        for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry().getDescriptors()) {
            NetworkPlugin.getDefault().getHelper().registerBackendListener( d.getId(), this );
            backendStatus.put( d.getId(), false ); // Offline by default
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void fill( Composite parent ) {
        CLabel sep = new CLabel( parent, SWT.SEPARATOR );
        StatusLineLayoutData data = new StatusLineLayoutData();
        sep.setLayoutData( data );

        for (String id: backendStatus.keySet())
            createLabelForBackend( parent, id );
    }

    private void createLabelForBackend( Composite parent, String id ) {
        StatusLineLayoutData data = new StatusLineLayoutData();

        CLabel bulb = new CLabel( parent, SWT.NONE );
        bulb.setData( data );

        final IBackendDescriptor descriptor = NetworkPlugin.getDefault().getRegistry().getDescriptor( id );
        // is the backend online?
        if (backendStatus.get( id )) { // yes,  
            final Image onlineImg = UiPlugin.getDefault().getImage( IImageResources.ICON_BACKEND_ONLINE );
            final ServerContext serverContext = 
                NetworkPlugin.getDefault().getRegistry().getBackend( id ).getServerContext();
            
            bulb.setToolTipText( descriptor.getName() + " connected to " + serverContext );
            bulb.setImage( onlineImg );
        } else { // no, 
            final Image offlineImg = UiPlugin.getDefault().getImage( IImageResources.ICON_BACKEND_OFFLINE );
            bulb.setToolTipText( descriptor.getName() + " not connected" );
            bulb.setImage( offlineImg );
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IContributionItem#update()
     */
    @Override
    @SwtAsyncExec
    public void update() {
        if (!isVisible()) {
            setVisible( true );
        }

        IContributionManager contributionManager = getParent();
        
        if (contributionManager != null)
            contributionManager.update( true );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendEventListener#onBackendEvent(it.uniba.di.cdg.xcore.network.IBackendEvent)
     */
    public void onBackendEvent( IBackendEvent event ) {
        if (event instanceof BackendStatusChangeEvent) {
            BackendStatusChangeEvent changeEvent = (BackendStatusChangeEvent) event;
            
            backendStatus.put( changeEvent.getBackendId(), changeEvent.isOnline() );
            updateBackendsStatus();
        }
    }
    
    /**
     * Update all bulbs on/off.
     */
    @SwtAsyncExec
    private void updateBackendsStatus() {
        update();
    }
}
