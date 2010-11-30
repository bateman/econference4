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

import it.uniba.di.cdg.xcore.m2m.IMultiChatHelper;
import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.events.IManagerEvent;
import it.uniba.di.cdg.xcore.m2m.events.IManagerEventListener;
import it.uniba.di.cdg.xcore.m2m.events.ViewReadOnlyEvent;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.m2m.model.Privileged;
import it.uniba.di.cdg.xcore.m2m.service.IInvitationRejectedListener;
import it.uniba.di.cdg.xcore.m2m.service.IMultiChatService;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatService;
import it.uniba.di.cdg.xcore.m2m.service.UserStatusAdapter;
import it.uniba.di.cdg.xcore.m2m.ui.MultiChatPerspective;
import it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.IMultiChatTalkView;
import it.uniba.di.cdg.xcore.m2m.ui.views.MultiChatTalkView;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.services.JoinException;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.views.IActivatableView;
import it.uniba.di.cdg.xcore.ui.views.ITalkView;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener3;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;

/**
 * The multichat controller. 
 * <p>
 * Note that this controller is pretty static: we do not support more than one multichat at
 * one time so we just listen at perspective events. In a future redesign this and the according
 * views will be switched and sync'ed dynamically using part-listeners. 
 */
public class MultiChatManager implements IMultiChatManager {
    /**
     * Provide access to the useful functionalities for manipulating / accessing backends.
     */
    private INetworkBackendHelper backendHelper;

    /**
     * The workbench window this manager is working into.
     */
    protected IWorkbenchWindow workbenchWindow;
    
    /**
     * Provides access to the UI interaction, without bothering about the implementation.
     */
    protected IUIHelper uihelper;

    /**
     * The view containing the ongoing talks.
     */
    protected IMultiChatTalkView talkView;
    
    /**
     * The view containing the room's participants.
     */
    protected IChatRoomView roomView;
    
    /**
     * The network service give us access to the remote clients.
     */
    protected IMultiChatService service;
    
    /**
     * Listeners for multichat events.
     */
    protected List<IMultiChatListener> chatlisteners;

    /**
     * The context for this multichat. This is set by <code>open()</code>.
     */
    protected MultiChatContext context;

    protected IPerspectiveListener3 perspectiveListener = new IPerspectiveListener3() {

        public void perspectiveOpened( IWorkbenchPage page, IPerspectiveDescriptor perspective ) {
            System.out.println( String.format( "perspectiveOpened( %s )", perspective.getId() ) );
        }

        public void perspectiveClosed( IWorkbenchPage page, IPerspectiveDescriptor perspective ) {           
            // The user has requested the perspective to be closed ... so let's clean-up all
            if (page == getWorkbenchWindow().getActivePage() && MultiChatPerspective.ID.equals( perspective.getId() )) {            	
                // Ask the user to save views even when the perspective is closed (by default the
                // eclipse framework asks only when closing the whole app, see BR #43).
                
            	//page.saveAllEditors( true );
                
                // close perspective means leave the room
                //service.leave(); It's called in close()
                close();
                System.out.println( String.format( "perspectiveClosed( %s )", perspective.getId() ) );
            }
        }

        public void perspectiveDeactivated( IWorkbenchPage page, IPerspectiveDescriptor perspective ) {
//            System.out.println( String.format( "perspectiveDeactivated( %s )", perspective.getId() ) );
        }

        public void perspectiveSavedAs( IWorkbenchPage page, IPerspectiveDescriptor oldPerspective, IPerspectiveDescriptor newPerspective ) {
//            System.out.println( String.format( "perspectiveSaveAs()") );
        }

        public void perspectiveChanged( IWorkbenchPage page, IPerspectiveDescriptor perspective, IWorkbenchPartReference partRef, String changeId ) {
//            System.out.println( String.format( "perspectiveChanged( %s )", perspective.getId() ) );
        }

        public void perspectiveActivated( IWorkbenchPage page, IPerspectiveDescriptor perspective ) {
//            System.out.println( String.format( "perspectiveActivated( %s )", perspective.getId() ) );
        }

        public void perspectiveChanged( IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId ) {
//            System.out.println( String.format( "perspectiveChanged( %s )", perspective.getId() ) );
        }
    };

    /**
     * 
     */
    protected IManagerEventListener managerEventListener = new IManagerEventListener() {
        public void onManagerEvent( IManagerEvent event ) {
            if (event instanceof ViewReadOnlyEvent) {
                final ViewReadOnlyEvent ee = (ViewReadOnlyEvent) event;
                
                final IViewPart viewPart = getWorkbenchWindow().getActivePage().findView( ee.getViewId() );
                if (viewPart instanceof IActivatableView) {
                    ((IActivatableView) viewPart).setReadOnly( ee.isReadOnly() );
                    ((IActivatableView) viewPart).refresh();
                }
            }
        }
    };
    
    /**
     * Close the perspective when disconnected.
     */
    protected IBackendEventListener backendListener = new IBackendEventListener() {
        public void onBackendEvent( IBackendEvent event ) {
            if (event instanceof BackendStatusChangeEvent) {
                BackendStatusChangeEvent changeEvent = (BackendStatusChangeEvent) event;
                if (!changeEvent.isOnline())
                    UiPlugin.getUIHelper().closePerspective( MultiChatPerspective.ID );
            }
        }
    };
    
    /**
     * Create a new multichat.
     */
    public MultiChatManager( ) {
        super();
        this.chatlisteners = new ArrayList<IMultiChatListener>();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#open(it.uniba.di.cdg.xcore.m2m.service.IMultiChatService.MultiChatContext)
     */
    public void open( MultiChatContext context, boolean autojoin ) throws Exception {
        this.context = context;
        service = setupChatService();
        
        try {
            service.join();
            if (autojoin) {
                setupUI();

                setupListeners();

                // Notify chat listeners that the chat is
                for (IMultiChatListener l : chatlisteners)
                    l.open();
            }
        }
        catch (JoinException ex) {
        	service.leave();
        	uihelper.showErrorMessage(ex.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#close()
     */
    public void close() {
        // Notify chat listeners that the chat is open
        for (IMultiChatListener l : chatlisteners) 
            l.closed();

        service.leave(); 
        service = null;

        chatlisteners.clear();
        chatlisteners = null;

        workbenchWindow.removePerspectiveListener( perspectiveListener );
        getBackendHelper().registerBackendListener( backendListener );
    }
    
    /**
     * Setup the UI
     * 
     * @throws WorkbenchException
     */
    protected void setupUI() throws WorkbenchException {
        // Switch to the multichat perspective too ...
        getUihelper().switchPerspective( MultiChatPerspective.ID );

        // Create the view part for this user (we can have one for each user we are chatting with
        // and the secondary id is what we give to the framework to distinguish about them).  
        final IViewPart talkViewPart = workbenchWindow.getActivePage().showView( MultiChatTalkView.ID );
        //final IViewPart chatRoomViewPart = workbenchWindow.getActivePage().showView( ChatRoomView.ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE );
        final IViewPart chatRoomViewPart = (IViewPart) workbenchWindow.getActivePage().findView( ChatRoomView.ID );

        // Setup the talk view: it must be able to display incoming messages (both local and from network service)
        // and from 
        talkView = (IMultiChatTalkView) talkViewPart;
        talkView.setTitleText( context.getRoom() );
        talkView.setModel( getService().getTalkModel() );
        
        roomView = (IChatRoomView) chatRoomViewPart;
        roomView.setManager( this );

        // Ensure that the focus is switched to this new chat
        talkViewPart.setFocus();
    }
   
    /**
     * Register all needed listeners.  
     */
    protected void setupListeners() {
        workbenchWindow.addPerspectiveListener( perspectiveListener );

        // Send messages typed by the user to the remote peers
        talkView.addListener( new ISendMessagelListener() {
            public void notifySendMessage( String message ) {
                service.sendMessage( message );
            }
        });
        // ... and receives them too!
        service.addMessageReceivedListener( talkView );
        
        // Typing notification to remote client ...
        talkView.addTypingListener( service );
        service.addTypingEventListener( talkView );
        
        service.addUserStatusListener( new UserStatusAdapter() {
            /* (non-Javadoc)
             * @see it.uniba.di.cdg.xcore.m2m.service.UserStatusAdapter#voiceGranted()
             */
            @Override
            public void voiceGranted() {
                talkView.appendMessage( "*** > Moderator has allowed you back in conversation!" );
                talkView.setReadOnly( false );
            }

            /* (non-Javadoc)
             * @see it.uniba.di.cdg.xcore.m2m.service.UserStatusAdapter#voiceRevoked()
             */
            @Override
            public void voiceRevoked() {
                talkView.appendMessage( "*** > Moderator has stopped you from contributing to conversation!" );
                talkView.setReadOnly( true );
            }
        });
        
        service.addInvitationRejectedListener( new IInvitationRejectedListener() {
            public void declined( String invitee, String reason ) {
                uihelper.showMessage( 
                        String.format( "User %s rejected invitation with motivation\n \"%s\"",
                                invitee, reason ) );
            }
        });
        
        // Make the view to react to model events
        talkView.setManager( this );
        
        getService().addManagerEventListener( managerEventListener );
        
        getBackendHelper().registerBackendListener( backendListener );
    }
    
    /**
     * Create the chat service and registers the 
     * @throws BackendException
     */
    protected IMultiChatService setupChatService( ) throws BackendException {
        IBackend backend = backendHelper.getRegistry().getDefaultBackend();
        return new MultiChatService(context, backend);
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#toggleFreezeUnfreeze(it.uniba.di.cdg.xcore.m2m.model.IParticipant[])
     */
    @Privileged( atleast = Role.MODERATOR )
    public void toggleFreezeUnfreeze( List<IParticipant> participants ) {
        final List<String> frozen = new ArrayList<String>();
        final List<String> unfrozen = new ArrayList<String>();
  
        // Discriminate among who must be frozen and who must be unfrozen: do not touch 
        // the model: we rely on server for notification about participant's status change
        for (IParticipant p : participants) {
            if (Status.NOT_JOINED.equals( p.getStatus() ))
                continue;
            if (Status.FROZEN.equals( p.getStatus() ))
                unfrozen.add( p.getNickName() );
            else if (Status.JOINED.equals( p.getStatus() ))
                frozen.add( p.getNickName() );
        }
        
        if (!frozen.isEmpty())
            service.revokeVoice( frozen );
        if (!unfrozen.isEmpty())
            service.grantVoice( unfrozen );
    }   

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#sendPrivateMessage(it.uniba.di.cdg.xcore.m2m.model.IParticipant[], java.lang.String)
     */
    @Privileged( atleast = Role.PARTICIPANT )
    public void sendPrivateMessage( List<IParticipant> participants, String text ) {
        for (IParticipant p : participants) 
            service.sendPrivateMessage( p, text );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#changeSubject(java.lang.String)
     */
    @Privileged( atleast = Role.MODERATOR )
    public void changeSubject( String subject ) {
        // The server will notify us back about the changed subject so there is no need to set it 
        service.changeSubject( subject );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#inviteNewParticipant(java.lang.String, java.lang.String)
     */
    @Privileged( atleast = Role.MODERATOR )
    public void inviteNewParticipant( String participantId ) {
        service.invite( participantId, IMultiChatHelper.MULTICHAT_REASON );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#getView()
     */
    public IChatRoomView getChatRoomView() {
        return roomView;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#getTalkView()
     */
    public ITalkView getTalkView() {
        return talkView;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#getService()
     */
    public IMultiChatService getService() {
        return service;
    }

    /**
     * @return Returns the context.
     */
    protected MultiChatContext getContext() {
        return context;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#addListener(it.uniba.di.cdg.xcore.m2m.IMultiChat.IMultiChatListener)
     */
    public void addListener( IMultiChatListener listener ) {
        chatlisteners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#removeListener(it.uniba.di.cdg.xcore.m2m.IMultiChat.IMultiChatListener)
     */
    public void removeListener( IMultiChatListener listener ) {
        chatlisteners.remove( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#getBackendHelper()
     */
    public INetworkBackendHelper getBackendHelper() {
        return backendHelper;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#setBackendHelper(it.uniba.di.cdg.xcore.network.INetworkBackendHelper)
     */
    public void setBackendHelper( INetworkBackendHelper backendHelper ) {
        this.backendHelper = backendHelper;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#getUihelper()
     */
    public IUIHelper getUihelper() {
        return uihelper;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#setUihelper(it.uniba.di.cdg.xcore.ui.IUIHelper)
     */
    public void setUihelper( IUIHelper uihelper ) {
        this.uihelper = uihelper;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IRoleProvider#getRole()
     */
    public Role getRole() {
        return getService().getModel().getLocalUser().getRole();
    }

    /**
     * @return Returns the workbenchWindow.
     */
    public IWorkbenchWindow getWorkbenchWindow() {
        return workbenchWindow;
    }
    
    /**
     * @param workbenchWindow The workbenchWindow to set.
     */
    public void setWorkbenchWindow( IWorkbenchWindow workbenchWindow ) {
        this.workbenchWindow = workbenchWindow;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IMultiChatManager#notifyViewReadOnly(java.lang.String, boolean)
     */
    public void notifyViewReadOnly( String viewId, boolean readOnly ) {
        getService().notifyViewReadOnly( viewId, readOnly );
    }
}
