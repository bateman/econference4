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
package it.uniba.di.cdg.xcore.econference.model;

import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;

/**
 * A conference model extends the usual chat room model by adding an item list and a status.
 */
public interface IConferenceModel extends IChatRoomModel {
    /**
     * The states of a conference.
     */
    public enum ConferenceStatus { STARTED, STOPPED }

    /**
     * Change the status for this conference.
     * 
     * @param status
     */
    void setStatus( ConferenceStatus status );
    
    /**
     * Returns the current status.
     * 
     * @return the conference status
     */
    ConferenceStatus getStatus();
 
    /**
     * Returns the item list for this conference.
     * 
     * @return
     */
    IItemList getItemList();

    /**
     * Replace current item list with a new one.
     * 
     * @param itemList The itemList to set.
     */
    void setItemList( IItemList itemList );
    
    /**
     * Returns the whiteboard text.
     * 
     * @return the whiteboard texts
     */
    String getWhiteBoardText();
       
    /**
     * Change the whiteboard text.
     *
     * @param text 
     */
    void setWhiteBoardText( String text );    
}
