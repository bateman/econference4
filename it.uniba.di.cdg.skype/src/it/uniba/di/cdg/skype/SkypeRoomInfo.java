package it.uniba.di.cdg.skype;

import com.skype.Chat;
import com.skype.SkypeException;

import it.uniba.di.cdg.xcore.network.services.IRoomInfo;

public class SkypeRoomInfo implements IRoomInfo {
	
	Chat skypeRoom;

	public SkypeRoomInfo(Chat skypeChat) {
		super();
		this.skypeRoom = skypeChat;
	}

	@Override
	public String getDescription() {
		try {
			return skypeRoom.getWindowTitle();
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getRoomId() {
		return skypeRoom.getId();
	}

	@Override
	public String getSubject() {
		try {
			return skypeRoom.getWindowTitle();
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		return null;
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
