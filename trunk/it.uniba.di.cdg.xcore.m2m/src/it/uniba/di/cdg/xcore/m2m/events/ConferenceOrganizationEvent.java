package it.uniba.di.cdg.xcore.m2m.events;


public class ConferenceOrganizationEvent extends InvitationEvent {
	
	public static final int ORGANIZATION_EVENT_TYPE = 0x02;

	private String[] participants;
	private String[] items;

	public ConferenceOrganizationEvent(String backendId, String room,
			String inviter, String schedule, String reason, String password,
			String[] invitees, String[] items) {
		super(backendId, room, inviter, schedule, reason, password);
		this.participants = invitees;
		this.items = items;
	}

	public String[] getParticipants() {
		return participants;
	}

	public String[] getItems() {
		return items;
	}

	public int getEventType() {
    	return ORGANIZATION_EVENT_TYPE;
    }
}
