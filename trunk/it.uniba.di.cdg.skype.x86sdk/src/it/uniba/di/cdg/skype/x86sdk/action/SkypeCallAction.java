package it.uniba.di.cdg.skype.x86sdk.action;


import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;

import it.uniba.di.cdg.xcore.network.action.ICallAction;

import it.uniba.di.cdg.xcore.one2one.ChatPlugin;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;

import java.util.Map;
import java.util.TreeMap;



import com.skype.api.Conversation;
import com.skype.api.Participant;



public class SkypeCallAction implements ICallAction {

	private Map<String, Conversation> calls;


	@Override
	public void acceptCall(String from) {
		String[] partecipant = {from};
		Conversation call = SkypeBackend.skype.GetConversationByParticipants(partecipant, true, false);
		call.JoinLiveSession(call.GetJoinBlob());
		final ChatContext chatContext = new ChatContext( from );
		ChatPlugin.getDefault().openChatWindow( chatContext );
		SkypeBackend.sound.stop();



	}

	@Override
	public void call(String to) {
		String[] part = {to};
		Conversation call = SkypeBackend.skype.GetConversationByParticipants(part, true, false);
		Participant[] participants = call.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
		Conversation.LOCAL_LIVESTATUS liveStatus =
				Conversation.LOCAL_LIVESTATUS.get(call.GetIntProperty(Conversation.PROPERTY.local_livestatus));

		if(!liveStatus.equals(Conversation.LOCAL_LIVESTATUS.IM_LIVE)){

			for (int i = 0; i < participants.length; i++) {
				participants[i].Ring("", false, 0, 0, false, "");
			}

			new Thread(
					new Runnable() {
						public void run() {
							SkypeBackend.sound.play("out_call"); 
						}
					}).start();

			addCall(to, call);

		}
	}




	public SkypeCallAction() {
		super();
		calls = new  TreeMap<String, Conversation>();
	}

	public void addCall(String from, Conversation call){
		calls.put(from, call);

	}

	@Override
	public void finishCall(String user) {
		String[] partecipant = {user};
		Conversation call = SkypeBackend.skype.GetConversationByParticipants(partecipant, true, false);
		call.LeaveLiveSession(true);
		calls.remove(user);

	}

	@Override
	public boolean isCalling(String user) {
		String[] partecipant = {user};
		Conversation call = SkypeBackend.skype.GetConversationByParticipants(partecipant, true, false);
		Conversation.LOCAL_LIVESTATUS liveStatus =
				Conversation.LOCAL_LIVESTATUS.get(call.GetIntProperty(Conversation.PROPERTY.local_livestatus));
		if(liveStatus.equals(Conversation.LOCAL_LIVESTATUS.RINGING_FOR_ME))
			return true;
		else return false;

	}

	@Override
	public void declineCall(String from) {
		String[] partecipant = {from};
		Conversation call = SkypeBackend.skype.GetConversationByParticipants(partecipant, true, false);
		call.LeaveLiveSession(true);
		SkypeBackend.sound.stop();
	}

}
