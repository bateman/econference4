package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatNameChangedEvent implements IBackendEvent {
	
	private String backendId;
	private String userId;
	private String userName;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public MultiChatNameChangedEvent(String backendId, String userId,
			String userName) {
		super();
		this.backendId = backendId;
		this.userId = userId;
		this.userName = userName;
	}

}
