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

import org.eclipse.ui.ISaveablePart;

import it.uniba.di.cdg.xcore.network.events.ITypingEventListener;
import it.uniba.di.cdg.xcore.network.events.ITypingListener;
import it.uniba.di.cdg.xcore.network.messages.IMessage;
import it.uniba.di.cdg.xcore.network.model.tv.Entry;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;

/**
 * Abstracts the functionalities needed for a 1-to-1 or 1-to-N conversation. We need the operations
 * for:
 * <ul>
 * <li>letting the user to insert some text and send it to the remote user(s)</li>
 * <li>provide a mechanism for appending messages (both sent and received)</li>
 * <li>put a read only mode that forbids the user to send text (useful in moderated chats)</li>
 * </ul>
 */
public interface ITalkView extends IActivatableView, ITypingEventListener, ISaveablePart {

    /**
     * All the listeners for input panel events want to implements this.
     */
    public static interface ISendMessagelListener {
        /**
         * Notify clients that the user wants to send a message.
         * 
         * @param message the text message
         */
        void notifySendMessage( String message );
    }
    
    /**
     * Setter injection for model.
     * 
     * @param model
     */
    void setModel( ITalkModel model );
    
    /**
     * Returns the model that this view is displaying ...
     * 
     * @return
     */
    ITalkModel getModel();
    
    /**
     * Set the title for the input panel. Implementations are expected to set one by default, 
     * that can be overridden by this method.
     * 
     * @param title the title for the panel
     */
    void setTitleText( String title );
    
    
    /**
     * Set the title for the input panel. Implementations are expected to set one by default, 
     * that can be overridden by this method.
     * 
     * @param receiver
     */
    void setReceiver( String id );
    
    
    /**
     * Set the title for the input panel. Implementations are expected to set one by default, 
     * that can be overridden by this method.
     * 
     * @param title the title for the panel
     */
    String getReceiver();
    
    /**
     * Add a new listener for panel events.
     * 
     * @param listener
     */
    void addListener( ISendMessagelListener listener );
    
    /**
     * Remove a listener.
     * 
     * @param listener
     */
    void removeListener( ISendMessagelListener listener );

    /**
     * Add a new typing lister.
     * 
     * @param listener
     */
    void addTypingListener( ITypingListener listener );
    
    /**
     * Remove a typing listener.
     * 
     * @param listener
     */
    void removeTypingListener( ITypingListener listener );

   

    /**
     * Append a message to the message board.
     * 
     * @param message
     */
    void appendMessage( IMessage message );

    /**
     * Returns the current text in the talk view.
     * 
     * @return the current chat text
     */
    String getChatLogText();
    
    /**
     * Convenience method for displaying some simple text messages.
     * 
     * @param text
     */
    void appendMessage( Entry entry );
    
    /**
     * Updates the status area with a new, client defined string
     * 
     * @param status the new message to display as status
     */
    void updateStatus( String status ); 
    
    /**
     * Add some kind of separator (i.e. a '------' line).
     * 
     * @param threadId the thread id where the separator must be put
     */
    void putSeparator( String threadId );
    
    void runCallExtension();
}
