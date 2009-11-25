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
package it.uniba.di.cdg.jabber.ui;

import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.util.RunnableWithReturn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Dialog for asking the user to create a multichat context.
 * TODO Factor a common base shared with other dialogs too
 */
public class JoinChatRoomDialog extends Dialog {
    /**
     * The actual UI.
     */
    private JoinChatRoomDialogUI ui;

    /**
     * Index in combo -->> backend id.
     */
    private final Map<Integer, String> backends = new HashMap<Integer, String>();
    
    /**
     * The working context.
     */
    private MultiChatContext context;
    
    /**
     * Create a new dialog.
     * 
     * @param parentShell
     */
    public JoinChatRoomDialog( IShellProvider parentShell ) {
        super( parentShell );
    }

    public JoinChatRoomDialog( IShellProvider parentShell, MultiChatContext context ) {
        super( parentShell );
        this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea( Composite parent ) {
        ui = new JoinChatRoomDialogUI( parent, SWT.NONE );
        
        if (initialize()) {
            // XXX Doesn't catch the event (I want to update the server text when the user selects
            // a backend in the combo box)
            ui.connectionCombo.addSelectionListener( new SelectionAdapter() {
                /* (non-Javadoc)
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetDefaultSelected( SelectionEvent e ) {
                    System.out.println( "widgetDefaultSelected()" );
                    String id = backends.get( ui.connectionCombo.getSelectionIndex() );
                    
                    IBackend backend = NetworkPlugin.getDefault().getRegistry().getBackend( id );
                    ui.serverText.setText( String.format( "conference.%s", backend.getServerContext().getServerHost() ) );
                }
            });
            ui.connectionCombo.select( 0 );
        }
        if (context != null)
            setContext( context );
        
        return ui;
    }

    /**
     * Initialize the dialog.
     * 
     * @return <code>true</code> if the dialog state is good to be shown, <code>false</code> otherwise
     */
    protected boolean initialize() {
        RunnableWithReturn r = new RunnableWithReturn() {
            public void run() {
                final INetworkBackendHelper helper = NetworkPlugin.getDefault().getHelper();
                int i = 0;
                List<IBackendDescriptor> descriptors = helper.getOnlineBackends();
                
                for (IBackendDescriptor d : descriptors) {
                    if (helper.isBackendOnline( d.getId() )) {
                        IBackend backend = NetworkPlugin.getDefault().getRegistry().getBackend( d.getId() );
                        
                        ServerContext context = backend.getServerContext();
                        String label = String.format( "%s on %s", d.getName(), context.getServerHost() );
                        
                        ui.connectionCombo.add( label );
                        backends.put( i++, d.getId() );
                    }
                }
                _returnValue = i > 0;
            }
        };
        Display.getCurrent().syncExec( r );
        return ((Boolean) r.getReturnValue()).booleanValue();
    }
    
    /**
     * Fill the fields with the values specified in the context.
     * 
     * @param context
     */
    public void setContext( final MultiChatContext context ) {
//        this.context = context;
//        Display.getCurrent().asyncExec( new Runnable() {
//            public void run() {
                // TODO Select the right connection: for now we just get the first ...
                ui.connectionCombo.select( 0 );
                ui.nickNameText.setText( context.getNickName() );
                ui.roomText.setText( context.getRoom() );
//                ui.serverText.setText( context.getChatServer() );
                ui.passwordText.setText( context.getPassword() );
//            }
//        });
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        String backendId = backends.get( ui.connectionCombo.getSelectionIndex() );

        context = new MultiChatContext( backendId, 
//                ui.serverText.getText(),
                String.format( "%s@%s", ui.roomText.getText(), ui.serverText.getText() ), 
                ui.nickNameText.getText(), 
                ui.passwordText.getText() );
        
        super.okPressed();
    }

    /**
     * Returns the context filled within this dialog.
     * 
     * @return the multichat context
     */
    public MultiChatContext getContext() {
        return context;
    }
}
