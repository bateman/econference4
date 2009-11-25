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
package it.uniba.di.cdg.xcore.ui.actions;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Action for connecting.
 * 
 * FIXME This should become a pulldown action, with an item for each backend.
 */
public class ConnectAction extends Action implements ActionFactory.IWorkbenchAction {
    /**
     * Action id.
     */
    public static final String ID = UiPlugin.ID + ".actions.connectAction";

    /**
     * Ehmm .. guarda against unexpected disconnections (such as errors ...)
     */
    private IBackendEventListener backendListener = new IBackendEventListener() {
        public void onBackendEvent( IBackendEvent event ) {
            if (event instanceof BackendStatusChangeEvent) {
                BackendStatusChangeEvent changeEvent = (BackendStatusChangeEvent) event;
                if (changeEvent.isOnline())
                    showAsReadyToDisconnect();
                else
                    showAsReadyToConnect();
            }
        }
    };

    /**
     * Handles the connection job.
     */
    private JobChangeAdapter jobListener = new JobChangeAdapter() {
        @Override
        public void done( final IJobChangeEvent event ) {
            if (Status.OK_STATUS.equals( event.getResult() )) {               
                showAsReadyToDisconnect();
            }
            else {
                System.out.println( event.getResult() );
                UiPlugin.getUIHelper().showErrorMessage(
                        "Could not connect to remote server. Please double check that your "
                        + "\nusername and/or password is correct.");
                showAsReadyToConnect();
            } 
        }
    };

    /**
     * Create a new connection action for the specified workbench window.
     * 
     * @param window
     */
    public ConnectAction( IWorkbenchWindow window ) {
    	super();
        setId( "connect" );
        //setActionDefinitionId( ID );        
        
        setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_ACTION_CONNECT ) );
        setText( "Connect" );
        setToolTipText( "Connect to " + getBackendDescriptor().getName() );

        // Ready to update when disconnected ...
        NetworkPlugin.getDefault().getHelper().registerBackendListener( 
        		NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), backendListener );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {
        // Ready to update when disconnected ...
        NetworkPlugin.getDefault().getHelper().unregisterBackendListener( 
        		NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), backendListener );
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
      setEnabled( false ); // Will be re-enabled in done()
      if (isConnected())
          disconnect();
      else
          connect();
    }
    
    /**
     * Start the connection job.
     * 
     * @param action
     */
    private void connect() {

    	IBackend backend = getBackend();
        Job job = backend.getConnectJob();
        
        if(job != null){
        	job.setPriority( Job.SHORT );
        	 //job.setProperty( KEY_SERVER_CONTEXT, null );
            job.addJobChangeListener( jobListener );
            job.schedule();
        } else {
            // Re-enable action
            setEnabled( true );
        }
    }

    /**
     * Disconnect from network.
     * 
     * @param action
     */
    private void disconnect() {
        showAsReadyToConnect();
        NetworkPlugin.getDefault().getHelper().unregisterBackendListener( backendListener );
        getBackend().disconnect();
    }

    @SwtAsyncExec
    private void showAsReadyToConnect() {
        setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_ACTION_CONNECT ) );
        setText( "Connect" );
        setToolTipText( "Connect to " + getBackendDescriptor().getName() );
        setEnabled( true );
    }

    @SwtAsyncExec
    private void showAsReadyToDisconnect() {
        setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_ACTION_DISCONNECT ) );
        setText( "Disconnect" );
        ServerContext ctx = getBackend().getServerContext();
        setToolTipText( "Disconnect from " + ctx );
        setEnabled( true );
    }
     
    /**
     * @return Returns the connected.
     */
    private boolean isConnected() {
        return NetworkPlugin.getDefault().getHelper().isBackendOnline(
        		NetworkPlugin.getDefault().getRegistry().getDefaultBackendId() );
    }

    private IBackend getBackend() {
        return NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
    }
    
    private IBackendDescriptor getBackendDescriptor() {
        return NetworkPlugin.getDefault().getRegistry().getDefaultDescriptor();
    }
}
