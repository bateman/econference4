package it.uniba.di.cdg.xcore.ui.dialogs;



import java.util.Iterator;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.views.BuddyListView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
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
public class NewContactDialog extends Dialog {
    private Shell dialogShell;
    private Text usernameText;
    private Text aliasText;
    private CCombo groupCombo;
    private Group newContactGroup;
    private Button undoButton;
    private Button sendButton;
    private Label groupLabel;
    private Label aliasLabel;
    private Label usernameLabel;

    public NewContactDialog( Shell parent, int style ) {
        super( parent, style );
        // TODO Auto-generated constructor stub
        
    }
    
    public void open(final IBuddyRoster roster) {
        try {
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM
                    | SWT.APPLICATION_MODAL);
            dialogShell.setText("NewContact");
            {
                newContactGroup = new Group(dialogShell, SWT.NONE);
                newContactGroup.setLayout(null);
                newContactGroup.setText("Add new contact");
                newContactGroup.setBounds(0, 0, 291, 148);
                {
                    groupLabel = new Label(newContactGroup, SWT.NONE);
                    groupLabel.setText("Group");
                    groupLabel.setBounds(7, 74, 60, 21);
                }
                {
                    undoButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    undoButton.setText("Cancel");
                    undoButton.setBounds(207, 116, 60, 30);
                    undoButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                                dialogShell.dispose();
                           }
                    });
                }
                {
                    sendButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    sendButton.setText("Ok");
                    sendButton.setBounds(141, 116, 60, 30);
                    sendButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                           try{
                        	String gruppo = groupCombo.getItem(groupCombo.getSelectionIndex());
                        	final String[] group = {gruppo};         
                            roster.addBuddy(aliasText.getText(), usernameText.getText(), group);
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
                    aliasLabel = new Label(newContactGroup, SWT.NONE);
                    aliasLabel.setText("Nickname");
                    aliasLabel.setBounds(7, 47, 60, 21);
                }
                {
                    usernameLabel = new Label(newContactGroup, SWT.NONE);
                    usernameLabel.setText("Username");
                    usernameLabel.setBounds(7, 20, 64, 15);
                }
                {
                	groupCombo = new CCombo(dialogShell, SWT.NONE);
                    groupCombo.setBounds(113, 74, 161, 18);
                    Iterator<IBuddyGroup> iter= roster.getAllGroups().iterator();
                    for(int i=0; i<roster.getAllGroups().size();i++){
                        IBuddyGroup group = iter.next();
                        groupCombo.add( group.getName() );
                    }
                    groupCombo.setEditable( false );
                    groupCombo.select( 0 );
                }
                {
                    aliasText = new Text(newContactGroup, SWT.NONE);
                    aliasText.setBounds(114, 20, 158, 19);
                }
                {
                    usernameText = new Text(newContactGroup, SWT.NONE);
                    usernameText.setBounds(114, 47, 158, 19);
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
