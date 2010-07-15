package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page shown when the user has chosen plane as means of transport
 */

public class LastPage extends WizardPage implements Listener {

	boolean isModerator = false;
	Composite composite;
	GridData gridData;
	private Button sendInvitationsCheckBox = null;
	EConferenceContext context = null;
	private CCombo currentUserRoles;
	private Table table = null;
	private Button addRow;
	private boolean canSendInvitations = true;

	private TableEditor editor = null;
	private final static String[] roles = { "Participant", "Moderator",
			"Scribe" };
	private final static String[] headers = { "Role *", "Id *", "Fullname",
			"Email", "Organization" };

	protected LastPage(String arg0) {
		super(arg0);
		setTitle("Invite people");
		setDescription("Step 3: Complete the information of participants\n Click \"Add Row\" to add participants to the conference");
		context = new EConferenceContext();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {

		// create the composite to hold the widgets
		composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(5, false);
		composite.setLayout(gridLayout);
		table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		gridData = new GridData(575, 200);
		gridData.horizontalAlignment = SWT.FILL;
		gridData.horizontalSpan = 5;
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (String i : headers)
			new TableColumn(table, SWT.NONE).setText(i);
		setCurrentUserInformation();
		gridData = new GridData();
		gridData.horizontalSpan = 4;
		gridData.grabExcessHorizontalSpace = true;
		addRow = new Button(composite, SWT.PUSH);
		addRow.setLayoutData(gridData);
		addRow.setText("Add Row");
		addRow.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				insertRow("", "");
			}
		});
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.widthHint = 140;
		sendInvitationsCheckBox = new Button(composite, SWT.CHECK);
		sendInvitationsCheckBox.setText("Send invitations");
		sendInvitationsCheckBox.setSelection(true);
		sendInvitationsCheckBox.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (sendInvitationsCheckBox.getSelection())
					canSendInvitations = true;
				else
					canSendInvitations = false;
			}

		});

		setControl(composite);
		setPageComplete(true);
	}

	private void setCurrentUserInformation() {
		TableColumn[] columns = table.getColumns();
		final TableItem item = new TableItem(table, SWT.NONE);
		currentUserRoles = new CCombo(table, SWT.NONE);
		currentUserRoles.setEditable(false);
		currentUserRoles.setText("Moderator");
		item.setText(0, currentUserRoles.getText());
		for (String j : roles)
			currentUserRoles.add(j);
		currentUserRoles.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				item.setText(0, currentUserRoles.getText());
				if (currentUserRoles.getText().equals("Moderator")) {
					sendInvitationsCheckBox.setEnabled(true);
				} else {
					sendInvitationsCheckBox.setEnabled(false);
					sendInvitationsCheckBox.setSelection(false);
				}
			}
		});
		currentUserRoles.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				item.setText(0, currentUserRoles.getText());
				if (currentUserRoles.getText().equals("Moderator"))
					sendInvitationsCheckBox.setSelection(true);
			}
		});
		editor = new TableEditor(table);
		editor.grabHorizontal = true;
		editor.setEditor(currentUserRoles, item, 0);
		final Text id = new Text(table, SWT.NONE);
		id.setText(NetworkPlugin.getDefault().getRegistry()
				.getBackend(((InviteWizard) this.getWizard()).getMedia())
				.getUserAccount().getId());
		item.setText(1, id.getText());
		id.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				item.setText(1, id.getText());
			}
		});
		editor = new TableEditor(table);
		editor.grabHorizontal = true;
		editor.setEditor(id, item, 1);
		editor = new TableEditor(table);
		final Text fullname = new Text(table, SWT.NONE);
		fullname.setText(NetworkPlugin.getDefault().getRegistry()
				.getBackend(((InviteWizard) this.getWizard()).getMedia())
				.getUserAccount().getName());
		item.setText(2, fullname.getText());
		fullname.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				item.setText(2, fullname.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(fullname, item, 2);
		editor = new TableEditor(table);
		final Text email = new Text(table, SWT.NONE);
		email.setText(NetworkPlugin.getDefault().getRegistry()
				.getBackend(((InviteWizard) this.getWizard()).getMedia())
				.getUserAccount().getEmail());
		item.setText(3, email.getText());
		email.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				item.setText(3, email.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(email, item, 3);
		editor = new TableEditor(table);
		final Text organization = new Text(table, SWT.NONE);
		organization.setText("");
		item.setText(4, organization.getText());
		organization.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				item.setText(4, organization.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(organization, item, 4);
		columns[0].setWidth(90);
		for (int i = 1; i < columns.length; i++) {
			columns[i].pack();
			columns[i].setWidth(100);
		}
	}

	public void init(String[] participants) {

		if (participants != null) {
			TableColumn[] columns = table.getColumns();
			for (int i = 0; i < participants.length; i++) {
				String[] partecipant = participants[i].split(" - ");
				String idPartecipant = (partecipant.length > 1 ? partecipant[1]
						: partecipant[0]);
				String namePartecipant = (partecipant.length > 1 ? partecipant[0]
						: "");
				if (notExist(idPartecipant))
					insertRow(idPartecipant, namePartecipant);
			}
			columns[0].setWidth(90);
			for (int s = 1; s < columns.length; s++) {
				columns[s].pack();
				columns[s].setWidth(100);
			}
		}
	}

	private boolean notExist(String id) {
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++)
			if (items[i].getText(1).equals(id))
				return false;
		return true;
	}

	private void insertRow(String idP, String name) {
		final TableItem newRow = new TableItem(table, SWT.NONE);
		final CCombo role = new CCombo(table, SWT.NONE);
		for (String j : roles)
			role.add(j);
		role.setText("Participant");
		newRow.setText(0, role.getText());
		role.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				newRow.setText(0, role.getText());
			}
		});
		role.setEditable(false);
		editor = new TableEditor(table);
		editor.grabHorizontal = true;
		editor.setEditor(role, newRow, 0);
		editor = new TableEditor(table);
		final Text id = new Text(table, SWT.NONE);
		id.setText(idP);
		newRow.setText(1, idP);
		id.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				newRow.setText(1, id.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(id, newRow, 1);
		editor = new TableEditor(table);
		final Text fullname = new Text(table, SWT.NONE);
		fullname.setText(name);
		newRow.setText(2, fullname.getText());
		fullname.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				newRow.setText(2, fullname.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(fullname, newRow, 2);
		editor = new TableEditor(table);
		final Text email = new Text(table, SWT.NONE);
		email.setText("");
		newRow.setText(3, email.getText());
		email.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				newRow.setText(3, email.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(email, newRow, 3);
		editor = new TableEditor(table);
		final Text organization = new Text(table, SWT.NONE);
		organization.setText("");
		newRow.setText(4, organization.getText());
		organization.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				newRow.setText(4, organization.getText());
			}
		});
		editor.grabHorizontal = true;
		editor.setEditor(organization, newRow, 4);
	}

	// private void changeUsersRole(boolean isModerator) {
	// TableItem current = table.getItem(0);
	// editor = new TableEditor(table);
	// if (isModerator)
	// currentUserRoles.setText("Moderator");
	// else
	// currentUserRoles.setText("Participant");
	// editor.grabHorizontal = true;
	// editor.setEditor(currentUserRoles, current, 0);
	// }

	public void setContext(EConferenceContext ctx) {
		this.context = ctx;
	}

	public boolean canFlipToNextPage() {
		return false;
	}

	/*
	 * Process the events: when the user has entered all information the wizard
	 * can be finished
	 */
	public void handleEvent(Event e) {
	}

	/*
	 * Sets the completed field on the wizard class when all the information is
	 * entered and the wizard can be completed
	 */
	public boolean isPageComplete() {
		return true;
	}

	public void saveData() {
		List<Invitee> participants = new ArrayList<Invitee>();
		TableItem[] items = table.getItems();
		Invitee p = null;
		for (int i = 0; i < items.length; i++) {
			p = new Invitee(items[i].getText(1), items[i].getText(2),
					items[i].getText(3), items[i].getText(4), items[i].getText(
							0).toLowerCase());
			if (items[i].getText(0).equals("Moderator"))
				this.getContext().setModerator(p);
			if (items[i].getText(0).equals("Scribe"))
				this.getContext().setScribe(p);
			participants.add(p);
		}
		context.setInvitees(participants);
	}

	public boolean getCanSendInvitations() {
		return this.canSendInvitations;
	}

	public void setCanSendInvitations(boolean canSendInvitations) {
		this.canSendInvitations = canSendInvitations;
	}

	public EConferenceContext getContext() {
		return this.context;
	}
	
}