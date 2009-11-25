package it.uniba.di.cdg.xcore.network.events.chat;

import java.util.HashMap;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class ChatExtensionProtocolEvent implements IBackendEvent{
	
	private HashMap<String, String>  param;
	private String extensionName;
	private String backendId;
	private String from;
	
	public String getFrom(){
		return from;
	}
	
	public String getExtensionName(){
		return extensionName;
	}
	
	public Object getExtensionParameter(String paramName){
		return param.get(paramName);
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

	public ChatExtensionProtocolEvent(
			String from,
			String extensionName, 
			HashMap<String, String> param,
			String backendId) {
		super();
		this.from = from;
		this.param = param;
		this.extensionName = extensionName;
		this.backendId = backendId;
	}

}
