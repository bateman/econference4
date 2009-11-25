package it.uniba.di.cdg.xcore.network.events.multichat;

import java.util.HashMap;

import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

public class MultiChatExtensionProtocolEvent implements IBackendEvent {
	
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
	
	public String getExtensionParameter(String paramName){
		return param.get(paramName);
	}
	
	public MultiChatExtensionProtocolEvent(HashMap<String, String> param,
			String extensionName, String backendId, String from) {
		super();
		this.param = param;
		this.extensionName = extensionName;
		this.backendId = backendId;
		this.from = from;
	}

	@Override
	public String getBackendId() {
		return backendId;
	}

}
