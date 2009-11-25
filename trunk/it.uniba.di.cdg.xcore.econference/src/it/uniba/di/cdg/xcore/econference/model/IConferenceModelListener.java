package it.uniba.di.cdg.xcore.econference.model;

import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModelListener;

/**
 * Listener interface for this model's observers.
 */
public interface IConferenceModelListener extends IChatRoomModelListener {
    /**
     * The conference status has changed.
     */
    void statusChanged();
    
    /**
     * A new item list instance has replaced the old one.
     */
    void itemListChanged();
    
    /**
     * Notified when the message board's text has changed.
     */
    void whiteBoardChanged();
}