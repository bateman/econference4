package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatSubjectUpdatedEvent implements IBackendEvent {

	private String backendId;
	private String from;
	private String subject;
	
	public MultiChatSubjectUpdatedEvent(String backendId, String from,
			String subject) {
		super();
		this.backendId = backendId;
		this.from = from;
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

}
