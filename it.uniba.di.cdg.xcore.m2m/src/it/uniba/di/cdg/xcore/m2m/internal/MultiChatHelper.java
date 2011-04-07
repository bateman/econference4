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
package it.uniba.di.cdg.xcore.m2m.internal;

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.m2m.IMultiChatHelper;
import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.IMultiChatManager.IMultiChatListener;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.Collection;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.m2m.IMultiChatHelper}.
 */
public class MultiChatHelper implements IMultiChatHelper {
    /**
     * The User interface helper provides function for accessing user interface without caring on
     * how it is implemented.
     */
    private final IUIHelper uihelper;

    /**
     * The backend helper provides dependency object for handling backends.
     */
    private final INetworkBackendHelper backendHelper;

    /**
     * Construct a new helper for econferences. This constructor acts as "constructor injection" in
     * IoC.
     * 
     * @param helper
     * @param backendHelper
     */
    public MultiChatHelper( IUIHelper helper, INetworkBackendHelper backendHelper ) {
        this.uihelper = helper;
        this.backendHelper = backendHelper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChatHelper#init()
     */
    public void init() {
        Collection<IBackendDescriptor> descriptors = NetworkPlugin.getDefault().getRegistry()
                .getDescriptors();
        for (IBackendDescriptor d : descriptors) {
            backendHelper.registerBackendListener( d.getId(), this );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChatHelper#dispose()
     */
    public void dispose() {
        Collection<IBackendDescriptor> descriptors = NetworkPlugin.getDefault().getRegistry()
                .getDescriptors();
        for (IBackendDescriptor d : descriptors) {
            backendHelper.unregisterBackendListener( d.getId(), this );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChatHelper#open(it.uniba.di.cdg.xcore.m2m.service.
     * MultiChatContext)
     */
    public IMultiChatManager open( MultiChatContext context, boolean autojoin ) {
        IMultiChatManager manager = null;

        try {
            final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

            manager = new MultiChatManager();
            manager.setBackendHelper( NetworkPlugin.getDefault().getHelper() );
            manager.setUihelper( UiPlugin.getUIHelper() );
            manager.setWorkbenchWindow( window );

            manager.addListener( new IMultiChatListener() {
                private Point previousSize;

                public void open() {
                    System.out.println( "Resizing window!" );
                    Shell shell = window.getShell();
                    Point size = shell.getSize();
                    if (size.x < 640 || size.y < 480)
                        shell.setSize( 640, 480 );
                    previousSize = size;
                }

                public void closed() {
                    Shell shell = window.getShell();
                    shell.setSize( previousSize );
                }
            } );

            manager.open( context, autojoin );
        } catch (Exception e) {
            e.printStackTrace();
            uihelper.showErrorMessage( "Could not start eConference: " + e.getMessage() );

            // Close this perspective since it is unuseful ...
            uihelper.closeCurrentPerspective();
        }
        return manager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * it.uniba.di.cdg.xcore.network.events.IBackendEventListener#onBackendEvent(it.uniba.di.cdg
     * .xcore.network.events.IBackendEvent)
     */
    @SwtAsyncExec
    public void onBackendEvent( IBackendEvent event ) {
        if (event instanceof InvitationEvent) {
            InvitationEvent invitation = (InvitationEvent) event;
            final MultiChatContext context = askUserAcceptInvitation( invitation );
            if (context != null) {
                Boolean autojoin = true;
                open( context, autojoin );
            }
        }
    }

    /**
     * Ask the user if he accept the invitation. Note that this method will automatically rejects
     * invitation which reason is not {@see IMultiChatHelper#MULTICHAT_REASON}.
     * 
     * @param invitation
     * @return <code>true</code> if the user said ok, <code>false</code> otherwise
     */
    private MultiChatContext askUserAcceptInvitation( InvitationEvent invitation ) {
        // Skip invitations which do not interest us ...
        if (!MULTICHAT_REASON.equals( invitation.getReason() ))
            return null;

        IBackend backend = NetworkPlugin.getDefault().getRegistry()
                .getBackend( invitation.getBackendId() );

        String message = String
                .format(
                        "User %s has invited you to join to a chat.\n" +
                        // "\nReason: %s.\n" +
                                "\nIf you want to accept choose a nickname in the field below and press Ok, otherwise press cancel.",
                        invitation.getInviter(), invitation.getReason() );
        String nickName = uihelper.askFreeQuestion( message, backend.getUserAccount().getId() );
        if (nickName != null) {
            MultiChatContext context = new MultiChatContext( nickName, "", invitation );
            return context;
        } else
            invitation.decline( "No reason" );

        return null;
    }
}
