package it.uniba.di.cdg.xcore.network.action;

import java.util.HashMap;

public interface IChatServiceActions {
	
	void SendMessage(String to, String message);
	
	void SendTyping(String from, String to);
	
	void OpenChat(String to);
	
	void CloseChat(String to);
	
	void SendExtensionProtocolMessage(
			String to,
			String extensionName, 
			HashMap<String, String> param);

}
