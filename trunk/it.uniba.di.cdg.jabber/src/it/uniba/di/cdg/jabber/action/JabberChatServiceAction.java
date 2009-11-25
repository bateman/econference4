package it.uniba.di.cdg.jabber.action;

import static it.uniba.di.cdg.smackproviders.SmackCommons.CDG_NAMESPACE;
import it.uniba.di.cdg.jabber.JabberBackend;
import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;

public class JabberChatServiceAction implements IChatServiceActions{

	JabberBackend backend;
	HashMap<String, Chat> chatList;
    public static final String CDG_NAMESPACE = "http://cdg.di.uniba.it/xcore/jabber";
	
	public JabberChatServiceAction(JabberBackend backend) {
		super();
		this.backend = backend;
		chatList = new HashMap<String, Chat>();
	}

	@Override
	public void SendMessage(String to, String message) {
		try {
			chatList.get(to).sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void SendTyping(String from, String to) {
		TypingNotificationPacket notification = new TypingNotificationPacket();
		notification.setWho(StringUtils.escapeForXML(from));

		Message msg = new Message();
		msg.addExtension(notification);

		try {
			chatList.get(to).sendMessage(msg);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void CloseChat(String to) {
		chatList.remove(to);
	}

	@Override
	public void OpenChat(String to) {
		ChatManager chatManager = backend.getConnection().getChatManager();
		Chat chat = chatManager.createChat(to, null);
		chatList.put(to, chat);
	}

	@Override
	public void SendExtensionProtocolMessage(String to, String extensionName,
			HashMap<String, String> param) {

		Message message = new Message();

		message.setProperty("ExtensionName", extensionName);
		Iterator<String> it = param.keySet().iterator();
		while(it.hasNext()){
			String val = it.next();
			message.setProperty(val, param.get(val));
		}

		try {
			chatList.get(to).sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
	}

}
