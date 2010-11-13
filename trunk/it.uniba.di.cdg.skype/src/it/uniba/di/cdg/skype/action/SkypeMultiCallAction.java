package it.uniba.di.cdg.skype.action;

import java.util.HashMap;

import it.uniba.di.cdg.skype.ui.SkypeJoinChatRoomDialog;
import it.uniba.di.cdg.skype.util.ExtensionConstants;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.skype.Call;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeMultiCallAction implements IMultiCallAction {
	
	private Call call = null;
	private String conferenceId = "";
	private IBackend backend;
	private String[] participants = null;
	
	public SkypeMultiCallAction(IBackend backend){
		super();
		this.backend = backend;
	}

	@Override
	public void acceptCall() {
		backend.getCallAction().acceptCall(conferenceId);
	}

	@Override
	public void call() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		SkypeJoinChatRoomDialog dlg = new SkypeJoinChatRoomDialog(window);
		if(dlg.open()==Window.OK){
			participants = dlg.getUsersSelected();
			try {
				call = Skype.call(participants);
				((SkypeCallAction)backend.getCallAction()).addCall(conferenceId, call);
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void declineCall() {
		backend.getCallAction().declineCall(conferenceId);
	}

	@Override
	public void finishCall() {
		if(participants != null)
		{
			for(String s: participants)
			{
				HashMap<String, String> param = new HashMap<String, String>();
				backend.getChatServiceAction().SendExtensionProtocolMessage(s,
						ExtensionConstants.CALL_FINISHED, param);
			}
		}
		backend.getCallAction().finishCall(conferenceId);
	}

	@Override
	public boolean isCalling() {
		return backend.getCallAction().isCalling(conferenceId);
	}

	public void addCall(String conferenceId, Call call2) {
		call = call2;
		this.conferenceId = conferenceId;
	}

}
