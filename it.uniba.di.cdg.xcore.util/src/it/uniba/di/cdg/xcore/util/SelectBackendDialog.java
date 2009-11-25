package it.uniba.di.cdg.xcore.util;

import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
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
public class SelectBackendDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Vector<String> defaultBackend;
	private Set<String> backends;
	private Button buttonOk;
	private List listBackends;

	public SelectBackendDialog(Shell parent, Vector<String> defaultBackend, 
			Set<String> backends){
		super(parent, SWT.NULL);
		this.defaultBackend = defaultBackend;
		this.backends = backends;
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.VERTICAL);
			dialogShellLayout.type = SWT.VERTICAL;

			dialogShell.setLayout(dialogShellLayout);
			dialogShell.setText("Protocol");
			{
				listBackends = new List(dialogShell, SWT.NONE);
				listBackends.setItems(backends.toArray(new String[backends.size()]));
				listBackends.select(0);
			}
			{
				buttonOk = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
				dialogShell.setDefaultButton(buttonOk);
				dialogShell.addShellListener(new ShellAdapter() {
					public void shellClosed(ShellEvent evt) {
						dialogShellShellClosed(evt);
					}
				});
				buttonOk.setText("Ok");
				buttonOk.setAlignment(SWT.DOWN);
				buttonOk.addMouseListener(new MouseAdapter() {
					public void mouseUp(MouseEvent evt) {
						buttonOkMouseUp(evt);
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
	
	private void dialogShellShellClosed(ShellEvent evt) {
		defaultBackend.add(listBackends.getItem(listBackends.getSelectionIndex()));
	}
	
	private void buttonOkMouseUp(MouseEvent evt) {
		dialogShell.close();
	}

}
