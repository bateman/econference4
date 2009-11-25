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
package it.uniba.di.cdg.xcore.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */
public class CompletableInputDialog extends Dialog {
    /**
     * The title of the dialog.
     */
    private String title;

    /**
     * The message to display, or <code>null</code> if none.
     */
    private String message;

    /**
     * The input value; the empty string by default.
     */
    private String value = "";//$NON-NLS-1$

    /**
     * The input validator, or <code>null</code> if none.
     */
    private IInputValidator validator;

    /**
     * Ok button widget.
     */
    private Button okButton;

    /**
     * Input text widget.
     */
    private CCombo text;

    /**
     * Error message label widget.
     */
    private Text errorMessageText;

    private String[] completableItems;

    private int selectedItem;

    /**
     * Creates an input dialog with OK and Cancel buttons. Note that the dialog will have no visual
     * representation (no widgets) until it is told to open. <p> Note that the <code>open</code>
     * method blocks for input dialogs. </p>
     * 
     * @param parentShell
     *        the parent shell, or <code>null</code> to create a top-level shell
     * @param dialogTitle
     *        the dialog title, or <code>null</code> if none
     * @param dialogMessage
     *        the dialog message, or <code>null</code> if none
     * @param initialValue
     *        the initial input value, or <code>null</code> if none (equivalent to the empty
     *        string)
     * @param validator
     *        an input validator, or <code>null</code> if none
     */
    public CompletableInputDialog( Shell parentShell, String dialogTitle, String dialogMessage,
            int selectedItem, String[] completableItems, IInputValidator validator ) {
        super( parentShell );
        this.title = dialogTitle;
        this.message = dialogMessage;
        this.completableItems = completableItems;
        this.selectedItem = selectedItem;        
        this.validator = validator;
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected void buttonPressed( int buttonId ) {
        if (buttonId == IDialogConstants.OK_ID) {
            value = text.getText();
        } else {
            value = null;
        }
        super.buttonPressed( buttonId );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell( Shell shell ) {
        super.configureShell( shell );
        if (title != null)
            shell.setText( title );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    protected void createButtonsForButtonBar( Composite parent ) {
        // create OK and Cancel buttons by default
        okButton = createButton( parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true );
        createButton( parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false );
        // do this here because setting the text will set enablement on the ok
        // button
        text.setFocus();
        if (value != null) {
            text.setText( value );
            text.select( 0 );
        }
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected Control createDialogArea( Composite parent ) {
        // create composite
        Composite composite = (Composite) super.createDialogArea( parent );
        // create message
        if (message != null) {
            Label label = new Label( composite, SWT.WRAP );
            label.setText( message );
            GridData data = new GridData( GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
                    | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER );
            data.widthHint = convertHorizontalDLUsToPixels( IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH );
            label.setLayoutData( data );
            label.setFont( parent.getFont() );
        }
        text = new CCombo( composite, SWT.FLAT | SWT.BORDER );
        text.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
                        | GridData.HORIZONTAL_ALIGN_FILL ) );
        text.addModifyListener( new ModifyListener() {
            public void modifyText( ModifyEvent e ) {
                validateInput();
            }
        } );
        errorMessageText = new Text( composite, SWT.READ_ONLY );
        errorMessageText.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL ) );
        errorMessageText.setBackground( errorMessageText.getDisplay().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND ) );

        this.text.setItems( completableItems );
        if (selectedItem > 0) {
            value = completableItems[selectedItem];
            text.select( selectedItem );
        } else
            value = "";
        
        applyDialogFont( composite );
        return composite;
    }

    /**
     * Returns the ok button.
     * 
     * @return the ok button
     */
    protected Button getOkButton() {
        return okButton;
    }

    /**
     * Returns the validator.
     * 
     * @return the validator
     */
    protected IInputValidator getValidator() {
        return validator;
    }

    /**
     * Returns the string typed into this input dialog.
     * 
     * @return the input string
     */
    public String getValue() {
        return value;
    }

    /**
     * Validates the input. <p> The default implementation of this framework method delegates the
     * request to the supplied input validator object; if it finds the input invalid, the error
     * message is displayed in the dialog's message line. This hook method is called whenever the
     * text changes in the input field. </p>
     */
    protected void validateInput() {
        String errorMessage = null;
        if (validator != null) {
            errorMessage = validator.isValid( text.getText() );
        }
        // Bug 16256: important not to treat "" (blank error) the same as null
        // (no error)
        setErrorMessage( errorMessage );
    }

    /**
     * Sets or clears the error message. If not <code>null</code>, the OK button is disabled.
     * 
     * @param errorMessage
     *        the error message, or <code>null</code> to clear
     * @since 3.0
     */
    public void setErrorMessage( String errorMessage ) {
        errorMessageText.setText( errorMessage == null ? "" : errorMessage ); //$NON-NLS-1$
        okButton.setEnabled( errorMessage == null );
        errorMessageText.getParent().update();
    }
}
