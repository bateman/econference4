package it.uniba.di.cdg.skype.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SkypeJoinChatRoomDialog extends Dialog {

	private Shell dialogShell;
	private SkypeJoinChatRoomComposite ui;
	private String[] users;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			SkypeJoinChatRoomDialog inst = new SkypeJoinChatRoomDialog(display.getActiveShell());
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SkypeJoinChatRoomDialog(IShellProvider parent) {
		super(parent);
	}
	
	public SkypeJoinChatRoomDialog(Shell parent) {
		super(parent);
	}


	@Override
    protected Control createDialogArea( Composite parent ) {
        ui = new SkypeJoinChatRoomComposite( parent, SWT.NONE );
        return ui;
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {    
    	int[] userSelected = ui.tableUsers.getSelectionIndices();
    	users = new String[userSelected.length];
    	for(int i=0; i<userSelected.length; i++){
    		users[i] = ui.tableUsers.getItem(userSelected[i]).getText(0);
    	}
        super.okPressed();
    }
    
    public String[] getUsersSelected(){
    	return users;
    }
	
}
