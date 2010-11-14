package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
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
public class RenameContactDialog extends Dialog {
    private Shell dialogShell;
    private Label emailLabel;
    private Label oldNamelabel;
    private Group newContactGroup;
    private Button undoButton;
    private Button sendButton;
    private Label usernameLabel;
    private Text emailText;

    public RenameContactDialog( Shell parent, int style ) {
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
            dialogShell.setText("RenameContact");
            {
                newContactGroup = new Group(dialogShell, SWT.NONE);
                newContactGroup.setLayout(null);
                newContactGroup.setText("Rename contact");
                newContactGroup.setBounds(0, 0, 281, 127);
                {
                    undoButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    undoButton.setText("Cancel");
                    undoButton.setBounds(215, 93, 60, 30);
                    undoButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                                dialogShell.dispose();
                           }
                    });
                }
                
                {
                    usernameLabel = new Label(newContactGroup, SWT.NONE);
                    usernameLabel.setText("Old nick");
                    usernameLabel.setBounds(7, 20, 95, 15);
                }
                {
                    emailLabel = new Label(newContactGroup, SWT.NONE);
                    emailLabel.setText("New nick");
                    emailLabel.setBounds(7, 51, 60, 21);
                }
                {
                    emailText = new Text(newContactGroup, SWT.NONE);
                    emailText.setBounds(114, 51, 158, 19);
                }
                {
                    oldNamelabel = new Label(newContactGroup, SWT.NONE);
                    oldNamelabel.setText( buddy.getName() );
                    oldNamelabel.setBounds(114, 20, 155, 19);
                }
                {
                    sendButton = new Button(newContactGroup, SWT.PUSH | SWT.CENTER);
                    sendButton.setText("Ok");
                    sendButton.setBounds(143, 93, 60, 30);
                    sendButton.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                           try{
                            roster.renameBuddy(contatto, emailText.getText());
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
