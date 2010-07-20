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
import it.uniba.di.cdg.xcore.econference.ui.dialogs.InviteWizard;
import it.uniba.di.cdg.xcore.econference.ui.dialogs.JoinConferenceDialog;
import it.uniba.di.cdg.xcore.m2m.IMultiChatManager.IMultiChatListener;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.IUIHelper;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.Preferences;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.econference.IEConferenceHelper}
 * .
 */
public class EConferenceHelper implements IEConferenceHelper {

	/**
	 * The User interface helper provides function for accessing user interface
	 * without caring on how it is implemented.
	 */
	private final IUIHelper uihelper;

	/**
	 * The backend helper provides dependency object for handling backends.
	 */
	private final INetworkBackendHelper backendHelper;

	/**
	 * Construct a new helper for econferences. This constructor acts as
	 * "constructor injection" in IoC.
	 * 
	 * @param uihelper
	 */
	public EConferenceHelper(IUIHelper uihelper,
			INetworkBackendHelper backendHelper) {
		this.uihelper = uihelper;
		this.backendHelper = backendHelper;
	}

	// nn credo sia + necessario che sia un backend listener
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.econference.IEConferenceHelper#init()
	 */
	public void init() {
		// Collection<IBackendDescriptor> descriptors =
		// backendHelper.getRegistry().getDescriptors();
		// for (IBackendDescriptor d : descriptors) {
		// backendHelper.registerBackendListener( d.getId(), this );
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.uniba.di.cdg.xcore.econference.IEConferenceHelper#dispose()
	 */
	public void dispose() {
		// Collection<IBackendDescriptor> descriptors =
		// backendHelper.getRegistry().getDescriptors();
		// for (IBackendDescriptor d : descriptors) {
		// backendHelper.unregisterBackendListener( d.getId(), this );
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceHelper#open(it.uniba.di
	 * .cdg.xcore.econference.EConferenceContext)
	 */
	public IEConferenceManager open(EConferenceContext context) {
		IEConferenceManager manager = null;

		try {
			final IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			manager = new EConferenceManager();
			manager.setBackendHelper(NetworkPlugin.getDefault().getHelper());
			manager.setUihelper(UiPlugin.getUIHelper());
			manager.setWorkbenchWindow(window);
			manager.addListener(new IMultiChatListener() {
				private Point previousSize;

				public void open() {
					System.out.println("Resizing window!");
					Shell shell = window.getShell();
					Point size = shell.getSize();
					if (size.x < 800 || size.y < 600)
						shell.setSize(800, 600);
					previousSize = size;
				}

				public void closed() {
					Shell shell = window.getShell();
					shell.setSize(previousSize);
				}
			});

			manager.open(context);
		} catch (Exception e) {
			e.printStackTrace();
			uihelper.showErrorMessage("Could not start eConference: "
					+ e.getMessage());

			// Close this perspective since it is unuseful ...
			uihelper.closeCurrentPerspective();
		}
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceHelper#openFromFile(java
	 * .lang.String)
	 */
	public void openFromFile() {
		openFromFile("");
	}
	
	@Override
	public void openFromFile(String filepath) {
		final JoinConferenceDialog dlg = new JoinConferenceDialog(null, filepath);
		dlg.setFileName(filepath);
		if (Dialog.OK == dlg.open()) {
			// 1. Open a file dialog, asking the conference file name
			IEConferenceManager manager = open(dlg.getContext());

			if (dlg.isSendInvitations()) {
				for (Invitee i : dlg.getContext().getInvitees())
					manager.inviteNewParticipant(i.getId());
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceHelper#askUserAcceptInvitation
	 * (it.uniba.di.cdg.xcore.m2m.InvitationEvent)
	 */

	public void openInviteWizard() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		InviteWizard wizard = new InviteWizard();
		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
		if (wizard.canSendInvitation()) {
			IEConferenceManager manager = open(wizard.getContext());
			for (Invitee i : wizard.getContext().getInvitees())
				manager.inviteNewParticipant(i.getId());
		}
	}

	public EConferenceContext askUserAcceptInvitation(InvitationEvent invitation) {
		// Skip invitations which do not interest us ...
		if (!ECONFERENCE_REASON.equals(invitation.getReason()))
			return null;

		IBackend backend = backendHelper.getRegistry().getBackend(
				invitation.getBackendId());

		String message = String
				.format("User %s has invited you to join to an eConference."
						+ "\nIf you want to accept, choose your display name and press Yes, otherwise press Cancel.",
						invitation.getInviter());
		String chosenNickNamer = uihelper.askFreeQuestion(
				"Invitation received", message, backend.getUserAccount()
						.getId());
		if (chosenNickNamer != null) {
			EConferenceContext context = new EConferenceContext(
					chosenNickNamer, "", invitation);
			return context;
		} else
			invitation.decline("No reason");

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceHelper#getBooleanPreference
	 * (java.lang.String)
	 */
	public boolean getBooleanPreference(final String prefName) {
		Preferences preferences = new ConfigurationScope()
				.getNode(ECONFERENCE_PREFS_NODE);
		return preferences.getBoolean(prefName, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.IEConferenceHelper#getStringPreference
	 * (java.lang.String)
	 */
	public String getStringPreference(String prefName) {
		Preferences preferences = new ConfigurationScope()
				.getNode(ECONFERENCE_PREFS_NODE);
		return preferences.get(prefName, "");
	}

}
