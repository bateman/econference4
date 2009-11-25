package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatMessageEvent implements IBackendEvent {
	
	private String backendId;
	private String message;
	private String from;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getMessage() {
		return message;
	}

	public MultiChatMessageEvent(String backendId, String message, String from) {
		super();
		this.backendId = backendId;
		this.message = message;
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

}
