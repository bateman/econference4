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

import it.uniba.di.cdg.aspects.GetSafety;
import it.uniba.di.cdg.aspects.SetSafety;
import it.uniba.di.cdg.xcore.econference.popup.handler.DeleteItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Implementation of {@see it.uniba.di.cdg.econference.core.conference.IItemList}. 
 */
public class ItemList implements IItemList {
    /**
     * Observers.
     */
    private final Set<IItemListListener> listeners;

    /** 
     * The ordered items. 
     */
    private final List<IDiscussionItem> items;
    
    /** 
     * Current selected item. 
     */
    private int current;

    /**
     * Default constructor which select no element.
     */
    public ItemList() {
        this( NO_ITEM_SELECTED );
    }
    
    /**
     * Convenience constructor.
     * 
     * @param model the conference model
     * @param initialIndex the current selected item.
     */
    public ItemList( int initialIndex ) {
        this.listeners = new HashSet<IItemListListener>();
        this.items = new ArrayList<IDiscussionItem>();
        this.current = initialIndex;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IItemList#addItem(java.lang.String)
     */
    @SetSafety
    public void addItem( Object item ) {
        items.add( (IDiscussionItem) item );
        
        for (IItemListListener l : listeners)
            l.itemAdded( item );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IItemList#addItem(java.lang.String)
     */
    @SetSafety
    public void addItem( String itemText ) {
        addItem( new DiscussionItem( itemText ) );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IItemList#removeItem(int)
     */
    @SetSafety
    public void removeItem( int itemIndex ) {
    	if(items.size() > 0) {
	        IDiscussionItem item = items.get( itemIndex );
	        
	        items.remove( itemIndex );
	
	        for (IItemListListener l : listeners)
	            l.itemRemoved( item );
    	}
    }
    	


    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IItemList#getItem(int)
     */
    @GetSafety
    public IDiscussionItem getItem( int itemIndex ) {
    	// FIXME -1 AIOoBE
        return items.get( itemIndex );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IItemList#size()
     */
    @GetSafety
    public int size() {
        return items.size();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IItemList#setCurrentItemIndex(int)
     */
    @SetSafety
    public void setCurrentItemIndex( int itemIndex ) {
    	// FIXME throws an index out of bound exception with planning poker agenda
        if (itemIndex >= size() || (itemIndex < IItemList.NO_ITEM_SELECTED && itemIndex != DeleteItem.DELETE_ITEM_INDEX))
            throw new IllegalArgumentException( "itemIndex out of range" );
        this.current = itemIndex;
        
        for (IItemListListener l : listeners)
            l.currentSelectionChanged( current );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IItemList#getCurrentItemIndex()
     */
    @GetSafety
    public int getCurrentItemIndex() {
        return current;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return items.toString();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IItemList#encode()
     */
    @GetSafety
    public String encode() {
        if (size() == 0)
            return "";
        
        StringBuffer sb = new StringBuffer();
        for (IDiscussionItem i : items) {
            sb.append( i.getText() );
            sb.append( ENCODING_SEPARATOR );
        }
        return sb.toString();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IItemList#decode(java.lang.String)
     */
    @SetSafety
    public void decode( String encodedItems ) {
        if (encodedItems == null || encodedItems.length() == 0)
            return;

        String[] decoded = encodedItems.split( ENCODING_SEPARATOR );
        if (decoded.length <= 0)
            return;

        // Remove previous ones ...
        items.clear();
        
        for (String s : decoded)
            items.add( new DiscussionItem( s ) );

        for (IItemListListener l : listeners)
            l.contentChanged( this );
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IItemList#addListener(it.uniba.di.cdg.xcore.econference.model.IItemList.IItemListener)
     */
    @SetSafety
    public void addListener( IItemListListener listener ) {
        listeners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.IItemList#removeListener(it.uniba.di.cdg.xcore.econference.model.IItemList.IItemListener)
     */
    @SetSafety
    public void removeListener( IItemListListener listener ) {
        listeners.remove( listener );
    }
    
 
	@Override
	public Iterator<IDiscussionItem> iterator() {		
		return items.iterator();
	}

	@Override
	public void toArray(String[] items) {
		Iterator<IDiscussionItem> i = this.iterator();
		for (int j = 0; j < items.length; j++) {
			items[j] = i.next().toString();
		}
		
	}


}
