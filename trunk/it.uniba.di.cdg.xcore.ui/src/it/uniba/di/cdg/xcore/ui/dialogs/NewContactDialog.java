package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
* 
*/
public class NewContactDialog extends Dialog {

	private Shell dialogShell;
    private Text usernameText;
    private Text aliasText;
    private CCombo groupCombo;
    private Label labelerror;
    private Group newContactGroup;
    private Button undoButton;
    private Button sendButton;
    private Label groupLabel;
    private Label aliasLabel;
    private Label usernameLabel;

    public NewContactDialog( Shell parent, int style ) {
        super( parent, style );        
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
                newContactGroup.setBounds(0, 0, 291, 157);
                {
                    groupLabel = new Label(newContactGroup, SWT.NONE);
                    groupLabel.setText("Group");
                    groupLabel.setBounds(7, 74, 60, 21);
                }
                {
                    undoButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    undoButton.setText("Cancel");
                    undoButton.setBounds(213, 122, 60, 30);
                    undoButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                                dialogShell.dispose();
                           }
                    });
                }
                {
                    sendButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    sendButton.setText("Ok");
                    sendButton.setBounds(147, 122, 60, 30);
                    sendButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                        	String gruppo = groupCombo.getItem(groupCombo.getSelectionIndex());
                        	String username = usernameText.getText();
                        	if(username.matches("[A-Za-z0-9_.-]+@[A-Za-z0-9_.]+[.]{1}[A-Za-z]{2,4}")){
                        		final String[] group = {gruppo};
                        		roster.addBuddy(username,aliasText.getText(), group);
                        		dialogShell.dispose();
                        	}
                        	else{
                        		labelerror.setVisible(true);
                        		
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
                    usernameLabel.setText("Username*");
                    usernameLabel.setBounds(7, 20, 79, 15);
                }
                {
                	groupCombo = new CCombo(dialogShell, SWT.NONE);
                    groupCombo.setBounds(119, 75, 161, 18);
                    groupCombo.add( "None" );
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
                    aliasText.setBounds(121, 47, 158, 19);
                }
                {
                    usernameText = new Text(newContactGroup, SWT.NONE);
                    usernameText.setBounds(121, 20, 158, 19);
                }
                {
                	labelerror = new Label(newContactGroup, SWT.NONE);
                	labelerror.setText("username is not valid");
                	labelerror.setBounds(86, 103, 184, 20);
                	labelerror.setAlignment(SWT.RIGHT);
                	labelerror.setVisible(false);
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
