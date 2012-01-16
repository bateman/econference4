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
package it.uniba.di.cdg.xcore.m2m.service;

import it.uniba.di.cdg.xcore.m2m.events.IManagerEventListener;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.events.ViewReadOnlyEvent;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.m2m.model.Participant;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.ITypingEventListener;
import it.uniba.di.cdg.xcore.network.events.TypingEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatInvitationDeclinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatModeratorGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatModeratorRevokedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatNameChangedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatOwnershipGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatOwnershipRevokedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatSubjectUpdatedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserJoinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceRevokedEvent;
import it.uniba.di.cdg.xcore.network.messages.IMessage;
import it.uniba.di.cdg.xcore.network.model.tv.Entry;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.model.tv.TalkModel;
import it.uniba.di.cdg.xcore.network.services.INetworkServiceContext;
import it.uniba.di.cdg.xcore.network.services.IRoomInfo;
import it.uniba.di.cdg.xcore.network.services.JoinException;
import it.uniba.di.cdg.xcore.network.services.NetworkServiceException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Jabber/XMPP implementation of the multi-chat service.
 * <p>
 * Note that we provide additional methods for realizing the IoC build-up of
 * instances of this class (they are not part of {@see
 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService} interface specification
 * but only of this class: they are "plug-in-locally" designed.
 */
public class MultiChatService implements IMultiChatService,
		IBackendEventListener {

	protected final static String PRIVATE_MESSAGE = "PrivateMessage";
	protected final static String MESSAGE = "Message";
	protected final static String TO = "To";
	protected final static String FROM = "From";

	protected final static String VIEW_READ_ONLY = "ViewReadOnly";
	protected final static String VIEW_ID = "viewId";
	protected final static String READ_ONLY = "readOnly";

	protected IMultiChatServiceActions multiChatServiceActions;

	/**
	 * Listeners for notifying the service status.
	 */
	private final List<IMultiChatServiceListener> multiChatListeners;

	/**
	 * Listeners for invitation refusal.
	 */
	protected final List<IInvitationRejectedListener> invitationRejectedListeners;

	/**
	 * Listeners for changes in the local user status.
	 */
	protected final List<IUserStatusListener> userStatusListeners;

	/**
	 * Clients for getting back the messages ...
	 */
	protected final List<IMessageReceivedListener> messageReceivedListeners;

	/**
	 * Typing event listeners for this chat.
	 */
	protected final List<ITypingEventListener> typingListeners;

	/**
	 * Listener for manager events.
	 */
	protected final List<IManagerEventListener> managerEventListeners;

	/**
	 * The chat room: clients may want to register as listeners to this.
	 */
	protected IChatRoomModel chatRoomModel;

	/**
	 * The talk model tracks the message entries.
	 */
	private ITalkModel talkModel;

	/**
	 * The jabber backend which has created this service.
	 */
	private IBackend backend;

	/**
	 * The context associated to this service.
	 */
	private MultiChatContext context;

	/**
	 * Makes up a new multi-chat service based on XMPP: this construct
	 * 
	 * @param context
	 * @param backend
	 */
	public MultiChatService(MultiChatContext context, IBackend backend) {
		this();
		this.context = context;
		this.backend = backend;
	}

	/**
	 * Default constructor
	 */
	public MultiChatService() {
		super();
		this.multiChatListeners = new ArrayList<IMultiChatServiceListener>();
		this.invitationRejectedListeners = new ArrayList<IInvitationRejectedListener>();
		this.messageReceivedListeners = new ArrayList<IMessageReceivedListener>();
		this.userStatusListeners = new ArrayList<IUserStatusListener>();
		this.typingListeners = new ArrayList<ITypingEventListener>();
		this.managerEventListeners = new ArrayList<IManagerEventListener>();

		this.talkModel = new TalkModel();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService#getContext()
	 */
	public MultiChatContext getContext() {
		return context;
	}

	@Override
	public void setContext(INetworkServiceContext context) {
		this.context = (MultiChatContext) context;
	}

	public IMultiChatServiceActions getMultiChatServiceActions() {
		return multiChatServiceActions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#getChatRoomModel()
	 */
	public IChatRoomModel getModel() {
		if (chatRoomModel == null)
			chatRoomModel = createModel();

		return chatRoomModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#setChatRoomModel(
	 * it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel)
	 */
	public void setChatRoomModel(IChatRoomModel model) {
		this.chatRoomModel = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#setTalkModel(it.uniba
	 * .di.cdg.xcore.network.model.tv.ITalkModel)
	 */
	public void setTalkModel(ITalkModel model) {
		this.talkModel = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#getTalkModel()
	 */
	public ITalkModel getTalkModel() {
		return talkModel;
	}

	/**
	 * @return Returns the backend.
	 */
	public IBackend getBackend() {
		return backend;
	}

	/**
	 * @param backend
	 *            The backend to set.
	 */
	public void setBackend(IBackend backend) {
		this.backend = backend;
	}

	/**
	 * @param context
	 *            The context to set.
	 */
	protected void setContext(MultiChatContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.IMultiChatService#getRoomInfo(java.lang.String)
	 */
	public IRoomInfo getRoomInfo(String room) {
		return multiChatServiceActions.getRoomInfo(room);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService#invite(java.lang.String,
	 * java.lang.String)
	 */
	public void invite(String userId, String reason) {
		multiChatServiceActions.invite(getRoomJid(), userId, reason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#declineInvitation
	 * (it.uniba.di.cdg.xcore.m2m.InvitationEvent, java.lang.String)
	 */
	public void declineInvitation(InvitationEvent event, String reason) {
		multiChatServiceActions.declineInvitation(event.getRoom(),
				event.getInviter(), reason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#join()
	 */
	public void join() throws NetworkServiceException, JoinException {

		// credo che il casino dipenda dal fatto che bisogna istanziare un altro
		// backend con il server corretto.....
		multiChatServiceActions = backend.getMultiChatServiceAction();
		getBackend().getHelper().registerBackendListener(this);
		// If no nick name is provided we just use current connection jid
		if ((context.getNickName() == null)
				|| (context.getNickName().equals("")))
			context.setNickName(backend.getUserId());

		boolean moderator = false;

		String userId = backend.getUserId();

		if (context.getModerator() != null
				&& userId.equals(context.getModerator().getId()))
			moderator = true;
		multiChatServiceActions.join(getRoomJid(), context.getPassword(),
				context.getNickName(), getLocalUserId(), moderator);
		updateInitialLocalUserStatus();

		// Notify listeners that we have joined ...
		for (IMultiChatServiceListener l : multiChatListeners)
			l.joined();
	}

	/**
	 * Create a new MUC Room if room doesn't exists
	 * 
	 * @throws XMPPException
	 */
	/*
	 * private void createRoom() throws XMPPException{ boolean created = false;
	 * // actually the only way to know if the room already exists is the
	 * exception try { smackMultiChat.create(context.getNickName()); created =
	 * true; }catch (XMPPException e) { System.out.println("Seems that room " +
	 * context.getRoom() +" already exists. " + "Error: "+ e.getMessage()); }
	 * if(created){ //If we can't get exception means that room doesn't exists
	 * String userId = StringUtils.parseBareAddress(backend.getUserJid());
	 * if(context.getModerator()!=null &&
	 * userId.equals(context.getModerator().getId())){
	 * smackMultiChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT)); // Form
	 * form = smackMultiChat.getConfigurationForm(); // Form answerForm =
	 * form.createAnswerForm(); //
	 * answerForm.setAnswer("muc#roomconfig_moderatedroom", true); //
	 * answerForm.setAnswer("muc#roomconfig_persistentroom", true); // //
	 * smackMultiChat.sendConfigurationForm(answerForm); }else{
	 * smackMultiChat.destroy("Not moderator", ""); throw new
	 * XMPPException("Forbidden. Only the moderator can create a room"); } }
	 * 
	 * }
	 */

	/**
	 * Update the initial participant status.
	 * 
	 */
	private void updateInitialLocalUserStatus() {
		String strRole = multiChatServiceActions.getUserRole(getLocalUserId());
		Role role;

		if ("moderator".equals(strRole) || "owner".equals(strRole))
			role = Role.MODERATOR;
		else if ("participant".equals(strRole))
			role = Role.PARTICIPANT;
		else
			role = Role.VISITOR;

		IParticipant localUser = createLocalParticipant();

		// Server not returned occupants info
		if (role == null) {
			System.out
					.println("Warning: unable to get own user info from server. Using defaults.");

			localUser.setStatus(Status.JOINED);
			getModel().setLocalUser(localUser);
			getModel().addParticipant(localUser);

		} else { // Server returned occupants info

			localUser.setRole(role);
			localUser.setStatus(Status.JOINED);

			getModel().addParticipant(localUser);
			getModel().setLocalUser(localUser);

		}
	}

	/**
	 * @param role
	 *            string
	 * @return role object
	 */
	public static Role fromSmackRole(String role) {
		if ("moderator".equals(role) || "owner".equals(role))
			return Role.MODERATOR;
		else if ("participant".equals(role))
			return Role.PARTICIPANT;
		return Role.VISITOR;
	}

	/**
	 * Returns the jabbed id of the chat room: this is needed when broadcasting
	 * messages.
	 * 
	 * @return the jabber id
	 */
	private String getRoomJid() {
		return context.getRoom();
	}

	/**
	 * Create and return an <code>IParticipant</code> which mocks the current
	 * local user.
	 * 
	 * @return the local participant
	 */
	private IParticipant createLocalParticipant() {
		String id = getLocalUserId();

		IParticipant local = new Participant(getModel(), id,
				context.getNickName(), context.getPersonalStatus(),
				Role.PARTICIPANT);

		return local;
	}

	/**
	 * Returns the id of the local user. The format depends on the backend.
	 * 
	 * @return the jid of the local user
	 */
	protected String getLocalUserId() {
		return backend.getUserId();
	}

	@Override
	public void leave() {
		getBackend().getHelper().unregisterBackendListener(this);

		multiChatServiceActions.leave();
		// Notify listeners that we have left the chatroom ...
		for (IMultiChatServiceListener l : multiChatListeners)
			l.left();
	}

	@Override
	public void sendPrivateMessage(IParticipant p, String message) {
		String userId = p.getId();

		if (userId.equals(getLocalUserId()))
			notifyLocalSystemMessage("It's not possible to send a private message to yourself");
		else {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put(MESSAGE, message);
			param.put(TO, userId);
			param.put(FROM, getLocalUserId());

			multiChatServiceActions.SendExtensionProtocolMessage(
					PRIVATE_MESSAGE, param);

			notifyLocalSystemMessage(String.format("[PM sent to %s] %s",
					p.getNickName(), message));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#sendMessage(java.
	 * lang.String)
	 */
	public void sendMessage(String message) {

		// here there's no need to escape the message
		multiChatServiceActions.sendMessage(getRoomJid(), message);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#grantVoice(java.util
	 * .List)
	 */
	public void grantVoice(List<String> nickNames) {

		for (int i = 0; i < nickNames.size(); i++)
			grantVoice(nickNames.get(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#grantVoice(java.lang
	 * .String)
	 */
	public void grantVoice(String nickName) {
		if (nickName.equals(getLocalUserId()))
			notifyLocalSystemMessage("It's not possible to allow back in conversation to yourself");
		else
			multiChatServiceActions.grantVoice(getRoomJid(), nickName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#revokeVoice(java.
	 * util.List)
	 */
	public void revokeVoice(List<String> nickNames) {
		for (int i = 0; i < nickNames.size(); i++)
			revokeVoice(nickNames.get(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#revokeVoice(java.
	 * lang.String)
	 */
	public void revokeVoice(String nickName) {
		if (nickName.equals(getLocalUserId()))
			notifyLocalSystemMessage("It's not possible to block back in conversation to yourself");
		else
			multiChatServiceActions.revokeVoice(getRoomJid(), nickName);
	}

	/**
	 * Create the model.
	 * <p>
	 * Derived classes may provide a different model implementation.
	 * 
	 * @return the model
	 */
	protected IChatRoomModel createModel() {
		return new ChatRoomModel();
	}

	/**
	 * @param incoming
	 * @return
	 */
	protected Entry makeEntryFromMessage(IMessage incoming) {
	    // XXX: shouldn't this be the same of TalkView::makeEntry()?
		final Date now = Calendar.getInstance().getTime();

		Entry entry = new Entry();
		entry.setTimestamp(now);
		entry.setText(incoming.getText());
		entry.setType(Entry.EntryType.CHAT_MSG);

		IParticipant p = getLocalUserOrParticipant(incoming.getFrom());
		if (p != null) {

			entry.setWho(p.getNickName());
		} else {
			entry.setWho(removeRoomAddress(incoming.getFrom()));

		}
		return entry;
	}

	protected String removeRoomAddress(String sender) {
		String nick = sender;
		String room = getContext().getRoom().concat("/");
		if (sender.startsWith(room)) {
			nick = sender.substring(room.length());
		}
		return nick;
	}

	/**
	 * Notify a message to the manager: this is usually meant for displaying
	 * some system status to the user (i.e. another user frozen).
	 * <p>
	 * Note that this message WON'T notify anything to remote clients: it is
	 * only meant as a display on local client.
	 * 
	 * @param message
	 */
	protected void notifyLocalSystemMessage(String message) {
		// SystemMessage sysMessage = new SystemMessage( message );
		final Date now = Calendar.getInstance().getTime();

		Entry entry = new Entry();
		entry.setType(Entry.EntryType.SYSTEM_MSG);
		entry.setTimestamp(now);
		entry.setText(message);

		getTalkModel().addEntry(entry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#changeSubject(java
	 * .lang.String)
	 */
	public void changeSubject(String subject) {
		multiChatServiceActions.changeSubject(getRoomJid(), subject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#
	 * addMessageReceivedListener
	 * (it.uniba.di.cdg.xcore.m2m.service.IMessageReceivedListener)
	 */
	public void addMessageReceivedListener(IMessageReceivedListener listener) {
		messageReceivedListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#
	 * removeMessageReceivedListener
	 * (it.uniba.di.cdg.xcore.m2m.service.IMessageReceivedListener)
	 */
	public void removeMessageReceivedListener(IMessageReceivedListener listener) {
		messageReceivedListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#addMultiChatListener
	 * (it
	 * .uniba.di.cdg.xcore.m2m.service.IMultiChatService.IMultiChatServiceListener
	 * )
	 */
	public void addServiceListener(IMultiChatServiceListener listener) {
		multiChatListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#removeMultiChatListener
	 * (
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService.IMultiChatServiceListener
	 * )
	 */
	public void removeServiceListener(IMultiChatServiceListener listener) {
		multiChatListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.IMultiChatService#addInvitationRejectedListener
	 * (it.uniba.di.cdg.xcore.m2m.IMultiChatService.IInvitationRejectedListener)
	 */
	public void addInvitationRejectedListener(
			IInvitationRejectedListener listener) {
		invitationRejectedListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.IMultiChatService#removeInvitationRejectedListener
	 * (it.uniba.di.cdg.xcore.m2m.IMultiChatService.IInvitationRejectedListener)
	 */
	public void removeInvitationRejectedListener(
			IInvitationRejectedListener listener) {
		invitationRejectedListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.events.ITypingListener#typing()
	 */
	public void typing() {
		System.out.println("Notify typing ...");
		multiChatServiceActions.sendTyping(getModel().getLocalUser()
				.getNickName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.events.ITypingNotificationSupport#
	 * addTypingEventListener
	 * (it.uniba.di.cdg.xcore.network.events.ITypingEventListener)
	 */
	public void addTypingEventListener(ITypingEventListener listener) {
		typingListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.network.events.ITypingNotificationSupport#
	 * removeTypingEventListener
	 * (it.uniba.di.cdg.xcore.network.events.ITypingEventListener)
	 */
	public void removeTypingEventListener(ITypingEventListener listener) {
		typingListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#addUserStatusListener
	 * (it.uniba.di.cdg.xcore.m2m.service.IUserStatusListener)
	 */
	public void addUserStatusListener(IUserStatusListener listener) {
		userStatusListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#removeUserStatusListener
	 * (it.uniba.di.cdg.xcore.m2m.service.IUserStatusListener)
	 */
	public void removeUserStatusListener(IUserStatusListener listener) {
		userStatusListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#notifyViewReadOnly
	 * (java.lang.String, boolean)
	 */
	public void notifyViewReadOnly(String viewId, boolean readOnly) {
		// sendCustomExtension( new ViewReadOnlyPacket( viewId, readOnly ) );
		HashMap<String, String> param = new HashMap<String, String>();
		param.put(VIEW_ID, viewId);
		param.put(READ_ONLY, new Boolean(readOnly).toString());

		multiChatServiceActions.SendExtensionProtocolMessage(VIEW_READ_ONLY,
				param);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#addManagerEventListener
	 * (it.uniba.di.cdg.xcore.m2m.events.IManagerEventListener)
	 */
	public void addManagerEventListener(IManagerEventListener listener) {
		managerEventListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.service.IMultiChatService#
	 * removeManagerEventListener
	 * (it.uniba.di.cdg.xcore.m2m.events.IManagerEventListener)
	 */
	public void removeManagerEventListener(IManagerEventListener listener) {
		managerEventListeners.remove(listener);
	}

	/**
	 * Returns the participant, searching between the local and the others
	 * remote.
	 * 
	 * @param who
	 *            the id of the participant to search
	 * @return the participant or <code>null</code> if the id is unknown
	 */
	protected IParticipant getLocalUserOrParticipant(String who) {
		return getModel().getLocalUserOrParticipant(who);
	}

	/**
	 * Provides internal thread synchronization.
	 */
//	@Aspect
//	public static class OwnThreadSafety extends ThreadSafetyAspect {
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#readOperations()
//		 */
//		@Pointcut("execution( public void org.jivesoftware.smackx.muc.DefaultUserStatusListener+.*(..) )")
//		protected void readOperations() {
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#writeOperations()
//		 */
//		@Pointcut("execution( private void JabberMultiChatService.updateFirstTime() )"
//				+ "|| execution( public void org.jivesoftware.smackx.muc.DefaultParticipantStatusListener+.*(..) )"
//				+ "|| execution( public void org.jivesoftware.smackx.muc.SubjectUpdatedListener+.*(..) )")
//		protected void writeOperations() {
//		}
//	}

	@Override
	public void onBackendEvent(IBackendEvent event) {

		if (manageMultiChatMessage(event))
			return;

		if (manageInvitationDeclined(event))
			return;

		if (manageSubjectUpdated(event))
			return;

		if (manageUserJoined(event))
			return;

		if (manageUserLeft(event))
			return;

		if (manageNameChanged(event))
			return;

		if (manageVoiceGranted(event))
			return;

		if (manageVoiceRevoked(event))
			return;

		if (manageModeratorGranted(event))
			return;

		if (manageModeratorRevoked(event))
			return;

		if (manageOwnershipGranted(event))
			return;

		if (manageOwnershipRevoked(event))
			return;

		if (manageChatComposing(event))
			return;

		if (event instanceof MultiChatExtensionProtocolEvent) {
			MultiChatExtensionProtocolEvent mcepe = (MultiChatExtensionProtocolEvent) event;
			if (managePrivateMessage(mcepe))
				return;
			if (manageViewReadOnly(mcepe))
				return;
		}

	}



	protected boolean manageViewReadOnly(MultiChatExtensionProtocolEvent mcepe) {
		if (mcepe.getExtensionName().equals(VIEW_READ_ONLY)) {
			String viewId = (String) mcepe.getExtensionParameter(VIEW_ID);
			String strReadOnly = (String) mcepe
					.getExtensionParameter(READ_ONLY);
			boolean readOnly = new Boolean(strReadOnly).booleanValue();
			ViewReadOnlyEvent viewReadOnlyEvent = new ViewReadOnlyEvent(viewId,
					readOnly);
			for (IManagerEventListener l : managerEventListeners)
				l.onManagerEvent(viewReadOnlyEvent);

			return true;
		}
		return false;
	}

	protected boolean managePrivateMessage(MultiChatExtensionProtocolEvent mcepe) {
		if (mcepe.getExtensionName().equals(PRIVATE_MESSAGE)) {
			if (mcepe.getExtensionParameter(TO).equals(getLocalUserId())) {
				final String from = (String) mcepe.getExtensionParameter(FROM);
				final String text = (String) mcepe
						.getExtensionParameter(MESSAGE);
				final IParticipant p = getLocalUserOrParticipant(from);

				if (p == null)
					return true;
				for (IMessageReceivedListener l : messageReceivedListeners)
					l.privateMessageReceived(text, p.getNickName());
			}
			return true;
		}
		return false;

	}

	protected boolean manageChatComposing(IBackendEvent event) {
		if (event instanceof MultiChatComposingEvent) {
			MultiChatComposingEvent mcte = (MultiChatComposingEvent) event;
			IParticipant p = getLocalUserOrParticipant(mcte.getFrom());
			TypingEvent typingEvent = new TypingEvent(
					p == null ? mcte.getFrom() : p.getNickName());
			for (ITypingEventListener l : typingListeners)
				l.onTyping(typingEvent);
			return true;
		}
		return false;
	}

	protected boolean manageOwnershipRevoked(IBackendEvent event) {
		if (event instanceof MultiChatOwnershipRevokedEvent) {
			MultiChatOwnershipRevokedEvent mcore = (MultiChatOwnershipRevokedEvent) event;
			if (mcore.getUserId().equals(getLocalUserId())) {
				for (IUserStatusListener l : userStatusListeners)
					l.ownershipRevoked();
			}
			return true;
		}
		return false;
	}

	protected boolean manageOwnershipGranted(IBackendEvent event) {
		if (event instanceof MultiChatOwnershipGrantedEvent) {
			MultiChatOwnershipGrantedEvent mcoge = (MultiChatOwnershipGrantedEvent) event;
			if (mcoge.getUserId().equals(getLocalUserId())) {
				for (IUserStatusListener l : userStatusListeners)
					l.ownershipGranted();
			}
			return true;
		}
		return false;
	}

	protected boolean manageModeratorRevoked(IBackendEvent event) {
		if (event instanceof MultiChatModeratorRevokedEvent) {
			MultiChatModeratorRevokedEvent mcmre = (MultiChatModeratorRevokedEvent) event;
			getModel().getParticipant(mcmre.getUserId()).setRole(Role.VISITOR);
			if (mcmre.getUserId().equals(getLocalUserId())) {
				for (IUserStatusListener l : userStatusListeners)
					l.moderatorRevoked();
			}
			return true;
		}
		return false;
	}

	protected boolean manageModeratorGranted(IBackendEvent event) {
		if (event instanceof MultiChatModeratorGrantedEvent) {
			MultiChatModeratorGrantedEvent mcmge = (MultiChatModeratorGrantedEvent) event;
			getModel().getParticipant(mcmge.getUserId())
					.setRole(Role.MODERATOR);
			if (mcmge.getUserId().equals(getLocalUserId())) {
				for (IUserStatusListener l : userStatusListeners)
					l.moderatorGranted();
			}
			return true;
		}
		return false;
	}

	protected boolean manageVoiceRevoked(IBackendEvent event) {
		if (event instanceof MultiChatVoiceRevokedEvent) {
			MultiChatVoiceRevokedEvent mcvre = (MultiChatVoiceRevokedEvent) event;
			IParticipant p = chatRoomModel.getParticipant(mcvre.getUserId());
			if (p == null)
				return true;
			p.setStatus(Status.FROZEN);
			// if is local user
			if (mcvre.getUserId().equals(getLocalUserId())) {
				for (IUserStatusListener l : userStatusListeners)
					l.voiceRevoked();
			} else
				notifyLocalSystemMessage(String.format(
						"Moderator has stopped %s from "
								+ "contributing to conversation",
						p.getNickName()));
			return true;
		}
		return false;
	}

	protected boolean manageVoiceGranted(IBackendEvent event) {
		if (event instanceof MultiChatVoiceGrantedEvent) {
			MultiChatVoiceGrantedEvent mcvge = (MultiChatVoiceGrantedEvent) event;
			IParticipant p = chatRoomModel.getParticipant(mcvge.getUserId());
			if (p == null)
				return true;
			p.setStatus(Status.JOINED);
			if (mcvge.getUserId().equals(getLocalUserId())) {
				for (IUserStatusListener l : userStatusListeners)
					l.voiceGranted();
			} else
				notifyLocalSystemMessage(String.format(
						"Moderator has allowed %s back " + "in conversation",
						p.getNickName()));
			return true;
		}
		return false;
	}

	protected boolean manageNameChanged(IBackendEvent event) {
		if (event instanceof MultiChatNameChangedEvent) {
			MultiChatNameChangedEvent mcnce = (MultiChatNameChangedEvent) event;
			IParticipant p = getModel().getParticipant(mcnce.getUserId());
			if (p != null) {
				// p.setNickName( nickName );
				// The server will notify a joined() after this which will make
				// us
				// to duplicate entries so
				// we remove it now
				getModel().removeParticipant(p);
			}
			return true;
		}
		return false;
	}

	protected boolean manageUserLeft(IBackendEvent event) {
		if (event instanceof MultiChatUserLeftEvent) {
			MultiChatUserLeftEvent mcule = (MultiChatUserLeftEvent) event;
			String nick = mcule.getNickname();
			IParticipant p = getModel().getParticipantByNickName(nick);
			if (p == null) {
				p = getModel().getParticipant(mcule.getUserId());
				if (p == null)
					return true;
			}
			getModel().removeParticipant(p);
			return true;
		}
		return false;
	}

	protected boolean manageUserJoined(IBackendEvent event) {
		if (event instanceof MultiChatUserJoinedEvent) {
			MultiChatUserJoinedEvent mcuje = (MultiChatUserJoinedEvent) event;
			IParticipant p = getModel().getParticipant(mcuje.getUserId());
			if (p == null) {
				Role role = fromSmackRole(mcuje.getRole());
				p = new Participant(getModel(), mcuje.getUserId(),
						mcuje.getUserName(), "", role, Status.JOINED);
				getModel().addParticipant(p);
			} else
				p.setStatus(Status.JOINED);
			return true;
		}
		return false;
	}

	protected boolean manageSubjectUpdated(IBackendEvent event) {
		if (event instanceof MultiChatSubjectUpdatedEvent) {
			MultiChatSubjectUpdatedEvent mcsue = (MultiChatSubjectUpdatedEvent) event;
			System.out.println(String.format("[SMACK] subjectUpdated(%s, %s)",
					mcsue.getSubject(), mcsue.getFrom()));
			getModel().setSubject(mcsue.getSubject(), mcsue.getFrom());
			return true;
		}
		return false;
	}

	protected boolean manageInvitationDeclined(IBackendEvent event) {
		if (event instanceof MultiChatInvitationDeclinedEvent) {
			MultiChatInvitationDeclinedEvent mcide = (MultiChatInvitationDeclinedEvent) event;
			System.out.println(String.format(
					"[SMACK] invitationDeclined(%s, %s)", mcide.getInvitee(),
					mcide.getReason()));
			for (IInvitationRejectedListener l : invitationRejectedListeners)
				l.declined(mcide.getInvitee(), mcide.getReason());
			return true;
		}
		return false;
	}

	protected boolean manageMultiChatMessage(IBackendEvent event) {
		// Normal message then ...
		if (event instanceof MultiChatMessageEvent) {
			MultiChatMessageEvent mcme = (MultiChatMessageEvent) event;
			IMessage incoming = new MultiChatMessage(getModel(),
					mcme.getFrom(), mcme.getMessage());

			getTalkModel().addEntry(makeEntryFromMessage(incoming));
			return true;
		}
		return false;
	}
}
