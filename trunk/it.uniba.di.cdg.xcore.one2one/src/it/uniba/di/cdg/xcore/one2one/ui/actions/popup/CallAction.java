package it.uniba.di.cdg.xcore.one2one.ui.actions.popup;

import org.eclipse.jface.action.IAction;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.one2one.ChatPlugin;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.actions.AbstractBuddyActionDelegate;


public class CallAction extends AbstractBuddyActionDelegate {

	public CallAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		IBackend backend = NetworkPlugin.getDefault().getHelper().getRoster().getBackend();
		ICallAction callAction = backend.getCallAction();
		if(callAction.isCalling(getSelected().getId())){
			callAction.finishCall(getSelected().getId());
		}else{
			final ChatContext chatContext = new ChatContext( getSelected().getId() );
			ChatPlugin.getDefault().openChatWindow( chatContext ); 
			callAction.call(getSelected().getId());
		}
	}

}
