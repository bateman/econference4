package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.IEConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.DiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.ItemList;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class GenInfoPage extends WizardPage {
	private static final String CUSTOM_MUC_SERVICE = "Other...";
	private static final int SKYPE_BACKEND = 1;
	private static final int XMPP_BACKEND = 0;
	private static final String[] BACKENDS = { "Text only (XMPP/GTalk)", "Audio (Skype)" };

	private String preferredFilePath;
	private Composite composite;
	private IEConferenceContext context;
	private Combo backendIdCombo = null;
	private Text nameConferenceText = null;
	private GridData gd;
	// private String nick;
	// private String password;
	private Text topicText = null;
	private Text itemText = null;
	private Text nickNameText = null;
	private Combo serviceCombo = null;
	//private Combo serviceEconference = null;
	private Text serviceText = null;
	String media = "";
	private DateTime schedule;
	private Text filePathText;
	private DateTime time;

	public GenInfoPage(String arg0, IEConferenceContext context) {
		super(arg0);
		setTitle("General conference info");
		setDescription("Step 1: Enter conference information.\nFields marked with * are required. "
				+ "You should avoid using reserved chars (e.g. <, >, &) as they will be escaped.");

		Preferences preferences = ConfigurationScope.INSTANCE.getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
		Preferences pathPref = preferences.node(IConfigurationConstant.PATH);
		preferredFilePath = pathPref.get(IConfigurationConstant.DIR, "");
		
		this.context = context;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {

		// create the composite to hold the widgets
		composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, true);
		composite.setLayout(gridLayout);
		new CLabel(composite, SWT.NONE).setText("Conference chair: *");
		nickNameText = new Text(composite, SWT.BORDER);
		nickNameText.setLayoutData(new GridData(143, 15));
		media = NetworkPlugin.getDefault().getRegistry().getDefaultBackendId();
		nickNameText.setText(NetworkPlugin.getDefault().getRegistry().getDefaultBackend().getUserId());
	
		new CLabel(composite, SWT.NONE).setText("");
		new CLabel(composite, SWT.NONE).setText("Media: * ");
		backendIdCombo = new Combo(composite, SWT.READ_ONLY);
		backendIdCombo.setLayoutData(new GridData(129, 15));
		backendIdCombo.setItems(BACKENDS);

		backendIdCombo
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						if (!backendIdCombo.getText().equals(""))
							serviceCombo.setEnabled(true);
						if (backendIdCombo.getText().equals(BACKENDS[XMPP_BACKEND])) {
							media = "it.uniba.di.cdg.jabber.jabberBackend";
							serviceCombo.setEnabled(true);
						} else if (backendIdCombo.getText().equals(BACKENDS[SKYPE_BACKEND])) {
							media = "it.uniba.di.cdg.skype.skypeBackend";							
							serviceCombo.select(-1);
							serviceCombo.setEnabled(false);
							serviceText.setText("");
							nameConferenceText.setText("econference");
						}
						filePathText.setText(computeFilePath());
					}
				});

		new CLabel(composite, SWT.NONE);
		new CLabel(composite, SWT.NONE).setText("Conference service: *");
		serviceCombo = new Combo(composite, SWT.READ_ONLY);
		final MucServers confServices = loadConferenceServices();

		String items[] = confServices.getServerAddresses();
		serviceCombo
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						if (serviceCombo.getText().equals(CUSTOM_MUC_SERVICE)) {
							serviceText.setEnabled(true);
							serviceText.setFocus();
						} else {
							serviceText.setEnabled(false);
						}
						serviceText.setText(confServices
								.getMucService(serviceCombo.getSelectionIndex()));
						if (nameConferenceText.getText().equals(""))
							nameConferenceText.setText("econference");
						filePathText.setText(computeFilePath());

					}					
				});
		serviceCombo.setEnabled(false);
		serviceCombo.setItems(items);
		serviceCombo.setLayoutData(new GridData(129, 15));
		serviceText = new Text(composite, SWT.BORDER);
		serviceText.setLayoutData(new GridData(188, 15));
		serviceText.setEnabled(false);

		new CLabel(composite, SWT.NONE)
				.setText("Conference name (recommended): ");
		gd = new GridData(435, 15);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		nameConferenceText = new Text(composite, SWT.BORDER);
		nameConferenceText.setText("econference");
		nameConferenceText.setLayoutData(gd);
		nameConferenceText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				String text = nameConferenceText.getText().replace(' ', '_');
					nameConferenceText.setText(text);
				filePathText.setText(computeFilePath());			
			}

			@Override
			public void focusGained(FocusEvent e) {
				nameConferenceText.setSelection(0);
			}
		});
		new CLabel(composite, SWT.NONE).setText("Topic:");
		gd = new GridData(435, 15);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		topicText = new Text(composite, SWT.BORDER);
		new CLabel(composite, SWT.NONE).setText("Schedule:");
		schedule = new DateTime(composite, SWT.DATE | SWT.LONG);
		time = new DateTime(composite, SWT.TIME | SWT.SHORT);
		topicText.setLayoutData(gd);
		// new CLabel(composite, SWT.NONE).setText("");
		new CLabel(composite, SWT.NONE).setText("Item/s:\n(One per line)");
		itemText = new Text(composite, SWT.BORDER | SWT.MULTI);
		itemText.setToolTipText("Enter newline to add new item");
		gd = new GridData(435, 110);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		itemText.setLayoutData(gd);

		new CLabel(composite, SWT.NONE).setText("File to save to: * ");
		gd = new GridData(230, 15);
		// gd.horizontalSpan = 2;
		// gd.grabExcessHorizontalSpace = true;
		filePathText = new Text(composite, SWT.BORDER);
		filePathText.setLayoutData(gd);
		filePathText.setText(computeFilePath());
		Button browseButton = new Button(composite, SWT.PUSH);
		browseButton.setText("Browse");
		browseButton.setLayoutData(new GridData(50, 22));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(composite.getShell(),
						SWT.SAVE);
				dialog.setFilterNames(new String[] { "eConference Context XML file (*.ecx)" });
				dialog.setFilterExtensions(new String[] { "*.ecx" });
				String path = dialog.open();
				if (path != null) {
					filePathText.setText(path);
						
				}
			}
		});

		// auto select current backend on display
		if (NetworkPlugin.getDefault().getRegistry().getDefaultBackendId()
				.equals("it.uniba.di.cdg.jabber.jabberBackend")) {
			backendIdCombo.select(XMPP_BACKEND);
			serviceCombo.setEnabled(true);
		} else if (NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackendId()
				.equals("it.uniba.di.cdg.skype.skypeBackend")){
			backendIdCombo.select(SKYPE_BACKEND);
		}
/*		
 *     ComboBox che verifica il servizio attivo: es. PlanningPoker
 * 
 * 
        new CLabel(composite, SWT.NONE).setText("e-conference Service:");
		serviceEconference = new Combo(composite, SWT.READ_ONLY);
		String bundleService = EconferenceBundleManager.getEconferenceExtention();
		serviceEconference.setItems(new String[] {"none", bundleService});
		
		serviceEconference.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = serviceCombo.getSelectionIndex();
				if ( index == -1 )
					return;
				String item = serviceCombo.getItem(index);
				if (!item.equals("none")) {
					EconferenceBundleManager.setService(item);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});*/
		setPageComplete(true);
		// set the composite as the control for this page
		setControl(composite);
	}

	private String computeRoom() {
		String room = null;
		switch (backendIdCombo.getSelectionIndex()) {
		case XMPP_BACKEND:
			room =  nameConferenceText.getText().equals("") || serviceText.getText().equals("") ? 
					"econference" : nameConferenceText.getText() + "@" + serviceText.getText();
			room = room.toLowerCase();
			
			break;
		case SKYPE_BACKEND:
			room = nameConferenceText.getText().equals("") ? 
					"econference$" + nickNameText.getText() : nameConferenceText.getText() + "$" + nickNameText.getText();
			break;	
		default:
			break;
		}
		return room; 
	}
	
	public String computeFilePath() {
		StringBuffer filepath = new StringBuffer( preferredFilePath
				 								 + media
				 								 + System.getProperty("file.separator")
				 								 + nickNameText.getText()
				 								 + System.getProperty("file.separator")
				 								 + nameConferenceText.getText());
		if(backendIdCombo.getSelectionIndex() == XMPP_BACKEND)
			filepath.append("@").append(serviceText.getText());
		
		filepath.append(".ecx");
		return filepath.toString();
	}

	public IWizardPage getNextPage() {
		// save information until dispose page
		if (this.checkData()) {
			this.saveData();
			InvitePage page = ((InviteWizard) getWizard()).invitePage;
			page.setContext(this.getContext());
			((InviteWizard) getWizard()).setOrganizer(nickNameText.getText());
			return page;
		} else
			MessageDialog.openWarning(getShell(), "",
					"Please fill in all the required fields shown with a *.");
		return this;
	}

	private IEConferenceContext getContext() {
		return this.context;
	}

	public boolean canFlipToNextPage() {
		return true;
	}

	/*
	 * public void setNick(String value) { this.nick = value; }
	 * 
	 * public void setPass(String value) { this.password = value; }
	 */

	private void saveData() {
		this.context.setBackendId(media);
		this.context.setName(computeName());
		this.context.setRoom(computeRoom());
		this.context.setTopic(this.topicText.getText());
		String[] items = this.itemText.getText().split(
				System.getProperty("line.separator"));
		IItemList il = new ItemList();
		for (int i = 0; i < items.length; i++)
			il.addItem(new DiscussionItem(items[i]));
		this.context.setItemList(il);

		String scheduleStr = this.schedule.getDay() + "-"
				+ (this.schedule.getMonth() + 1) + "-"
				+ this.schedule.getYear();
		int min = this.time.getMinutes();
		String dateStr = this.time.getHours() + ":"
				+ (min <= 9 ? "0" + min : min);
		this.context.setSchedule(scheduleStr + ", h. " + dateStr);
	}

	private String computeName() {
		String s = nameConferenceText.getText();
		int n = -1;
		switch (backendIdCombo.getSelectionIndex()) {
		case XMPP_BACKEND:
			n = s.indexOf('@');
			break;
		case SKYPE_BACKEND:
			n = s.indexOf('$');
			break;	
		default:
			break;
		}
		
		return n == -1? s: s.substring(0, n);
	}

	private boolean checkData() {
		if (nickNameText.getText().equals(""))
			return false;
		if (backendIdCombo.getSelectionIndex() == -1)
			return false;
		if (media.equals("it.uniba.di.cdg.jabber.jabberBackend")) {
			if (serviceCombo.getText().equals(""))
				return false;
			if (serviceCombo.getText().equals(CUSTOM_MUC_SERVICE)
					&& serviceText.getText().equals(""))
				return false;
		}
		if (filePathText.getText().equals(""))
			return false;
		return true;
	}

	public String getFilePath() {
		return filePathText.getText();
	}

	/*
	 * Sets the completed field on the wizard class when all the information is
	 * entered and the wizard can be completed
	 */
	public boolean isPageComplete() {
		return true;
	}

	public String getConferenceName() {
		return nameConferenceText.getText();
	}

	private static MucServers loadConferenceServices() {
		MucServers confServices = new MucServers();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder
					.parse("http://www.jabberes.org/servers/servers.xml");
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xpath
					.compile("//server[@offline='no']/component[@category='conference'][@available='yes'][@type='x-muc']");

			NodeList nodes = (NodeList) expr.evaluate(doc,
					XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				// muc service
				confServices.addMucService(nodes.item(i).getAttributes()
						.getNamedItem("jid").getTextContent());
				// xmpp server
				confServices.addServerAddress(nodes.item(i).getParentNode()
						.getAttributes().getNamedItem("jid").getTextContent());

			}
		} catch (ConnectException ce) {
			// use codingteam.net in case list of servers is offline
			Logger.getAnonymousLogger().log(Level.WARNING, "http://www.jabberes.org/servers/servers.xml seems to be unreacheable.\nUsing default server");
			confServices.addMucService("conference.codingteam.net");
			confServices.addServerAddress("codingteam.net");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return confServices;
	}

	private static class MucServers {
		private ArrayList<String> serverAddresses = new ArrayList<String>(120);
		private ArrayList<String> mucServices = new ArrayList<String>(120);

		public MucServers() {
			addServerAddress(CUSTOM_MUC_SERVICE);
			addMucService("");
		}

		public void addServerAddress(String serverAddress) {
			this.serverAddresses.add(serverAddress);
		}

		public String[] getServerAddresses() {
			return (String[]) serverAddresses
					.toArray(new String[serverAddresses.size()]);
		}

		public void addMucService(String mucService) {
			this.mucServices.add(mucService);
		}

		public String getMucService(int index) {
			return mucServices.get(index);
		}

	}

}
