package it.uniba.di.cdg.skype.x86sdk.action;

import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;
import it.uniba.di.cdg.skype.x86sdk.SkypeRoomInfo;
import it.uniba.di.cdg.skype.x86sdk.util.ExtensionConstants;
import it.uniba.di.cdg.skype.x86sdk.util.XmlUtil;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserJoinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.services.IRoomInfo;
import it.uniba.di.cdg.xcore.network.services.JoinException;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.Assert;

import com.skype.api.Contact;
import com.skype.api.Conversation;
import com.skype.api.Participant;

public class SkypeMultiChatServiceAction implements IMultiChatServiceActions {

	private Conversation skypeRoom;
	public String roomID;
	private IBackend backend;
	private Map<String, String> roomsId;
	private String userRole;
	public Vector<String> participants;
	private String moderator;

	public String getModerator() {
		return moderator;
	}

	public void setModerator(String moderator) {
		this.moderator = moderator;
	}

	public SkypeMultiChatServiceAction(IBackend backend) {
		super();
		this.backend = backend;
		roomsId = new HashMap<String, String>();
		participants = new Vector<String>();
	}

	@Override
	public void SendExtensionProtocolMessage(String extensionName,
			HashMap<String, String> param) {

		param.put(ExtensionConstants.CHAT_TYPE, ExtensionConstants.M_TO_M);

		String message = XmlUtil.writeXmlExtension(extensionName, param);
		String[] part=new String[participants.size()];
		participants.copyInto(part);

		skypeRoom = SkypeBackend.skype.GetConversationByParticipants(part, false, false);

		if(skypeRoom!=null)	
			skypeRoom.PostText(message,true);


		((SkypeBackend)backend).processMessageReceived(message, 
				backend.getUserId(), backend.getUserAccount().getName(),
				skypeRoom);



	}

	@Override
	public void changeSubject(String room, String subject) {
		// do nothing
	}

	@Override
	public void declineInvitation(String room, String inviter, String reason) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.REASON, reason);
		backend.getChatServiceAction().SendExtensionProtocolMessage(inviter, 
				ExtensionConstants.ROOM_INVITE_DECLINE, param);
	}

	@Override
	public IRoomInfo getRoomInfo(String room) {
		return new SkypeRoomInfo(null);
	}

	@Override
	public String getUserRole(String userId) {
		return userRole;
	}

	@Override
	public void grantVoice(String room, String to) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.USER, to);
		SendExtensionProtocolMessage(ExtensionConstants.GRANT_VOICE, param);
	}

	@Override
	public void invite(String room, String to, String reason) {
		// prevent self messaging
		if (to.equals(backend.getUserId()))
			return;
		HashMap<String, String> param = new HashMap<String, String>();

		param.put(ExtensionConstants.REASON, reason);
		backend.getChatServiceAction().SendExtensionProtocolMessage(
				to, ExtensionConstants.ROOM_INVITE, param);
	}

	@Override
	public void join(String roomName, String password, String nickName,
			String userId, boolean moderator) throws JoinException {
		String[] part=new String[participants.size()];
		String[] participant = null;
		String inviter = roomsId.get(roomName);


		participants.copyInto(part);
		// this wont be null if we put it in as a waiting room after receiving an online invitation
		if (inviter == null){

			if(moderator){
				userRole = "moderator";
				setModerator(userId);
				participant = new String[1];
				participant[0] = "";
				skypeRoom = SkypeBackend.skype.CreateConference();
				roomID=skypeRoom.GetStrProperty(Conversation.PROPERTY.identity);
				inviter=userId;


				for(String s: participants){
					if(s != null && !s.equals(""))
						invite(skypeRoom.GetStrProperty(Conversation.PROPERTY.identity), s, "");
				}

			}
			else {
				//inviter dal room name			
				// room name is #A/$B;key
				// you cant say who the inviter is upfront
				// if your id matches A, then inviter is B
				// and viceversa
				if (roomName.contains(";")) {
					if (roomName.startsWith("#")) { // it's an online
						// invitation
						// trims the leading "#" and the trailing ";key"
						roomName = roomName.substring(1,
								roomName.indexOf(";"));
						String[] splits = roomName.split("/");
						Assert.isTrue(splits.length == 2);
						for (String s : splits) {
							if (s.startsWith("$"))
								s = s.substring(1);
							if (!s.equals(userId)) {
								inviter = s;
								break;
							}
						}
					}
				} else { // it's a load from file
					String[] splits = roomName.split("\\$");
					Assert.isTrue(splits.length == 2);
					inviter = splits[1];
				}

				roomInviteAccepted(inviter);	
			}



		}else{

			roomInviteAccepted(inviter);		
		}
		// we have to wait until the moderator
		// notifies the room
		int millis = 0;
		while(skypeRoom == null && (moderator || millis < 50000))
			try {
				millis += 50;
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		if (skypeRoom == null) {
			throw new JoinException("The moderator is not in the room. Try again later.");
		}
	}

	protected void roomInviteAccepted(String inviter) {
		HashMap<String, String> param = new HashMap<String, String>();
		backend.getChatServiceAction().SendExtensionProtocolMessage(inviter,
				ExtensionConstants.ROOM_INVITE_ACCEPT, param);	
	}

	@Override
	public void leave() {
		if (null != skypeRoom) {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put(ExtensionConstants.PRESENCE_TYPE,
					ExtensionConstants.PRESENCE_UNAVAILABLE);
			SendExtensionProtocolMessage(ExtensionConstants.PRESENCE_MESSAGE,
					param);

			skypeRoom.LeaveLiveSession(false);
			skypeRoom = null;

		}

	}

	@Override
	public void revokeVoice(String room, String to) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.USER, to);
		SendExtensionProtocolMessage(ExtensionConstants.REVOKE_VOICE, param);
	}

	@Override
	public void sendMessage(String room, String message) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.MESSAGE, message);
		SendExtensionProtocolMessage(ExtensionConstants.CHAT_MESSAGE, param);
	}

	@Override
	public void sendTyping(String userName) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.USER, userName);
		SendExtensionProtocolMessage(ExtensionConstants.CHAT_COMPOSING, param);

	}

	public void putWaitingRoom(String roomId, String inviter){
		roomsId.put(roomId, inviter);
	}

	public void sendChatRoom(String userId){

		boolean flag=false;
		String[] identities = {userId};
		skypeRoom = SkypeBackend.skype.GetConversationByIdentity(roomID);
		Participant[] users = skypeRoom.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);
		for (int i=0;i<users.length;i++){
			if (users[i].GetStrProperty(Participant.PROPERTY.identity).equals(userId))
				flag=true;
		}



		if (!flag)
			skypeRoom.AddConsumers(identities);


		String name = SkypeBackend.skype.GetContact(userId).GetStrProperty(Contact.PROPERTY.displayname);
		addParticipant(userId, (name.equals("")) ? userId : name, ""); 
		SendExtensionProtocolMessage(ExtensionConstants.CHAT_ROOM, new HashMap<String, String>());

	}


	public void updateChatRoom(Conversation chat){		
		this.skypeRoom = chat;

		for(String s: participants){
			backend.getHelper().notifyBackendEvent(new MultiChatUserLeftEvent(
					backend.getBackendId(), s, s));
		}
		participants.clear();

		Participant[] users = null;

		users = skypeRoom.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);
		for(Participant u: users){
			if(!u.GetStrProperty(Participant.PROPERTY.identity).equals(backend.getUserId())){
				String name =SkypeBackend.skype.GetContact(u.GetStrProperty(Participant.PROPERTY.identity)).GetStrProperty(Contact.PROPERTY.displayname);
				String role = "";
				if (moderator != null)
					role = moderator.equals(u.GetStrProperty(Participant.PROPERTY.identity)) ? "moderator" : "";
				addParticipant(u.GetStrProperty(Participant.PROPERTY.identity), (name.equals("") ? u.GetStrProperty(Participant.PROPERTY.identity) : name), 
						role);
			}
		}



	}

	private void addParticipant(String participantId, String participantName, String role){

		participants.add(participantId);
		backend.getHelper().notifyBackendEvent(new MultiChatUserJoinedEvent(
				backend.getBackendId(), participantId, participantName, role));
	}





}
