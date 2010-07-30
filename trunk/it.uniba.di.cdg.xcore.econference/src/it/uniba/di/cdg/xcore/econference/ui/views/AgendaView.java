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
package it.uniba.di.cdg.xcore.econference.ui.views;

import static it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus.STARTED;
import static it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus.STOPPED;
import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.aspects.SwtSyncExec;
import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.IItemListListener;
import it.uniba.di.cdg.xcore.econference.model.ItemListListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;

/**
 * The agenda view provides a way for moderators   
 */
public class AgendaView extends AgendaViewUI implements IAgendaView {
    /**
     * Unique id for this view.
     */
    public static final String ID = EConferencePlugin.ID + ".ui.views.agendaView"; 

    private IEConferenceManager manager;

    private ConferenceModelListenerAdapter conferenceModelListener = new ConferenceModelListenerAdapter() {
        @Override
        public void statusChanged() {
            System.out.println( "statusChanged()" );
            changeButtonStatus( getModel().getStatus() );
        }

        @Override
        public void itemListChanged() {
            // XXX Note that we should also deregister from the old one! Let's hope the caller
            // that has changed the item list will also clear its listeners :S 
//            getModel().getItemList().addListener( itemListListener );

            System.out.println( "itemListChanged()" );
            changeItemList( getModel().getItemList() );
        }
    };

    private IItemListListener itemListListener = new ItemListListenerAdapter() {
        @SwtAsyncExec
        @Override
        public void currentSelectionChanged( int currItemIndex ) {
            //getManager().notifyCurrentAgendaItemChanged( Integer.toString( currItemIndex ) );
            itemList.setSelection( currItemIndex );
        }

        @Override
        public void contentChanged( IItemList itemList ) {
            changeItemList( itemList );
        }

//        @SwtAsyncExec
//        @Override
//        public void itemAdded( IDiscussionItem item ) {
//            itemList.add( item.getText() );
//        }
    };
    
    /**
     * Action for appending a new item to the discussion.
     */
    private IAction addNewItemAction;

    public AgendaView() {
        super();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.AgendaViewUI#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        super.createPartControl( parent );

        // The item list will be enabled when the user start the conference: since the conference
        // starts stopped we disable it now 
        itemList.setEnabled( false );

        startStopButton.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (STARTED.compareTo( getModel().getStatus()) == 0) {
                    itemList.setEnabled( false );
                    manager.setStatus( STOPPED );
                } else {
                    itemList.setEnabled( true );
                    manager.setStatus( STARTED );
                }
            }
        });
        
        itemList.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // Only moderators can change the current discussion item
                if (!Role.MODERATOR.equals( getModel().getLocalUser().getRole() ))
                    return;
                // The view is disabled by default for non-moderators: so no way for them
                // to genereate selection events ...
                String threadId = String.format( "%d", itemList.getSelectionIndex() ); 
                manager.notifyCurrentAgendaItemChanged( threadId );
            }
        });
        
        makeActions();
        contributeToActionBars( getViewSite().getActionBars() );
    }

    private void makeActions() {
        addNewItemAction = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                String newItem = UiPlugin.getUIHelper().askFreeQuestion( "Please, type the text referring to the new item and press Ok." +
                       "\nOtherwise press cancel to discard the operation.", 
                       "Add a new item" );
                if (newItem == null) // Cancel pressed
                    return;
                
                getModel().getItemList().addItem( newItem );
                getManager().notifyItemListToRemote();
                //getManager().addAgendaItem( newItem );
            }
        };
        addNewItemAction.setEnabled( false ); // changed when setManager() is called
        addNewItemAction.setText( "Add a new discussion item" );
        addNewItemAction.setToolTipText( "Click to append a new discussion item to the agenda" );
        addNewItemAction.setImageDescriptor( EConferencePlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/action_add_agenda_item.png" ) );
    }

    /**
     * Add actions to the action and menu bars (local to this view).
     * 
     * @param bars
     */
    private void contributeToActionBars( IActionBars bars ) {
        bars.getToolBarManager().add( addNewItemAction );
        //        bars.getMenuManager().add( refreshRemoteAction );
    }
   
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IAgendaView#setConference(it.uniba.di.cdg.xcore.econference.IEConference)
     */
    public void setManager( IEConferenceManager manager ) {
        // Switching conference, so deregister from previous manager ...
        if (this.manager != null) {
            getModel().removeListener( conferenceModelListener );
            getModel().getItemList().removeListener( itemListListener );
        }
        this.manager = manager;
        
        getModel().addListener( conferenceModelListener );
        getModel().getItemList().addListener( itemListListener );

        changeItemList( getModel().getItemList() );
        changeButtonStatus( getModel().getStatus() );
        updateActionsAccordingToRole();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IAgendaView#getConference()
     */
    public IEConferenceManager getManager() {
        return manager;
    }

    @SwtAsyncExec
    private void changeItemList( IItemList items ) {
        itemList.removeAll();        
        for (int i=0; i<items.size(); i++)
            itemList.add( ((IDiscussionItem)items.getItem(i)).getText() );
    }

    @SwtAsyncExec
    private void changeButtonStatus( ConferenceStatus status ) {
        if (STARTED.compareTo( status ) == 0) {
            startStopButton.setText( "Stop conference" );
            startStopButton.setToolTipText( "Press to stop the conference" );
            itemList.setEnabled( 
                    Role.MODERATOR.equals( getModel().getLocalUser().getRole() ) );
        }
        else {
            startStopButton.setText( "Start conference" );
            startStopButton.setToolTipText( "Press to start the conference" );
            itemList.setEnabled( false );
        }
    }

    @SwtAsyncExec
    private void updateActionsAccordingToRole() {
        addNewItemAction.setEnabled(
                getManager() != null && // There is a manager
                Role.MODERATOR.equals( getModel().getLocalUser().getRole() ) // and we are moderators 
                );
    }
    
    @SwtAsyncExec
    public void setReadOnly( boolean readOnly ) {
        startStopButton.setEnabled( !readOnly );
//        itemList.setEnabled( !readOnly );
    }

    @SwtSyncExec
    public boolean isReadOnly() {
        return !startStopButton.isEnabled();
    }

    private IConferenceModel getModel() {
        return getManager().getService().getModel();
    }

	@Override
	public void refresh() {
		// Do nothing
		
	}
}    
    