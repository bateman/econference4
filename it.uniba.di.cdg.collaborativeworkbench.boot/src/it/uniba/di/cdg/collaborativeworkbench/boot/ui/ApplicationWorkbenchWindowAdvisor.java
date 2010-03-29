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

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Provides configuration for the workbench window and handles the workbench's life cycle.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    //private static final String APP_TITLE = "Collaborative workbench";
    private static final String APP_TITLE = "eConference";
    
    private TrayItem trayItem;
    
    private Image trayImage;
    
    private ApplicationActionBarAdvisor actionBarAdvisor;

	private boolean trayIconImage;

	private Image trayNoImage;
    
    public ApplicationWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer ) {
        super( configurer );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    @Override
    public ActionBarAdvisor createActionBarAdvisor( IActionBarConfigurer configurer ) {
        actionBarAdvisor = new ApplicationActionBarAdvisor( configurer );
        return actionBarAdvisor;
    }
    public boolean preWindowShellClose() {
    	final IWorkbenchWindow window = getWindowConfigurer().getWindow();
    	window.getShell().setMinimized(true);
		return false;
    	
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
     */
    @Override
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        
        configurer.setInitialSize( new Point( 320, 520 ) );
        configurer.setShowCoolBar( true );
        configurer.setShowStatusLine( true );
        configurer.setShowProgressIndicator( false );
        configurer.setShowPerspectiveBar( true );
        configurer.setTitle( APP_TITLE );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
     */
    @Override
    public void postWindowOpen() {
        super.postWindowOpen();
        
        //load chatPlugIn

        //try {
		//	Platform.getBundle("it.uniba.di.cdg.xcore.one2one").start();
		//} catch (BundleException e) {
		//	e.printStackTrace();
		//}

//        centerWorkbenchWindow();
        
        final IWorkbenchWindow window = getWindowConfigurer().getWindow();
        getWindowConfigurer().getActionBarConfigurer();
        trayItem = initTaskItem( window );
        if (trayItem != null) {
            hookPopupMenu( window );
            hookMinimize( window );
        }
        //window.addPageListener(new pag)
        
        
        final BackendEventListener daemon = new BackendEventListener();
        daemon.start();
        
        new Thread(new Runnable() {
            public void run() {
               while (true) {
                  try { Thread.sleep(1000); } catch (Exception e) { }
                  Display.getDefault().asyncExec(new Runnable() {
                     public void run() {
                        //... do any work that updates the screen ...
                    	 if (daemon.getMessageIncoming()) {
                    		 //if (!window.getShell().isVisible()) {
                    			if (!window.getShell().isFocusControl()) {
                    				 if (trayIconImage) {
                    					 setTrayIconNoImage();
                    				 }
                    				 else {
                    					 setTrayIconImage();
                    				 }
                    			 
                    		 } else {
                    			 daemon.setNewMessageIncoming(false);
                    			 setTrayIconImage();
                    		 }
                    	 }
                     }
                  });
               }
            }
         }).start();
        
        
        
        window.getShell().addShellListener(new ShellAdapter(){


        	public void shellActivated(ShellEvent e) {

        		daemon.setNewMessageIncoming(false);
   			 	setTrayIconImage();
        	}
        	});

     
        
    }

    /**
     * @param window
     */
    private void hookMinimize( final IWorkbenchWindow window ) {
        window.getShell().addShellListener( new ShellAdapter() {
            public void shellIconified( ShellEvent e ) {
                //window.getShell().setVisible( false );
            }
        });
        trayItem.addListener( SWT.DefaultSelection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				Shell shell = window.getShell();
               // if (!shell.isVisible()) {
                    shell.setVisible( true );
                    window.getShell().setMinimized( false );
                    shell.setActive();
               // }
			}
            
        });
    }

    private void hookPopupMenu( final IWorkbenchWindow window ) {
        trayItem.addListener( SWT.MenuDetect, new Listener() {
            public void handleEvent( Event event ) {
                MenuManager trayMenu = new MenuManager();
                Menu menu = trayMenu.createContextMenu( window.getShell() );
                actionBarAdvisor.fillTrayItem( trayMenu );
                menu.setVisible( true );
            }
        });
    }

    private TrayItem initTaskItem( IWorkbenchWindow window ) {
        final Tray tray = window.getShell().getDisplay().getSystemTray();
        if (tray == null)
            return null;
        TrayItem trayItem = new TrayItem( tray, SWT.NONE );
        trayImage = BootPlugin.getImageDescriptor( "icons/collab_tray.gif" ).createImage();
        trayNoImage = BootPlugin.getImageDescriptor( "icons/collab_tray_NewMessage.gif" ).createImage();
        
        trayItem.setImage( trayImage );
        trayIconImage=true;
        //setTrayIconImage();
        trayItem.setToolTipText( "eConference" );
        
        return trayItem;
    }
    private void setTrayIconImage() {
    	//trayImage = BootPlugin.getImageDescriptor( "icons/collab_tray.gif" ).createImage();
        trayItem.setImage( trayImage );
        trayIconImage=true;
    }
    public void setTrayIconNoImage() {
    	
        trayItem.setImage(trayNoImage);
        trayIconImage=false;
    }
//    private void centerWorkbenchWindow() {
//        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
//        
//        Display display = configurer.getWindow().getWorkbench().getDisplay();
//        Shell shell = configurer.getWindow().getShell();
//
//        // Get the resolution
//        Rectangle pDisplayBounds = display.getBounds();
//
//        int nMinWidth = 260;
//        int nMinHeight = 520;
        
        // Set Width and Height(doesn't matter value)
//        int nWidth = pDisplayBounds.width / 2;
//        int nHeight = pDisplayBounds.height / 2;

        // This formulae calculate the shell's Left ant Top
//        int nLeft = (pDisplayBounds.width - nMinWidth) / 2;
//        int nTop = (pDisplayBounds.height - nMinHeight) / 2;
//
//        // Set shell bounds,
//        shell.setBounds( nLeft, nTop, nMinWidth, nMinHeight );
//    }

    /* (non-Javadoc)
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
