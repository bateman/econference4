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
package it.uniba.di.cdg.xcore._1to1.ui;

import it.uniba.di.cdg.xcore._1to1.ChatPlugin;
import it.uniba.di.cdg.xcore.ui.views.BuddyListView;
import it.uniba.di.cdg.xcore.ui.views.TalkView;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Compose a perspective which defines the layout of a chat window. 
 */
public class ChatPerspective implements IPerspectiveFactory {
    /**
     * Perspective id.
     */
    public static final String ID = ChatPlugin.ID + ".ui.chatPerspective"; 

    /** 
     * Left folder's id. 
     */
    public static final String FI_LEFT = ID + ".leftFolder";

    public static final String FI_RIGHT = ID + ".rightFolder";
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout( IPageLayout layout ) {
        String editorAreaId = layout.getEditorArea();
        layout.setEditorAreaVisible( false );
        layout.setFixed( false );
        
//        layout.createFolder( FI_LEFT, IPageLayout.LEFT, 0.3f, editorAreaId );
        layout.createPlaceholderFolder( FI_LEFT, IPageLayout.LEFT, 0.3f, editorAreaId );
        layout.addPlaceholder( BuddyListView.ID + ":*", IPageLayout.LEFT, 0.2f, FI_LEFT );
        layout.addView( BuddyListView.ID, IPageLayout.LEFT, 0.4f, FI_LEFT );

//      layout.createFolder( FI_RIGHT, IPageLayout.RIGHT, 0.3f, editorAreaId );
        layout.createPlaceholderFolder( FI_RIGHT, IPageLayout.RIGHT, 0.3f, editorAreaId );
        layout.addPlaceholder( TalkView.ID + ":*", IPageLayout.TOP, 0.75f, FI_RIGHT );
        
        layout.addPerspectiveShortcut( ID );
    }
}
