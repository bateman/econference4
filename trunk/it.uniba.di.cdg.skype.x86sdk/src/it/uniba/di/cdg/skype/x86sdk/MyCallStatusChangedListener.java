package it.uniba.di.cdg.skype.x86sdk;

import java.util.Map;


import com.skype.api.Conversation;

public class MyCallStatusChangedListener {

	private Map<String, Conversation> calls;
	private String to;
			
	public MyCallStatusChangedListener(Map<String, Conversation> calls, String to) {
		super();
		this.calls = calls;
		this.to = to;
	}


	public void statusChanged(Conversation.LOCAL_LIVESTATUS status) {
		if(status.equals("RECENTLY_LIVE")||status.equals("NONE")){
			calls.remove(to);
		}
	}

}
