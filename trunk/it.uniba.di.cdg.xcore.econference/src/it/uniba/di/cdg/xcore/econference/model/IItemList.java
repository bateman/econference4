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

import java.util.Iterator;

/**
 * Operations possible on a conference's items list. This is an ordered list of items to be
 * discussed. 
 */
public interface IItemList{
    /** 
     * Indentifies the condition of 'no item selected'. 
     */
    public static final int NO_ITEM_SELECTED = -1;

    /**
     * Separator used during encoding operations.
     */
    public static final String ENCODING_SEPARATOR = "//"; 
    
    /**
     * Add a discussing item.
     * 
     * @param item
     */
    void addItem( Object item );
    
    /**
     * Convenience method for simply giving the item's text.
     * 
     * @param itemText
     */
    void addItem( String itemText );
    
    /**
     * Remove an item, by specifying its index.
     * 
     * @param itemIndex 
     */
    void removeItem( int itemIndex );

    /**
     * Returns the item at a specified position
     * 
     * @param itemIndex
     * @return the item at the indexed position
     */
    Object getItem( int itemIndex );

    /**
     * Select the current item index.
     * 
     * @param itemIndex
     */
    void setCurrentItemIndex( int itemIndex );
    
    /**
     * Returns the currently selected item's index.
     * 
     * @return the index of the currently selected item.
     */
    int getCurrentItemIndex();
    
    /**
     * Returns the number of items 
     * 
     * @return the number of items
     */
    int size();

    /**
     * Convert the current items in a string. This method is thought to collaborate with
     * {@see #decode(String)}.
     * 
     * @return the encoded item list (maybe empty if no item is present).
     */
    String encode();
    
    /**
     * Decode an enconded string representing the items. Note that the decoded items will
     * replace the ones that already exist here. If the operations is successufully completed
     * an {@see IItemListListener#contentChanged(IItemList)} event will be notified. 
     * 
     * @param encodedItems
     */
    void decode( String encodedItems );
    
    
    /**
     * Add a new event listener.
     * 
     * @param listener
     */
    void addListener( IItemListListener listener );
    
    /**
     * Remove an event listener.
     * 
     * @param listener
     */
    void removeListener( IItemListListener listener );
    
    /**
     * Returns the list as a IDiscussionItem iterator
     * @return string iterator
     */
    public Iterator<IDiscussionItem> iterator();
}
