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

import it.uniba.di.cdg.aspects.SwtAsyncExec;

import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModelAdapter;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModelListener;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.ParticipantAdapterFactory;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView}.
 */
public class ChatRoomView extends ViewPart implements IChatRoomView {
    /**
     * This view unique id.
     */
    public static final String ID = MultiChatPlugin.ID + ".ui.views.chatRoomView";

    private static class ChatRoomContentProvider implements IStructuredContentProvider {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements( Object inputElement ) {
            if (inputElement instanceof IChatRoomModel) 
                return ((IChatRoomModel) inputElement).getParticipants();
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
    }
    
    /**
     * Sort the participants lexicographically.
     */
    private static class BuddySorter extends ViewerSorter {
        @Override
        public int compare( Viewer viewer, Object o1, Object o2 ) {
            String s1 = o1.toString();
            String s2 = o2.toString();
            return String.CASE_INSENSITIVE_ORDER.compare( s1, s2 );
        }
    }

    /**
     * The listener to chat room model events.
     */
    private final IChatRoomModelListener chatRoomListener = new ChatRoomModelAdapter() {
        @Override
        public void added( IParticipant participant ) {
            System.out.println( "chatRoomListener.added()" );
            refreshView();
        }

        @Override
        public void removed( IParticipant participant ) {
            System.out.println( "chatRoomListener.removed()" );
            refreshView();
        }

        @Override
        public void changed( IParticipant participant ) {
            System.out.println( "chatRoomListener.changed()" );
            refreshView();
//            refreshParticipant( participant );
        }

        @Override
        public void subjectChanged( String who ) {
            System.out.println( "chatRoomListener.subjectChanged()" );
            final String subject = manager.getService().getModel().getSubject();
            manager.getTalkView().setTitleText( subject );
        }
    };
    
    /** 
     * The tree view presenting the buddies. 
     */
    private TableViewer participantViewer;

    /**
     * The adapter factory.
     * TODO This could be declared in plugin.xml but it currently doesn't work :S So we use it
     * explicetely.
     */
    protected IAdapterFactory adapterFactory;

    /**
     * The multichat object.
     */
    private IMultiChatManager manager;
    
    /**
     * Makes up a new buddy list view.
     */
    public ChatRoomView() {
        super();
        adapterFactory = new ParticipantAdapterFactory();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        participantViewer = new TableViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
        getSite().setSelectionProvider( participantViewer );

        Platform.getAdapterManager().registerAdapters( adapterFactory, IParticipant.class );        
        
        participantViewer.setLabelProvider( new WorkbenchLabelProvider() );
        
        participantViewer.setSorter( new BuddySorter() );

        participantViewer.setContentProvider( new ChatRoomContentProvider() );
        participantViewer.setInput( new ChatRoomModel() );
        
        hookContextMenu();
    }
    
    /**
     * Hook the context menu. Do not ask me how this works since it is copy'n'pasted  ;)
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager( "participantsPopup" ); //$NON-NLS-1$
        menuMgr.add( new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS ) );
        
        Menu menu = menuMgr.createContextMenu( participantViewer.getControl() );
        participantViewer.getControl().setMenu( menu );
        
        // Register viewer with site. This must be done before making the actions.
        getSite().registerContextMenu( menuMgr, participantViewer );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        Platform.getAdapterManager().unregisterAdapters( adapterFactory );        
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        participantViewer.getControl().setFocus();
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
    public void setManager( IMultiChatManager newManager ) {
        // Deregister from previous chat, if there was any
        if (manager != null && manager.getService() != null && manager.getService().getModel()!=null)
            manager.getService().getModel().removeListener( chatRoomListener );
        
        this.manager = newManager;
        if (newManager == null)
            return;

        // Update (sort of ...)
        participantViewer.setInput( manager.getService().getModel() );
        
        manager.getService().getModel().addListener( chatRoomListener );
        refreshView();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.ui.views.IChatRoomView#getChat()
     */
    public IMultiChatManager getManager() {
        return manager;
    }

    /**
     * Refresh the whole view. 
     */
    @SwtAsyncExec
    protected void refreshView() {
    	if (!participantViewer.getControl().isDisposed())
    		participantViewer.refresh();
    }
}