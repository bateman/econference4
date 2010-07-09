/*
 * Licensed Material - Property of IBM 

 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */

package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * Wizard class
 */

public class InviteWizard extends Wizard implements INewWizard {
	// wizard pages
	GenInfoPage genInfoPage;
	InvitePage invitePage;
	LastPage lastOnePage;
	EConferenceContext context;
	// skypeBackend or jabber's one
	private String media;

	// the workbench instance
	protected IWorkbench workbench;

	public InviteWizard() {
		super();
	}

	public void addPages() {
		genInfoPage = new GenInfoPage("");
		addPage(genInfoPage);
		invitePage = new InvitePage("");
		addPage(invitePage);
		lastOnePage = new LastPage("");
		addPage(lastOnePage);
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getMedia() {
		return this.media;
	}

	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() == genInfoPage)
			return false;
		if (this.getContainer().getCurrentPage() == invitePage)
			return false;
		return true;
	}

	public boolean performCancel() {
		lastOnePage.setCanSendInvitations(false);
		return true;
	}

	public boolean performFinish() {
		lastOnePage.saveData();
		this.context = lastOnePage.getContext();
		return true;
	}

	public boolean canSendInvitation() {
		return this.lastOnePage.getCanSendInvitations();
	}

	public EConferenceContext getContext() {
		return this.context;
	}
}
