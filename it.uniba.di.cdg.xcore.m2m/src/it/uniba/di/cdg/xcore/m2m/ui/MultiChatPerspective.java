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
package it.uniba.di.cdg.xcore.m2m.ui;

import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.ui.views.ChatRoomView;
import it.uniba.di.cdg.xcore.m2m.ui.views.MultiChatTalkView;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The layout of a multichat perspective: this includes a list view containing the user list,
 * an input panel for sending / receiving messages and a message board for displaying the results.
 */
public class MultiChatPerspective implements IPerspectiveFactory {
    /**
     * Perspective id.
     */
    public static final String ID = MultiChatPlugin.ID + ".ui.multiChatPerspective"; 
    
    /** 
     * Bottom folder's id. 
     */
    public static final String FI_RIGHT = ID + ".rightFolder";

    /** 
     * Left folder's id. 
     */
    public static final String FI_LEFT = ID + ".leftFolder";

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout( IPageLayout layout ) {
        String editorAreaId = layout.getEditorArea();
        layout.setEditorAreaVisible( false );
        layout.setFixed( true );

        layout.createPlaceholderFolder( FI_LEFT, IPageLayout.LEFT, 0.30f, editorAreaId );
        layout.createPlaceholderFolder( FI_RIGHT, IPageLayout.RIGHT, 0.60f, editorAreaId );
        
        layout.addPlaceholder( ChatRoomView.ID + ":*", IPageLayout.LEFT, 0.2f, FI_LEFT );
        layout.addView( ChatRoomView.ID, IPageLayout.LEFT, 0.4f, FI_LEFT );
        layout.addShowViewShortcut( ChatRoomView.ID );

        layout.addPlaceholder( MultiChatTalkView.ID /*+ ":*"*/, IPageLayout.RIGHT, 0.2f, FI_RIGHT );
        
        layout.addPerspectiveShortcut( ID );
    }
}
