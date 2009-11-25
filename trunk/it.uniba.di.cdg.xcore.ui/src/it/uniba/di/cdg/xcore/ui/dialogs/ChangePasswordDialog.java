package it.uniba.di.cdg.xcore.ui.dialogs;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.ProfileContext;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.actions.ConnectAction;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
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
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

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
public class ChangePasswordDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Group groupPassword;
	private Text txtNewPassword;
	private Label currentPasswdLbl;
	private Label newPasswdLbl;
	private Text txtConfirmPassword;
	private Button btnConfirm;
	private Button btnCancel;
	private Text txtPassword;
	private Label confirmPasswdLbl;
	private Text txtJabberId;
	private Label jidLbl;

	private static final String CONFIGURATION_NODE_QUALIFIER = UiPlugin.ID;

	public static final String USERID = "userid";

	private static final String PASSWORD = "password";

	private static final String SERVER = "server";

	private static final String NAME = "name";

	private static final String EMAIL = "email";

	private static final String SECURE = "secure";

	private static final String USE_DEFAULT_PORT = "default-port";

	private static final String PORT = "port";

	private static final String SAVED_PROFILES = "saved-connection-profiles";

	private static final String LAST_USER = "last-connection-profile";

	private String password;

	private ProfileContext profileContext;

	private Map<String, ProfileContext> savedProfileContexts;

	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */
	/*
	 * public static void main(String[] args) { try { Display display =
	 * Display.getDefault(); Shell shell = new Shell(display);
	 * ChangePasswordDialog inst = new ChangePasswordDialog(shell, SWT.NULL);
	 * inst.open(); } catch (Exception e) { e.printStackTrace(); } }
	 */

	public ChangePasswordDialog(Shell parent, int style) {
		super(parent, style);
		savedProfileContexts = new HashMap<String, ProfileContext>();
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM
					| SWT.APPLICATION_MODAL);

			dialogShell.setLayout(new FormLayout());
			dialogShell.setText("Modify Password...");
			{
				groupPassword = new Group(dialogShell, SWT.NONE);
				groupPassword.setLayout(null);
				FormData groupPasswordLData = new FormData();
				groupPasswordLData.width = 275;
				groupPasswordLData.height = 169;
				groupPassword.setLayoutData(groupPasswordLData);
				groupPassword.setText("Change Password");
				{
					txtPassword = new Text(groupPassword, SWT.BORDER);
					txtPassword.setText("");
					txtPassword.setEnabled(true);
					txtPassword.setEchoChar('*');
					txtPassword.setFocus();
					txtPassword.setTabs(1);
					txtPassword.setBounds(139, 48, 119, 19);
				}
				{
					txtNewPassword = new Text(groupPassword, SWT.BORDER);
					txtNewPassword.setText("");
					txtNewPassword.setEnabled(true);
					txtNewPassword.setEchoChar('*');
					txtNewPassword.setTabs(2);
					txtNewPassword.setBounds(139, 81, 119, 19);
					txtNewPassword.setTabs(9);
				}	
				{
					txtConfirmPassword = new Text(groupPassword, SWT.BORDER);
					txtConfirmPassword.setText("");
					txtConfirmPassword.setEchoChar('*');
					txtConfirmPassword.setEnabled(true);
					txtConfirmPassword.setTabs(3);
					txtConfirmPassword.setBounds(139, 114, 119, 19);
					txtConfirmPassword.setTabs(10);
				}
				{
					currentPasswdLbl = new Label(groupPassword, SWT.NONE);
					currentPasswdLbl.setText("Current Password :");
					currentPasswdLbl.setBounds(12, 48, 96, 13);
				}
				{
					confirmPasswdLbl = new Label(groupPassword, SWT.NONE);
					confirmPasswdLbl.setText("Confirm new Password :");
					confirmPasswdLbl.setBounds(12, 114, 115, 13);
				}
				{
					newPasswdLbl = new Label(groupPassword, SWT.NONE);
					newPasswdLbl.setText("New Password :");
					newPasswdLbl.setBounds(12, 81, 83, 13);
				}
				
				{
					btnConfirm = new Button(groupPassword, SWT.PUSH
							| SWT.CENTER);
					btnConfirm.setText("OK");
					btnConfirm.setSelection(true);
					btnConfirm.setAlignment(SWT.CENTER);
					btnConfirm.setBounds(120, 149, 60, 24);
					btnConfirm.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							if ((arePasswordsEqual(txtNewPassword.getText(),
									txtConfirmPassword.getText()) == true)
									& (checkPassword() == true)) {

								try {
									NetworkPlugin.getDefault().getRegistry().getDefaultBackend().connect(profileContext.getServerContext(), profileContext.getUserContext());
									NetworkPlugin.getDefault().getRegistry().getDefaultBackend().changePassword(txtNewPassword.getText());
									profileContext.getUserContext().setPassword(txtNewPassword.getText());
									savePreferences();
									UiPlugin.getUIHelper().showMessage(
											"Password changed correctly!");
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									UiPlugin.getUIHelper().showMessage(
											"ERROR: no changes executed!");
								} 
								dialogShell.dispose();
							} else {
								UiPlugin.getUIHelper().showMessage(
										"ERROR: data not inserted correctly!");
							}
						}
					});
				}
				{
					btnCancel = new Button(groupPassword, SWT.PUSH
							| SWT.CENTER);
					btnCancel.setText("Undo");
					btnCancel.setBounds(192, 149, 60, 24);
					btnCancel.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							dialogShell.dispose();
						}
					});
				}
				{
					jidLbl = new Label(groupPassword, SWT.NONE);
					jidLbl.setText("Jabber ID :");
					jidLbl.setBounds(12, 15, 96, 13);
				}
				{
					txtJabberId = new Text(groupPassword, SWT.BORDER);
					txtJabberId.setText("");
					txtJabberId.setBounds(139, 15, 119, 19);
					txtJabberId.setEditable(false);
				}
			}
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.open();
			loadUser();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadUser() throws BackingStoreException {
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);
		Preferences connections = preferences.node(SAVED_PROFILES);
		String[] userNames = connections.childrenNames();

		for (int i = 0; i < userNames.length; i++) {
			String userName = userNames[i];
			Preferences node = connections.node(userName);
			UserContext ua = new UserContext(node.get(USERID, ""), node.get(
					PASSWORD, ""), node.get(NAME, ""), node.get(EMAIL, ""));
			ServerContext sc = new ServerContext(
					node.get(SERVER, "jabber.org"), Boolean.parseBoolean(node
							.get(USE_DEFAULT_PORT, "true")), Boolean
							.parseBoolean(node.get(SECURE, "false")), Integer
							.parseInt(node.get(PORT, "5222")));
			savedProfileContexts.put(userName, new ProfileContext(userName, ua,
					sc, false));
		}
		profileContext = savedProfileContexts.get(preferences.get(LAST_USER, ""));
		password = profileContext.getUserContext().getPassword();
		txtJabberId.setText(profileContext.getUserContext().getId());
	}

	public boolean arePasswordsEqual(String passwd1, String passwd2) {
		boolean positivo = true;
		if (!passwd1.equals(passwd2)) {
			positivo = false;
		}
		return positivo;
	}

	public boolean checkPassword() {
		boolean positivo = true;
		if (txtPassword.getText().equals(password)) {
			positivo = true;
		} else {
			positivo = false;

		}
		return positivo;
	}
	
	protected void savePreferences() {
        Preferences preferences = new ConfigurationScope().getNode( CONFIGURATION_NODE_QUALIFIER );
        preferences.put( LAST_USER, profileContext.getProfileName() );
        Preferences connections = preferences.node( SAVED_PROFILES );
        //for (String profileId : savedProfileContexts.keySet()) {   //savedProfileContexts è riempito in loadUser
        String profileId = profileContext.getProfileName();    
        ProfileContext profile = savedProfileContexts.get( profileId );
            Preferences connection = connections.node( profileId );
            connection.put( SERVER, profile.getServerContext().getServerHost() );
            connection.put( SECURE, String.valueOf( profile.getServerContext().isSecure() ) );
            connection.put( PORT, String.valueOf( profile.getServerContext().getPort() ) );
            connection.put( USERID, profile.getUserContext().getId() );
            connection.put( PASSWORD, profileContext.getUserContext().getPassword());
            connection.put( NAME, profile.getUserContext().getName() );
            connection.put( EMAIL, profile.getUserContext().getEmail() );
            connection.put( USE_DEFAULT_PORT, String.valueOf( profile.getServerContext()
                    .isUsingDefaultPort() ) );
        //}
        try {
            connections.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
}
