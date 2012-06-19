package it.uniba.di.cdg.skype.x86sdk.ui;

import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


import com.skype.api.Contact;
import com.skype.api.ContactGroup;

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
public class SkypeJoinChatRoomComposite extends Composite {
	protected Table tableUsers;

	/**
	 * Auto-generated main method to display this
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args) {
		showGUI();
	}

	@Override
	protected void checkSubclass() {
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		FillLayout shellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
		shell.setLayout(shellLayout);
		SkypeJoinChatRoomComposite inst = new SkypeJoinChatRoomComposite(shell,
				SWT.NULL);
		Point size = inst.getSize();
		shell.layout();
		if (size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public SkypeJoinChatRoomComposite(Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FillLayout thisLayout = new FillLayout(
					org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			{
				tableUsers = new Table(this, SWT.MULTI | SWT.BORDER
						| SWT.FULL_SELECTION);
				tableUsers.setLinesVisible(true);
				tableUsers.setHeaderVisible(true);
				String[] titles = { "User ID", "Full Name", "City" };
				for (int i = 0; i < titles.length; i++) {
					TableColumn column = new TableColumn(tableUsers, SWT.NONE);
					column.setText(titles[i]);
				}
				
				ContactGroup cg = SkypeBackend.skype.GetHardwiredContactGroup(ContactGroup.TYPE.ALL_BUDDIES);
				Contact[] contacts = cg.GetContacts();
				
				for (int i = 0; i < contacts.length; i++) {
					 Contact f = contacts[i];
					 String availability = Contact.AVAILABILITY.get(f.GetIntProperty(Contact.PROPERTY.availability)).toString();
					 
					if (availability == "ONLINE"
							|| availability == "AWAY"
							|| availability == "DND") {
						TableItem item = new TableItem(tableUsers, SWT.NONE);
						item.setText(0, f.GetIdentity());
						item.setText(1, f.GetStrProperty(Contact.PROPERTY.fullname));
						item.setText(2, f.GetStrProperty(Contact.PROPERTY.city));
					}
				}
				for (int i = 0; i < titles.length; i++) {
					tableUsers.getColumn(i).pack();
				}
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
