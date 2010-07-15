package it.uniba.di.cdg.xcore.econference.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public abstract class AbstractSwitchBackendRelaunchAction implements IWorkbenchWindowActionDelegate {

	protected static final String PROP_EXIT_CODE = "eclipse.exitcode";
	protected static final String PROP_EXIT_DATA = "eclipse.exitdata";
	protected static final String PROP_VM = "eclipse.vm";
	protected static final String PROP_VMARGS = "eclipse.vmargs";
	protected static final String PROP_COMMANDS = "eclipse.commands";
	protected static final String CMD_DATA = "-data";
	protected static final String CMD_VMARGS = "-vmargs";
	protected static final String NEW_LINE = "\n";
	protected IWorkbenchWindow window;

	public AbstractSwitchBackendRelaunchAction() {
		super();
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
		window = null;
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	protected String buildCommandLine(String newBackend, String oldBackend) {
		StringBuffer result = new StringBuffer(512);
		String property = System.getProperty(PROP_VM);
		if (property != null) {
			result.append(property);
			result.append(NEW_LINE);
		}
		// append the vmargs and commands. Assume that these already end in \n
		String vmargs = System.getProperty(PROP_VMARGS);
		if (vmargs != null) {
			result.append(vmargs);
		}

		// append the rest of the args, replacing or adding -data as required
		property = System.getProperty(PROP_COMMANDS);
		if (property == null) {
			result.append(CMD_DATA);
			result.append(NEW_LINE);
			result.append(newBackend);
			result.append(NEW_LINE);
		} else {
			// find the index of the arg to replace its value
			int cmd_data_pos = property.lastIndexOf(CMD_DATA);
			if (cmd_data_pos != -1) {
				int begin = property.lastIndexOf(oldBackend);
				int end = begin + oldBackend.length() + 1;
				result.append(property.substring(0, begin));
				result.append(newBackend);
				result.append(NEW_LINE);
				result.append(property.substring(end,
						property.length()));				
			} else {
				result.append(CMD_DATA);
				result.append(NEW_LINE);
				result.append(newBackend);
				result.append(NEW_LINE);
				result.append(property);
			}
		}

		// put the vmargs back at the very end (the eclipse.commands property
		// already contains the -vm arg)
		if (vmargs != null) {
			result.append(CMD_VMARGS);
			result.append(NEW_LINE);
			result.append(vmargs);
		}

		return result.toString();
	}
}