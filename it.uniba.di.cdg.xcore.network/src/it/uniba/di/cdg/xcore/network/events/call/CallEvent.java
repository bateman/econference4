package it.uniba.di.cdg.xcore.network.events.call;

import it.uniba.di.cdg.xcore.network.events.AbstractBackendEvent;

public class CallEvent extends AbstractBackendEvent {

	private String from;

	public String getFrom() {
		return from;
	}

	public CallEvent(String backendId, String from) {
		super(backendId);
		this.from = from;
	}

}
