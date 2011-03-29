package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
public class NewGroupDialog extends Dialog {
    private Shell dialogShell;
    private Text groupName;
    private Group newContactGroup;
    private Button undoButton;
    private Button sendButton;
    private Label usernameLabel;
    
   
    public NewGroupDialog( Shell parent, int style ) {
        super( parent, style );
    }
    
    public void open(final IBuddyRoster roster) {
        try {
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM
                    | SWT.APPLICATION_MODAL);
            dialogShell.setText("NewGroup");
            {
                newContactGroup = new Group(dialogShell, SWT.NONE);
                newContactGroup.setLayout(null);
                newContactGroup.setText("Add new group");
                newContactGroup.setBounds(0, 0, 294, 91);
                {
                    undoButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    undoButton.setText("Cancel");
                    undoButton.setBounds(210, 51, 60, 30);
                    undoButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                                dialogShell.dispose();
                           }
                    });
                }
                {
                    sendButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    sendButton.setText("Ok");
                    sendButton.setBounds(138, 51, 60, 30);
                    sendButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                           try{
                        	   roster.addGroup(groupName.getText());
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
                    usernameLabel = new Label(newContactGroup, SWT.NONE);
                    usernameLabel.setText("Group name");
                    usernameLabel.setBounds(3, 20, 99, 15);
                }
                {
                    groupName = new Text(newContactGroup, SWT.NONE);
                    groupName.setBounds(114, 20, 158, 19);
                }
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
