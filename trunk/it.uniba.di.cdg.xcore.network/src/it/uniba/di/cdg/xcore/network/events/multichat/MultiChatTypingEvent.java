package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatTypingEvent implements IBackendEvent {
	
	private String backendId;
	private String from;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getFrom() {
		return from;
	}

	public MultiChatTypingEvent(String backendId, String from) {
		super();
		this.backendId = backendId;
		this.from = from;
	}

}
