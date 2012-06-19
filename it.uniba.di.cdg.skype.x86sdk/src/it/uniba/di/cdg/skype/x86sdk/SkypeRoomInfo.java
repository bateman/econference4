package it.uniba.di.cdg.skype.x86sdk;


import com.skype.api.Conversation;

import it.uniba.di.cdg.xcore.network.services.IRoomInfo;

public class SkypeRoomInfo implements IRoomInfo {
	
	Conversation skypeRoom;

	public SkypeRoomInfo(Conversation skypeChat) {
		super();
		this.skypeRoom = skypeChat;
	}

	@Override
	public String getDescription() {
			return skypeRoom.GetStrProperty(Conversation.PROPERTY.given_displayname);
	}

	@Override
	public String getRoomId() {
		return skypeRoom.GetStrProperty(Conversation.PROPERTY.displayname);
	}

	@Override
	public String getSubject() {
		return skypeRoom.GetStrProperty(Conversation.PROPERTY.given_displayname);
	}

	@Override
	public boolean isMembersOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isModerated() {
		// TODO Auto-generated method stub
		return false;
	}

}
