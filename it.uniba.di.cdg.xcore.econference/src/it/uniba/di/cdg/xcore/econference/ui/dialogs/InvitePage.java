package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddy.Status;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Wizard page shown when the user has chosen plane as means of transport
 */

public class InvitePage extends WizardPage implements Listener {
	Composite composite;
	GridData gd;
	private Button addMember = null;
	private EConferenceContext context = null;
	private Table fromBuddy = null;
	private ImageRegistry imageRegistry;
	private Table toConference = null;
	private Button removeMember = null;

	protected InvitePage(String arg0) {
		super(arg0);
		setTitle("Invite people");
		setDescription("Step 2: Select participants from your buddy list\n You can add participants who are not in your buddy list in the next step");
		context = new EConferenceContext();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {

		composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, false);
		composite.setLayout(gridLayout);
		CLabel buddyList = new CLabel(composite, SWT.NONE);
		buddyList.setText("Buddy List:");
		new CLabel(composite, SWT.NONE);
		CLabel eConfList = new CLabel(composite, SWT.NONE);
		eConfList.setText("Selected conference members:");
		IBuddyRoster roster = NetworkPlugin.getDefault().getHelper()
				.getRoster();
		Collection<IBuddy> contatti = roster.getBuddies();
		Iterator<IBuddy> it = contatti.iterator();
		fromBuddy = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		gd = new GridData(300, 250);
		fromBuddy.setLayoutData(gd);
		imageRegistry = new ImageRegistry();
		ImageDescriptor activeIcon = UiPlugin.getDefault().getImageDescriptor(
				IImageResources.ICON_USER_ACTIVE);
		ImageDescriptor busyIcon = UiPlugin.getDefault().getImageDescriptor(
				IImageResources.ICON_USER_BUSY);
		ImageDescriptor awayIcon = UiPlugin.getDefault().getImageDescriptor(
				IImageResources.ICON_USER_AWAY);
		ImageDescriptor offlineIcon = UiPlugin.getDefault().getImageDescriptor(
				IImageResources.ICON_USER_OFFLINE);
		imageRegistry.put("active", activeIcon);
		imageRegistry.put("busy", busyIcon);
		imageRegistry.put("away", awayIcon);
		imageRegistry.put("offline", offlineIcon);
		while (it.hasNext()) {
			IBuddy buddy = (IBuddy) it.next();
			String nextName = buddy.getName();
			TableItem item = new TableItem(fromBuddy, SWT.NULL);
			item.setImage(getImage(buddy));
			item.setText((nextName.equals("") ? buddy.getId() : nextName
					+ " - " + buddy.getId()));			
		}
		gd = new GridData(40, 100);
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(gd);
		addMember = new Button(group, SWT.PUSH);
		addMember.setLocation(8, 33);
		addMember.setText(">>");
		addMember.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {	
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] items = fromBuddy.getSelection();
				for (int i = 0; i < items.length; i++) {
					setTableRow(items[i], toConference);
					items[i].dispose();
				}
			}

		});

		addMember.pack();
		removeMember = new Button(group, SWT.PUSH);
		removeMember.setText("<<");
		removeMember.setLocation(8, 70);
		removeMember
				.addMouseListener(new org.eclipse.swt.events.MouseListener() {
					public void mouseDoubleClick(MouseEvent e) {
					}

					public void mouseDown(MouseEvent e) {
					}

					@Override
					public void mouseUp(MouseEvent e) {
						TableItem[] items = toConference.getSelection();
						for (int i = 0; i < items.length; i++) {
							setTableRow(items[i], fromBuddy);
							items[i].dispose();
						}
					}

				});
		removeMember.pack();
		toConference = new Table(composite, SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(300, 250);
		toConference.setLayoutData(gd);
		setControl(composite);
		setPageComplete(true);
	}

	private void setTableRow(TableItem Olditem, Table table) {
		TableItem item = new TableItem(table, SWT.NULL);
		item.setImage(Olditem.getImage());
		item.setText(Olditem.getText());
	}

	private Image getImage(IBuddy buddy) {
		if (Status.AVAILABLE.equals(buddy.getStatus()))
			return imageRegistry.get("active");
		else if (Status.CHAT.equals(buddy.getStatus()))
			return imageRegistry.get("active");
		else if (Status.BUSY.equals(buddy.getStatus()))
			return imageRegistry.get("busy");
		else if (Status.AWAY.equals(buddy.getStatus()))
			return imageRegistry.get("away");
		else if (Status.EXTENDED_AWAY.equals(buddy.getStatus()))
			return imageRegistry.get("away");
		return imageRegistry.get("offline");
	}

	public void setContext(EConferenceContext ctx) {
		this.context = ctx;
	};

	private EConferenceContext getContext() {
		return this.context;
	};

	public boolean canFlipToNextPage() {
		return true;
	}

	public IWizardPage getNextPage() {
		// save information until dispose page
		LastPage page = ((InviteWizard) getWizard()).lastOnePage;
		page.setContext(this.getContext());
		TableItem[] items = toConference.getItems();
		String[] partecipants = new String[items.length];
		for (int i = 0; i < items.length; i++)
			partecipants[i] = items[i].getText();
		page.init(partecipants);
		return page;
	}

	public void handleEvent(Event e) {
	}

	/*
	 * Sets the completed field on the wizard class when all the information is
	 * entered and the wizard can be completed
	 */
	public boolean isPageComplete() {
		return true;
	}

}
