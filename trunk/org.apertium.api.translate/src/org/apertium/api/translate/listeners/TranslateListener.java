package org.apertium.api.translate.listeners;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.ui.views.ITalkView.ISendMessagelListener;


public class TranslateListener implements IBackendEventListener, ISendMessagelListener {
	@Override
	public void onBackendEvent(IBackendEvent event) {
		System.out.println("TranslateListener.onBackendEvent() - event is " + event.getClass().toString());
	}

	@Override
	public void notifySendMessage(String message) {
		System.out.println("TranslateListener.notifySendMessage(): " + message);
	}

}
