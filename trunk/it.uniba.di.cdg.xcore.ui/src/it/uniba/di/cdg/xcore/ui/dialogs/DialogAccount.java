package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.ProfileContext;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DialogAccount extends org.eclipse.swt.widgets.Dialog {

	private Group group1;
	private Button btnConfirm;
	private Text txtJabberID;
	private Text txtEMail;
	private Text txtPassword;
	private Text txtConfPassword;
	private Label lblConfirmPassword;
	private Label lblPassword;
	private Label lblEMail;
	private Text txtName;
	private Label label11;
	private Label lblUrl;
	private Label lblPhone;
	private Label lblZipCode;
	private Text txtZip;
	private Text txtPhone;
	private Text txtUrl;
	private Text txtState;
	private Text txtCity;
	private Label lblState;
	private Label lblCity;
	private Text txtServer;
	private Label lblServer;
	private Label lblJabberId;
	private Label lblFullName;
	private Button btnUndo;
	private Shell dialogShell;
	private ProfileContext profile;
	private Map<String, String> info = new HashMap<String, String>();

	public DialogAccount(Shell parent, int style, ProfileContext prof) {
		super(parent, style);
		this.profile = prof;
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM
					| SWT.APPLICATION_MODAL);
			dialogShell.setLayout(new FormLayout());
			dialogShell.layout();
			createGroup();
			initializeTxt(profile);
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

	public void createGroup() {
		{
			group1 = new Group(dialogShell, SWT.NONE);
			group1.setLayout(null);
			group1.setText("New Account Information");
			FormData group1LData = new FormData();
			group1LData.width = 334;
			group1LData.height = 338;
			group1.setLayoutData(group1LData);
			{
				txtServer = new Text(group1, SWT.BORDER);
				txtServer.setText("");
				txtServer.setBounds(142, 92, 173, 18);
			}
			{
				lblServer = new Label(group1, SWT.NONE);
				lblServer.setText("Server:");
				lblServer.setBounds(25, 90, 55, 13);
			}
			{
				lblFullName = new Label(group1, SWT.NONE);
				lblFullName.setText("Full Name * :");
				lblFullName.setBounds(25, 118, 70, 18);
			}
			{
				lblJabberId = new Label(group1, SWT.NONE);
				lblJabberId.setText("Jabber Id:");
				lblJabberId.setBounds(25, 12, 55, 18);
			}
			{
				lblEMail = new Label(group1, SWT.NONE);
				lblEMail.setText("E-Mail * :");
				lblEMail.setBounds(25, 144, 44, 18);
			}
			{
				lblPassword = new Label(group1, SWT.NONE);
				lblPassword.setText("Password:");
				lblPassword.setBounds(25, 38, 70, 18);
			}
			{
				lblConfirmPassword = new Label(group1, SWT.NONE);
				lblConfirmPassword.setText("Confirm Password * :");
				lblConfirmPassword.setBounds(25, 64, 105, 18);
			}
			{
				txtConfPassword = new Text(group1, SWT.BORDER | SWT.PASSWORD);
				txtConfPassword.setText("");
				txtConfPassword.setBounds(142, 66, 173, 18);
			}
			{
				txtPassword = new Text(group1, SWT.BORDER | SWT.PASSWORD);
				txtPassword.setText("");
				txtPassword.setBounds(142, 40, 173, 18);
			}

			{
				txtJabberID = new Text(group1, SWT.BORDER);
				txtJabberID.setText("");
				txtJabberID.setOrientation(SWT.HORIZONTAL);
				txtJabberID.setBounds(142, 14, 173, 18);
			}
			{
				txtName = new Text(group1, SWT.BORDER);
				txtName.setText("");
				txtName.setBounds(142, 119, 173, 18);
			}
			{
				txtEMail = new Text(group1, SWT.BORDER);
				txtEMail.setText("");
				txtEMail.setBounds(142, 145, 173, 18);
			}
			{
				lblCity = new Label(group1, SWT.NONE);
				lblCity.setText("City:");
				lblCity.setBounds(25, 171, 55, 18);
			}
			{
				lblState = new Label(group1, SWT.NONE);
				lblState.setText("State:");
				lblState.setBounds(25, 197, 44, 18);
			}
			{
				txtCity = new Text(group1, SWT.BORDER);
				txtCity.setText("");
				txtCity.setBounds(142, 171, 173, 18);
			}
			{
				txtState = new Text(group1, SWT.BORDER);
				txtState.setText("");
				txtState.setBounds(142, 197, 173, 18);
			}
			{
				txtZip = new Text(group1, SWT.BORDER);
				txtZip.setText("");
				txtZip.setBounds(142, 223, 173, 18);
			}
			{
				txtPhone = new Text(group1, SWT.BORDER);
				txtPhone.setText("");
				txtPhone.setBounds(142, 250, 173, 18);
			}
			{
				txtUrl = new Text(group1, SWT.BORDER);
				txtUrl.setText("");
				txtUrl.setBounds(142, 276, 173, 18);
			}
			{
				lblZipCode = new Label(group1, SWT.NONE);
				lblZipCode.setText("ZIP Code:");
				lblZipCode.setBounds(25, 223, 55, 18);
			}
			{
				lblPhone = new Label(group1, SWT.NONE);
				lblPhone.setText("Phone:");
				lblPhone.setBounds(25, 250, 55, 18);
			}
			{
				lblUrl = new Label(group1, SWT.NONE);
				lblUrl.setText("URL website:");
				lblUrl.setBounds(25, 276, 78, 18);
			}
			{
				label11 = new Label(group1, SWT.NONE);
				label11.setText("The field * are required");
				label11.setBounds(203, 301, 112, 18);
			}

			{
				btnConfirm = new Button(group1, SWT.PUSH | SWT.CENTER);
				btnConfirm.setText("OK");
				btnConfirm.setSelection(true);
				btnConfirm.setAlignment(SWT.CENTER);
				btnConfirm.setBounds(131, 325, 86, 23);
				btnConfirm.addSelectionListener(new SelectionAdapter() {
					
					public void widgetSelected(SelectionEvent evt) {
						if ((checkPassword(txtPassword.getText(),
								txtConfPassword.getText()) == true)
								& (checkFields() == true)) {
							registerAccount();
						} else {
							UiPlugin.getUIHelper().showMessage(
									"ERROR: Entered data are not correct");
						}
					}
				});
			}
			{
				btnUndo = new Button(group1, SWT.PUSH | SWT.CENTER);
				btnUndo.setText("Undo");
				btnUndo.setBounds(229, 325, 86, 23);
				btnUndo.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						dialogShell.dispose();
					}
				});
			}
		}
	}

	public void initializeTxt(ProfileContext profile) {
		String jid = profile.getUserContext().getId();
		txtJabberID.insert(jid);
		txtJabberID.setEnabled(false);
		String pass = profile.getUserContext().getPassword();
		txtPassword.setText(pass);
		txtPassword.setEnabled(false);
		String serv = profile.getServerContext().getServerHost();
		txtServer.insert(serv);
		txtServer.setEnabled(false);
	}

	public boolean checkPassword(String passwd1, String passwd2) {
		boolean result = true;
		if (!passwd1.equals(passwd2)) {
			result = false;
		}
		return result;
	}

	public boolean checkFields() {
		boolean result = true;
		if (txtEMail.getText().equals("") & txtName.getText().equals("")) {
			result = false;
		}
		if (!txtEMail.getText().contains("@")) {
			result = false;
		}
		return result;
	}

	private void registerAccount() {
		info.put("name", txtName.getText());
		info.put("email", txtEMail.getText());
		info.put("city", txtCity.getText());
		info.put("state", txtState.getText());
		info.put("zip", txtZip.getText());
		info.put("phone", txtPhone.getText());
		info.put("url", txtUrl.getText());
		Date x = new Date();
		info.put("date", x.toString());
		
		IBackend backend = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();	
		try{
			backend.registerNewAccount(profile.getUserContext().getId(),
					profile.getUserContext().getPassword(), profile.getServerContext(), info);
			UiPlugin.getUIHelper().showMessage(	"The New Account was correctly created !");
		} catch (Exception e) {
			e.printStackTrace();
			UiPlugin
					.getUIHelper()
					.showMessage(
							"ERROR: The new Account was not created on the server");
		}
		dialogShell.dispose();
	}

}
