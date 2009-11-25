package it.uniba.di.cdg.xcore.network.events.chat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class ChatComposingtEvent implements IBackendEvent {
	
	private String from;
	private String backendId;

	public ChatComposingtEvent(String from, String backendId) {
		super();
		this.from = from;
		this.backendId = backendId;
	}

	public String getFrom() {
		return from;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

}
