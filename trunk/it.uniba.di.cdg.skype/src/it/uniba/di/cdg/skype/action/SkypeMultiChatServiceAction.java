package it.uniba.di.cdg.skype.action;

import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.skype.SkypeRoomInfo;
import it.uniba.di.cdg.skype.util.ExtensionConstants;
import it.uniba.di.cdg.skype.util.XmlUtil;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserJoinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.services.IRoomInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.Assert;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeMultiChatServiceAction implements IMultiChatServiceActions {

	private Chat skypeRoom;
	private IBackend backend;
	private Map<String, String> roomsId;
	private String userRole;
	private Vector<String> partecipants;
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
		partecipants = new Vector<String>();
	}

	@Override
	public void SendExtensionProtocolMessage(String extensionName,
			HashMap<String, String> param) {
		
		param.put(ExtensionConstants.CHAT_TYPE, ExtensionConstants.M_TO_M);

		String message = XmlUtil.writeXmlExtension(extensionName, param);
		try {
			skypeRoom.send(message);
			((SkypeBackend)backend).processMessageReceived(message, 
					backend.getUserId(), backend.getUserAccount().getName(),
					skypeRoom);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
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
		return new SkypeRoomInfo(skypeRoom);
	}

	@Override
	public String getUserRole(String userId) {
		return userRole;
	}

	@Override
	public void grantVoice(String room, String to) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.USER, getIdFromNick(to));
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
			String userId, boolean moderator) {

		String[] participant = null;
		String inviter = roomsId.get(roomName);
		// this wont be null if we put it in as a waiting room after receiving an online invitation
		if (inviter != null){
			roomInviteAccepted(inviter);
		}else{
			/*if(roomName.equals("")){
				final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				SkypeJoinChatRoomDialog dlg = new SkypeJoinChatRoomDialog(window);
				if(dlg.open()==Window.OK){
					participant = dlg.getUsersSelected();
				}
			}
			else{*/
				
			//}
			try {
				
				
				//setto i privilegi da moderatore
				if(moderator){
					userRole = "moderator";
					setModerator(userId);
					participant = new String[1];
					participant[0] = "";
					skypeRoom = Skype.chat(new String[0]);
				
				
					//invito gli utenti coinvolti
					for(String s: participant){
						if(s != null && !s.equals(""))
							invite(skypeRoom.getId(), s, "");
					}
				}
				else {
					//inviter dal room name			
					// room name is #A/$B;key
					// you cant say who the inviter is upfront
					// if your id matches A, then inviter is B
					// and viceversa
					if(roomName.contains(";"))
						// trims the leading "#" and the trailing ";key"
						roomName = roomName.substring(1, roomName.indexOf(";"));
					String[] splits = roomName.split("/");
					Assert.isTrue(splits.length == 2);
					for (String s : splits) {
						if(s.startsWith("$"))
							s = s.substring(1);
						if(!s.equals(userId)) {
							inviter = s;
							break;
						}
					}
					
					roomInviteAccepted(inviter);
				}
					
				
				//inserisco l'utente locale nella lista dei partecipanti alla stanza
				/*backend.getHelper().notifyBackendEvent(new MultiChatUserJoinedEvent(
						backend.getBackendId(), backend.getUserId(), 
						backend.getUserAccount().getName(), ""));
				*/
				//notifico l'aggiunta di tutti gli altri utenti della stanza
				//for(String s: partecipant){
					//addPartecipant(s, Skype.getUser(s).getFullName());
				//}
			
			
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		}
		
	}

	protected void roomInviteAccepted(String inviter) {
		HashMap<String, String> param = new HashMap<String, String>();
		backend.getChatServiceAction().SendExtensionProtocolMessage(
				inviter, ExtensionConstants.ROOM_INVITE_ACCEPT, param);	
	}

	@Override
	public void leave() {
		if (null != skypeRoom) {
			System.out.println("Sending precence unavailable update");
			HashMap<String, String> param = new HashMap<String, String>();
			param.put(ExtensionConstants.PRESENCE_TYPE,
					ExtensionConstants.PRESENCE_UNAVAILABLE);
			SendExtensionProtocolMessage(ExtensionConstants.PRESENCE_MESSAGE,
					param);
//			try {
//				skypeRoom.leave();
//				//skypeRoom = null;
//			} catch (SkypeException e) {
//				e.printStackTrace();
//			}
		}
		
	}

	@Override
	public void revokeVoice(String room, String to) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.USER, getIdFromNick(to));
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
		System.out.println("Skype room id "+skypeRoom.getId());
	}
	
	public void putWaitingRoom(String roomId, String inviter){
		roomsId.put(roomId, inviter);
	}
	
	public void sendChatRoom(String userId){
		try {
			skypeRoom.addUser(Skype.getUser(userId));
			String name = Skype.getUser(userId).getFullName();
			addPartecipant(userId, (name.equals("")) ? userId : name, ""); 
			SendExtensionProtocolMessage(ExtensionConstants.CHAT_ROOM, new HashMap<String, String>());
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
	
	public void updateChatRoom(Chat chat){		
		this.skypeRoom = chat;
		
		for(String s: partecipants){
			backend.getHelper().notifyBackendEvent(new MultiChatUserLeftEvent(
					backend.getBackendId(), s, s));
		}
		partecipants.clear();
		
		User[] users = null;
		try {
			users = chat.getAllMembers();
			for(User u: users){
				if(!u.getId().equals(backend.getUserId())){
					String name = u.getFullName();
					String role = "";
					if (moderator != null)
						role = moderator.equals(u.getId()) ? "moderator" : "";
					addPartecipant(u.getId(), (name.equals("") ? u.getId() : name), 
							role);
				}
			}
		} catch (SkypeException e) {
			e.printStackTrace();
		}

		
	}
	
	private void addPartecipant(String partecipantId, String partecipantName, String role){
		partecipants.add(partecipantId);
		backend.getHelper().notifyBackendEvent(new MultiChatUserJoinedEvent(
						backend.getBackendId(), partecipantId, partecipantName, role));
	}
	
	private String getIdFromNick(String nick){
		String id = null;
		try {
			User[] users = skypeRoom.getAllMembers();
			for(User u: users){
				if(u.getFullName().equals(nick)){
					id = u.getId();
					break;
				}
			}
					
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		
		return id;		
	}

}
