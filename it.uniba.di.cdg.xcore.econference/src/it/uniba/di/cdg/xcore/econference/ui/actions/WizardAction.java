package it.uniba.di.cdg.xcore.econference.ui.actions;


import it.uniba.di.cdg.xcore.ui.dialogs.*;
import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.internal.EConferenceHelper;
import it.uniba.di.cdg.xcore.econference.ui.dialogs.InviteWizard;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionFactory;


public class WizardAction extends Action implements IWorkbenchWindowActionDelegate {
	
	private IWorkbenchWindow workbenchWindow;
	
    public static final String ID = EConferencePlugin.ID + "ui.actions.invitePeopleAction";

	public void init( IWorkbenchWindow window ) {
        this.workbenchWindow = window;
    }
	public void run(IAction action) {
		
		if (NetworkPlugin.getDefault().getHelper().getOnlineBackends().size() == 0) {
            UiPlugin.getUIHelper().showErrorMessage( "Please, connect first!" );
            return;
        }
		EConferencePlugin defaultPlugin = EConferencePlugin.getDefault();
        defaultPlugin.setHelper(new EConferenceHelper( UiPlugin.getUIHelper(), NetworkPlugin.getDefault().getHelper()));
		defaultPlugin.getDefault().getHelper().openInviteWizard();
	}
	 public void selectionChanged( IAction action, ISelection selection ) {
	        // Nothing
	    }
		
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			this.workbenchWindow = null;
		}
}
