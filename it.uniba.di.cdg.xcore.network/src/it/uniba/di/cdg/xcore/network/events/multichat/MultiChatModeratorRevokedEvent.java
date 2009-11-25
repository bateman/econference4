package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatModeratorRevokedEvent implements IBackendEvent {
	
	private String backenId;
	private String userId;

	@Override
	public String getBackendId() {
		return backenId;
	}

	public String getUserId() {
		return userId;
	}

	public MultiChatModeratorRevokedEvent(String backenId, String userId) {
		super();
		this.backenId = backenId;
		this.userId = userId;
	}

}
