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
package it.uniba.di.cdg.jabber;

import it.uniba.di.cdg.jabber.action.JabberChatServiceAction;
import it.uniba.di.cdg.jabber.action.JabberMultiChatSeviceAction;
import it.uniba.di.cdg.jabber.internal.BuddyRoster;
import it.uniba.di.cdg.jabber.internal.CustomInvitationEvent;
import it.uniba.di.cdg.jabber.internal.XMPPUtils;
import it.uniba.di.cdg.jabber.ui.ConnectionDialog;
import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatComposingtEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.messages.IMessage;
import it.uniba.di.cdg.xcore.network.messages.SystemMessage;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.services.Capabilities;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.network.services.INetworkServiceContext;
import it.uniba.di.cdg.xcore.one2one.IChatService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Jabber/XMPP backend implementation. A backend may be extended by providing
 * additional services through the use of the
 * <code>it.uniba.di.cdg.jabber.services</code> extension point.
 * 
 * FIXME ConnectionEstablishedListener doesn't work. Check this again with SMACK
 * 2.1.0. FIXED: ConnectionEstablishedListener class is not present in the 3.1.0
 * version of Smack, so it has removed
 */
public class JabberBackend implements IBackend, PacketListener,
		ConnectionListener {
	/**
	 * This backend's unique id.
	 */
	public static final String ID = JabberPlugin.ID + ".jabberBackend";

	public static final String EXTENSION_NAME = "ExtensionName";
	
	private JabberChatServiceAction jabberChatServiceAction;
	private JabberMultiChatSeviceAction jabberMultiChatSeviceAction;

	/**
	 * The capabilities supported by this backend (these depend on the
	 * implemented feature set).
	 */
	private final ICapabilities capabilities;

	/**
	 * Connection to the XMPP server: it can be used for creating new chats,
	 * adding filters, callbacks, ...
	 */
	private XMPPConnection connection;

	public JabberBackend getBackendFromProxy() {
		return this;
	}

	/**
	 * The roster is the core
	 */
	private BuddyRoster buddies;

	/**
	 * Provides useful methods.
	 */
	private INetworkBackendHelper helper;

	// Multi-user chat setup
	private InvitationListener invitationListener = new InvitationListener() {
		public void invitationReceived(XMPPConnection connection, String room,
				String inviter, String reason, String password, Message message) {
			if (JabberBackend.this.connection == connection) {
				final IMessage m = convertFromSmack(message);
				notifyEventListeners(new CustomInvitationEvent(
						JabberBackend.this, ID, room, "schedule n/a", inviter, reason,
						password, m));
			}
		}
	};

	/**
	 * The context information related to the server we are currently connected.
	 * It is initialized by {@see #connect(ServerContext, UserAccount)}. When
	 * disconneted it should be <code>null</code>.
	 */
	private ServerContext serverContext;

	/**
	 * The user account we are currently authenticated with. It is initialized
	 * by {@see #connect(ServerContext, UserAccount)}. When disconneted it
	 * should be <code>null</code>.
	 */
	private UserContext userAccount;

	/**
	 * Create a new jabber backend.
	 */
	public JabberBackend() {
		super();
		jabberChatServiceAction = new JabberChatServiceAction(this);
		jabberMultiChatSeviceAction = new JabberMultiChatSeviceAction(this);
		this.capabilities = new Capabilities();
		this.buddies = new BuddyRoster(this);

		// Chat, multi-chat and e-conference support are built-in.
		capabilities.add(IChatService.CHAT_SERVICE);
		// capabilities.add( IMultiChatService.MULTI_CHAT_SERVICE );
		// capabilities.add( IEConferenceService.ECONFERENCE_SERVICE );
	}

	/**
	 * Create a normal XMPP connection. Clients are not expected to re-implement
	 * this method: it is used as an hook for testing.
	 * 
	 * @param host
	 * @param port
	 * @param serviceName
	 *            this is not required, you can use the empty string
	 * @return a new XMPP connection
	 * @throws XMPPException
	 */
	protected XMPPConnection createConnection(String host, int port,
			String serviceName) throws XMPPException {
		ConnectionConfiguration config;
		if (serviceName == "" || serviceName == null)
			config = new ConnectionConfiguration(host, port);
		else
			config = new ConnectionConfiguration(host, port, serviceName);
		// TODO: verify how the following statement works
		config.setReconnectionAllowed(true);

		return new XMPPConnection(config);
	}

	/**
	 * Create an SSL XMPP connection. Clients are not expected to re-implement
	 * this method: it is used as an hook for testing.
	 * 
	 * @param host
	 * @param port
	 * @param serviceName
	 *            this is not required, you can use the empty string
	 * @return a new XMPP connection
	 * @throws XMPPException
	 */
	protected XMPPConnection createSecureConnection(String host, int port,
			String serviceName) throws XMPPException {
		ConnectionConfiguration config;
		if (serviceName == "" || serviceName == null)
			config = new ConnectionConfiguration(host, port);
		else
			config = new ConnectionConfiguration(host, port, serviceName);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		// TODO: verify how the following statement works
		config.setReconnectionAllowed(true);
		return new XMPPConnection(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.IBackend#connect(it.uniba.di.cdg.xcore.
	 * network.ServerContext, it.uniba.di.cdg.xcore.network.UserAccount)
	 */
	public void connect(final ServerContext ctx, final UserContext userAccount)
			throws BackendException {
		disconnect();
		this.serverContext = ctx;
		this.userAccount = userAccount;

		// TODO make the strings constants
		/*
		 * Preferences preferences = new ConfigurationScope().getNode(
		 * "it.uniba.di.cdg.xcore.ui" ); XMPPConnection.DEBUG_ENABLED =
		 * preferences.getBoolean(
		 * "it.uniba.di.cdg.xcore.ui.preferences_xmppbackend_showdebugger",
		 * false );
		 */
		final ServerContext GOOGLE_TALK = new ServerContext("talk.google.com",
				true, false, 5222, "googlemail.com");

		try {
			if (userAccount.isGmail()) {
				serverContext = GOOGLE_TALK;
			}
			if (ctx.isSecure())
				connection = createSecureConnection(
						serverContext.getServerHost(), ctx.getPort(),
						serverContext.getServiceName());
			else
				connection = createConnection(serverContext.getServerHost(),
						ctx.getPort(), serverContext.getServiceName());// serverContext.getServiceName()
																		// );

			// increase the response timeout to 10 secs. for slow connections and muc servers
			SmackConfiguration.setPacketReplyTimeout(10000);
			
			// This doesn't work ...
			// XMPPConnection.addConnectionListener(
			// (ConnectionEstablishedListener) this );
			// ///TODO controllare l'ordine tra connect & addListener
			connection.connect();
			System.out.println("secure: " + connection.isSecureConnection() + " TLS:" + connection.isUsingTLS());

			connection.addConnectionListener(this);
			connection.login(userAccount.getId(), userAccount.getPassword());
			buddies.setJabberRoster(connection.getRoster());
		} catch (XMPPException e) {
			// TODO: inserire il metodo per avviare la finestra di riconnessione
			// automatica
			throw new BackendException(e);
		}
		// [CHAT] We need a way to monitor incoming traffic for generating
		// "Wanna chat" events.
		PacketFilter filter = new PacketTypeFilter(Message.class);
		connection.addPacketListener(this, filter);

		MultiUserChat.addInvitationListener(connection, invitationListener);

		// FIXME XMPPConnection.addConnectionListener(
		// (ConnectionEstablishedListener) this ) does nothing :S
		connectionEstablished(connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.
	 * smack.packet.Packet)
	 */
	public void processPacket(Packet packet) {

		Message mess = (Message) packet;
		IBackendEvent event;

		// Il messaggio si riferisce alla chat 1 ad 1
		if (Message.Type.chat.equals(mess.getType())) {

			// test su estensione al protocollo
			if (mess.getProperty(EXTENSION_NAME) != null) {
				HashMap<String, String> prop = new HashMap<String, String>();
				Collection<String> propName = mess.getPropertyNames();
				Iterator<String> it = propName.iterator();
				while (it.hasNext()) {
					String val = it.next();
					prop.put(val, (String) mess.getProperty(val));
				}

				event = new ChatExtensionProtocolEvent(getRoster().getBuddy(
						mess.getFrom()).getId(), (String) mess.getProperty(EXTENSION_NAME)
						, prop, ID);
				notifyEventListeners(event);
				return;
			}

			// ricevuta notifica di composizione
			if (mess.getExtension(TypingNotificationPacket.ELEMENT_NAME,
					TypingNotificationPacket.ELEMENT_NS) != null) {
				event = new ChatComposingtEvent(mess.getFrom(), ID);
				notifyEventListeners(event);
				return;
			}

			// ricevuto messaggio vuoto
			if (mess.getBody() == null || mess.getBody().length() == 0)
				return;

			// ricevuto messaggio contenente testo
			event = new ChatMessageReceivedEvent(getRoster().getBuddy(
					mess.getFrom()), mess.getBody(), ID);
			notifyEventListeners(event);

		}
		// Il messaggio si riferisce alla multichat in una stanza
		else if (Message.Type.groupchat.equals(mess.getType())) {

		}

		/**
		 * final TypingNotificationPacket typingPacket =
		 * (TypingNotificationPacket) mess
		 * .getExtension(TypingNotificationPacket.ELEMENT_NAME,
		 * TypingNotificationPacket.ELEMENT_NS);
		 * 
		 * if(typingPacket != null){ for(IChatServiceListener l:
		 * chatServiceListener) l.TypingMessage(mess.getFrom()); }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#disconnect()
	 */
	public void disconnect() {
		if (connection == null)
			return;
		connection.disconnect();
		connection = null;

		connectionClosed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#isConnected()
	 */
	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#getRoster()
	 */
	public IBuddyRoster getRoster() {
		return buddies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#getUserAccount()
	 */
	public UserContext getUserAccount() {
		return userAccount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#getServerContext()
	 */
	public ServerContext getServerContext() {
		return serverContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jivesoftware.smack.ConnectionEstablishedListener#connectionEstablished
	 * (org.jivesoftware.smack.XMPPConnection)
	 */
	public void connectionEstablished(XMPPConnection connection) {
		notifyOnline();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
	 */
	public void connectionClosed() {
		notifyOffline();

		getRoster().clear();
		this.serverContext = null;
		this.userAccount = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java
	 * .lang.Exception)
	 */
	public void connectionClosedOnError(Exception ex) {
		System.err.println("Connection closed because of an error: "
				+ ex.getMessage());
		// TODO Should diplay and error dialog and clear state.
		notifyOffline();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.IBackend#getConnectJob(it.uniba.di.cdg.
	 * xcore.network.ServerContext, it.uniba.di.cdg.xcore.network.UserAccount)
	 */
	public Job getConnectJob() {
		ConnectionDialog dlg = new ConnectionDialog(new Shell());
		int result = dlg.open();
		if (result == Dialog.OK) {
			final ServerContext serverCtx = dlg.getProfileContext()
					.getServerContext();
			final UserContext userCtx = dlg.getProfileContext()
					.getUserContext();
			final Job connectJob = new Job("Connecting ...") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						connect(serverCtx, userCtx);
					} catch (BackendException e) {
						e.printStackTrace();
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}
			};
			return connectJob;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#getCapabilities()
	 */
	public ICapabilities getCapabilities() {
		return capabilities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.IBackend#createService(it.uniba.di.cdg.
	 * xcore.network.services.ICapability,
	 * it.uniba.di.cdg.xcore.network.services.INetworkServiceContext)
	 */
	public INetworkService createService(ICapability service,
			INetworkServiceContext context) throws BackendException {
		// if (IChatService.CHAT_SERVICE.equals( service ))
		// return new JabberChatService( (ChatContext) context, this );
		// else if (IMultiChatService.MULTI_CHAT_SERVICE.equals( service ))
		// return new JabberMultiChatService( (MultiChatContext) context, this
		// );
		// else if (IEConferenceService.ECONFERENCE_SERVICE.equals( service ))
		// return new JabberEConferenceService( (EConferenceContext) context,
		// this );
		// else if (IPlanningPokerService.PLANNINGPOKER_SERVICE.equals( service
		// ))
		// return new JabberPlanningPokerService( (PlanningPokerContext)
		// context, this );
		INetworkService networkService = findInServiceExtensionPoint(service,
				context);
		if (networkService != null)
			return networkService;
		throw new BackendException(String.format(
				"Unknown service (%s) requested", service));
	}

	private INetworkService findInServiceExtensionPoint(ICapability service,
			INetworkServiceContext context) {
		try {
			ServiceRegistry registry = JabberPlugin.getDefault().getRegistry();
			INetworkService networkService = registry.getService(service
					.getName());
			networkService.setBackend(this);
			networkService.setContext(context);
			return networkService;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.IBackend#setHelper(it.uniba.di.cdg.xcore
	 * .network.INetworkBackendHelper)
	 */
	public void setHelper(INetworkBackendHelper helper) {
		this.helper = helper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.IBackend#getHelper()
	 */
	public INetworkBackendHelper getHelper() {
		return helper;
	}

	/**
	 * Returns the XMPP connection. This method is meant to be used by
	 * extensions.
	 * 
	 * @return the XMPP connection
	 */
	public XMPPConnection getConnection() {
		return connection;
	}

	/**
	 * Returna the user jid. If the backend has no connection then it returns
	 * <code>null</code>.
	 * 
	 * @return a string like <code>harry@ugres.di.uniba.it/Smack</code>
	 */
	public String getUserJid() {
		if (connection != null)
			return connection.getUser();
		return null;
	}

	/**
	 * Convert from a SMACK message to a normal <code>IMessage</code>. TODO Add
	 * more SMACK message types and checks here
	 * 
	 * @param smackMessage
	 * @return the converted message or <code>null</code> if it doesn't know how
	 *         to convert it
	 */
	public IMessage convertFromSmack(Message smackMessage) {
		IMessage message = null;

		// Cripple messages with empty body: they are often used for
		// "secret functions"
		if (smackMessage.getBody() == null
				|| smackMessage.getBody().length() == 0)
			return null;

		/*
		 * if (Message.Type.chat.equals( smackMessage.getType() ) ||
		 * Message.Type.normal.equals( smackMessage.getType() )) { message = new
		 * ChatMessage( smackMessage.getThread(), smackMessage.getFrom(),
		 * smackMessage.getBody(), smackMessage.getSubject() ); } else
		 */if (Message.Type.error.equals(smackMessage.getType())) {
			message = new SystemMessage(smackMessage.getBody());
		}
		return message;
	}

	/**
	 * Notify listeners that this backend has gone online.
	 */
	private void notifyOnline() {
		notifyEventListeners(new BackendStatusChangeEvent(ID, true));
	}

	/**
	 * Notify listeners that this backend has gone offline.
	 */
	private void notifyOffline() {
		notifyEventListeners(new BackendStatusChangeEvent(ID, false));
	}

	/**
	 * Notify all event listeners about an event generated by this backend.
	 * 
	 * @param event
	 */
	private void notifyEventListeners(IBackendEvent event) {
		helper.notifyBackendEvent(event);
	}

	@Override
	public void changePassword(String newpasswd) throws Exception {
		AccountManager man = new AccountManager(this.connection);
		man.changePassword(newpasswd);

	}

	@Override
	public void reconnectingIn(int arg0) {
		
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		
	}

	@Override
	public void reconnectionSuccessful() {
		notifyOnline();
	}

	@Override
	public void registerNewAccount(String userId, String password,
			ServerContext server, Map<String, String> info) throws Exception {
		XMPPConnection conn = new XMPPConnection(server.getServiceName());
		conn.connect();
		conn.getAccountManager().createAccount(userId, password, info);
		conn.disconnect();

	}

	@Override
	public IChatServiceActions getChatServiceAction() {
		return jabberChatServiceAction;
	}

	@Override
	public IMultiChatServiceActions getMultiChatServiceAction() {
		return jabberMultiChatSeviceAction;
	}

	@Override
	public String getUserId() {
		return XMPPUtils.cleanJid(getConnection().getUser());
	}

	@Override
	public String getBackendId() {
		return ID;
	}

	@Override
	public ICallAction getCallAction() {		
		return null;
	}

	@Override
	public IMultiCallAction getMultiCallAction() {		
		return null;
	}

}
