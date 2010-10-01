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
package it.uniba.di.cdg.xcore.m2m.ui.views;

import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.messages.IMessage;
import it.uniba.di.cdg.xcore.ui.views.TalkView;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;

/**
 * Implementation of the talk view with some custom UI functions, useful when dealing with
 * multi-chats.
 */
public class MultiChatTalkView extends TalkView implements IMultiChatTalkView {
    /**
     * Unique id of this view.
     */
    public static final String ID = MultiChatPlugin.ID + ".ui.views.multiChatTalkView";

    private static class MultiCallViewAction extends Action {
    	
        /* (non-Javadoc)
         * @see org.eclipse.jface.action.Action#run()
         */
        @Override
        public void run() {
        	IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
        	if(b.getMultiCallAction().isCalling())
        		b.getMultiCallAction().finishCall();
        	else
        		b.getMultiCallAction().call();
        }
        
    }
    
    /**
     * The invitation action needs to display the invitees' list (from the ECX file, i.e.) and 
     * collect the typed invitees' ids as they are going inserted. 
     */
    private static class InviteUserViewAction extends Action {
        private final IMultiChatTalkView view;

        private final List<String> completionEntries;

        public InviteUserViewAction( IMultiChatTalkView view ) {
            super();
            this.view = view;
            this.completionEntries = new ArrayList<String>();
        }

        private List<String> getInvitees() {
            final MultiChatContext context = getManager().getService().getContext();
            final List<Invitee> invitees = context.getInvitees();
            // Return an empty list if there is no invitees' list (this happens when we are invited, and not inviter)
            if (invitees == null)
                return new ArrayList<String>();

            final List<String> ids = new ArrayList<String>( invitees.size() );
            for (Invitee i : invitees)
                ids.add( i.getId() );
            return ids;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.action.Action#run()
         */
        @Override
        public void run() {
            List<String> entries = getInvitees();
            entries.addAll( completionEntries );

            final String[] array = new String[entries.size()];
            entries.toArray( array );

            String userId = getManager().getUihelper().askChoice( "Invite user",
                    "Please, type id (i.e. \"user@server.net\") of the user you want to invite", 0,
                    array );
            if (userId != null) {
                if (!entries.contains( userId ))
                    completionEntries.add( userId );

                getManager().inviteNewParticipant( userId );
            }
        }

        private IMultiChatManager getManager() {
            return view.getManager();
        }
    }

//    /**
//     * Action for changing the subject.
//     */
//    private IAction changeSubjectAction;

    /**
     * Action for inviting a user.
     */
    private IAction inviteUserAction;
    
    private IAction multiCallViewAction;

    private IMultiChatManager manager;

    /**
     * Create a new Multichat talk view.
     */
    public MultiChatTalkView() {
        super();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.TalkView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        super.createPartControl( parent );

        makeActions();

        contributeToActionBars( getViewSite().getActionBars() );
    }

    /**
     * Create the actions to attach to this view action bar.
     */
    protected void makeActions() {
//        changeSubjectAction = new Action() {
//            /* (non-Javadoc)
//             * @see org.eclipse.jface.action.Action#run()
//             */
//            @Override
//            public void run() {
//                InputDialog input = new InputDialog( getSite().getShell(),
//                        "Change the chat subject", "Please, type the new subject:", null, null );
//                if (Dialog.OK == input.open()) {
//                    getManager().changeSubject( input.getValue() );
//                }
//            }
//        };
//        changeSubjectAction.setText( "Change Subject" );
//        changeSubjectAction.setToolTipText( "Change current discussion subject" );
//        changeSubjectAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
//                MultiChatPlugin.ID, "icons/action_change_subject.png" ) );

        inviteUserAction = new InviteUserViewAction( this );
        inviteUserAction.setText( "Invite user" );
        inviteUserAction.setToolTipText( "Invite another user to join this chat" );
        inviteUserAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
                MultiChatPlugin.ID, "icons/action_invite_user.png" ) );
        
        multiCallViewAction = new MultiCallViewAction();
        multiCallViewAction.setToolTipText( "Call Group / Close Call" );
        multiCallViewAction.setImageDescriptor(MultiChatPlugin.imageDescriptorFromPlugin(
        		MultiChatPlugin.ID, "icons/Telephone-1-icon-16.png"));
        
    }

    /**
     * Add actions to the action and menu bars (local to this view).
     * @param bars
     */
    protected void contributeToActionBars( IActionBars bars ) {
        // NO change subject upon fc request on 20.01.2006
        //        bars.getToolBarManager().add( changeSubjectAction );
        bars.getToolBarManager().add( inviteUserAction );
        bars.getToolBarManager().add(multiCallViewAction);

        //        bars.getMenuManager().add( changeSubjectAction );
        bars.getMenuManager().add( inviteUserAction );
        bars.getMenuManager().add(multiCallViewAction);
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.service.IMessageReceivedListener#messageReceived(it.uniba.di.cdg.xcore.network.messages.IMessage)
     */
    public void messageReceived( IMessage incoming ) {
        mangleMessage( incoming );

        appendMessage( incoming );
    }

    /**
     * Mangle the from field of the message so that the nickname instead of the ugly complete id
     * is used when appending text.
     * 
     * @param incoming
     */
    private void mangleMessage( IMessage incoming ) {
        final IChatRoomModel model = getManager().getService().getModel();

        String from = incoming.getFrom();

        IParticipant p = model.getLocalUser();
        if (p == null || !p.getId().equals( from ))
            p = model.getParticipant( from );
        if (p != null)
            incoming.setFrom( p.getNickName() );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.ui.views.IMultiChatTalkView#listenToChat(it.uniba.di.cdg.xcore.m2m.IMultiChat)
     */
    public void setManager( IMultiChatManager manager ) {
        //        if (this.manager != null) {
        //            getModel().removeListener( talkListener );
        //        }
        this.manager = manager;
        if (manager == null)
            return;

        //        getModel().addListener( talkListener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.ui.views.IMultiChatTalkView#getMultiChat()
     */
    public IMultiChatManager getManager() {
        return manager;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.service.IMessageReceivedListener#privateMessageReceived(java.lang.String, java.lang.String)
     */
    public void privateMessageReceived( String text, String who ) {
        appendMessage( String.format( "[PM from %s] %s", who, text ) );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.TalkView#isSaveOnCloseNeeded()
     */
    @Override
    public boolean isSaveOnCloseNeeded() {
        return true;
    }

}