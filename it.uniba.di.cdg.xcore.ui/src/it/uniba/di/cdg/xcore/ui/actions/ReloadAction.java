package it.uniba.di.cdg.xcore.ui.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.dialogs.ChangePasswordDialog;
import it.uniba.di.cdg.xcore.ui.dialogs.NewContactDialog;
import it.uniba.di.cdg.xcore.ui.dialogs.NewGroupDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

public class ReloadAction extends Action implements ActionFactory.IWorkbenchAction {

	public static final String ID = UiPlugin.ID + ".actions.ChangePasswordAction";
	
	public ReloadAction() {
		super();
    }
	
	public void run(IBuddyRoster roster) {
	    //setEnabled( false ); // Will be re-enabled in done()
	    		if (NetworkPlugin.getDefault().getRegistry().getDefaultBackend().isConnected() == false){
			UiPlugin.getUIHelper().showMessage("You must to be connected to the server in order to do this!");
		}else {
		    roster.reload();
		}
	    }
	public void Enable (boolean valore) {
		setEnabled(valore);
	}
	
	@Override
	public void dispose() {
	}
}