package it.uniba.di.cdg.skype.action;

import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.skype.SkypeRoomInfo;
import it.uniba.di.cdg.skype.ui.SkypeJoinChatRoomDialog;
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

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeMultiChatServiceAction implements IMultiChatServiceActions {

	Chat skypeRoom;
	IBackend backend;
	Map<String, String> roomsId;
	String userRule;
	Vector<String> partecipants;
	
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
		// TODO Auto-generated method stub
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
		return userRule;
	}

	@Override
	public void grantVoice(String room, String to) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.USER, to);
		SendExtensionProtocolMessage(ExtensionConstants.GRANT_VOICE, param);
	}

	@Override
	public void invite(String room, String to, String reason) {
		HashMap<String, String> param = new HashMap<String, String>();
		
		param.put(ExtensionConstants.REASON, reason);
		backend.getChatServiceAction().SendExtensionProtocolMessage(
				to, ExtensionConstants.ROOM_INVITE, param);
	}

	@Override
	public void join(String roomName, String password, String nickName,
			String userId, boolean moderator) {

		String[] partecipant = null;
		String inviter = roomsId.get(roomName);
		if (inviter != null){
			HashMap<String, String> param = new HashMap<String, String>();
			backend.getChatServiceAction().SendExtensionProtocolMessage(
					inviter, ExtensionConstants.ROOM_INVITE_ACCEPT, param);
		}else{
			if(roomName.equals("")){
				final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				SkypeJoinChatRoomDialog dlg = new SkypeJoinChatRoomDialog(window);
				if(dlg.open()==Window.OK){
					partecipant = dlg.getUsersSelected();
				}
			}
			else{
				partecipant = new String[1];
				partecipant[0] = "";
			}
			try {
				skypeRoom = Skype.chat(new String[0]);
				
				//setto i privilegi da moderatore
				if(moderator){
					userRule = "moderator";
				}
				
				//invito gli utenti coinvolti
				for(String s: partecipant){
					invite(skypeRoom.getId(), s, "");
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

	@Override
	public void leave() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}
	
	public void putWaitingRoom(String roomId, String inviter){
		roomsId.put(roomId, inviter);
	}
	
	public void sendChatRoom(String userId){
		try {
			skypeRoom.addUser(Skype.getUser(userId));
			String name = Skype.getUser(userId).getFullName();
			addPartecipant(userId, (name.equals("")) ? userId : name); 
			SendExtensionProtocolMessage(ExtensionConstants.CHAT_ROOM, new HashMap<String, String>());
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}
	
	public void updateChatRoom(Chat chat){		
		this.skypeRoom = chat;
		
		for(String s: partecipants){
			backend.getHelper().notifyBackendEvent(new MultiChatUserLeftEvent(
					backend.getBackendId(), s));
		}
		partecipants.clear();
		
		User[] users = null;
		try {
			users = chat.getAllMembers();
			for(User u: users){
				if(!u.getId().equals(backend.getUserId())){
					String name = u.getFullName();
					addPartecipant(u.getId(), (name.equals("") ? u.getId() : name));
				}
			}
		} catch (SkypeException e) {
			e.printStackTrace();
		}

		
	}
	
	private void addPartecipant(String partecipantId, String partecipantName){
		partecipants.add(partecipantId);
		backend.getHelper().notifyBackendEvent(new MultiChatUserJoinedEvent(
						backend.getBackendId(), partecipantId, partecipantName, ""));
	}

}
