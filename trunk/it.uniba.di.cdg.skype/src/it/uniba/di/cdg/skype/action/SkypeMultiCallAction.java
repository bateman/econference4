package it.uniba.di.cdg.skype.action;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.skype.Call;
import com.skype.Skype;
import com.skype.SkypeException;

import it.uniba.di.cdg.skype.ui.SkypeJoinChatRoomDialog;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;

public class SkypeMultiCallAction implements IMultiCallAction {
	
	Call call = null;
	private String conferenceId = "";
	private IBackend backend;
	
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
		String[] partecipants;
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		SkypeJoinChatRoomDialog dlg = new SkypeJoinChatRoomDialog(window);
		if(dlg.open()==Window.OK){
			partecipants = dlg.getUsersSelected();
			try {
				call = Skype.call(partecipants);
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
