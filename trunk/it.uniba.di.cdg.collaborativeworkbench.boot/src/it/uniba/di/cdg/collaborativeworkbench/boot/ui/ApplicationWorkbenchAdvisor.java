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

import it.uniba.di.cdg.xcore.ui.perspectives.InstantMessengerPerspective;
import it.uniba.di.cdg.xcore.ui.wizards.ConfigurationWizard;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Provides workbench configuration like the initial perspective.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	private boolean startWizard = false;
	
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
     */
    @Override
    public void initialize( IWorkbenchConfigurer configurer ) {
        super.initialize( configurer );

        getWorkbenchConfigurer().setSaveAndRestore( true );
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#preStartup()
     */
    @Override
    public void preStartup(){
    	Preferences preferences = new ConfigurationScope().getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
		Preferences pathPref = preferences.node(IConfigurationConstant.PATH);

		if (pathPref.get(IConfigurationConstant.DIR, "").isEmpty()) {
			/* If there isn't preferences, create set default path */
			startWizard = true;
			pathPref.put(IConfigurationConstant.DIR,
				     System.getProperty("user.home")
			         + System.getProperty("file.separator")
			         + ".econference"
			         + System.getProperty("file.separator"));		
			try {
				preferences.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#postStartup()
     */
    @Override
    public void postStartup(){
		if (startWizard) {
			/* If there isn't preferences, run configuration wizard */
			Display display = Display.getDefault();
			Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			WizardDialog wizard = new WizardDialog(shell, new ConfigurationWizard(true));
			wizard.open();
		}
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
     */
    @Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor( configurer );
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
     */
    @Override
	public String getInitialWindowPerspectiveId() {
		return InstantMessengerPerspective.ID;
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#eventLoopException(java.lang.Throwable)
     */
    @Override
    public void eventLoopException( Throwable exception ) {
        exception.printStackTrace();
    }
}
