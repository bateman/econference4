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
package it.uniba.di.cdg.xcore.econference.internal;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.IEConferenceHelper;
import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.IEConferenceService;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel.ConferenceStatus;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;
import it.uniba.di.cdg.xcore.econference.service.EConferenceService;
import it.uniba.di.cdg.xcore.econference.toolbar.handler.ManagerTransport;
import it.uniba.di.cdg.xcore.econference.ui.views.AgendaView;
import it.uniba.di.cdg.xcore.econference.ui.views.HandRaisingView;
import it.uniba.di.cdg.xcore.econference.ui.views.IAgendaView;
import it.uniba.di.cdg.xcore.econference.ui.views.IHandRaisingView;
import it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard;
import it.uniba.di.cdg.xcore.econference.ui.views.WhiteBoardView;
import it.uniba.di.cdg.xcore.m2m.internal.MultiChatManager;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModelAdapter;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.Privileged;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.messages.SystemMessage;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.WorkbenchException;

/**
 * E-Conference controller.
 */
public class EConferenceManager extends MultiChatManager implements
		IEConferenceManager {

	public String FREE_TALK_NOW_MESSAGE = "Free talk now ...";

	protected String conferenceStartedMessage = "The conference has been STARTED";

	public String conferenceStoppedMessage = "The conference has been STOPPED";

	/**
	 * The agenda view give the moderator the ability to start / stop the
	 * conference. This is <code>null</code> if the current local user's role is
	 * not "moderator".
	 */
	protected IAgendaView agendaView;

	/**
	 * The whiteboard gives
	 */
	protected IWhiteBoard whiteBoardView;

	/**
	 * Hand raising view.
	 */
	protected IHandRaisingView handRaisingView;

	// We just put the talk view read-only when the conference is stopped, and
	// reenable it
	// when it is started.
	protected ConferenceModelListenerAdapter conferenceModelListener = new ConferenceModelListenerAdapter() {
		@Override
		public void statusChanged() {
			final IConferenceModel model = getService().getModel();
			boolean conferenceStopped = ConferenceStatus.STOPPED.equals(model
					.getStatus());

			String threadId = ITalkModel.FREE_TALK_THREAD_ID;
			if (conferenceStopped) {
				getTalkView().getModel().setCurrentThread(ITalkModel.FREE_TALK_THREAD_ID);
				getTalkView().appendMessage(
						new SystemMessage(conferenceStoppedMessage));
				getTalkView().setTitleText(FREE_TALK_NOW_MESSAGE);
			} else {
				getTalkView().appendMessage(
						new SystemMessage(conferenceStartedMessage));

				int currItem = getService().getModel().getItemList()
						.getCurrentItemIndex();
				// This case tipically occurs when the conference is started for
				// the first time: no
				// item is still selected so we remain on the previous
				// "free talk" thread ...
				if (currItem == IItemList.NO_ITEM_SELECTED)
					return;
				threadId = Integer.toString(currItem);
			}
			// Notify to remote clients if we are moderators ...
			if (Role.MODERATOR.equals(getService().getModel().getLocalUser()
					.getRole()))
				notifyCurrentAgendaItemChanged(threadId);
		}
	};

	// TODO eliminare?
	/**
	 * Keep the subject in sync with the agenda selections made by moderators.
	 *
	protected IItemListListener itemListListener = new ItemListListenerAdapter() {
		@Override
		public void currentSelectionChanged(int currItemIndex) {
			// Commented out upon request from fc on 20.01.2006.
			// The change of the subject is performed in the econferenceservice
			// ...
			// if (currItemIndex > -1 ) {
			// String newSubject =
			// getService().getModel().getItemList().getItem( currItemIndex
			// ).getText();
			// getService().getModel().setSubject( newSubject, "" );
			// }
		}
	};*/

	/**
	 * Construct a new conference manager.
	 */
	public EConferenceManager() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConference#setStatus(it.uniba.di.
	 * cdg.xcore.econference.IEConferenceService.ConferenceStatus)
	 */
	public void setStatus(ConferenceStatus status) {
		if (status.equals(ConferenceStatus.STOPPED))
			whiteBoardView.setReadOnly(true);
		else
			whiteBoardView.setReadOnly(false);

		getService().notifyStatusChange(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.econference.IEConference#getStatus()
	 */
	public ConferenceStatus getStatus() {
		return getService().getModel().getStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.MultiChat#setupUI()
	 */
	@Override
	protected void setupUI() throws WorkbenchException {
		super.setupUI();

		IViewPart viewPart;
		viewPart = setupUIAgendaView();

		viewPart = setupUIWhiteBoard();

		viewPart = setupUIHandRaise();

		setupUIInitializaTalkView();

	}

	protected void setupUIInitializaTalkView() {
		// By default user can chat freely before the conference is started
		getTalkView().setReadOnly(false);
		// Display that there is free talk ongoing
		getTalkView().setTitleText(FREE_TALK_NOW_MESSAGE);

		// Ensure that the focus is switched to this new chat
		((IViewPart) getTalkView()).setFocus();

	}

	protected IViewPart setupUIHandRaise() throws WorkbenchException {
		// Hand raising panel is for all: context menu actions will be disabled
		// using the
		// actions' enablements provided by the plugin.xml and the adapter
		// factory
		IViewPart viewPart = getWorkbenchWindow().getActivePage().showView(
				HandRaisingView.ID);
		handRaisingView = (IHandRaisingView) viewPart;
		handRaisingView.setManager(this);
		// handRaisingView.setReadOnly( true );
		return viewPart;
	}

	protected IViewPart setupUIWhiteBoard() throws WorkbenchException {
		IViewPart viewPart = getWorkbenchWindow().getActivePage().showView(
				WhiteBoardView.ID);
		whiteBoardView = (IWhiteBoard) viewPart;
		whiteBoardView.setManager(this);
		// By default the whiteboard cannot be modified: when the user is given
		// the SCRIBE
		// special role than it will be set read-write
		whiteBoardView.setReadOnly(true);
		return viewPart;
	}

	protected IViewPart setupUIAgendaView() throws WorkbenchException {
		// The agenda is visible to moderators only
		// XXX This should be made dynamic to copy with runtime role changes. A
		// model
		// listener for this local user should perform ok.
		IViewPart viewPart = getWorkbenchWindow().getActivePage().showView(
				AgendaView.ID);
		agendaView = (IAgendaView) viewPart;
		agendaView.setManager(this);
		//agendaView.setReadOnly(!Role.MODERATOR.equals(getService().getModel().getLocalUser()
			//	.getRole()));
		
		ManagerTransport m_t = new ManagerTransport();
		m_t.setManager(this);
		
		return viewPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.MultiChat#setupChatService()
	 */
	@Override
	protected IEConferenceService setupChatService() throws BackendException {
		IBackend backend = getBackendHelper().getRegistry().getBackend(
				getContext().getBackendId());

		return new EConferenceService(getContext(), backend);
		/*
		 * return (IEConferenceService) backend.createService(
		 * IEConferenceService.ECONFERENCE_SERVICE, getContext() );
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.MultiChat#setupListeners()
	 */
	@Override
	protected void setupListeners() {
		super.setupListeners();

		final IConferenceModel model = (IConferenceModel) getService()
				.getModel();
		model.addListener(conferenceModelListener);

		// TODO model.getItemList().addListener(itemListListener);

		// The multichat has already registered a listener for participant
		// status changes: so
		// we simply register a listener and notify the item list to users that
		// joins:
		model.addListener(new ChatRoomModelAdapter() {
			@Override
			public void added(IParticipant participant) {
				// XXX Sigh!, we notify to _all_ since current XMPP server
				// doesn't work
				// (returns error "400") with messages to single participants :S
				getService().notifyItemListToRemote();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.IMultiChat#getService()
	 */
	public IEConferenceService getService() {
		return (IEConferenceService) super.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.m2m.MultiChat#getContext()
	 */
	@Override
	protected EConferenceContext getContext() {
		return (EConferenceContext) super.getContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.m2m.MultiChatManager#inviteNewParticipant(java.
	 * lang.String)
	 */
	@Override
	public void inviteNewParticipant(String participantId) {
		getService().invite(participantId,
				IEConferenceHelper.ECONFERENCE_REASON);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceManager#notifyWhiteBoardChanged
	 * (java.lang.String)
	 */
	public void notifyWhiteBoardChanged(String text) {
		if (null != text)
			getService().notifyWhiteBoardChanged(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.econference.IEConferenceManager#
	 * notifySpecialRoleChanged(it.uniba.di.cdg.xcore.m2m.model.IParticipant,
	 * java.lang.String)
	 */
	@Privileged(atleast = Role.MODERATOR)
	public void notifySpecialPrivilegeChanged(IParticipant p,
			String newPrivilege, String action) {
		getService().notifyChangedSpecialPrivilege(p, newPrivilege, action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.econference.IEConferenceManager#
	 * notifySpecialRoleChanged(it.uniba.di.cdg.xcore.m2m.model.IParticipant,
	 * java.lang.String)
	 */
	public void notifyChangedMUCPersonalPrivilege(IParticipant participant,
			String personalStatus) {
		getService().notifyChangedMUCPersonalPrivilege(participant,
				personalStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceManager#notifyRaiseHand
	 * (it.uniba.di.cdg.xcore.m2m.model.IParticipant, java.lang.String)
	 */
	public void notifyRaiseHand(IParticipant moderator, String question) {
		getService().notifyRaiseHand(question, moderator.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceManager#notifyQuestionUpdate
	 * (it.uniba.di.cdg.xcore.econference.model.hr.IQuestion)
	 */
	public void notifyQuestionUpdate(IQuestion q) {
		final IParticipant p = getService().getModel().getParticipant(
				q.getWho());
		if (p == null) {
			System.err
					.println("Arghhh!!! Notifying question update for unknown participant "
							+ q.getWho());
			return;
		}

		System.out.println(String.format(
				"Notify update for question %d from %s (%s)", q.getId(),
				q.getWho(), q.getStatus()));
		if (QuestionStatus.APPROVED.equals(q.getStatus())) {
			getService().grantVoice(p.getNickName());
		} else if (QuestionStatus.REJECTED.equals(q.getStatus())) {
			// Leave his status invariated for now ...
			// getService().revokeVoice( p.getNickName() );
		}
		getService().notifyQuestionUpdate(q);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.econference.IEConferenceManager#
	 * notifyCurrentAgendaItemChanged(java.lang.String)
	 */
	@Privileged(atleast = Role.MODERATOR)
	public void notifyCurrentAgendaItemChanged(String itemId) {
		getService().notifyCurrentAgendaItemChanged(itemId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceManager#notifyItemListToRemote
	 * ()
	 */
	@Privileged(atleast = Role.MODERATOR)
	public void notifyItemListToRemote() {
		getService().notifyItemListToRemote();
	}
}
