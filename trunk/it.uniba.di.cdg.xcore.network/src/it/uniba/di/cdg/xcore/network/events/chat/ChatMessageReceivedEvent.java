package it.uniba.di.cdg.xcore.network.events.chat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class ChatMessageReceivedEvent implements IBackendEvent{
	
	private String from;
	private String message;
	private String backendId;
	
	public ChatMessageReceivedEvent(String from, String message, String backendId) {
		super();
		this.from = from;
		this.message = message;
		this.backendId = backendId;
	}

	public String getFrom(){
		return from;
	}
	
	public String getMessage(){
		return message;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

}
