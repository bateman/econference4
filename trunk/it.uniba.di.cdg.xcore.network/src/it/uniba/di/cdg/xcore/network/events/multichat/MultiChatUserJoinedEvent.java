package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatUserJoinedEvent implements IBackendEvent {
	
	private String backendId;
	private String userId;
	private String userName;
	private String role;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getRole(){
		return role;
	}
	
	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public MultiChatUserJoinedEvent(String backendId, String userId,
			String userName, String role) {
		super();
		this.backendId = backendId;
		this.userId = userId;
		this.userName = userName;
		this.role = role;
	}

}
