package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
*
*/
public class MoveContactDialog extends Dialog {
	private Shell dialogShell;
	private Button undoButton;
	private CCombo groupCombo;
	private Text textNameBuddy;
	private Label labelBuddy;
	private Button sendButton;
	private Label usernameLabel;

	public MoveContactDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open(IBuddy buddy) {
		try {
			String buddyNome = buddy.getId();
			final String buddyName = buddyNome.split("/")[0];
			final IBuddyRoster roster = buddy.getRoster();
			Shell parent = getParent();

			dialogShell = new Shell(parent, SWT.DIALOG_TRIM
					| SWT.APPLICATION_MODAL);
			
			dialogShell.setText("ChangeGroup");
			
			{
				usernameLabel = new Label(dialogShell, SWT.NONE);
				usernameLabel.setText("Choose group");
				usernameLabel.setBounds(12, 69, 96, 15);
				
			}
			{
				sendButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				sendButton.setText("OK");
				sendButton.setBounds(171, 101, 60, 30);
				sendButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						try {
							roster.moveToGroup(
									buddyName,
									groupCombo.getItem(
											groupCombo.getSelectionIndex())
											.toString());
						} catch (Exception e) {
							e.getMessage();
						} finally {
							dialogShell.dispose();
						}
					}
				});
			}
			{
				undoButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				dialogShell.setDefaultButton(undoButton);
				undoButton.setText("Cancel");
				undoButton.setBounds(237, 101, 60, 30);
				undoButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						dialogShell.dispose();
					}
				});
			}
			{
				groupCombo = new CCombo(dialogShell, SWT.NONE);
				groupCombo.setBounds(120, 69, 176, 18);
				
				Iterator<IBuddyGroup> iter = roster.getAllGroups().iterator();
				IBuddyGroup buddygroup = null;
				if (!roster.getGroups(buddy).isEmpty()) {
					groupCombo.add("None");
					buddygroup = roster.getGroups(buddy).iterator().next();
				}
				for (int i = 0; i < roster.getAllGroups().size(); i++) {
					IBuddyGroup group = iter.next();
					if (buddygroup == null
							|| !(group.getName().equals(buddygroup.getName()))) {
						groupCombo.add(group.getName());
					}
				}
				groupCombo.setEditable(false);
				groupCombo.select(0);
				
			}
			{
				labelBuddy = new Label(dialogShell, SWT.NONE);
				labelBuddy.setText("Contact");
				labelBuddy.setBounds(12, 22, 96, 17);
			}
			{
				textNameBuddy = new Text(dialogShell, SWT.NONE);
				textNameBuddy.setBounds(120, 22, 176, 17);
				textNameBuddy.setText(buddy.getName());
				textNameBuddy.setEditable(false);
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
