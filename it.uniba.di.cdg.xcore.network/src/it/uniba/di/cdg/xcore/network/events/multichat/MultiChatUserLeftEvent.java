package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatUserLeftEvent implements IBackendEvent {
	
	private String backendId;
	private String userId;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getUserId() {
		return userId;
	}

	public MultiChatUserLeftEvent(String backendId, String userId) {
		super();
		this.backendId = backendId;
		this.userId = userId;
	}

}
