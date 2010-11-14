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
package it.uniba.di.cdg.xcore.m2m.ui.actions;

import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Join to an already existing chat room.
 */
public class JoinChatRoomAction implements IWorkbenchWindowActionDelegate {
    /**
     * The unique id of this action.
     */
    public static final String ID = MultiChatPlugin.ID + "ui.actions.joinChatRoomAction";
    
    @SuppressWarnings("unused")
    private IWorkbenchWindow window;

    @Override
    public void dispose() {
    }

    @Override
    public void init( IWorkbenchWindow window ) {
        this.window = window;
    }

    @Override
    public void run( IAction action ) {
        if (NetworkPlugin.getDefault().getHelper().getOnlineBackends().size() == 0) {
            UiPlugin.getUIHelper().showErrorMessage( "Please, connect first!" );
            return;
        }
        Boolean autojoin = true;
        MultiChatPlugin
                .getDefault()
                .getHelper()
                .open( new MultiChatContext( NetworkPlugin.getDefault().getRegistry()
                        .getDefaultBackendId(), "", NetworkPlugin.getDefault().getRegistry()
                        .getDefaultBackend().getUserAccount().getName(), "" ), autojoin );
    }

    @Override
    public void selectionChanged( IAction action, ISelection selection ) {
    }
}
