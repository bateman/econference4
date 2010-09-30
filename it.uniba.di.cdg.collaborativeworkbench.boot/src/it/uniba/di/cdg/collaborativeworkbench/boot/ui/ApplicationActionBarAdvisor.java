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

import it.uniba.di.cdg.xcore.ui.UiConstants;
import it.uniba.di.cdg.xcore.ui.actions.CollaborativeWorkbenchActionFactory;
import it.uniba.di.cdg.xcore.ui.contribution.OnlineStatusIndicator;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the actions added to
 * a workbench window. Each window will be populated with new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction exitAction;

    private IWorkbenchAction connectAction;
    
    private IWorkbenchAction aboutAction;
    
    private IWorkbenchAction preferencesAction;   
     
    /**
     * We use the save action to save the content of the current talk view: note that since we 
     * are implementing talk views as "ViewPart" and not as "EditorPart" we rely on a feature of 
     * the eclipse platform that is a bit "on the run": see eclipse bug #10234.
     */
    private IWorkbenchAction saveAction;

    private IContributionItem showViewsMenu;

    private IContributionItem switchPerspectivesMenu;
    
    public ApplicationActionBarAdvisor( IActionBarConfigurer configurer ) {
        super( configurer );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
     */
    protected void makeActions( final IWorkbenchWindow window ) {
        exitAction = ActionFactory.QUIT.create( window );
        register( exitAction );

        showViewsMenu = ContributionItemFactory.VIEWS_SHORTLIST.create( window );
        switchPerspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create( window );
        
        aboutAction = ActionFactory.ABOUT.create( window );
        aboutAction.setText("About eConference");
        register( aboutAction );
    
        preferencesAction = ActionFactory.PREFERENCES.create( window );
        register( preferencesAction );
        
        connectAction = CollaborativeWorkbenchActionFactory.CONNECT.create( window );
        register( connectAction );
        
        saveAction = ActionFactory.SAVE.create( window );
        register( saveAction );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
     */
    protected void fillMenuBar( IMenuManager menuBar ) {
        // File menu
        MenuManager fileMenu = new MenuManager( "&File", IWorkbenchActionConstants.M_FILE );
        menuBar.add( fileMenu );
        fileMenu.add( saveAction );
        fileMenu.add( new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS ) );
        fileMenu.add( new Separator() );
        fileMenu.add( exitAction );

        // Workbench menu
        MenuManager workbenchMenu = new MenuManager( "Workbench", UiConstants.M_WORKBENCH );
        menuBar.add( workbenchMenu );

        // Workbench / Switch perspective submenu
        MenuManager perspectivesMenu = new MenuManager( "Switch &perspective", "perspectives" );
        workbenchMenu.add( perspectivesMenu );
        perspectivesMenu.add( switchPerspectivesMenu );

        // Workbench / Show views perspective submenu
        MenuManager viewsMenu = new MenuManager( "Show &view", "views" );
        workbenchMenu.add( viewsMenu );
        viewsMenu.add( showViewsMenu );
        
        workbenchMenu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
        
        // This will ensure that all new menus are added _before_ the "options" and "help" menus
        menuBar.add( new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS ) );

        // Options menu
        MenuManager optionsMenu = new MenuManager( "&Options", UiConstants.M_OPTIONS );
        menuBar.add( optionsMenu );
        optionsMenu.add( connectAction );
        optionsMenu.add( new GroupMarker( IWorkbenchActionConstants.MB_ADDITIONS ) );
        optionsMenu.add( new Separator() );
        optionsMenu.add( preferencesAction );
        
        // Help menu
        MenuManager helpMenu = new MenuManager( "&Help", IWorkbenchActionConstants.M_HELP );
        menuBar.add( helpMenu );
        
        helpMenu.add( aboutAction );
        helpMenu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
     */
    @Override
    protected void fillCoolBar( ICoolBarManager coolBar ) {
        super.fillCoolBar( coolBar );
        
        ToolBarManager connectionBar = new ToolBarManager( coolBar.getStyle() );
        coolBar.add( connectionBar );
        connectionBar.add( connectAction );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillStatusLine(org.eclipse.jface.action.IStatusLineManager)
     */
    @Override
    protected void fillStatusLine( IStatusLineManager statusLine ) {
        super.fillStatusLine( statusLine );

        OnlineStatusIndicator onlineIndicator = new OnlineStatusIndicator();
        // Display the status line indicator by default ...
        onlineIndicator.setVisible( true );
        statusLine.add( onlineIndicator );
    }
    
    protected void fillTrayItem( IMenuManager trayItem ) {
        trayItem.add( aboutAction );
        trayItem.add( exitAction );
    }
}
