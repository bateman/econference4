package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class RemoveContactDialog extends Dialog {
    private Shell dialogShell;
    private Button undoButton;
    private Button sendButton;
    private Label usernameLabel;
    

    public RemoveContactDialog( Shell parent, int style ) {
        super( parent, style );
        // TODO Auto-generated constructor stub
        
    }
    
    public void open(IBuddy buddy) {
        try {
            final IBuddyRoster roster =  buddy.getRoster();
            final String contatto = buddy.getId().split( "[/]")[0];
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM
                    | SWT.APPLICATION_MODAL);
            dialogShell.setText("RemoveContact");
            {
                usernameLabel = new Label(dialogShell, SWT.NONE);
                usernameLabel.setText("Want to remove the contact " + buddy.getName() + "?");
                usernameLabel.setBounds(12, 24, 366, 15);
            }
            {
                sendButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                sendButton.setText("OK");
                sendButton.setBounds(246, 66, 60, 30);
                sendButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                       try{
                        roster.removeBuddy( contatto );
                        }
                       catch(Exception e){
                           e.getMessage();
                       }
                       finally{
                           dialogShell.dispose();
                       }
                    }
                });
            }
            {
                undoButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                dialogShell.setDefaultButton(undoButton);
                undoButton.setText("Cancel");
                undoButton.setBounds(318, 66, 60, 30);
                undoButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                            dialogShell.dispose();
                       }
                });
            }

            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setLocation(getParent().toDisplay(100, 100));
            dialogShell.open();
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
       
    }
}
