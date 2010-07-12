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

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

/**
 * GUI for the buddy list: its shows an hierachical view of the buddies this
 * user account provides. Buddies are organized in groups.
 * <p>
 * Actions and context menu-related functionalities mustr be provided by derived
 * classes.
 */
public class BuddyListView extends ViewPart {

	private Action addBuddyAction;
	private Action addGroupAction, reloadRosterAction;

	/**
	 * This view unique id.
	 */
	public static final String ID = UiPlugin.ID + ".views.buddyListView";

	/**
	 * Sort the groups and buddies lexicographically. 
	 */
	private class BuddySorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object o1, Object o2) {
			int res;

			if (o1 instanceof IBuddy && o2 instanceof IBuddy) {

				IBuddy b1 = (IBuddy) o1;
				IBuddy b2 = (IBuddy) o2;
				if (b1.isNotOffline() && b2.isOffline())
					res = -1;
				else if (b1.isOffline() && b2.isNotOffline())
					res = 1;
				else { // both not offline
					String s1 = o1.toString();
					String s2 = o2.toString();
					res = String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
				}
			} else { // at least one is a buddygroup
				if (o1 instanceof IBuddyGroup && o2 instanceof IBuddy) {
					res = -1;
				} else if (o2 instanceof IBuddyGroup && o1 instanceof IBuddy) {
					res = 1;
				} else { // both are groups
					String s1 = o1.toString();
					String s2 = o2.toString();
					res = String.CASE_INSENSITIVE_ORDER.compare(s1, s2);					
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
	 * The adapter factory let us to adapt domain objects to eclipse UI
	 * facilities (like context menus).
	 */
	private IAdapterFactory adapterFactory;

	/**
	 * Listen to roster events.
	 */
	private IBuddyRosterListener rosterListener = new IBuddyRosterListener() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * net.osslabs.jabber.client.model.IBuddyRosterListener#presenceChanged
		 * (net.osslabs.jabber.client.model.IBuddy)
		 */
		@SwtAsyncExec
		public void presenceChanged(final IBuddy buddy) {
			if (!treeViewer.getControl().isDisposed())
				//treeViewer.refresh(buddy);
				// refresh the whole tree because, if buddy goes offline
				// it'll be moved down, below those online
				treeViewer.refresh();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * net.osslabs.jabber.client.model.IBuddyRosterListener#rosterChanged()
		 */
		public void rosterChanged() {
			refreshTreeViewer();
		}
	};

	/**
	 * Ehmm .. guarda against unexpected disconnections (such as errors ...)
	 */
	private IBackendEventListener backendListener = new IBackendEventListener() {
		public void onBackendEvent(IBackendEvent event) {
			if (event instanceof BackendStatusChangeEvent) {
				refreshTreeViewer();
			}
		}
	};

	/**
	 * Makes up a new buddy list view.
	 */
	public BuddyListView() {
		super();
		adapterFactory = new BuddyAdapterFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		getSite().setSelectionProvider(treeViewer);

		Platform.getAdapterManager().registerAdapters(adapterFactory,
				IEntry.class);

		treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		treeViewer.setSorter(new BuddySorter());
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		NetworkPlugin.getDefault().getHelper()
				.registerBackendListener(backendListener);

		final IBuddyRoster roster = NetworkPlugin.getDefault().getHelper()
				.getRoster();

		roster.addListener(rosterListener);
		treeViewer.setContentProvider(new BaseWorkbenchContentProvider());
		treeViewer.setInput(roster);
		makeActions();
		contributeToActionBars();
		hookContextMenu();
	}

	/**
	 * Hook the context menu. Do not ask me how this works since it is
	 * copy'n'pasted ;)
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("buddyPopup"); //$NON-NLS-1$
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		// Register viewer with site. This must be done before making the
		// actions.
		getSite().registerContextMenu(menuMgr, treeViewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		Platform.getAdapterManager().unregisterAdapters(adapterFactory);

		NetworkPlugin.getDefault().getHelper()
				.unregisterBackendListener(backendListener);
		NetworkPlugin.getDefault().getHelper().getRoster()
				.removeListener(rosterListener);
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
		if (!treeViewer.getControl().isDisposed())
			treeViewer.refresh();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(reloadRosterAction);
		manager.add(addBuddyAction);
		manager.add(addGroupAction);

	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(reloadRosterAction);
		manager.add(addBuddyAction);
		manager.add(addGroupAction);
	}

	private void makeActions() {
		reloadRosterAction = new Action() {
			public void run() {
				IBuddyRoster roster = NetworkPlugin.getDefault().getHelper()
						.getRoster();
				ReloadAction action = new ReloadAction();
				action.run(roster);
			}
		};
		reloadRosterAction.setText("Reload roster");
		reloadRosterAction.setToolTipText("Force reload");
		reloadRosterAction.setImageDescriptor(UiPlugin.getDefault()
				.getImageDescriptor(IImageResources.ICON_ACTION_RELOAD));

		addBuddyAction = new Action() {
			public void run() {
				final IBuddyRoster roster = NetworkPlugin.getDefault()
						.getHelper().getRoster();
				NewContactAction action = new NewContactAction();
				action.run(roster);

			}
		};
		addBuddyAction.setText("Add Contact");
		addBuddyAction.setToolTipText("Add new contact");
		addBuddyAction.setImageDescriptor(UiPlugin.getDefault()
				.getImageDescriptor(IImageResources.ICON_USER_ACTIVE));

		addGroupAction = new Action() {
			public void run() {
				final IBuddyRoster roster = NetworkPlugin.getDefault()
						.getHelper().getRoster();
				NewGroupAction action = new NewGroupAction();
				action.run(roster);

			}
		};
		addGroupAction.setText("Add Group");
		addGroupAction.setToolTipText("Add new group");
		addGroupAction.setImageDescriptor(UiPlugin.getDefault()
				.getImageDescriptor(IImageResources.ICON_GROUP));

	}
}