package it.uniba.di.cdg.skype.x86sdk.action;

import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;
import it.uniba.di.cdg.skype.x86sdk.util.ExtensionConstants;
import it.uniba.di.cdg.skype.x86sdk.util.XmlUtil;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;

import java.util.HashMap;

import com.skype.api.Conversation;




public class SkypeChatServiceAction implements IChatServiceActions {

	public SkypeChatServiceAction() {
		super();
	}

	@Override
	public void CloseChat(String to) {
	}

	@Override
	public void OpenChat(String to) {	

	}

	@Override
	public void SendExtensionProtocolMessage(String to, String extensionName,
			HashMap<String, String> param) {

		param.put(ExtensionConstants.CHAT_TYPE, ExtensionConstants.ONE_TO_ONE);

		String message = XmlUtil.writeXmlExtension(extensionName, param);

		System.out.println("to: " + to);
		System.out.println("message: " + message);
		String [] part = {to};
		Conversation conversation = SkypeBackend.skype.GetConversationByParticipants(part, true, false);

		if(conversation!=null)
			conversation.PostText(message, true);

	}


	@Override
	public void SendMessage(String to, String message) {

		HashMap<String, String> param = new HashMap<String, String>();
		param.put(ExtensionConstants.MESSAGE, message);
		SendExtensionProtocolMessage(to, ExtensionConstants.CHAT_MESSAGE, param);

	}

	@Override
	public void SendTyping(String from, String to) {
		HashMap<String, String> param = new HashMap<String, String>();
		SendExtensionProtocolMessage(to, ExtensionConstants.CHAT_COMPOSING, param);
	}
}
