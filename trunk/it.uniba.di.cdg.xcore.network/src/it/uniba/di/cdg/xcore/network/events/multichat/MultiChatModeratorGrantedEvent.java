package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatModeratorGrantedEvent implements IBackendEvent {
	
	private String backendId;
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public MultiChatModeratorGrantedEvent(String backendId, String userId) {
		super();
		this.backendId = backendId;
		this.userId = userId;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

}
