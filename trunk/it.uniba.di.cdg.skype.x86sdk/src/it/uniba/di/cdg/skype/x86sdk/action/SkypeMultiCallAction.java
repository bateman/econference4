package it.uniba.di.cdg.skype.x86sdk.action;

import java.util.HashMap;

import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;
import it.uniba.di.cdg.skype.x86sdk.util.ExtensionConstants;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;

import com.skype.api.Conversation;
import com.skype.api.Participant;




public class SkypeMultiCallAction implements IMultiCallAction {

	private Conversation call = null;
	private String conferenceId = "";



	public SkypeMultiCallAction(){
		super();

	}

	@Override
	public void acceptCall() {

		String[] part= new String[SkypeBackend.skypeMultiChatServiceAction.participants.size()];
		SkypeBackend.skypeMultiChatServiceAction.participants.copyInto(part); 
		call = SkypeBackend.skype.GetConversationByParticipants(part, false, false);
		call.JoinLiveSession(call.GetJoinBlob());
		SkypeBackend.sound.stop();
	}

	@Override
	public void call() {
		String[] part= new String[SkypeBackend.skypeMultiChatServiceAction.participants.size()];
		SkypeBackend.skypeMultiChatServiceAction.participants.copyInto(part); 
		call = SkypeBackend.skype.GetConversationByParticipants(part, false, false);
		Participant[] dest = call.GetParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
		for (int i = 0; i < dest.length; i++) {
			dest[i].Ring("", false, 0, 0, false, "");

		}

		new Thread(
				new Runnable() {
					public void run() {
						SkypeBackend.sound.play("out_call"); 
					}
				}).start();
		addCall(SkypeBackend.skypeMultiChatServiceAction.roomID, call);

	}

	@Override
	public void declineCall() {
		String[] part= new String[SkypeBackend.skypeMultiChatServiceAction.participants.size()];
		SkypeBackend.skypeMultiChatServiceAction.participants.copyInto(part); 
		call = SkypeBackend.skype.GetConversationByParticipants(part, false, false);
		call.LeaveLiveSession(true);
		SkypeBackend.sound.stop();
	}

	@Override
	public void finishCall() {

		HashMap<String, String> param = new HashMap<String, String>();
		SkypeBackend.skypeMultiChatServiceAction.SendExtensionProtocolMessage(ExtensionConstants.CALL_FINISHED, param);


	}



	@Override
	public boolean isCalling() {
		String[] part= new String[SkypeBackend.skypeMultiChatServiceAction.participants.size()];
		SkypeBackend.skypeMultiChatServiceAction.participants.copyInto(part); 
		call = SkypeBackend.skype.GetConversationByParticipants(part, false, false);

		Conversation.LOCAL_LIVESTATUS liveStatus =
				Conversation.LOCAL_LIVESTATUS.get(call.GetIntProperty(Conversation.PROPERTY.local_livestatus));

		if (liveStatus.equals(Conversation.LOCAL_LIVESTATUS.IM_LIVE))
			return true;
		else
			return false;

	}

	public void addCall(String conferenceId, Conversation call2) {
		call = call2;
		this.conferenceId = conferenceId;
	}

	@Override
	public  void endCall(){
		String[] partc= new String[SkypeBackend.skypeMultiChatServiceAction.participants.size()];
		SkypeBackend.skypeMultiChatServiceAction.participants.copyInto(partc); 
		call = SkypeBackend.skype.GetConversationByParticipants(partc, false, false);
		call.LeaveLiveSession(false);
	};

}
