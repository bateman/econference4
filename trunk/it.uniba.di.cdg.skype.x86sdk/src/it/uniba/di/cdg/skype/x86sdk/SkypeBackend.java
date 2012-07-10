/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package it.uniba.di.cdg.skype.x86sdk;

//import it.uniba.di.cdg.skype.recorder.win32.FreeRecorder;
import it.uniba.di.cdg.skype.x86sdk.SkypeListeners.jwcObserver;
import it.uniba.di.cdg.skype.x86sdk.action.SkypeCallAction;
import it.uniba.di.cdg.skype.x86sdk.action.SkypeChatServiceAction;
import it.uniba.di.cdg.skype.x86sdk.action.SkypeMultiCallAction;
import it.uniba.di.cdg.skype.x86sdk.action.SkypeMultiChatServiceAction;
import it.uniba.di.cdg.skype.x86sdk.util.ExtensionConstants;
import it.uniba.di.cdg.skype.x86sdk.util.SkypeSound;
import it.uniba.di.cdg.skype.x86sdk.util.XmlUtil;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.IUserStatus;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.call.CallEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatInvitationDeclinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceRevokedEvent;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.network.services.INetworkServiceContext;
import it.uniba.di.cdg.xcore.ui.wizards.IConfigurationConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.api.Skype;
import com.skype.api.SkypeObject;
import com.skype.api.Contact.AVAILABILITY;
import com.skype.api.Conversation.LIST_TYPE;
import com.skype.ipc.TCPSocketTransport;
import com.skype.ipc.TLSServerTransport;
import com.skype.ipc.Transport;
import com.skype.util.PemReader;

public class SkypeBackend implements IBackend, jwcObserver {

	/**
	 * This backend's unique id.
	 */
	public static final String ID = "it.uniba.di.cdg.skype.x86sdk.skypeBackend";
	// private static final String RECORDER_ID =
	// "it.uniba.di.cdg.skype.recorder";

	private INetworkBackendHelper helper;
	public static SkypeBuddyRoster skypeBuddyRoster;
	public static SkypeMultiChatServiceAction skypeMultiChatServiceAction;
	private SkypeCallAction skypeCallAction;
	private SkypeMultiCallAction skypeMultiCallAction;
	public static Skype skype;
	public Account account;
	private boolean connected = false;

	private String inetAddr = "127.0.0.1";
	private String pemFileName;
	private String transportLogName = null;
	private boolean internal = false;
	private Transport transport;
	private int portNum = 8963;
	private String my_username;
	public SkypeListeners theListeners;
	public static SkypeSound sound;
	private Runtime rt;
	private UserContext userContext;
	private ServerContext serverContext;

	int count = 0;

	public void processMessageReceived(String content, String senderId,
			String senderName, Conversation chat) {

		if (content.equals(""))
			return;

		// is Skype internal message
		if (XmlUtil.isSkypeXmlMessage(content))
			return;

		String extensionName = XmlUtil.extensionName(content);
		System.out.println(extensionName);
		// è presente un estensione al protocollo
		if (extensionName != null) {

			if (XmlUtil.chatType(content).equals(ExtensionConstants.ONE_TO_ONE)) // chat
			// one2one
			{
				if (extensionName.equals(ExtensionConstants.CHAT_COMPOSING)) {
					IBackendEvent event = new ChatComposingEvent(senderId,
							getBackendId());
					getHelper().notifyBackendEvent(event);
				}

				else if (extensionName.equals(ExtensionConstants.ROOM_INVITE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String reason = param.get(ExtensionConstants.REASON);
					if (reason == null)
						reason = "";
					String roomId = chat
							.GetStrProperty(Conversation.PROPERTY.identity);

					skypeMultiChatServiceAction
							.putWaitingRoom(
									chat.GetStrProperty(Conversation.PROPERTY.identity),
									senderId);
					IBackendEvent event = new InvitationEvent(getBackendId(),
							roomId, senderId, "schedule n/a", reason, "");
					getHelper().notifyBackendEvent(event);
				}

				else if (extensionName
						.equals(ExtensionConstants.ROOM_INVITE_ACCEPT)) {
					skypeMultiChatServiceAction.sendChatRoom(senderId);

					if (skypeMultiChatServiceAction.getModerator().equals(
							getUserId())) {
						HashMap<String, String> param = new HashMap<String, String>();
						param.put(ExtensionConstants.USER, getUserId());
						skypeMultiChatServiceAction
								.SendExtensionProtocolMessage(
										ExtensionConstants.MODERATOR, param);
					}
				}

				else if (extensionName
						.equals(ExtensionConstants.ROOM_INVITE_DECLINE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String reason = param.get(ExtensionConstants.REASON);
					if (reason == null)
						reason = "";
					IBackendEvent event = new MultiChatInvitationDeclinedEvent(
							senderId, reason, getBackendId());
					getHelper().notifyBackendEvent(event);
				}

				else if (extensionName.equals(ExtensionConstants.CHAT_MESSAGE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String msg = param.get(ExtensionConstants.MESSAGE);
					if (!my_username.equals(senderId)) {
						IBackendEvent event = new ChatMessageReceivedEvent(
								getRoster().getBuddy(senderId), msg,
								getBackendId());
						getHelper().notifyBackendEvent(event);
					}
					// }

				}

				// è un estensione gestita dal core
				else {
					HashMap<String, String> param;
					param = XmlUtil.readXmlExtension(content);
					IBackendEvent event = new ChatExtensionProtocolEvent(
							senderId, extensionName, param, getBackendId());
					getHelper().notifyBackendEvent(event);
				}
			} else { // chat m2m

				if (extensionName.equals(ExtensionConstants.CHAT_ROOM)) {
					skypeMultiChatServiceAction.updateChatRoom(chat);
				} else if (extensionName
						.equals(ExtensionConstants.CHAT_COMPOSING)) {
					IBackendEvent event = new MultiChatComposingEvent(senderId,
							getBackendId());
					getHelper().notifyBackendEvent(event);

				} else if (extensionName
						.equals(ExtensionConstants.PRESENCE_MESSAGE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String type = param.get(ExtensionConstants.PRESENCE_TYPE);
					if (ExtensionConstants.PRESENCE_UNAVAILABLE.equals(type)) {
						System.out
								.println("Received presence unvailable update");
						IBackendEvent event = new MultiChatUserLeftEvent(
								getBackendId(), senderId, senderName);
						getHelper().notifyBackendEvent(event);
					}
				} else if (extensionName
						.equals(ExtensionConstants.CHAT_MESSAGE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String msg = param.get(ExtensionConstants.MESSAGE);
					IBackendEvent event = new MultiChatMessageEvent(
							getBackendId(), msg, senderId);
					getHelper().notifyBackendEvent(event);
				}

				else if (extensionName.equals(ExtensionConstants.REVOKE_VOICE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String userId = param.get(ExtensionConstants.USER);
					IBackendEvent event = new MultiChatVoiceRevokedEvent(
							getBackendId(), userId);
					getHelper().notifyBackendEvent(event);
				}

				else if (extensionName.equals(ExtensionConstants.GRANT_VOICE)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String userId = param.get(ExtensionConstants.USER);
					IBackendEvent event = new MultiChatVoiceGrantedEvent(
							getBackendId(), userId);
					getHelper().notifyBackendEvent(event);
				}

				else if (extensionName.equals(ExtensionConstants.MODERATOR)) {
					HashMap<String, String> param = XmlUtil
							.readXmlExtension(content);
					String user = param.get(ExtensionConstants.USER);
					skypeMultiChatServiceAction.setModerator(user);
					skypeMultiChatServiceAction.updateChatRoom(chat);
				} else if (extensionName
						.equals(ExtensionConstants.CALL_FINISHED)) {

					getMultiCallAction().endCall();
				}

				// è un estensione gestita dal core
				else {
					HashMap<String, String> param;
					param = XmlUtil.readXmlExtension(content);
					IBackendEvent event = new MultiChatExtensionProtocolEvent(
							param, extensionName, getBackendId(), senderId);
					getHelper().notifyBackendEvent(event);
				}
			}
		}

		// it's just a regular skype text-based msg
		else {
			String chatMsg = null;
			if (XmlUtil.isSkypeXmlMessage(content))
				chatMsg = XmlUtil.chatType(content);

			// we assume it's always a one2one chat in case we get a regular msg
			if (null == chatMsg // null means a regular message has no
								// extensions
					|| chatMsg.equals(ExtensionConstants.ONE_TO_ONE)) {
				IBackendEvent event = new ChatMessageReceivedEvent(getRoster()
						.getBuddy(senderId), content, getBackendId());
				getHelper().notifyBackendEvent(event);
			} else {
				// check: this else would probably be never reached
				IBackendEvent event = new MultiChatMessageEvent( // chat m2m
						getBackendId(), content, senderName);
				getHelper().notifyBackendEvent(event);
			}
		}
	}

	public SkypeBackend getBackendFromProxy() {
		return this;
	}

	public SkypeBackend() {
		super();
		skype = new Skype();
		skypeBuddyRoster = new SkypeBuddyRoster(this);
		skypeMultiChatServiceAction = new SkypeMultiChatServiceAction(this);
		skypeCallAction = new SkypeCallAction();
		skypeMultiCallAction = new SkypeMultiCallAction();
		sound = new SkypeSound();
		pemFileName = System.getProperty("user.dir")
				+ "\\SkypeRuntime\\key\\eConfKey.pem";
	}

	@Override
	public void changePassword(String newpasswd) throws Exception {

	}

	private String getPemContents() throws IOException {
		File tokenFile = new File(pemFileName);
		InputStream in = new FileInputStream(tokenFile);
		long fileSize = tokenFile.length();
		byte[] bytes = new byte[(int) fileSize];
		int offset = 0;
		int count = 0;
		while (offset < fileSize) {
			count = in.read(bytes, offset, (int) fileSize - offset);
			if (count >= 0)
				offset += count;
			else
				throw new IOException("Unable to read App Token file: "
						+ tokenFile.getName());
		}
		if (in != null)
			in.close();

		String rawString = new String(bytes);
		return rawString.trim();
	}

	@SuppressWarnings("deprecation")
	private void skypeConnect() throws IOException {

		try {
			String runtime = System.getProperty("user.dir")
					+ "\\SkypeRuntime\\skypekit-run.exe";
			rt = Runtime.getRuntime();
			rt.exec(runtime);

		} catch (Exception e) {
		}

		skypeCleanup();
		theListeners = new SkypeListeners(this, skype);

		try {
			if (internal) {
				transport = new TCPSocketTransport(inetAddr, portNum);

				transport.startLogging(transportLogName);

				skype.InitNonTLSInsecure(getPemContents(), transport);
			} else {
				PemReader donkey = new PemReader(pemFileName);
				X509Certificate c = donkey.getCertificate();
				PrivateKey p = donkey.getKey();
				Transport t = new TCPSocketTransport(inetAddr, portNum);

				transport = new TLSServerTransport(t, c, p);

				transport.startLogging(transportLogName);

				skype.Init(transport);
			}
		} catch (IOException e) {
			System.out.print(e);

		} catch (InvalidKeySpecException e) {

		}

		try {
			if (transport.isConnected()) {

				String version = skype.GetVersionString();
				System.out.print("Connected to ");
				System.out.println(version);

			} else {
				System.out
						.println("\n::: Error connecting to skypekit, enter 'r' to reconnect...\n");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connect(ServerContext ctx, UserContext userAccount)
			throws BackendException {

		try {
			skypeConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		my_username = userAccount.getId();

		login(userAccount.getId(), userAccount.getPassword());

		Account.GetStatusWithProgressResult loginStatus = account
				.GetStatusWithProgress();
		while (!(loginStatus.status == Account.STATUS.LOGGED_IN)) {
			loginStatus = account.GetStatusWithProgress();
		}

		helper.notifyBackendEvent(new BackendStatusChangeEvent(ID, true));
		skypeBuddyRoster.reload();
		connected = true;

	}

	/*
	 * private void runRecorderExtension() { IConfigurationElement[] config =
	 * Platform.getExtensionRegistry()
	 * .getConfigurationElementsFor(RECORDER_ID); try { for
	 * (IConfigurationElement e : config) {
	 * System.out.println("Evaluating extension skype recorder"); final Object o
	 * = e.createExecutableExtension("class"); if (o instanceof ISkypeRecorder)
	 * { ISafeRunnable runnable = new ISafeRunnable() {
	 * 
	 * @Override public void handleException(Throwable exception) {
	 * System.out.println("Exception in extension skype recorder"); }
	 * 
	 * @Override public void run() throws Exception { ((ISkypeRecorder)
	 * o).recorderStartConfirmDialog(); } }; SafeRunner.run(runnable); } } }
	 * catch (CoreException ex) { System.out.println(ex.getMessage()); } }
	 */

	@Override
	public INetworkService createService(ICapability service,
			INetworkServiceContext context) throws BackendException {
		return null;
	}

	@Override
	public void disconnect() {
		Conversation[] conversations = skype
				.GetConversationList(Conversation.LIST_TYPE.LIVE_CONVERSATIONS);
		for (Conversation c : conversations) {
			c.close();
		}

		// remove listeners
		skypeCleanup();

		try {
			skype.Close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		skypeBuddyRoster.disconnectRoster();

		helper.notifyBackendEvent(new BackendStatusChangeEvent(ID, false));

		connected = false;

	}

	@Override
	public ICapabilities getCapabilities() {
		return null;
	}

	@Override
	public Job getConnectJob() {
		final Job connectJob = new Job("Connecting ...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					@SuppressWarnings("deprecation")
					Preferences preferences = new ConfigurationScope()
							.getNode(IConfigurationConstant.CONFIGURATION_NODE_QUALIFIER);
					Preferences skypePref = preferences
							.node(IConfigurationConstant.SKYPE);
					my_username = skypePref.get(
							IConfigurationConstant.USERNAME, "no_user");
					userContext = new UserContext(skypePref.get(
							IConfigurationConstant.USERNAME, "no_user"),
							skypePref.get(IConfigurationConstant.PASSWORD,
									"no_user"));
					serverContext = new ServerContext("Skype", true, false, 0,
							"Skype");
					connect(serverContext, userContext);
				} catch (BackendException e) {
					e.printStackTrace();
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		return connectJob;
	}

	@Override
	public INetworkBackendHelper getHelper() {
		return this.helper;
	}

	@Override
	public IBuddyRoster getRoster() {
		// TODO da sistemare
		return skypeBuddyRoster;
	}

	@Override
	public ServerContext getServerContext() {
		return new ServerContext("Skype", true, false, 0, "Skype");
	}

	@Override
	public UserContext getUserAccount() {

		UserContext userContect = new UserContext(getUserId(), "");
		userContect.setName(account.GetStrProperty(Account.PROPERTY.fullname));

		return userContect;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void setHelper(INetworkBackendHelper helper) {
		this.helper = helper;
	}

	@Override
	public String getBackendId() {
		return ID;
	}

	@Override
	public IChatServiceActions getChatServiceAction() {
		return new SkypeChatServiceAction();
	}

	@Override
	public IMultiChatServiceActions getMultiChatServiceAction() {
		return skypeMultiChatServiceAction;
	}

	@Override
	public String getUserId() {
		return account.GetStrProperty(Account.PROPERTY.skypename);

	}

	@Override
	public ICallAction getCallAction() {
		return skypeCallAction;
	}

	@Override
	public IMultiCallAction getMultiCallAction() {
		return skypeMultiCallAction;
	}

	@Override
	public void registerNewAccount(String userId, String password,
			ServerContext server, Map<String, String> attributes)
			throws Exception {
	}

	@Override
	public void setUserStatus(int status) {

		switch (status) {
		case IUserStatus.AVAILABLE:

			account.SetAvailability(AVAILABILITY.ONLINE);

			break;

		case IUserStatus.AWAY:

			account.SetAvailability(AVAILABILITY.AWAY);

			break;

		case IUserStatus.BUSY:

			account.SetAvailability(AVAILABILITY.DO_NOT_DISTURB);

			break;

		case IUserStatus.OFFLINE:

			account.SetAvailability(AVAILABILITY.OFFLINE);

			// disconnect();

			break;
		}

	}

	public void skypeCleanup() {

		if (theListeners != null)
			theListeners.unRegisterAllListeners();
		theListeners = null;
		try {
			if (transport != null && transport.isConnected())
				transport.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login(String user, String pass) {

		account = SkypeBackend.skype.GetAccount(user);
		account.LoginWithPassword(pass, false, true);

	}

	public void onAccountStatusChange() {

	}

	@Override
	public void OnConversationListChange(Conversation conversation,
			LIST_TYPE type, boolean added) {
		if (added)
			System.out.println("OnConversationListChange:"
					+ conversation
							.GetStrProperty(Conversation.PROPERTY.displayname)
					+ ", list_type " + type + ", was added");
		else
			System.out.println("OnConversationListChange:"
					+ conversation
							.GetStrProperty(Conversation.PROPERTY.displayname)
					+ ", list_type " + type + ", was removed");

	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Contact.PROPERTY prop, Object value) {
		Contact c = (Contact) obj;
		String skypeName;
		if (prop == Contact.PROPERTY.availability) {
			c = (Contact) obj;
			skypeName = c.GetIdentity();
			int i = (Integer) value;
			System.out.println("ACCOUNT." + skypeName + ":AVAILABILITY = "
					+ Contact.AVAILABILITY.get(i));
			SkypeBackend.skypeBuddyRoster.reload();

		} else if (prop == Contact.PROPERTY.mood_text) {
			c = (Contact) obj;
			skypeName = c.GetIdentity();
			System.out.println("ACCOUNT." + skypeName + ":MOOD = "
					+ c.GetStrProperty(prop));
		}

	}

	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Participant.PROPERTY prop, Object value) {

	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Message.PROPERTY prop, Object value) {

	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Conversation.PROPERTY prop, Object value) {
		Conversation c = (Conversation) obj;

		if (prop == Conversation.PROPERTY.local_livestatus) {
			Conversation affectedConversation = (Conversation) obj;
			Conversation.LOCAL_LIVESTATUS liveStatus = Conversation.LOCAL_LIVESTATUS
					.get(affectedConversation
							.GetIntProperty(Conversation.PROPERTY.local_livestatus));

			switch (liveStatus) {
			case RINGING_FOR_ME:
				System.out.println("RING RING...");
				Participant[] p = c
						.GetParticipants(Conversation.PARTICIPANTFILTER.ALL);

				if (p.length <= 2) {
					skypeCallAction.addCall(p[0].toString(), c);
					IBackendEvent event = new CallEvent(getBackendId(),
							p[1].GetStrProperty(Participant.PROPERTY.identity));
					getHelper().notifyBackendEvent(event);

					new Thread(new Runnable() {
						public void run() {
							sound.play("in_call");
						}
					}).start();

				} else {
					IBackendEvent event = new CallEvent(getBackendId(),
							"conference");
					getHelper().notifyBackendEvent(event);
					new Thread(new Runnable() {
						public void run() {
							sound.play("in_call");
						}
					}).start();
					skypeMultiCallAction
							.addCall(
									c.GetStrProperty(Conversation.PROPERTY.identity),
									c);
				}

				break;
			case ON_HOLD_REMOTELY:
				break;
			case RECENTLY_LIVE:
				System.out.println("end_live");
				sound.stop();
			case NONE:
				System.out.println("none");
				break;
			case IM_LIVE:
				sound.stop();
				System.out.println("Conv: Live session is up!");
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void OnPropertyChange(SkypeObject obj,
			com.skype.api.Account.PROPERTY prop, Object value) {

		String skypename = "(not logged in)";
		if (account != null) {
			skypename = account.GetStrProperty(Account.PROPERTY.skypename);
		}

		if (account != null && prop == Account.PROPERTY.status) {
			Account.GetStatusWithProgressResult loginStatus = account
					.GetStatusWithProgress();

			if (loginStatus.status == Account.STATUS.LOGGED_IN) {
				System.out.println("Login complete.");

			} else if ((loginStatus.status == Account.STATUS.LOGGED_OUT)
					|| (loginStatus.status == Account.STATUS.LOGGED_OUT_AND_PWD_SAVED)) {
				System.out.println("Logout complete.");
			} else {
				System.out.println("Account Status: " + loginStatus.status
						+ " Progress: " + loginStatus.progress);
			}
		} else if (prop == Account.PROPERTY.availability) {
			int i = (Integer) (value);
			Contact.AVAILABILITY v = Contact.AVAILABILITY.get(i);
			System.out.println("ACCOUNT." + skypename + ":" + prop.name()
					+ " = " + v.name());
		} else if (prop == Account.PROPERTY.logoutreason) {
			int i = (Integer) (value);
			System.out.println("ACCOUNT." + skypename + ":LOGOUTREASON" + " = "
					+ Account.LOGOUTREASON.get(i));
		} else {
			System.out.println("ACCOUNT." + skypename + ":" + prop.name()
					+ " = " + value);
		}
	}

	@Override
	public void OnMessage(Message message, boolean changesInboxTimestamp,
			Message supersedesHistoryMessage, Conversation conversation) {
		String author = message.GetStrProperty(Message.PROPERTY.author);

		String text = message.GetStrProperty(Message.PROPERTY.body_xml);
		text = text.replaceAll("&lt;", "<");
		text = text.replaceAll("&gt;", ">");
		text = text.replaceAll("&quot;", "\"");
		text = text.replaceAll("&apos;", "\'");

		conversation.SetConsumedHorizon(
				message.GetIntProperty(Message.PROPERTY.timestamp), false);

		if (!author.equals(my_username)) {

			processMessageReceived(text, author, author, conversation);

		}
		int type = message.GetIntProperty(Message.PROPERTY.type);
		System.out.println("SKYPE.OnMessage." + author + " Message::TYPE = "
				+ Message.TYPE.get(type));

		if (type == Message.TYPE.ENDED_LIVESESSION.getId()) {
			sound.stop();
		}
	}

}
