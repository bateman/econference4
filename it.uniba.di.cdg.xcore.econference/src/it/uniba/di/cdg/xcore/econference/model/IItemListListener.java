package it.uniba.di.cdg.xcore.econference.model;

/**
 * Interface for item list event listeners.
 */
public interface IItemListListener {
    /**
     * Notify that a new element has been selected.
     * 
     * @param currItemIndex
     */
    void currentSelectionChanged( int currItemIndex );
    
    /**
     * Notified when a new item has been added.
     * 
     * @param item the newly added item.
     */
    void itemAdded( Object item );
    
    /**
     * Notified when an item is removed.
     * 
     * @param item the just removed item
     */
    void itemRemoved( Object item );
    
    /**
     * The content of the specified item list has changed.
     * 
     * @param itemList
     */
    void contentChanged( IItemList itemList );
}