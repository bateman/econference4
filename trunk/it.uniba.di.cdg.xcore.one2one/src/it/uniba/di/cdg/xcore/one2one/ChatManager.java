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
package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.IServiceManager;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.ITypingEvent;
import it.uniba.di.cdg.xcore.network.events.ITypingListener;
import it.uniba.di.cdg.xcore.network.events.TypingEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatComposingtEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.model.tv.TalkModel;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.one2one.ui.ChatPerspective;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.util.PartListenerAdapter;
import it.uniba.di.cdg.xcore.ui.views.ITalkView;
import it.uniba.di.cdg.xcore.ui.views.TalkView;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**
 * The controller for wiring views (input panel and message board) and model (the chat service).
 * TODO Implement "Chat with buddy not in contact list"  
 */
public class ChatManager implements IServiceManager, ISendMessagelListener, ITypingListener, IBackendEventListener{
    /**
     * Interface for listeners regarding a chat objects. Currently only <code>closed()</code>
     * event is supported.
     */
    public static interface IChatStatusListener {
        /**
         * Emitted when the chat is closed. 
         * 
         * @param chat the chat object that has been closed
         */
        void chatClosed( ChatManager chat );
        
        /**
         * Emitted whe the chat is open.
         * 
         * @param chat
         */
        void chatOpen( ChatManager chat );
    }
	
	IChatServiceActions chatServiceAction;
	
	IBackend backend;
    
    /**
     * The UI for typing and displaying text.
     */
    private ITalkView talkView;
    
    /**
     * The chat service represents our model and allow us to receive and send messages.
     */
    private IChatService service;

    /**
     * The listener for monitoring the changes of this chat controller.
     */
    private Set<IChatStatusListener> chatListeners;
    
    /**
     * Provides access to the backend facilities.
     */
    private INetworkBackendHelper backendHelper;
    
    /**
     * Gives access to UI interactions. 
     */
    private IUIHelper uihelper;
    
    private ChatContext chatContext;
    
    /**
     * Create a new chat.
     */
    public ChatManager() {
        super();
        this.chatListeners = new HashSet<IChatStatusListener>();
    }

	/**
     * Open a new chat.
     * @param chatContext 
     * @return 
     */
    public boolean open( final ChatContext chatContext ) {
        try {
        	this.chatContext = chatContext;
            setupViews();
            getBackendHelper().registerBackendListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            uihelper.showErrorMessage( "Could not start the chat: " + e.getMessage() );
            return false;
        } 

        // Append the greeting message, if it is present
        if (chatContext.getHelloMessage() != null) {
            talkView.appendMessage( chatContext.getHelloMessage() );
        }

        // Send and receive typing notifications ...
        talkView.addTypingListener( this );
        
        // Notify chat listeners that the chat is open
        for (IChatStatusListener l : chatListeners) 
            l.chatOpen( this );
        
        this.backend = getBackendHelper().getRoster().getBackend();
       
        chatServiceAction = backend.getChatServiceAction();
        chatServiceAction.OpenChat(getBuddyId());
        
        return true;
    }

    /**
     * Configures and open the views (input panel and message board).  
     * 
     * @throws WorkbenchException
     * @throws PartInitException
     */
    private void setupViews() throws WorkbenchException, PartInitException {
        PlatformUI.getWorkbench().showPerspective( ChatPerspective.ID, PlatformUI.getWorkbench().getActiveWorkbenchWindow() );
        final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        // Create the view part for this user (we can have one for each user we are chatting with
        // and the secondary id is what we give to the framework to distinguish about them).  
        final String secondaryId = getBuddyId();
        final IViewPart talkViewPart = workbenchWindow.getActivePage().showView( TalkView.ID, secondaryId, IWorkbenchPage.VIEW_ACTIVATE );

        // Register a listener so that we can close the chat when the view is closed ...
        workbenchWindow.getPartService().addPartListener( new PartListenerAdapter() {
            @Override
            public void partClosed( IWorkbenchPart part ) {
                if (part == talkView) {
                    workbenchWindow.getPartService().removePartListener( this );
                    ChatManager.this.close();
                }
            }
        });
        
        talkView = (ITalkView) talkViewPart;
        talkView.setTitleText( backendHelper.getRoster().getBuddy(getBuddyId()).getName() );
        // XXX Hack: the model should go in the service ...
        talkView.setModel( new TalkModel() );
        talkView.addListener( this );

        // Ensure that the focus is switched to this new chat
        talkViewPart.setFocus();
    }

    /**
     * Close the chat (this includes disconnecting from the underlying chat service).
     */
    public void close() {
        // Notify listeners
        for (IChatStatusListener l : chatListeners) 
            l.chatClosed( this );
        // clean-up now
        if (service != null) {
            service.close(); // The chat service will clean-up all us as listeners too
            service = null;
        }
        // Good bye, friends
        chatListeners.clear();
        chatListeners = null;
        
        chatServiceAction.CloseChat(getBuddyId());
        getBackendHelper().unregisterBackendListener(this);
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IInputPanel.IInputPanelListener#notifySendMessage(java.lang.String)
     */
    public void notifySendMessage( String message ) {
        System.out.println( "Sending message: " + message );
        talkView.appendMessage( "me > " + message );
        chatServiceAction.SendMessage(getBuddyId(), message);
    }

    /**
     * Add a new status listener for this chat.
     * 
     * @param listener
     */
    public void addChatStatusListener( IChatStatusListener listener ) {
        this.chatListeners.add( listener );
    }

    /**
     * Remove a chat listener.
     * 
     * @param listener
     */
    public void removeChatStatusListener( IChatStatusListener listener ) {
        this.chatListeners.remove( listener );
    }

    /**
     * @return Returns the buddy.
     */
    public String getBuddyId() {
        return chatContext.getBuddyId();
    	//return service.getContext().getBuddyId();
    }

    /**
     * @return Returns the backendHelper.
     */
    public INetworkBackendHelper getBackendHelper() {
        return backendHelper;
    }

    /**
     * @param backendHelper The backendHelper to set.
     */
    public void setBackendHelper( INetworkBackendHelper backendHelper ) {
        this.backendHelper = backendHelper;
    }

    /**
     * @return Returns the uihelper.
     */
    public IUIHelper getUihelper() {
        return uihelper;
    }

    /**
     * @param uihelper The uihelper to set.
     */
    public void setUihelper( IUIHelper uihelper ) {
        this.uihelper = uihelper;
    }

	@Override
	public void typing() {
		chatServiceAction.SendTyping(backend.getUserAccount().getId(), getBuddyId());
	}

	@Override
	public void onBackendEvent(IBackendEvent event) {
		
		if (event instanceof ChatComposingtEvent){
			ChatComposingtEvent chatComposingtEvent = (ChatComposingtEvent)event; 
			if (chatComposingtEvent.getFrom().equals(chatContext.getBuddyId())){
				ITypingEvent typingEvent = new TypingEvent(backendHelper.getRoster().getBuddy(getBuddyId()).getName());
				talkView.onTyping(typingEvent);
			}
		}else 
		if (event instanceof ChatMessageReceivedEvent){
			ChatMessageReceivedEvent chatMessageReceivedEvent = (ChatMessageReceivedEvent)event;
			if (chatMessageReceivedEvent.getFrom().equals(chatContext.getBuddyId()))
				talkView.appendMessage( backendHelper.getRoster().getBuddy(getBuddyId()).getName() + " > " + chatMessageReceivedEvent.getMessage() );
		
		}
		if (event instanceof ChatExtensionProtocolEvent){
			ChatExtensionProtocolEvent chatExtensionProtocolEvent =
				(ChatExtensionProtocolEvent)event;
		}
			
		
	}
}
