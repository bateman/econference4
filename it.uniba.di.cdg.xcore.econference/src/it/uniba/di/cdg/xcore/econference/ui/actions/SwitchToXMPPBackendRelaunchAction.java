package it.uniba.di.cdg.xcore.econference.ui.actions;

import it.uniba.di.cdg.xcore.econference.EConferencePlugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SwitchToXMPPBackendRelaunchAction extends
		AbstractSwitchBackendRelaunchAction {

	/**
	 * The unique id of this action.
	 */
	public static final String ID = EConferencePlugin.ID
			+ "ui.actions.SwitchToXMPPBackendRelaunchAction";

	/**
	 * The constructor.
	 */
	public SwitchToXMPPBackendRelaunchAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		saveNextBackend("Jabber");
		window.getWorkbench().restart();
	}

}