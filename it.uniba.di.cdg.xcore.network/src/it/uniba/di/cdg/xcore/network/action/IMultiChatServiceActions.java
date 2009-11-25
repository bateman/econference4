package it.uniba.di.cdg.xcore.network.action;

import it.uniba.di.cdg.xcore.network.services.IRoomInfo;

import java.util.HashMap;

public interface IMultiChatServiceActions {
	
	public void sendMessage(String room, String message);
	
	public void sendTyping(String userName);
	
	public void grantVoice(String room, String to );
	
	public void revokeVoice(String room, String to );
	
	public void invite(String room, String to, String reason );
	
	public void declineInvitation(String room, String inviter, String reason);
	
	public void join(String roomName, String password, String nickName, String userId, boolean moderator);
	
    public void leave();
    
    public IRoomInfo getRoomInfo(String room);
    
    public void changeSubject(String room, String subject);
	
	public String getUserRole(String userId);
	
	public void SendExtensionProtocolMessage(String extensionName,
			HashMap<String, String> param);

}
