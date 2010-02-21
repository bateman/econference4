package it.uniba.di.cdg.xcore.ui.dialogs;

import java.util.Iterator;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

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
public class RenameGroupDialog extends Dialog {
    private Shell dialogShell;
    private Label emailLabel;
    private Label oldNamelabel;
    private Group newContactGroup;
    private Button undoButton;
    private Button sendButton;
    private Label usernameLabel;
    private Text newNameText;

    public RenameGroupDialog( Shell parent, int style ) {
        super( parent, style );
        // TODO Auto-generated constructor stub
        
    }
    
    public void open(IBuddyGroup buddyGroup) {
        try {
            Shell parent = getParent();
            final String current = buddyGroup.getName();
            Iterator<IBuddy> buddies = buddyGroup.getBuddies().iterator();
            Boolean emptygroup = false;
            IBuddyRoster roster_ap = null;
            if(!buddies.hasNext()){
            	roster_ap = NetworkPlugin.getDefault().getHelper().getRoster();
            	emptygroup = true;
			}
            else {
				roster_ap = buddies.next().getRoster();
				emptygroup = false;
			}
            final Boolean empty= emptygroup;
            final IBuddyRoster roster = roster_ap;
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM
                    | SWT.APPLICATION_MODAL);
            dialogShell.setText("RenameGroup");
            {
                newContactGroup = new Group(dialogShell, SWT.NONE);
                newContactGroup.setLayout(null);
                newContactGroup.setText("Rename group");
                newContactGroup.setBounds(0, 0, 318, 127);
                {
                    undoButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    undoButton.setText("Cancel");
                    undoButton.setBounds(250, 93, 60, 30);
                    undoButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                                dialogShell.dispose();
                           }
                    });
                }
                {
                    sendButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    sendButton.setText("Ok");
                    sendButton.setBounds(178, 93, 60, 30);
                    sendButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                           try{
                        	   if (empty){
                        		   roster.reload();
                        		   roster.addGroup(newNameText.getText());
                        	   }
                        	   else{
                        		   roster.renameGroup( current, newNameText.getText());
                        	   }
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
                    usernameLabel.setText("Old group name");
                    usernameLabel.setBounds(7, 20, 115, 15);
                }
                {
                    emailLabel = new Label(newContactGroup, SWT.NONE);
                    emailLabel.setText("New group name");
                    emailLabel.setBounds(7, 51, 110, 21);
                }
                {
                    newNameText = new Text(newContactGroup, SWT.NONE);
                    newNameText.setBounds(152, 51, 158, 19);
                }
                {
                    oldNamelabel = new Label(newContactGroup, SWT.NONE);
                    oldNamelabel.setText( buddyGroup.getName() );
                    oldNamelabel.setBounds(155, 20, 155, 19);
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
