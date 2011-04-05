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
package it.uniba.di.cdg.xcore.ui.internal;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.aspects.SwtSyncExec;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.dialogs.CompletableInputDialog;
import it.uniba.di.cdg.xcore.ui.dialogs.UserInputsProviderDialog;
import it.uniba.di.cdg.xcore.ui.util.NotEmptyStringValidator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

/**
 * 
 */
public class UIHelper implements IUIHelper {

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#showMessage(java.lang.String, java.lang.String)
     */
    @SwtSyncExec
    public void showMessage( String windowTitle, String message ) {
        MessageDialog.openInformation( getShell(), windowTitle, message );
        //        System.out.println( message );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#showErrorMessage(java.lang.String, java.lang.String)
     */
    @SwtSyncExec
    public void showErrorMessage( String windowTitle, String errorMessage ) {
        MessageDialog.openError( getShell(), windowTitle, errorMessage );
        //        System.err.println( errorMessage );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#showMessage(java.lang.String)
     */
    public void showMessage( String message ) {
        showMessage( DEFAULT_MESSAGE_WINDOW_TITLE, message );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#showErrorMessage(java.lang.String)
     */
    public void showErrorMessage( String errorMessage ) {
        showErrorMessage( DEFAULT_ERROR_WINDOW_TITLE, errorMessage );
    }

    /**
     * Retrives the active shell object.
     * 
     * @return the active shell
     */
    protected Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#askFreeQuestion(java.lang.String, java.lang.String)
     */
    public String askFreeQuestion( final String question, final String initialValue ) {
        return askFreeQuestion( "Question", question, initialValue );
    }

    @SwtSyncExec
    public String askFreeQuestion( final String title, final String question, final String initialValue ) {
        InputDialog input = new InputDialog( getShell(), title, question, initialValue,
                new NotEmptyStringValidator() );
        if (Dialog.OK == input.open())
            return input.getValue();
        else
            return null;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#askYesNoQuestion(java.lang.String)
     */
    @SwtSyncExec
    public boolean askYesNoQuestion(String title, String question ) {
        return MessageDialog.openQuestion( getShell(), title, question );        
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#switchPerspective(java.lang.String)
     */
    @SwtSyncExec
    public void switchPerspective( final String perspectiveId ) {
        try {
            PlatformUI.getWorkbench().showPerspective( perspectiveId,
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow() );
        } catch (WorkbenchException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#closeCurrentPerspective()
     */
    @SwtSyncExec
    public void closeCurrentPerspective() {
        // Close this perspective since it is unuseful ...
        // Avoid NPE when closing the application without closing the perspective first  ...
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage();
        if (activePage == null)
            return;

        IPerspectiveDescriptor pd = activePage.getPerspective();
        System.out.println( "Closing perspective: " + pd.getId() );

        activePage.closePerspective( pd, false, false );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#closePerspective(java.lang.String)
     */
    @SwtSyncExec
    public void closePerspective( String perspectiveId ) {
        IPerspectiveDescriptor pd = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId( perspectiveId );
        if (pd == null) {
            System.err.println( "Wanna close unknown perspective " + perspectiveId );
            return;
        }
            
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null)
        	return;
        
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        if (activePage == null)
            return;
        
        activePage.closePerspective( pd, true, false );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#requestFile(java.lang.String[])
     */
    @SwtSyncExec
    public String requestFile( String[] fileExtensions, String filePath ) {
        FileDialog fileDialog = new FileDialog( getShell(), SWT.OPEN );
        fileDialog.setText("Open File");
        fileDialog.setFilterExtensions( fileExtensions );
        fileDialog.setFilterPath(filePath);

        return fileDialog.open();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#requestFileNameForSaving(java.lang.String[])
     */
    @SwtSyncExec
    public String requestFileNameForSaving( String... fileExtensions ) {
        FileDialog fileDialog = new FileDialog( getShell(), SWT.SAVE );
        fileDialog.setText("Save File");
        fileDialog.setFilterExtensions( fileExtensions );

        return fileDialog.open();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#findView(java.lang.String)
     */
    public IViewPart findView( String viewId ) {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                viewId );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.IUIHelper#askChoice(java.lang.String, java.lang.String, int, java.lang.String[])
     */
    public String askChoice( String title, String question, int selectedItem, String[] completionItems ) {
        CompletableInputDialog input = new CompletableInputDialog( getShell(), title, question, 
                selectedItem,
                completionItems,
                new NotEmptyStringValidator() );
        if (Dialog.OK == input.open())
            return input.getValue();
        else
            return null;
    }

	@Override
	public String[] askUserInput(String title, String question,
			String[] userInputs, String[] initialValue, IInputValidator[] validators) {
		UserInputsProviderDialog input = new UserInputsProviderDialog(getShell(), title, question, 
				userInputs, initialValue, validators);
		 if (Dialog.OK == input.open())
	            return input.getValues();
	        else
	            return null;
	}
}
