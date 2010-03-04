package it.uniba.di.cdg.xcore.network.events.chat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.model.IBuddy;

public class ChatMessageReceivedEvent implements IBackendEvent{
	
	private IBuddy buddy;
	private String message;
	private String backendId;
	
	public ChatMessageReceivedEvent(IBuddy buddy, String message, String backendId) {
		super();
		this.buddy = buddy;
		this.message = message;
		this.backendId = backendId;
	}
	
	public IBuddy getBuddy(){
		return buddy;
	}

	
	public String getMessage(){
		return message;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

}
