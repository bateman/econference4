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
package it.uniba.di.cdg.xcore.ui;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.ui.IViewPart;

/**
 * User Interface helper methods (for displaying messages, errors, ...). 
 */
public interface IUIHelper {
    public static final String DEFAULT_MESSAGE_WINDOW_TITLE = "Message"; 
    public static final String DEFAULT_ERROR_WINDOW_TITLE = "Error"; 

    /**
     * Show a messagebox with an error message.
     * 
     * @param windowTitle
     * @param message
     */
    void showMessage( String windowTitle, String message );
    
    /**
     * Show an informative message box.
     * 
     * @param windowTitle
     * @param errorMessage
     */
    void showErrorMessage( String windowTitle, String errorMessage );

    /**
     * Convenience method that requires only the message. 
     * {@see #showMessage(String, String)}
     * 
     * @param message
     */
    void showMessage( String message );
    
    /**
     * Convenience method that requires only the message. 
     * {@see #showErrorMessage(String, String)}
	 *
     * @param errorMessage     
     * 
     */
    void showErrorMessage( String errorMessage );
    
    /**
     * Searches for the specified view.
     * 
     * @param viewId
     * @return the view or <code>null</code> if the id is unknown
     */
    IViewPart findView( String viewId );
    
    /**
     * Convenience method which uses the default "Question" text as dialog title.
     * 
     * @param question the question to present to the user
     * @param initialValue the initial text, if any
     * @return the text or <code>null</code> if the user pressed 'CANCEL' instead
     */
    String askFreeQuestion( String question, String initialValue );

    /**
     * Ask the user to type some text.
     * 
     * @param title the window title 
     * @param question the question to present to the user
     * @param initialValue the initial text, if any
     * @return the text or <code>null</code> if the user pressed 'CANCEL' instead
     */
    String askFreeQuestion( final String title, final String question, final String initialValue );


    /**
     * Ask the user to type many text
     * 
     * @param title the window title 
     * @param question the question to present to the user
     * @param userInputs the string array representing labels of fields to show to the user
     * @param initialValues the initial values of the fields corresponding to the user
     * Inputs
     * @param validators the validator array for each user input
     * 
     * @return all the user inputs or <code>null</code> if the user pressed 'CANCEL' instead
     */
    String[] askUserInput( final String title, final String question, String[] userInputs, String[] initialValues,
    		IInputValidator[] validators );
    
    String askChoice( final String title, final String question, final int selectedItem, String[] completionItems );
    
    /**
     * Ask the user for a Yes-No confirmation
     * 
     * @param question  the question to show to the user
     * @param title the message dialog title
     * @return <code>true</code>, if Yes pressed; <code>false</code> if No pressed
     */
    boolean askYesNoQuestion (String title, String question );
    
    /**
     * Show an open file dialog for picking up a single file matching the specified extensions.
     * 
     * @param fileExtensions extensions to accept (i.e. "*.ecx", "*.*" ) 
     * @param filePath 
     * @return the absolute path file or <code>null</code> if no file was selected
     */
    String requestFile( String[] fileExtensions, String filePath);

    /**
     * Show a "save as" dialog for inputting a filename for saving.
     * 
     * @param fileExtensions
     * @return the user selected file name
     */
    String requestFileNameForSaving( String... fileExtensions );

    /**
     * Change focus of the current perspective to another one. If the specified perspective isn't 
     * shown, than it will be shown and given focus.
     * 
     * @param perspectiveId
     */
    void switchPerspective( String perspectiveId );
    
    /**
     * Close the currently active perspective.
     */
    void closeCurrentPerspective();

    /**
     * Close the specified perspective.
     * 
     * @param perspectiveId
     */
    public void closePerspective( String perspectiveId );
}
