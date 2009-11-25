package it.uniba.di.cdg.xcore._1to1.ui.actions.popup;

import org.eclipse.jface.action.IAction;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
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
			callAction.call(getSelected().getId());
		}
	}

}
