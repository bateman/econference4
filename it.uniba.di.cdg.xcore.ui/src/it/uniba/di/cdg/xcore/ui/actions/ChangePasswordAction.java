package it.uniba.di.cdg.xcore.ui.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.dialogs.ChangePasswordDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

public class ChangePasswordAction extends Action implements ActionFactory.IWorkbenchAction {

	public static final String ID = UiPlugin.ID + ".actions.ChangePasswordAction";
	
	public ChangePasswordAction( IWorkbenchWindow window ) {
		super();
        setId( "password" );
        //setActionDefinitionId( ID );
        setText( "Modify Password..." );
        //setEnabled (false);
    }
	
	public void run() {
	    //setEnabled( false ); // Will be re-enabled in done()
	    Display display = Display.getDefault();
		Shell shell = new Shell(display);
		ChangePasswordDialog inst = new ChangePasswordDialog(shell, SWT.NULL);
		if (NetworkPlugin.getDefault().getRegistry().getDefaultBackend().isConnected() == false){
			UiPlugin.getUIHelper().showMessage("You must to be connected to the server in order to do this!");
		}else {
		inst.open();
		}
	    }
	public void Enable (boolean valore) {
		setEnabled(valore);
	}
	
	@Override
	public void dispose() {
	}}