package it.uniba.di.cdg.skype;

import it.uniba.di.cdg.xcore.network.model.AbstractBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.model.IEntry;
import it.uniba.di.cdg.xcore.network.model.IBuddy.Status;

public class SkypeBuddy extends AbstractBuddy {

	private IBuddyRoster skypeRoster;
	private String buddyId;
	private String buddyName;
	private String statusMessage;
	private Status buddyStatus;
	private boolean buddyOnline;

	public SkypeBuddy(IBuddyRoster skypeRoster, String buddyId,
			String buddyName, String statusMessage, Status buddyStatus,
			boolean buddyOnline) {
		super();
		this.skypeRoster = skypeRoster;
		this.buddyId = buddyId;
		this.buddyName = buddyName;
		this.statusMessage = statusMessage;
		this.buddyStatus = buddyStatus;
		this.buddyOnline = buddyOnline;
	}

	@Override
	public String getId() {
		return buddyId;
	}

	@Override
	public String getName() {
		return buddyName;
	}

	@Override
	public String getPrintableLabel() {
        String s = getName();
        if (s == null)
            s = getId();
        
        if (!Status.OFFLINE.equals( getStatus() ) && 
        		statusMessage!=null && !statusMessage.isEmpty())
            s += " (" + statusMessage + ")";
        
        return s;
	}

	@Override
	public IBuddyRoster getRoster() {
		return skypeRoster;
	}

	@Override
	public Status getStatus() {
		return buddyStatus;
	}

	@Override
	public String getStatusMessage() {
		return statusMessage;
	}

	@Override
	public boolean isOnline() {
		return buddyOnline;
	}

	@Override
	public void setId(String id) {
		buddyId = id;
	}

	@Override
	public void setName(String name) {
		buddyName = name;
	}

	@Override
	public void setOnline(boolean online) {
		buddyOnline = online;
	}

	@Override
	public void setStatus(Status status) {
		buddyStatus = status;
	}

	@Override
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public IEntry[] getChilds() {
		return new IEntry[0];
	}

	@Override
	public IEntry getParent() {
		IBuddyGroup parentGroup = (IBuddyGroup) getRoster().getGroups( this ).toArray()[0];
        return parentGroup;
	}

	@Override
	public int compareTo(IBuddy that) {
		return String.CASE_INSENSITIVE_ORDER.compare( this.getName(), that.getName() );
	}

}
