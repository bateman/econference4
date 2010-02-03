package org.apertium.api.translate.listeners;

//import org.apertium.api.translate.views.TranslateView;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;


public class TranslateListener implements IBackendEventListener {
	@Override
	public void onBackendEvent(IBackendEvent event) {
		System.out.println("TranslateListener.onBackendEvent() - event is " + event.getClass().toString());
	
		//TranslateView.getInstance().appendMessage(event.getClass().toString());
	}

}
