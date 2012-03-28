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
package it.uniba.di.cdg.xcore.ui.views;

import java.util.HashMap;

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.model.IBuddyRosterListener;
import it.uniba.di.cdg.xcore.network.model.IEntry;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.actions.NewContactAction;
import it.uniba.di.cdg.xcore.ui.actions.NewGroupAction;
import it.uniba.di.cdg.xcore.ui.actions.ReloadAction;
import it.uniba.di.cdg.xcore.ui.adapters.BuddyAdapterFactory;
import it.uniba.di.cdg.xcore.ui.service.IConfigurationCostantAccount;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
//import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport; //mio
import org.osgi.service.prefs.Preferences;

/**
 * GUI for the buddy list: its shows an hierachical view of the buddies this user account provides.
 * Buddies are organized in groups. Now it even contains the hashmap with buddy email, buddyprofile
 * info.
 * 
 * <p> Actions and context menu-related functionalities mustr be provided by derived classes.
 * Modified to support the buddy info.
 */
public class BuddyListView extends ViewPart {

    // It's possible to extend this hashmap to put
    // additional info associated with the buddy, now we save only his url image
    // but u can add other info that you can retrieve from the account and
    // even choose to save the information retrieved by serializing the hashmap.
    // accessible only with his static methods.
    private static HashMap<String, String> BuddyUrlImage = new HashMap<String, String>();

    private Action addBuddyAction;

    private Action addGroupAction, reloadRosterAction;

    private eventListenerAccount listener = null;

    /**
     * This view unique id.
     */
    public static final String ID = UiPlugin.ID + ".views.buddyListView";

    /**
     * Sort the groups and buddies lexicographically.
     */
    private class BuddySorter extends ViewerSorter {
        @Override
        public int compare( Viewer viewer, Object o1, Object o2 ) {
            int res = 0;

            if (o1 instanceof IBuddy && o2 instanceof IBuddy) {
                IBuddy b1 = (IBuddy) o1;
                IBuddy b2 = (IBuddy) o2;

                if (b1.isNotOffline() && b2.isOffline())
                    res = -1;
                else if (b1.isOffline() && b2.isNotOffline())
                    res = 1;
                else if (b1.isOnline() && b2.isOnline()) {
                    String s1 = o1.toString();
                    String s2 = o2.toString();
                    res = String.CASE_INSENSITIVE_ORDER.compare( s1, s2 );
                } else if (b1.isOffline() && b2.isOffline()) {
                    String s1 = o1.toString();
                    String s2 = o2.toString();
                    res = String.CASE_INSENSITIVE_ORDER.compare( s1, s2 );
                }

            } else {
                if (o1 instanceof IBuddyGroup && o2 instanceof IBuddy) {
                    res = -1;
                } else if (o2 instanceof IBuddyGroup && o1 instanceof IBuddy) {
                    res = 1;
                } else {
                    IBuddyGroup b1 = (IBuddyGroup) o1;
                    IBuddyGroup b2 = (IBuddyGroup) o2;
                    String s1 = b1.getName();
                    String s2 = b2.getName();
                    res = String.CASE_INSENSITIVE_ORDER.compare( s1, s2 );

                }
            }
            return res;
        }
    }

    /**
     * The tree view presenting the buddies.
     */
    private TreeViewer treeViewer;

    /**
     * The adapter factory let us to adapt domain objects to eclipse UI facilities (like context
     * menus).
     */
    private IAdapterFactory adapterFactory;

    /**
     * Listen to roster events.
     */
    private IBuddyRosterListener rosterListener = new IBuddyRosterListener() {
        /*
         * (non-Javadoc)
         * 
         * @see net.osslabs.jabber.client.model.IBuddyRosterListener#presenceChanged
         * (net.osslabs.jabber.client.model.IBuddy)
         */
        @SwtAsyncExec
        public void presenceChanged( final IBuddy buddy ) {

            if (!treeViewer.getControl().isDisposed()) {
                updateBuddy( buddy );
                // treeViewer.refresh(buddy);
                // refresh the whole tree because, if buddy goes offline
                // it'll be moved down, below those online
                treeViewer.refresh();

            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see net.osslabs.jabber.client.model.IBuddyRosterListener#rosterChanged()
         */
        public void rosterChanged() {
            refreshTreeViewer();
        }
    };

    /**
     * Ehmm .. guarda against unexpected disconnections (such as errors ...)
     */

    /**
     * Makes up a new buddy list view.
     */
    public BuddyListView() {
        super();
        adapterFactory = new BuddyAdapterFactory();
        // register the eventlistener
        listener = new eventListenerAccount();
        listener.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets .Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        treeViewer = new TreeViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
        getSite().setSelectionProvider( treeViewer );

        Platform.getAdapterManager().registerAdapters( adapterFactory, IEntry.class );

        ColumnViewerToolTipSupport.enableFor( treeViewer );
        // treeViewer.get
        treeViewer.setLabelProvider( new LabelProviderWithTooltips() );

        treeViewer.setSorter( new BuddySorter() );
        treeViewer.setAutoExpandLevel( TreeViewer.ALL_LEVELS );

        final IBuddyRoster roster = NetworkPlugin.getDefault().getHelper().getRoster();

        roster.addListener( rosterListener );
        // NetworkPlugin.getDefault().getHelper().registerBackendListener(new
        // buddyProfileEventListener());
        treeViewer.setContentProvider( new BaseWorkbenchContentProvider() );
        treeViewer.setInput( roster );
        makeActions();
        contributeToActionBars();
        hookContextMenu();

    }

    /**
     * Hook the context menu. Do not ask me how this works since it is copy'n'pasted ;)
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager( "buddyPopup" ); //$NON-NLS-1$
        menuMgr.add( new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS ) );

        Menu menu = menuMgr.createContextMenu( treeViewer.getControl() );
        treeViewer.getControl().setMenu( menu );

        // Register viewer with site. This must be done before making the
        // actions.
        getSite().registerContextMenu( menuMgr, treeViewer );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        Platform.getAdapterManager().unregisterAdapters( adapterFactory );
        // NetworkPlugin.getDefault().getHelper()
        // .unregisterBackendListener(backendListener);
        // se funge togliamo regstraszione.
        NetworkPlugin.getDefault().getHelper().getRoster().removeListener( rosterListener );
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        treeViewer.getControl().setFocus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ID;
    }

    @SwtAsyncExec
    private void refreshTreeViewer() {
        if (!treeViewer.getControl().isDisposed()) {
            treeViewer.refresh();
        }
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown( bars.getMenuManager() );
        fillLocalToolBar( bars.getToolBarManager() );
    }

    private void fillLocalPullDown( IMenuManager manager ) {
        manager.add( reloadRosterAction );
        manager.add( addBuddyAction );
        manager.add( addGroupAction );

    }

    private void fillLocalToolBar( IToolBarManager manager ) {
        manager.add( reloadRosterAction );
        manager.add( addBuddyAction );
        manager.add( addGroupAction );
    }

    private void makeActions() {
        reloadRosterAction = new Action() {
            public void run() {
                IBuddyRoster roster = NetworkPlugin.getDefault().getHelper().getRoster();
                ReloadAction action = new ReloadAction();
                action.run( roster );
            }
        };
        reloadRosterAction.setText( "Reload roster" );
        reloadRosterAction.setToolTipText( "Force reload" );
        reloadRosterAction.setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_ACTION_RELOAD ) );

        addBuddyAction = new Action() {
            public void run() {
                final IBuddyRoster roster = NetworkPlugin.getDefault().getHelper().getRoster();
                NewContactAction action = new NewContactAction();
                action.run( roster );
            }

        };
        addBuddyAction.setText( "Add Contact" );
        addBuddyAction.setToolTipText( "Add new contact" );
        addBuddyAction.setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_USER_ACTIVE ) );

        addGroupAction = new Action() {
            public void run() {
                final IBuddyRoster roster = NetworkPlugin.getDefault().getHelper().getRoster();
                NewGroupAction action = new NewGroupAction();
                action.run( roster );

            }
        };
        addGroupAction.setText( "Add Group" );
        addGroupAction.setToolTipText( "Add new group" );
        addGroupAction.setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_GROUP ) );

    }

    /**
     * 
     * 
     * @author Francesco Malerba This class register the backend in order to send and read the
     *         special message to implement's a protocol to retrieve buddy info.
     */
    private class eventListenerAccount implements IBackendEventListener {
        public void start() {
            for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry().getDescriptors())
                NetworkPlugin.getDefault().getHelper().registerBackendListener( d.getId(), this );
        }

        public void onBackendEvent( IBackendEvent event ) {
            if (event instanceof ChatExtensionProtocolEvent) {
                IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
                IChatServiceActions chat = b.getChatServiceAction();
                ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent) event;
                // someone ask info about me
                if (cepe.getExtensionName().equals( "GET_PROFILE_INFO" )) {
                    System.out.println( "Received GET_PROFILE_INFO" );
                    HashMap<String, String> param = new HashMap<String, String>();
                    // load MY preferences
                    Preferences preferences = ConfigurationScope.INSTANCE
                            .getNode( IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER );
                    Preferences gmailPref = preferences.node( IConfigurationConstant.GMAIL );
                    if (gmailPref.get( IConfigurationCostantAccount.ACCOUNTMAIL, "" )
                            .equalsIgnoreCase( b.getUserId() )) {
                        System.out
                                .println( "gmail account and account connected are the same, i can send my info" );
                        // send the retrieved info from preferences.
                        param.put( "PROFILE_ID",
                                gmailPref.get( IConfigurationCostantAccount.ACCOUNTID, "" ) );
                        param.put( "IMAGE_URL",
                                gmailPref.get( IConfigurationCostantAccount.IMAGEURL, "" ) );
                        chat.OpenChat( cepe.getFrom() );

                        chat.SendExtensionProtocolMessage( cepe.getFrom(), "PROFILE_INFO", param );
                        chat.CloseChat( cepe.getFrom() );
                        System.out.println( "Info sended to " + cepe.getFrom() );
                    } else {
                        // My account is not the same from the one that is created in configuration
                        // wizard
                    }
                }
                if (cepe.getExtensionName().equals( "PROFILE_INFO" )) { // todo create a static for
                                                                        // the string
                    System.out.println( "Received PROFILE_INFO" );
                    // NOT USED String profileId = (String)
                    // cepe.getExtensionParameter("PROFILE_ID");
                    String profileUrl = (String) cepe.getExtensionParameter( "IMAGE_URL" );
                    // NOT USED String profileType = (String)
                    // cepe.getExtensionParameter("PROFILE_TYPE");

                    BuddyListView.addBuddy( cepe.getFrom(), profileUrl );
                    System.out.println( "Received from Buddy:" + cepe.getFrom() + " imageurl: "
                            + profileUrl );
                    chat.CloseChat( cepe.getFrom() );
                }
            }

            if (event instanceof BackendStatusChangeEvent) {
                refreshTreeViewer();

            }

        }
    };

    /**
     * used to write in the hashmap the buddy email and his image
     * 
     * @param name
     *        buddy email
     * @param url
     *        buddy url
     */

    public static void addBuddy( String name, String url ) {
        BuddyUrlImage.put( name, url );

    }

    /**
     * Method used to see if it's possible to retrieve the buddy account info by asking to him
     * through a cepe message. If his account info are already stored in the hashmap it won't ask
     * the info again. This have to be called every time a Buddy change is status.
     * 
     * @param buddy
     *        the buddy that changed is state, like went online from offline
     * 
     */
    public static void updateBuddy( IBuddy buddy ) {
        if (buddy != null) {
            IBuddy i = (IBuddy) buddy;
            String URLL = BuddyListView.getBuddy( BuddyListView.getBuddyId( buddy ) );
            if (URLL == null) {
                IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();

                IChatServiceActions chat = b.getChatServiceAction();
                chat.OpenChat( i.getId() );
                chat.SendExtensionProtocolMessage( i.getId(), "GET_PROFILE_INFO",
                        new HashMap<String, String>() );
                System.out.println( "Sended request to " + i.getId() );
            } else {
                // System.out.println(buddy.getId() +" is in hasmap , i won't ask again");
            }
        }
    }

    /**
     * 
     * @param buddyid
     *        the buddy email
     * @return the buddy url image
     */
    public static String getBuddy( String buddyid ) {
        if (BuddyUrlImage.get( buddyid ) != null)
            return BuddyUrlImage.get( buddyid );
        else
            return null;
    }

    /**
     * Retrieve the buddy email given the buddy. transform ***@gmail.com/***** in ***@gmail.com
     */
    public static String getBuddyId( IBuddy i ) {
        String buddyId = i.getId();
        int posi = buddyId.indexOf( "/" );
        if (posi != -1)
            return buddyId.substring( 0, posi );
        else
            return buddyId;
    }
}
