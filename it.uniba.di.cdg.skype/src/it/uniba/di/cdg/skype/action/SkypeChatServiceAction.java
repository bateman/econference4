package it.uniba.di.cdg.skype.action;

import it.uniba.di.cdg.skype.util.ExtensionConstants;
import it.uniba.di.cdg.skype.util.XmlUtil;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;

import java.util.HashMap;

import com.skype.Skype;
import com.skype.SkypeException;

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
		try {
			Skype.chat(to).send(message);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
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
