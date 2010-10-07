package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatUserLeftEvent implements IBackendEvent {
	
	private String backendId;
	private String userId;
	private String nickname;

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getUserId() {
		return userId;
	}

	public String getNickname() {
		return nickname;
	}
	
	public MultiChatUserLeftEvent(String backendId, String userId, String nickname) {
		super();
		this.backendId = backendId;
		this.userId = userId;
		this.nickname = nickname;
	}

}
