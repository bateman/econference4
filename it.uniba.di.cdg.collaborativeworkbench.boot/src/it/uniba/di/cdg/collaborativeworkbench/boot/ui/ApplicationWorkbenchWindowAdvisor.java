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
package it.uniba.di.cdg.collaborativeworkbench.boot.ui;

import it.uniba.di.cdg.collaborativeworkbench.ui.BootPlugin;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IUserStatus;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * Provides configuration for the workbench window and handles the workbench's
 * life cycle.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final int FONT_SIZE_SMALL = 14;

	private static final int FONT_SIZE_BIG = 18;

	private static final String FONT_FAMILY = "Times New Roman";

	// private static final String APP_TITLE = "Collaborative workbench";
	private static final String APP_TITLE = "eConference";

	private TrayItem trayItem;

	private Image trayImage;

	private ApplicationActionBarAdvisor actionBarAdvisor;

	private Image trayNoImage;

	private boolean activeMenu;

	private MenuItem status;

	private Image oval;

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor
	 * (org.eclipse.ui.application.IActionBarConfigurer)
	 */
	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		actionBarAdvisor = new ApplicationActionBarAdvisor(configurer);
		return actionBarAdvisor;
	}

	public boolean preWindowShellClose() {
		final IWorkbenchWindow window = getWindowConfigurer().getWindow();
		window.getShell().setMinimized(true);
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
	 */
	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

		configurer.setInitialSize(new Point(320, 520));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(false);
		configurer.setShowPerspectiveBar(true);
		configurer.setTitle(APP_TITLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		super.postWindowOpen();

		// centerWorkbenchWindow();

		final IWorkbenchWindow window = getWindowConfigurer().getWindow();
		getWindowConfigurer().getActionBarConfigurer();
		trayItem = initTaskItem(window);
		if (trayItem != null) {
			hookPopupMenu(window);
			hookMinimize(window);
		}
		// window.addPageListener(new pag)

		final BackendEventListener daemon = new BackendEventListener();
		daemon.start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {

							// enable or disable popup menu status when the
							// backend is online/offline
							activeMenu = daemon.getActiveMenu();
							status.setEnabled(activeMenu);

							// ... do any work that updates the screen ...
							if (daemon.getIncomingMessage()) {
								// if (!window.getShell().isVisible()) {
								if (!getWindowConfigurer().getWindow()
										.getShell().isFocusControl()) {

									setTrayIconNumber(daemon
											.getNumberOfOngoingChats());

								} else {
									daemon.setNewMessageIncoming(false);
									setTrayIconImage();
									daemon.emptyHistory();

								}
							}
						}
					});
				}
			}
		}).start();
		window.getShell().addShellListener(new ShellAdapter() {

			public void shellActivated(ShellEvent e) {

				daemon.setNewMessageIncoming(false);
				setTrayIconImage();
				daemon.emptyHistory();
			}
		});
	}

	/**
	 * @param window
	 */
	private void hookMinimize(final IWorkbenchWindow window) {
		window.getShell().addShellListener(new ShellAdapter() {
			public void shellIconified(ShellEvent e) {
				// window.getShell().setVisible( false );
			}
		});
		trayItem.addListener(SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Shell shell = window.getShell();
				// if (!shell.isVisible()) {
				shell.setVisible(true);
				window.getShell().setMinimized(false);
				shell.setActive();
				// }
			}

		});
	}

	private void hookPopupMenu(final IWorkbenchWindow window) {
		// Creates a new menu item that terminates the program
		// when selected
		final Menu menu = new Menu(window.getShell(), SWT.POP_UP);
		final MenuItem exit = new MenuItem(menu, SWT.NONE);
		exit.setText("Exit");
		exit.addListener(SWT.Selection, new Listener() {
			private String COMMAND_ID = "it.uniba.di.cdg.xcore.econference.ui.commands.exit";

			public void handleEvent(Event event) {
				// Lets call our command
				IHandlerService handlerService = (IHandlerService) window
						.getService(IHandlerService.class);
				try {
					handlerService.executeCommand(COMMAND_ID, null);
				} catch (Exception ex) {
					throw new RuntimeException(COMMAND_ID);
				}
			}
		});

		// Creates a new menu item that shows the program about
		// when selected
		final MenuItem about = new MenuItem(menu, SWT.NONE);
		about.setText("About");
		about.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				// Lets call our command
				IHandlerService handlerService = (IHandlerService) window
						.getService(IHandlerService.class);
				try {
					handlerService.executeCommand(
							IWorkbenchCommandConstants.HELP_ABOUT, null);
				} catch (Exception ex) {
					throw new RuntimeException(
							IWorkbenchCommandConstants.HELP_ABOUT);
				}
			}
		});

		final String id = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackendId();
		final IBackend backend = NetworkPlugin.getDefault().getRegistry()
				.getBackend(id);
		UiPlugin ui = UiPlugin.getDefault();

		// status cascade-menu
		status = new MenuItem(menu, SWT.CASCADE);
		status.setText("Change Status");

		status.setEnabled(activeMenu);

		// sub menu
		final Menu menu1 = new Menu(status);

		// online
		final MenuItem online = new MenuItem(menu1, SWT.NONE);
		online.setText("Available");
		online.setImage(ui.getImage(IImageResources.ICON_USER_ACTIVE));
		online.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				backend.setUserStatus(IUserStatus.AVAILABLE);
			}
		});

		// away
		final MenuItem away = new MenuItem(menu1, SWT.NONE);
		away.setText("Away");
		away.setImage(ui.getImage(IImageResources.ICON_USER_AWAY));
		away.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				backend.setUserStatus(IUserStatus.AWAY);
			}
		});

		// busy
		final MenuItem busy = new MenuItem(menu1, SWT.NONE);
		busy.setText("Busy");
		busy.setImage(ui.getImage(IImageResources.ICON_USER_BUSY));
		busy.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				backend.setUserStatus(IUserStatus.BUSY);
			}
		});

		// offline
		final MenuItem offline = new MenuItem(menu1, SWT.NONE);
		offline.setText("Offline");
		offline.setImage(ui.getImage(IImageResources.ICON_USER_OFFLINE));
		offline.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				backend.setUserStatus(IUserStatus.OFFLINE);
			}
		});

		status.setMenu(menu1);
		menu1.setVisible(true);

		trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				// We need to make the menu visible
				menu.setVisible(true);
			}
		});
	}

	private TrayItem initTaskItem(IWorkbenchWindow window) {
		final Tray tray = window.getShell().getDisplay().getSystemTray();
		if (tray == null)
			return null;
		TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		trayImage = BootPlugin.getImageDescriptor("icons/collab_tray.gif")
				.createImage();
		trayNoImage = BootPlugin.getImageDescriptor(
				"icons/collab_tray_NewMessage.gif").createImage();
		oval = BootPlugin.getImageDescriptor("icons/oval.gif").createImage();
		trayItem.setImage(trayImage);
		trayItem.setToolTipText("eConference");

		return trayItem;
	}

	private void setTrayIconImage() {
		if (!System.getProperty("os.name").equals("Windows XP"))
			getWindowConfigurer().getWindow().getShell().getDisplay()
					.getSystemTaskBar().getItem(0).setOverlayImage(null);

		trayItem.setImage(trayImage);
	}

	public void setTrayIconNumber(int n) {
		String number;

		Font font = new Font(getWindowConfigurer().getWindow().getShell()
				.getDisplay(), FONT_FAMILY, FONT_SIZE_BIG, SWT.BOLD);
		GC gc = new GC(trayNoImage);
		gc.setForeground(getWindowConfigurer().getWindow().getShell()
				.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		if (n >= 10) {
			font = new Font(getWindowConfigurer().getWindow().getShell()
					.getDisplay(), FONT_FAMILY, FONT_SIZE_SMALL, SWT.BOLD);
			gc.setFont(font);
			number = "9+";
			gc.drawString(number, 10, 8, true);
		} else {
			gc.setFont(font);
			number = Integer.toString(n);
			gc.drawString(number, 12, 8, true);
		}
		gc.dispose();
		if (!System.getProperty("os.name").equals("Windows XP"))
			getWindowConfigurer().getWindow().getShell().getDisplay()
					.getSystemTaskBar().getItem(0).setOverlayImage(null);

		trayItem.setImage(trayNoImage);

		GC gc1 = new GC(oval);
		gc1.setFont(font);
		gc1.setForeground(getWindowConfigurer().getWindow().getShell()
				.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		if (n >= 10)
			gc1.drawString(number, 10, 9, true);
		else
			gc1.drawString(number, 12, 6, true);
		gc1.dispose();
		if (!System.getProperty("os.name").equals("Windows XP"))
			getWindowConfigurer().getWindow().getShell().getDisplay()
					.getSystemTaskBar().getItem(0).setOverlayImage(oval);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();

		if (trayItem != null) {
			trayImage.dispose();
			trayItem.dispose();
		}
	}
}
