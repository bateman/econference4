package it.uniba.di.cdg.skype.x86sdk;

import it.uniba.di.cdg.xcore.network.model.AbstractBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

public class SkypeBuddy extends AbstractBuddy {

	public SkypeBuddy(IBuddyRoster skypeRoster, String buddyId,
			String buddyName, String statusMessage, Status buddyStatus,
			boolean buddyOnline) {
		super();
		this.roster = skypeRoster;
		this.id = buddyId;
		this.name = (buddyName.equals("")) ? buddyId : buddyName;
		this.statusMessage = statusMessage;
		this.status = buddyStatus;
		this.online = buddyOnline;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
