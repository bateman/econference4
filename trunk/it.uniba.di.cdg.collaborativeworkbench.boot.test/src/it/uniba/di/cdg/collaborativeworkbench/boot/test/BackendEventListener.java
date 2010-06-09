package it.uniba.di.cdg.collaborativeworkbench.boot.test;


import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;

public class BackendEventListener implements IBackendEventListener {
	private boolean messageIncoming = false; 
	
	@Override
	public void onBackendEvent(IBackendEvent event) {
		// TODO Auto-generated method stub
		if (event instanceof MultiChatMessageEvent) {
			messageIncoming=true;
		}

		if (event instanceof ChatMessageReceivedEvent) {
			messageIncoming=true;
		}
	}
	public boolean getMessageIncoming() {
		return messageIncoming;
	}
	public void setNewMessageIncoming(boolean value) {
		messageIncoming= value;
	}
	public void start() {
		// TODO Auto-generated method stub
		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry().getDescriptors())
    		NetworkPlugin.getDefault().getHelper().registerBackendListener(d.getId(), this); 
	}

}
