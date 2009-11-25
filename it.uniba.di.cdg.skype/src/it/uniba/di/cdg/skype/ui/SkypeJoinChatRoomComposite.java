package it.uniba.di.cdg.skype.ui;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

import com.skype.Friend;
import com.skype.Skype;
import com.skype.User;
import com.skype.User.Status;


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
public class SkypeJoinChatRoomComposite extends org.eclipse.swt.widgets.Composite {
	protected Table tableUsers;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		FillLayout shellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
		shell.setLayout(shellLayout);
		SkypeJoinChatRoomComposite inst = new SkypeJoinChatRoomComposite(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.layout();
		if(size.x == 0 && size.y == 0) {
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

	public SkypeJoinChatRoomComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FillLayout thisLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			{
				tableUsers = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
				tableUsers.setLinesVisible (true);
				tableUsers.setHeaderVisible (true);
				String[] titles = {"User ID", "Full Name", "city"};
				for (int i=0; i<titles.length; i++) {
					TableColumn column = new TableColumn (tableUsers, SWT.NONE);
					column.setText (titles [i]);
				}	
				for (Friend f: Skype.getContactList().getAllFriends()) {
					if((f.getOnlineStatus()==Status.ONLINE)||
							(f.getOnlineStatus()==Status.AWAY)){
						TableItem item = new TableItem (tableUsers, SWT.NONE);
						item.setText (0, f.getId());
						item.setText (1, f.getFullName());
						item.setText (2, f.getCity());
					}
				}
				for (int i=0; i<titles.length; i++) {
					tableUsers.getColumn (i).pack ();
				}
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
