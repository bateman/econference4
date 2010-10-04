package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatComposingEvent implements IBackendEvent {
	
	private String backendId;
	private String from;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getFrom() {
		return from;
	}

	public MultiChatComposingEvent(String from, String backendId) {
		super();
		this.backendId = backendId;
		this.from = from;
	}

}
