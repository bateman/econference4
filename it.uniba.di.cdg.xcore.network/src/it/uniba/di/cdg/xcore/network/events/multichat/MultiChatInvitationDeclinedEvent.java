package it.uniba.di.cdg.xcore.network.events.multichat;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatInvitationDeclinedEvent implements IBackendEvent {

	private String backendId; 
	private String invitee;
	private String reason;
	
	public MultiChatInvitationDeclinedEvent(String invitee, String reason, String backendId) {
		this.invitee = invitee;
		this.reason = reason; 		
		this.backendId = backendId;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

	public String getInvitee() {
		return invitee;
	}

	public String getReason() {
		return reason;
	}
	

}
