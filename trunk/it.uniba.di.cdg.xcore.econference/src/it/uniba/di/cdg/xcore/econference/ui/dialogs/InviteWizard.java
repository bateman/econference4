/*
 * Licensed Material - Property of IBM 

 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */

package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.IEConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.ConferenceContextWriter;
import it.uniba.di.cdg.xcore.econference.util.MailFactory;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import java.io.FileNotFoundException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.google.gdata.GoogleDocManager;

/**
 * Wizard class
 */

public class InviteWizard extends Wizard implements INewWizard {

    protected static final String DEFAULT_FILE_PATH = System.getProperty( "user.home" )
            + System.getProperty( "file.separator" ) + ".econference"
            + System.getProperty( "file.separator" );

    // wizard pages
    protected GenInfoPage genInfoPage;
    protected InvitePage invitePage;
    protected LastPage lastOnePage;
    protected IEConferenceContext context;

    // the workbench instance
    protected IWorkbench workbench;

    private String organizer = "";

    public InviteWizard() {
        super();
        
    }

    public void addPages() {
        context = new EConferenceContext();
        genInfoPage = new GenInfoPage( "", context);
        invitePage = new InvitePage( "" , context);
        lastOnePage = new LastPage( "", context );
        addPage( genInfoPage );
        addPage( invitePage );
        addPage( lastOnePage );
     }

    /**
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init( IWorkbench workbench, IStructuredSelection selection ) {
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

    @SuppressWarnings("unused")
	public boolean performFinish() {
        lastOnePage.saveData();
        this.context = ((EConferenceContext) lastOnePage.getContext());
        String filepath = genInfoPage.getFilePath();
        try {

            ConferenceContextWriter writer = new ConferenceContextWriter(filepath, (EConferenceContext) context);

            writer.serialize();
            // if we save the ecx file not in the default location
            // we store a copy there
            if (!filepath.startsWith( DEFAULT_FILE_PATH )) {
                String filename = genInfoPage.getConferenceName();
                filename += ".ecx";
                writer = new ConferenceContextWriter( DEFAULT_FILE_PATH + filename, (EConferenceContext) context );
                writer.serialize();
            }
            
            String backendID = NetworkPlugin.getDefault().getRegistry().getDefaultBackendId();
            
            //the following code work only with Jabber/Xmpp protocol
            if (canSendInvitation() && backendID.equals( "it.uniba.di.cdg.jabber.jabberBackend" ) ) {

                String user = NetworkPlugin.getDefault().getRegistry().getDefaultBackend()
                        .getUserId();
                String passwd = NetworkPlugin.getDefault().getRegistry().getDefaultBackend()
                        .getUserAccount().getPassword();
                String title = genInfoPage.getConferenceName() + ".ecx";
                String subject = context.getRoom().split( "@" )[0];
                Vector<String> toRecipients = lastOnePage.recipients();
                String mailBody;

                if (!toRecipients.isEmpty()) {
                    GoogleDocManager manager = new GoogleDocManager( user, passwd );
                    String googleDocLink = manager.uploadFile( filepath, title, toRecipients );
                    mailBody = MailFactory.createMailBody( context, googleDocLink );

                    manager.sendMail( subject, toRecipients, mailBody, filepath );
                }
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
		return (EConferenceContext) this.context;
	}
	
	public void setOrganizer(String organizer) {
		this.organizer  = organizer;		
	}
	
	public String getOrganizer() {
		return organizer;
	}
}
