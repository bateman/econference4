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

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry;
import it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel;
import it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModelListener;
import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.internal.MultiChatManager;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * 
 */
public class StoredEventsView extends ViewPart implements IStoredEventsView {

    public static final String ID = EConferencePlugin.ID + ".ui.views.storedEventsView";

    private Composite top = null;

    private SashForm sashForm = null;

    private TableViewer viewer;

    private Action joinAction;

    private Action removeAction;

    private Action clearAllAction;

    private Action doubleClickAction;

    private IStoredEventsModel storedEventsModel;

    private final IStoredEventsModelListener storedEventModelListener = new IStoredEventsModelListener() {
        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModelListener#notifyUpdate()
         */
        @SwtAsyncExec
        public void notifyUpdate() {
            refreshView();
        }
    };

    private StyledText detailsText;

    /*
     * The content provider class is responsible for providing objects to the view. It can wrap
     * existing objects in adapters or simply return objects as-is. These objects may be sensitive
     * to the current input of the view, or ignore it and always show the same content (like Task
     * List, for example).
     */

    class ViewContentProvider implements IStructuredContentProvider {
        public void inputChanged( Viewer v, Object oldInput, Object newInput ) {
            // Do nothing
        }

        public void dispose() {
            // Do nothing
        }

        public Object[] getElements( Object inputElement ) {
            if (inputElement instanceof IStoredEventsModel) {
                return ((IStoredEventsModel) inputElement).getStoredEvents();
            } else {
                System.err.println( "Null" );
                return null;
            }
        }
    }

    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
        private Image image;

        public ViewLabelProvider() {
            super();
            image = EConferencePlugin.imageDescriptorFromPlugin( EConferencePlugin.ID,
                    "icons/view_stored_events_join_action_enabled.png" ).createImage( true );
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        @Override
        public void dispose() {
            image.dispose();
        }

        public String getColumnText( Object obj, int index ) {
            return getText( obj );
        }

        public Image getColumnImage( Object obj, int index ) {
            return getImage( obj );
        }

        public Image getImage( Object obj ) {
            return image;
        }
    }

    // class DateSorter extends ViewerSorter {
    // // TODO sort by date
    // }

    /**
     * The constructor.
     */
    public StoredEventsView() {
        storedEventsModel = EConferencePlugin.getDefault().getStoredEventsModel();
        storedEventsModel.registerStoredEventsModelListener( storedEventModelListener );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        storedEventsModel.unregisterStoredEventsModelListener( storedEventModelListener );
    }

    /**
     * Refreshes the view in case of updates notified thru the model listener.
     */
    protected void refreshView() {
        viewer.refresh();
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl( Composite parent ) {
        top = new Composite( parent, SWT.NONE );
        top.setLayout( new FillLayout() );

        createSashForm();
        createViewer( sashForm );
        createDetailsViewer( sashForm );

        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }

    private void createDetailsViewer( Composite parent ) {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.horizontalSpacing = 1;
        gridLayout1.marginWidth = 0;
        gridLayout1.marginHeight = 1;
        gridLayout1.verticalSpacing = 1;
        Composite bottomComposite = new Composite( parent, SWT.NONE );
        bottomComposite.setLayout( gridLayout1 );
        detailsText = new StyledText( bottomComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP );
        detailsText.setLayoutData( gridData );
    }

    private void createViewer( Composite parent ) {
        viewer = new TableViewer( parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
        viewer.setContentProvider( new ViewContentProvider() );
        viewer.setLabelProvider( new ViewLabelProvider() );
        // TODO viewer.setSorter( new DateSorter() );
        viewer.setInput( storedEventsModel );
        viewer.addSelectionChangedListener( new ISelectionChangedListener() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            public void selectionChanged( SelectionChangedEvent event ) {
                detailsText.setText( "" );
                IStoredEventEntry invitation = getSingleSelection();
                if (null != invitation) {
                    detailsText
                            .setText( String.format(
                                    "Event: %s\nFor: %s\nInviter: %s\nRoom: %s\nPassword: %s",
                                    invitation.getReason(), invitation.getAccountId(), invitation
                                            .getInviter(), invitation.getRoom(), invitation
                                            .getPassword() ) );
                }
            }
        } );
    }

    private void createSashForm() {
        sashForm = new SashForm( top, SWT.NONE );
        sashForm.setOrientation( org.eclipse.swt.SWT.VERTICAL );
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager( "#PopupMenu" );
        menuMgr.setRemoveAllWhenShown( true );
        menuMgr.addMenuListener( new IMenuListener() {
            public void menuAboutToShow( IMenuManager manager ) {
                StoredEventsView.this.fillContextMenu( manager );
            }
        } );
        Menu menu = menuMgr.createContextMenu( viewer.getControl() );
        viewer.getControl().setMenu( menu );
        getSite().registerContextMenu( menuMgr, viewer );
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown( bars.getMenuManager() );
        fillLocalToolBar( bars.getToolBarManager() );
    }

    private void fillLocalPullDown( IMenuManager manager ) {
        manager.add( joinAction );
        manager.add( new Separator() );
        manager.add( removeAction );
        manager.add( new Separator() );
        manager.add( clearAllAction );
    }

    private void fillContextMenu( IMenuManager manager ) {
        manager.add( joinAction );
        manager.add( removeAction );
        manager.add( clearAllAction );
        // Other plug-ins can contribute there actions here
        manager.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    private void fillLocalToolBar( IToolBarManager manager ) {
        manager.add( joinAction );
        manager.add( removeAction );
        manager.add( clearAllAction );
    }

    private void makeActions() {
        joinAction = new Action() {
            public void run() {
                IStoredEventEntry invitation = getSingleSelection();
                if (null != invitation) {
                    if (NetworkPlugin.getDefault().getHelper().getOnlineBackends().size() == 0) {
                        UiPlugin.getUIHelper().showErrorMessage( "Please, connect first!" );
                    } else {                    	
	                        try{//User pressed yes
	                        	
	                        	
	                        	EConferenceContext context = EConferencePlugin.getDefault().getHelper()
                    			.askUserAcceptInvitation( (InvitationEvent) invitation);
	                        	if( context!=null){

	                        		EConferencePlugin.getDefault().getHelper().open( context );
	                        	}else{ 
		                        	MultiChatContext mcContext = new MultiChatContext(null, null, (InvitationEvent) invitation);
		                        	MultiChatPlugin.getDefault().getHelper().open( mcContext );
	                        	}
	                        }catch(NullPointerException e){
	                        	//User pressed no
	                        	e.printStackTrace();
	                        }
//                    	}else{
//                    		UiPlugin.getUIHelper().showErrorMessage( "You do not have the right plugin for " +
//                    				"partecipating to this event. Plugin name: ");//+invitation.getPluginId() );
//                    	}
                    }
                    
                }
            }
        };
        joinAction.setText( "Join" );
        joinAction.setToolTipText( "Join the selected event that you have been invited to" );
        joinAction.setImageDescriptor( EConferencePlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/view_stored_events_join_action_enabled.png" ) );

        removeAction = new Action() {
            public void run() {
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                IStoredEventEntry event = (IStoredEventEntry) selection.getFirstElement();
                if (null != event) {
                    boolean answer = UiPlugin.getUIHelper().askYesNoQuestion(
                            "Remove invitation confirmation",
                            "Do you really want to remove the selected invitation?" );
                    if (true == answer) {
                        storedEventsModel.removeStoredEventEntry( event );
                        System.out.println( "View: Remove action executed" );
                    }
                }
            }
        };
        removeAction.setText( "Remove event" );
        removeAction.setToolTipText( "Remove the selected event from the list" );
        removeAction.setImageDescriptor( EConferencePlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/view_stored_events_cancel_action_enabled.png" ) );
        // double click means join action
        doubleClickAction = new Action() {
            public void run() {
                joinAction.run();
            }
        };
        clearAllAction = new Action() {
            public void run() {
                if (null != viewer.getElementAt( 0 )) {
                    boolean answer = UiPlugin.getUIHelper().askYesNoQuestion(
                            "Remove all invitations confirmation",
                            "Do you really want to remove all the existing invitations?" );
                    if (true == answer) {
                        storedEventsModel.removeAllStoredEventEntries();
                        System.out.println( "View: Clear all action executed" );
                    }
                }
            }
        };
        clearAllAction.setText( "Clear all events listed in the view" );
        clearAllAction.setToolTipText( "Clear all events riceived listed in the view" );
        clearAllAction.setImageDescriptor( EConferencePlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/view_stored_events_clear_all_action_enabled.png" ) );
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener( new IDoubleClickListener() {
            public void doubleClick( DoubleClickEvent event ) {
                doubleClickAction.run();
            }
        } );
    }

    private IStoredEventEntry getSingleSelection() {
        ISelection selection = viewer.getSelection();
        return (IStoredEventEntry) ((IStructuredSelection) selection).getFirstElement();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

}