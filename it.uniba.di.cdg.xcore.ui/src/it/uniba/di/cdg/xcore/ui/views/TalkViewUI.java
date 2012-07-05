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

import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.formatter.EmailFormatter;
import it.uniba.di.cdg.xcore.ui.formatter.LinkFormatter;
import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;
import it.uniba.di.cdg.xcore.ui.widget.EntryRichStyledText;
import it.uniba.di.cdg.xcore.ui.widget.RichStyledText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * The GUI of the talking with remote user(s) (a message board and an input area).
 * 
 */
public class TalkViewUI extends ViewPart {
    private Composite top = null;

    private SashForm sashForm = null;

    protected EntryRichStyledText messageBoardText = null;

    private Composite inputTextComposite = null;
    
    private Composite sendComposite = null;

    protected RichStyledText userInputText = null;

    protected Button sendButton = null;

    private Composite bottomComposite = null;

    protected CLabel statusLabel = null;
    
    protected Composite callComposite = null;

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        top = new Composite( parent, SWT.NONE );
        top.setLayout( new FillLayout() );
        createSashForm();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        userInputText.setFocus();
        userInputText.getShell().setDefaultButton( sendButton );
    }

    /**
     * This method initializes the sashform container.
     */
    private void createSashForm() {
        sashForm = new SashForm( top, SWT.NONE );
        sashForm.setOrientation( org.eclipse.swt.SWT.VERTICAL );
        messageBoardText = new EntryRichStyledText(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        messageBoardText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        messageBoardText.setEditable( false );
        // Adding formatting listener
        messageBoardText.addFormatListener( new LinkFormatter() );
        messageBoardText.addFormatListener( new RichFormatting() );
        messageBoardText.addFormatListener( new EmailFormatter() );
        
        createComposite1();
    }
    
    /**
     * This method initializes composite.
     */
    private void createComposite() {
    	GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
    	GridData gridData1 = new org.eclipse.swt.layout.GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        
        callComposite = new Composite( bottomComposite, SWT.NONE );
		callComposite.setLayout( gridLayout );
		callComposite.setLayoutData(gridData2);
		
        inputTextComposite = new Composite( bottomComposite, SWT.NONE );
        inputTextComposite.setLayout( gridLayout );
        inputTextComposite.setLayoutData( gridData1 );
        userInputText = new RichStyledText(inputTextComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        userInputText.setLayoutData( gridData );
        
        userInputText.addFormatListener( new LinkFormatter() );
        userInputText.addFormatListener( new EmailFormatter() );
        userInputText.addFormatListener( new RichFormatting() );
        
        
        
        createHelpLabel();

        sendButton = new Button( sendComposite, SWT.NONE );
        sendButton.setText( "Send" );
    }
    
    private void createHelpLabel() {
        RowLayout sendLayout = new RowLayout();
        sendLayout.type = SWT.VERTICAL;
        sendLayout.justify = true;
        sendLayout.pack = true;
        sendLayout.center = true;
        sendLayout.fill = true;
        sendComposite = new Composite( inputTextComposite, SWT.NONE );
        sendComposite.setLayout( sendLayout );

        final CLabel helpLabel = new CLabel( sendComposite, SWT.NONE );
        final Image onlineImg = UiPlugin.getDefault().getImage( IImageResources.ICON_HELP );
        helpLabel.setToolTipText( "*bold* (Ctrl+B)\n_underline_  (Ctrl+U)\n-strike-  (Ctrl+H)");
        helpLabel.setImage( onlineImg );
    }

    /**
     * This method initializes composite1	
     *
     */
    private void createComposite1() {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.horizontalSpacing = 1;
        gridLayout1.marginWidth = 0;
        gridLayout1.marginHeight = 1;
        gridLayout1.verticalSpacing = 1;
        GridData gridData2 = new org.eclipse.swt.layout.GridData();
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        bottomComposite = new Composite( sashForm, SWT.NONE );
        createComposite();
        bottomComposite.setLayout( gridLayout1 );
        statusLabel = new CLabel( bottomComposite, SWT.NONE );
        statusLabel.setLayoutData( gridData2 );
    }

    /**
     * Scroll to the bottom of the messageBoard
     */
    // TODO Bug 53: https://cde.di.uniba.it/tracker/index.php?func=detail&aid=53&group_id=9&atid=138
    protected void scrollToEnd() {
        int n = messageBoardText.getCharCount();
        messageBoardText.setSelection( n, n );
        messageBoardText.showSelection();
    }
    
    /**
     * This method applies the formatting style that corresponds to the string passed as parameter
     * 
     * @param s a String that rappresents a formatting style
     */
    public  void applyFormatting( String s ) {
        Point sel = userInputText.getSelectionRange();
        String new_text = s + userInputText.getSelectionText() + s;

        if ((sel == null) || (sel.y == 0))
            return;

        userInputText.replaceTextRange( sel.x, sel.y, new_text );
    }

} // @jve:decl-index=0:visual-constraint="54,58"
