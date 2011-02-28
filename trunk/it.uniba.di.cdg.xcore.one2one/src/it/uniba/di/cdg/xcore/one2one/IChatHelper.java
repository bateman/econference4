package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;

public interface IChatHelper extends IBackendEventListener{
	
	/**
     * Perform helper initialization.
     */
	void init();
	
	/**
     * Release all resources and listeners.
     */
	void dispose();
	
	/**
     * Open a new chat: a new view will be created and the focus shifted.
     * 
     * @param context
     *        the context needed to create this new chat
     * @return the created chat object
     */
	void openChatWindow(ChatContext chatContext);

}
