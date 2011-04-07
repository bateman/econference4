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

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel;
import it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModelListener;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.model.hr.QuestionAdapterFactory;
import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.econference.ui.views.IHandRaisingView}. 
 */
public class HandRaisingView extends ViewPart implements IHandRaisingView {

    private static final int COL_ID = 0;

    private static final int COL_TEXT = 2;

    private static final int COL_REQUESTOR = 1;

    /**
     * This view unique id.
     */
    public static final String ID = EConferencePlugin.ID + ".ui.views.handRaisingView";

    /**
     * Fills each row in the table.
     */
    private static class QuestionLabelProvider implements ITableLabelProvider {
        
        private HandRaisingView view; 
        
        public QuestionLabelProvider( HandRaisingView view ) {
            this.view = view;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage( Object element, int columnIndex ) {
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText( Object element, int columnIndex ) {
            if (element == null)
                return null;

            IQuestion q = (IQuestion) element;

            switch (columnIndex) {
            case COL_ID:
                return Integer.toString( q.getId() );
            case COL_TEXT:
                return q.getText();
            case COL_REQUESTOR:
                IParticipant p = view.getChatRoomModel().getLocalUserOrParticipant( q.getWho() );
                if (p != null)
                    return p.getNickName();
                return q.getWho();
            }

            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener( ILabelProviderListener listener ) {
            // TODO Implement this method
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose() {
            // TODO Implement this method
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty( Object element, String property ) {
            // TODO Implement this method
            return false;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener( ILabelProviderListener listener ) {
            // TODO Implement this method
        }
    }

    /**
     * Sort the participants lexicographically.
     */
    private static class QuestionSorter extends ViewerSorter {
        @Override
        public int compare( Viewer viewer, Object o1, Object o2 ) {
            IQuestion q1 = (IQuestion) o1;
            IQuestion q2 = (IQuestion) o2;

            return q1.getId() - q2.getId();
        }
    }

    private static class QuestionContentProvider implements IStructuredContentProvider {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements( Object inputElement ) {
            if (inputElement instanceof IHandRaisingModel)
                return ((IHandRaisingModel) inputElement).getQuestions();
            return null;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
            // Do nothing
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
            // Do nothing
        }
    };

    /**
     * Just refresh the list view when the model changes (Ok, this could be optimized a bit
     * better ;)).
     */
    private final IHandRaisingModelListener hrListener = new IHandRaisingModelListener() {
        public void questionAdded( IQuestion q ) {
            System.out.println( "questionAdded( " + q.getText() + ")" );
            refreshView();
        }

        public void questionRemoved( IQuestion q ) {
            System.out.println( "questionRemoved( " + q.getText() + ")" );
            refreshView();
        }

        public void questionModified( IQuestion q ) {
            System.out.println( "questionModified( " + q.getText() + ")" );
            refreshView();
        }
    };

    /**
     * Just keep in sync the UI with the local user role's changes.
     */
    private final ConferenceModelListenerAdapter chatRoomListener = new ConferenceModelListenerAdapter() {
        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.model.ChatRoomModelAdapter#localUserChanged()
         */
        @Override
        public void localUserChanged() {
            syncUIWithUserRole();
        }

        /*
         * HR is disabled if the conference is stopped or we are frozen.
         */
        private boolean mustDisableHR() {
            final IConferenceModel model = getManager().getService().getModel();
            return ConferenceStatus.STOPPED.equals( model.getStatus() )
                    || Status.FROZEN.equals( model.getStatus() );
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter#statusChanged()
         */
        @Override
        public void statusChanged() {
            setReadOnly( mustDisableHR() );
            updateActionStatusAccordingToStatus();
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.model.ChatRoomModelAdapter#changed(it.uniba.di.cdg.xcore.m2m.model.IParticipant)
         */
        @Override
        public void changed( IParticipant participant ) {
            setReadOnly( mustDisableHR() );
            updateActionStatusAccordingToStatus();
        }
    };

    /** 
     * The tree view presenting the buddies. 
     */
    protected TableViewer questionViewer;

    /**
     * Action for inviting a user.
     */
    protected IAction enableDisableHRAction;

    /**
     * Action for signaling the hand raising to a moderator.
     */
    protected IAction raiseHandAction;
    
    /**
     * The adapter factory.
     * TODO This could be declared in plugin.xml but it currently doesn't work :S So we use it
     * explicitely.
     */
    private IAdapterFactory adapterFactory;

    /**
     * The conference manager.
     */
    private IEConferenceManager manager;

    /**
     * Flag: the view is read-write by default. Note that we do not disable any control or menu:
     * the mechanism is only needed at runtime for UI action enablements.
     */
    private boolean readOnly;

    /**
     * Makes up a new buddy list view.
     */
    public HandRaisingView() {
        super();
        this.adapterFactory = new QuestionAdapterFactory();
        // By default HR is disabled (moderators must enable it explicitely, see BR #41) 
        setReadOnly( true ); 
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        questionViewer = new TableViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.BORDER | SWT.FULL_SELECTION );
        // Create the table columns ...
        TableColumn col = new TableColumn( questionViewer.getTable(), SWT.LEFT );
        col.setText( "Id" );
        col.setWidth( 40 );

        col = new TableColumn( questionViewer.getTable(), SWT.LEFT );
        col.setText( "Requestor" );
        col.setWidth( 120 );

        col = new TableColumn( questionViewer.getTable(), SWT.LEFT );
        col.setText( "Question" );
        col.setWidth( 460 );

        questionViewer.getTable().setHeaderVisible( true );
        questionViewer.getTable().setToolTipText(
                "To ask a question, right-click the Moderator icon in the \"Who's on\" list" );
        getSite().setSelectionProvider( questionViewer );

        Platform.getAdapterManager().registerAdapters( adapterFactory, IQuestion.class );

        questionViewer.setLabelProvider( new QuestionLabelProvider( this ) );
        questionViewer.setSorter( new QuestionSorter() );

        questionViewer.setContentProvider( new QuestionContentProvider() );

        hookContextMenu();

        makeActions();
        contributeToActionBars( getViewSite().getActionBars() );
    }

    /**
     * Hook the context menu. Do not ask me how this works since it is copy'n'pasted  ;)
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager( "questionsPopup" ); //$NON-NLS-1$
        menuMgr.add( new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS ) );

        Menu menu = menuMgr.createContextMenu( questionViewer.getControl() );
        questionViewer.getControl().setMenu( menu );

        // Register viewer with site. This must be done before making the actions.
        getSite().registerContextMenu( menuMgr, questionViewer );
    }

    /**
     * Create the actions to attach to this view action bar.
     */
    protected void makeActions() {
        enableDisableHRAction = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                if (getManager() == null)
                    return;
                
                setReadOnly( !isReadOnly() );
                getManager().notifyViewReadOnly( ID, isReadOnly() );
                updateActionStatusAccordingToStatus();
            }
        };
        raiseHandAction = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                IParticipant moderator = findModerator();
                
                if (moderator == null) {
                   UiPlugin.getUIHelper().showErrorMessage( "Sorry, no moderator seems to be online at the moment." );
                   return;
                }
                
                String question = UiPlugin.getUIHelper().askFreeQuestion(
                        "Please, type your question in the text box below and press OK.", 
                        "" );
                if (question != null)
                    getManager().notifyRaiseHand( moderator, question );
            }
        };
        raiseHandAction.setText( "Raise hand" );
        raiseHandAction
                .setToolTipText( "Click to raise hand and ask something to a moderator" );
        raiseHandAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/action_raise_hand.png" ) );

        updateActionStatusAccordingToStatus();
    }

    /**
     * Find the first moderator in the chat room.
     * 
     * @return the moderator or <code>null</code> if no moderator is present
     */
    public IParticipant findModerator() {
        IParticipant[] all = getChatRoomModel().getParticipants();
        
        for (int i = 0; i < all.length; i++) {
            IParticipant p = all[i];
            if (Role.MODERATOR.equals( p.getRole() ))
                return p;
        }
        
        return null;
    }

    /**
     * Change action text accordingly to current view status. 
     */
    public void updateActionStatusAccordingToStatus() {
        if (isReadOnly()) {
            enableDisableHRAction.setText( "Enable hand raising" );
            enableDisableHRAction
                    .setToolTipText( "Click so other users can raise hands and ask questions" );
            enableDisableHRAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
                    EConferencePlugin.ID, "icons/action_enable_hr.png" ) );
            
            raiseHandAction.setEnabled( false );
        } else {
            enableDisableHRAction.setText( "Disable hand raising" );
            enableDisableHRAction
                    .setToolTipText( "Click so other users WON'T raise hands and ask questions" );
            enableDisableHRAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
                    EConferencePlugin.ID, "icons/action_disable_hr.png" ) );

            boolean weAreModerator = Role.MODERATOR.equals( getChatRoomModel().getLocalUser().getRole() );
            enableDisableHRAction.setEnabled( weAreModerator );
            // Only non-moderator participants can raise hands!
            raiseHandAction.setEnabled( !weAreModerator );
        }
    }

    /**
     * Add actions to the action and menu bars (local to this view).
     * @param bars
     */
    protected void contributeToActionBars( IActionBars bars ) {
        bars.getToolBarManager().add( enableDisableHRAction );
        bars.getToolBarManager().add( raiseHandAction );

        bars.getMenuManager().add( enableDisableHRAction );
        bars.getMenuManager().add( raiseHandAction );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        Platform.getAdapterManager().unregisterAdapters( adapterFactory );
    }

    @SwtAsyncExec
    private void syncUIWithUserRole() {
        final IParticipant localUser = getManager().getService().getModel().getLocalUser();

        enableDisableHRAction.setEnabled( Role.MODERATOR.equals( localUser.getRole() ) );
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        questionViewer.getControl().setFocus();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ID;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView#setChat(it.uniba.di.cdg.xcore.m2m.IMultiChat)
     */
    public void setManager( IEConferenceManager newManager ) {
        // Deregister from previous chat, if there was any
        if (manager != null) {
            manager.getService().getHandRaisingModel().removeListener( hrListener );
            manager.getService().getModel().removeListener( chatRoomListener );
        }

        this.manager = newManager;
        if (newManager == null)
            return;

        manager.getService().getModel().addListener( chatRoomListener );

        final IHandRaisingModel model = manager.getService().getHandRaisingModel();
        // Update (sort of ...)
        questionViewer.setInput( model );
        model.addListener( hrListener );

        syncUIWithUserRole();
        refreshView();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView#getChat()
     */
    public IEConferenceManager getManager() {
        return manager;
    }

    /**
     * Refresh the whole view. 
     */
    @SwtAsyncExec
    private void refreshView() {
        questionViewer.refresh();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.IRoleProvider#getRole()
     */
    public Role getRole() {
        return getManager().getRole();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IActivatableView#setReadOnly(boolean)
     */
    public void setReadOnly( boolean readOnly ) {
        this.readOnly = readOnly;
        //updateActionStatusAccordingToStatus();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IActivatableView#isReadOnly()
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    private IChatRoomModel getChatRoomModel() {
        return getManager().getService().getModel();
    }

	@Override
	public void refresh() {
		updateActionStatusAccordingToStatus();
		System.out.println("HandRaising view refreshed");
	}
}
