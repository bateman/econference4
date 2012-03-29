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
import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.aspects.SwtSyncExec;
import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.IItemListListener;
import it.uniba.di.cdg.xcore.econference.model.ItemListListenerAdapter;
import it.uniba.di.cdg.xcore.econference.popup.agenda.SelectedItemListener;
import it.uniba.di.cdg.xcore.econference.popup.handler.DeleteItem;
import it.uniba.di.cdg.xcore.econference.sourceprovider.AgendaCommandState;
import it.uniba.di.cdg.xcore.econference.sourceprovider.AgendaCommandStateModerator;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * The agenda view provides a way for moderators   
 */
public class AgendaView extends AgendaViewUI implements IAgendaView {
    /**
     * Unique id for this view.
     */
    public static final String ID = EConferencePlugin.ID + ".ui.views.agendaView"; 

    private IEConferenceManager manager;
    
    private ISourceProviderService sourceProviderService;
    
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
  
        if (currItemIndex == DeleteItem.DELETE_ITEM_INDEX)
        	setDiscussedItem("");
        	else{
        		viewer.getList().setSelection(currItemIndex);
        		setDiscussedItem(viewer.getList().getItem(currItemIndex));
        	}
        }

        @Override
        public void contentChanged( IItemList itemList ) {
            changeItemList( itemList );
        }

    };

    public AgendaView() {
        super();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.AgendaViewUI#createPartControl(org.eclipse.swt.widgets.Composite)
     */

	@Override
    public void createPartControl( Composite parent ) {
        super.createPartControl( parent );
        
        SelectedItemListener selected = new SelectedItemListener();
        selected.setAgendaView(this);
        
        // Get the source provider service
		sourceProviderService =  (ISourceProviderService) this.getSite()
						.getWorkbenchWindow().getService(ISourceProviderService.class);
		
            
        // The item list will be enabled when the user start the conference: since the conference
        // starts stopped we disable it now 
        viewer.getControl().setEnabled(false);

        startStopButton.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (STARTED.compareTo( getModel().getStatus()) == 0) {

                    manager.setStatus( STOPPED );
                    selText.setBackground(top.getDisplay().getSystemColor(SWT.COLOR_RED));
                } else {

                    manager.setStatus( STARTED );
                    selText.setBackground(top.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                }
            }
        });
        
    
        viewer.getList().addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // Only moderators can change the current discussion item
                if (!Role.MODERATOR.equals( getModel().getLocalUser().getRole() ))
                    return;
                // The view is disabled by default for non-moderators: so no way for them
                // to genereate selection events ...
                String threadId = String.format( "%d", viewer.getList().getSelectionIndex() );
                SelectedItemListener selectedItem = new SelectedItemListener();
                selectedItem.setSelectedIndex(threadId);
                selectedItem.setSelectedAgendaView(viewer);
            }
        });
            
            
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
            	if (STARTED.compareTo( getModel().getStatus() ) == 0) {
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                if (selection.isEmpty()) return;
                
                if (!Role.MODERATOR.equals( getModel().getLocalUser().getRole() )){
                    return;
                    }

                String threadId = String.format( "%d", viewer.getList().getSelectionIndex() );
                setDiscussedItem(viewer.getList().getItem(viewer.getList().getSelectionIndex()));
                
                manager.notifyCurrentAgendaItemChanged( threadId );
            	}
            }
        });

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
		
		// Now get AgendaCommandState service
		AgendaCommandState agendaStateService = (AgendaCommandState) sourceProviderService
				.getSourceProvider(AgendaCommandState.MY_STATE);
		
		agendaStateService.toogleEnabled(Role.MODERATOR.equals( getModel().getLocalUser().getRole() ));
		
		//Set startStopButton
		startStopButton.setVisible(Role.MODERATOR.equals( getModel().getLocalUser().getRole() ));
                

    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IAgendaView#getConference()
     */
    public IEConferenceManager getManager() {
        return manager;
    }

    @SwtAsyncExec
    private void changeItemList( IItemList items ) {
        viewer.getList().removeAll();
        
        for (int i=0; i<items.size(); i++){
            viewer.add(((IDiscussionItem)items.getItem(i)).getText() );
        	}
    }

    @SwtAsyncExec
    private void changeButtonStatus( ConferenceStatus status ) {
    	// Now get AgendaCommandState service
		AgendaCommandStateModerator agendaStateService = (AgendaCommandStateModerator) sourceProviderService
				.getSourceProvider(AgendaCommandStateModerator.MY_STATE);
		  	
    	
    	if (STARTED.compareTo( status ) == 0) {
            startStopButton.setText( "Stop conference" );
            startStopButton.setToolTipText( "Press to stop the conference" );

            viewer.getControl().setEnabled(
                    Role.MODERATOR.equals( getModel().getLocalUser().getRole() ) );
            selText.setBackground(top.getDisplay().getSystemColor(SWT.COLOR_GREEN));

    		agendaStateService.toogleEnabled(false);
        }
        else {
            startStopButton.setText( "Start conference" );
            startStopButton.setToolTipText( "Press to start the conference" );

            viewer.getControl().setEnabled(
                    Role.MODERATOR.equals( getModel().getLocalUser().getRole() ) );
            selText.setBackground(top.getDisplay().getSystemColor(SWT.COLOR_RED));

    		agendaStateService.toogleEnabled(true);
        }
    }

    
    @SwtAsyncExec
    public void setReadOnly( boolean readOnly ) {
        startStopButton.setEnabled( !readOnly );
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
    