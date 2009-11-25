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
package it.uniba.di.cdg.xcore.econference.ui.views;

import org.eclipse.ui.ISaveablePart;

import it.uniba.di.cdg.xcore.econference.IEConferenceManager;

/**
 * Interface for the whiteboard. A whiteboard give a place for a particular user (called
 * a "scribe") to write some free form text and to send it to all conference's participants.
 * <p>
 * For all other users, the whiteboard is read-only and the manager will take care about putting
 * it in read-only state.  
 */
public interface IWhiteBoard extends ISaveablePart {
    /**
     * Returns the current text in the whiteboard.
     * 
     * @return the whiteboard text
     */
    String getText();
    
    /**
     * Changes the text on the whiteboard, replacing the previous one.
     * 
     * @param text
     */
    void setText( String text );
    
    /**
     * Put this whiteboard in read-only (unmodifiable) mode. This is usually performed by the
     * manager.
     * 
     * @param readOnly
     */
    void setReadOnly( boolean readOnly );
    
    /**
     * Check whether this whiteboard is read-only or not.
     * 
     * @return <code>true</code> if the local user can change the text, <code>false</code> otherwise
     */
    boolean isReadOnly();
    
    /**
     * Set the manger: this methods is used for dependency injection by the manager itself
     * and should not be used by clients.
     *  
     * @param manager
     */
    void setManager( IEConferenceManager manager );
    
    /**
     * Returns the current manager for this whiteboard.
     * 
     * @return the conference manager
     */
    IEConferenceManager getManager();
}
