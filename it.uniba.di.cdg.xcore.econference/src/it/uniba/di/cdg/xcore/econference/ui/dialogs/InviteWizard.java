/*
 * Licensed Material - Property of IBM 

 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */

package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.ConferenceContextWriter;

import java.io.FileNotFoundException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * Wizard class
 */

public class InviteWizard extends Wizard implements INewWizard {

	protected static final String DEFAULT_FILE_PATH = System
			.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ ".econference" + System.getProperty("file.separator");

	// wizard pages
	protected GenInfoPage genInfoPage;
	protected InvitePage invitePage;
	protected LastPage lastOnePage;
	protected EConferenceContext context;

	// the workbench instance
	protected IWorkbench workbench;

	private String organizer = "";

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

	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	public boolean canFinish() {
		IWizardPage current = this.getContainer().getCurrentPage();
		if (current == genInfoPage)
			return false;
		if (current == invitePage)
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
		String filepath = genInfoPage.getFilePath();
		try {
			ConferenceContextWriter writer = new ConferenceContextWriter(
					filepath, context);
			writer.serialize();
			// if we save the ecx file not in the default location
			// we store a copy there
			if (!filepath.startsWith(DEFAULT_FILE_PATH)) {
				String filename = genInfoPage.getConferenceName();
				filename += ".ecx";
				writer = new ConferenceContextWriter(
						DEFAULT_FILE_PATH + filename, context);
				writer.serialize();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean canSendInvitation() {
		return this.lastOnePage.getCanSendInvitations();
	}

	public EConferenceContext getContext() {
		return this.context;
	}
	
	public void setOrganizer(String organizer) {
		this.organizer  = organizer;		
	}
	
	public String getOrganizer() {
		return organizer;
	}
}
