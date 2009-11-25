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

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import it.uniba.di.cdg.xcore.aspects.ThreadSafetyAspect;
import it.uniba.di.cdg.xcore.econference.model.internal.ItemList;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModelListener;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.econference.model.IConferenceModel}. 
 */
public class ConferenceModel extends ChatRoomModel implements IConferenceModel {
    /**
     * The discussion item list.
     */
    private IItemList itemList;

    /**
     * Current conference status.
     */
    private ConferenceStatus status;
    
    /**
     * The whiteboard text: initially empty.
     */
    private String whiteBoardText;
    
    /**
     * Change the conference model.
     */
    public ConferenceModel() {
        super();
        this.status = ConferenceStatus.STOPPED;
        this.itemList = new ItemList();
        this.whiteBoardText = "";
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IConferenceModel#getStatus()
     */
    public ConferenceStatus getStatus() {
        return status;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IConferenceModel#setStatus(it.uniba.di.cdg.xcore.econference.ConferenceStatus)
     */
    public void setStatus( ConferenceStatus status ) {
        this.status = status;
        // Notify clients ...
        for (IChatRoomModelListener l : listeners()) {
            if (l instanceof IConferenceModelListener) {
                ((IConferenceModelListener) l).statusChanged();
            }
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IConferenceModel#getItemList()
     */
    public IItemList getItemList() {
        return itemList;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IConferenceModel#setItemList(it.uniba.di.cdg.xcore.econference.model.IItemList)
     */
    public void setItemList( IItemList itemList ) {
        this.itemList = itemList;
        // Notify clients ...
        for (IChatRoomModelListener l : listeners()) {
            if (l instanceof IConferenceModelListener) {
                ((IConferenceModelListener) l).itemListChanged();
            }
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IConferenceModel#getWhiteBoardText()
     */
    public String getWhiteBoardText() {
        return whiteBoardText;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IConferenceModel#setWhiteBoardText(java.lang.String)
     */
    public void setWhiteBoardText( String text ) {
        this.whiteBoardText = text;
        
        for (IChatRoomModelListener l : listeners()) {
            if (l instanceof IConferenceModelListener) {
                ((IConferenceModelListener) l).whiteBoardChanged();
            }
        }
    }
    
    /**
     * Provides internal thread synchronization.
     */
    @Aspect
    public static class OwnThreadSafety extends ThreadSafetyAspect {
        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#readOperations()
         */
        @Override
        @Pointcut( "execution( public * ConferenceModel.get*(..) )" )
        protected void readOperations() {}

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#writeOperations()
         */
        @Override
        @Pointcut( "execution( public void ConferenceModel.set*(..) )" )
        protected void writeOperations() {}
    }
}
