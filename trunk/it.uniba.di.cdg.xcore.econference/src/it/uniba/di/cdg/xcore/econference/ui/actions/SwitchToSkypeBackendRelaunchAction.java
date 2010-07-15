package it.uniba.di.cdg.xcore.econference.ui.actions;

import it.uniba.di.cdg.xcore.econference.EConferencePlugin;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public class SwitchToSkypeBackendRelaunchAction extends AbstractSwitchBackendRelaunchAction  {
	/**
     * The unique id of this action.
     */
    public static final String ID = EConferencePlugin.ID + "ui.actions.SwitchToSkypeBackendRelaunchAction";
	/**
	 * The constructor.
	 */
	public SwitchToSkypeBackendRelaunchAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		System.setProperty(PROP_EXIT_CODE, IApplication.EXIT_RELAUNCH.toString());
		String cmdline = buildCommandLine("Skype", "Jabber");
		System.out.println(cmdline);
		System.setProperty(PROP_EXIT_DATA, cmdline);
		System.out.println(System.getProperty(PROP_EXIT_DATA));
		window.getWorkbench().restart();
	}

}