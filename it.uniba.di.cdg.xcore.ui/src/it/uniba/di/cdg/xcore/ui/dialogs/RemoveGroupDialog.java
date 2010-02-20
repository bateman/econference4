package it.uniba.di.cdg.xcore.ui.dialogs;

import java.util.Iterator;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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
public class RemoveGroupDialog extends Dialog {
    private Shell dialogShell;
    private Button undoButton;
    private Label whereLabel;
    private Button sendButton;
    private Label usernameLabel;
    private CCombo groupCombo;

    public RemoveGroupDialog( Shell parent, int style ) {
        super( parent, style );
        // TODO Auto-generated constructor stub
        
    }
    
    public void open(IBuddyGroup buddyGroup) {
        try {
            Shell parent = getParent();
            final String gruppo = buddyGroup.getName();
            Iterator<IBuddy> buddies = buddyGroup.getBuddies().iterator();
            if(!buddies.hasNext()){
            	NetworkPlugin.getDefault().getHelper().getRoster().reload();
            	return;
			}
            final IBuddyRoster roster = buddies.next().getRoster();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM
                    | SWT.APPLICATION_MODAL);
            dialogShell.setText("RemoveGroup");
            {
                usernameLabel = new Label(dialogShell, SWT.NONE);
                usernameLabel.setText("Want to remove the group " + buddyGroup.getName() + " ?");
                usernameLabel.setBounds(12, 24, 366, 15);
            }
            {
                sendButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                sendButton.setText("OK");
                sendButton.setBounds(331, 97, 60, 30);
                sendButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                       try{
                        roster.removeGroup( gruppo,groupCombo.getItem(groupCombo.getSelectionIndex()).toString() );
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
                undoButton.setBounds(397, 97, 60, 30);
                undoButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                            dialogShell.dispose();
                       }
                });
            }
            {
            	whereLabel = new Label(dialogShell, SWT.NONE);
            	whereLabel.setText("Where you want to move the buddies items?");
            	whereLabel.setBounds(12, 51, 276, 19);
            }
            {
                groupCombo = new CCombo(dialogShell, SWT.NONE);
                groupCombo.setBounds(294, 51, 176, 18);
                groupCombo.add( "None" );
                Iterator<IBuddyGroup> iter= roster.getAllGroups().iterator();
                for(int i=0; i<roster.getAllGroups().size();i++){
                	IBuddyGroup group = iter.next();
                	if(!(group.getName().equals(buddyGroup.getName()))){
                    groupCombo.add( group.getName() );
                	}
                }
                groupCombo.setEditable( false );
                groupCombo.select( 0 );
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
