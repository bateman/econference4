package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatOwnershipGrantedEvent implements IBackendEvent {
	
	private String backendId;
	private String userId;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getUserId() {
		return userId;
	}

	public MultiChatOwnershipGrantedEvent(String backendId, String userId) {
		super();
		this.backendId = backendId;
		this.userId = userId;
	}

}
