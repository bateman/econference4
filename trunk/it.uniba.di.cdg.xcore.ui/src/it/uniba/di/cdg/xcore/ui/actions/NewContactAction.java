package it.uniba.di.cdg.xcore.ui.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.dialogs.NewContactDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory;

public class NewContactAction extends Action implements
		ActionFactory.IWorkbenchAction {

	public static final String ID = UiPlugin.ID
			+ ".actions.ChangePasswordAction";

	public NewContactAction() {
		super();
	}

	public void run(IBuddyRoster roster) {
		// setEnabled( false ); // Will be re-enabled in done()
		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		String backend = "it.uniba.di.cdg.skype.skypeBackend";
		NewContactDialog contact = new NewContactDialog(shell, SWT.NULL);
		if (NetworkPlugin.getDefault().getRegistry().getDefaultBackend()
				.isConnected() == false) {
			UiPlugin.getUIHelper()
					.showMessage(
							"You must to be connected to the server in order to do this!");
		} else if (roster.getBackend().getBackendId().equals(backend)) {
			UiPlugin.getUIHelper().showMessage(
					"This function is unimplemented on Skype!");

		} else {
			contact.open(roster);
		}
	}

	public void Enable(boolean valore) {
		setEnabled(valore);
	}

	@Override
	public void dispose() {
	}
}