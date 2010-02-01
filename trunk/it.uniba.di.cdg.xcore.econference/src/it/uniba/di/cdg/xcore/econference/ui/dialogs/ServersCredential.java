package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class ServersCredential extends Dialog {

	private Shell dialogShell;
	private Button buttonOk;
	Text nickName;
	Text password;
	GenInfoPage infopage;

	public ServersCredential(Shell parent,GenInfoPage info){
		super(parent, SWT.NULL);
		this.infopage = info;
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.VERTICAL);
			dialogShellLayout.type = SWT.VERTICAL;

			dialogShell.setLayout(dialogShellLayout);
			dialogShell.setText("Server's credential");
			new CLabel(dialogShell, SWT.NONE).setText("Nickname: ");
	        nickName = new Text(dialogShell, SWT.BORDER);
	        new CLabel(dialogShell, SWT.NONE).setText("Password: ");
	        password = new Text(dialogShell, SWT.BORDER | SWT.PASSWORD);
	        buttonOk = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
			dialogShell.setDefaultButton(buttonOk);
			buttonOk.setText("Ok");
			buttonOk.setAlignment(SWT.DOWN);
			buttonOk.addMouseListener(new MouseAdapter() {
				public void mouseUp(MouseEvent evt) {
					buttonOkMouseUp(evt);
				}
			});
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

	private void buttonOkMouseUp(MouseEvent evt) {
		this.infopage.setNick(this.nickName.getText());
		this.infopage.setPass(this.password.getText());
		dialogShell.close();
	}

}
