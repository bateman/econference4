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
package it.uniba.di.cdg.xcore.m2m.ui.actions.popup;

import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.ui.actions.AbstractBuddyActionDelegate;

import org.eclipse.jface.action.IAction;

/**
 * Let the user to create a multichat with the selected buddies. This action attaches itself to 
 * the buddy list view, NOT the participants view.
 * TODO Implement this action
 */
public class NewMultiChatAction extends AbstractBuddyActionDelegate {
    /**
     * Action id. 
     */
    public static final String ID = MultiChatPlugin.ID + ".ui.actions.popup.newMultiChatAction";

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run( IAction action ) {
//        System.out.println( "run()" );
//        JoinChatRoomDialog dlg = new JoinChatRoomDialog( getTargetPart().getSite() );
//        if (!dlg.initialize()) {
//            UiPlugin.getUIHelper().showErrorMessage( "Please, connect some backend first." );
//            return;
//        }
//
//        int result = dlg.open();
//        if (Dialog.OK == result) {
//            MultiChatContext context = dlg.getContext();
//            context.setInvitees( getSelectedBuddiesId() );
//            
//            MultiChatPlugin.getDefault().openMultiChat( context );
//        }
    }
}
