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
package it.uniba.di.cdg.xcore.econference.ui.views;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.aspects.SwtSyncExec;
import it.uniba.di.cdg.xcore.econference.EConferencePlugin;
import it.uniba.di.cdg.xcore.econference.IEConferenceHelper;
import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.m2m.MultiChatPlugin;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.ParticipantSpecialPrivileges;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;

/**
 * Implementation of the whiteboard. 
 */
public class WhiteBoardView extends WhiteBoardViewUI implements IWhiteBoard {
    /**
     * The unique ID of this view.
     */
    public static final String ID = EConferencePlugin.ID + ".ui.views.whiteBoardView";

    private static final String DEFAULT_LOG_FILENAME = "decisions.txt";

    private IEConferenceManager manager;

    /**
     * Action for notifying remote clients about the new content of the white board.
     */
    protected IAction refreshRemoteAction;

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.WhiteBoardViewUI#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        super.createPartControl( parent );

        makeActions();

        contributeToActionBars( getViewSite().getActionBars() );
    }

    /**
     * Create the actions to attach to this view action bar.
     */
    protected void makeActions() {
        refreshRemoteAction = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
                //                IParticipant thisUser = getManager().getService().getModel().getLocalUser();
                //                if (!thisUser.isScribe()) 
                //                    return;
                // Note that we do not change the model directly but notify the server directly:
                // this will be so gentle to notify us the new whiteboard content.
                // Obviously, I don't know if it is an XMPP feature only or if other server will
                // behave the same.
                getManager().notifyWhiteBoardChanged( WhiteBoardView.this.getText() );
            }
        };
        refreshRemoteAction.setText( "Refresh remote clients" );
        refreshRemoteAction.setToolTipText( "Refresh white board contents for remote peers" );
        refreshRemoteAction.setImageDescriptor( MultiChatPlugin.imageDescriptorFromPlugin(
                EConferencePlugin.ID, "icons/action_refresh_white_board.png" ) );
    }

    /**
     * Add actions to the action and menu bars (local to this view).
     * @param bars
     */
    protected void contributeToActionBars( IActionBars bars ) {
        bars.getToolBarManager().add( refreshRemoteAction );
        //        bars.getMenuManager().add( refreshRemoteAction );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard#getText()
     */
    @SwtSyncExec
    public String getText() {
        return whiteBoardText.getText();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard#setManager(it.uniba.di.cdg.xcore.econference.IEConferenceManager)
     */
    public void setManager( IEConferenceManager manager ) {
        this.manager = manager;

        final IConferenceModel model = manager.getService().getModel();

        model.addListener( new ConferenceModelListenerAdapter() {
            /* (non-Javadoc)
             * @see it.uniba.di.cdg.xcore.econference.model.ConferenceModelListenerAdapter#whiteBoardChanged()
             */
            @Override
            public void whiteBoardChanged() {
                setText( model.getWhiteBoardText() );
                // XXX people have complained about the fact
                // that the whiteboard (and the msg board too)
                // dont remember the last position, and scroll
                // back up on refresh.
                // as a quick fix, we force to scroll to the
                // bottom after any refreh
                scrollToEnd();
            }

            /* (non-Javadoc)
             * @see it.uniba.di.cdg.xcore.m2m.model.ChatRoomModelAdapter#changed(it.uniba.di.cdg.xcore.m2m.model.IParticipant)
             */
            @Override
            public void changed( IParticipant p ) {
                if (p == model.getLocalUser()) {
                    boolean readOnly = !p.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE);
                    setReadOnly( readOnly );
                }
            }
        } );
    }

    /**
     * Scroll to the bottom of the board
     */
    // TODO Bug 53: https://cde.di.uniba.it/tracker/index.php?func=detail&aid=53&group_id=9&atid=138
    @SwtSyncExec
    protected void scrollToEnd() {
        //ScrollBar scrollBar = whiteBoardText.getVerticalBar();
        int n = whiteBoardText.getCharCount();
        whiteBoardText.setSelection( n, n );
        whiteBoardText.showSelection();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard#getManager()
     */
    public IEConferenceManager getManager() {
        return manager;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard#setText(java.lang.String)
     */
    @SwtAsyncExec
    public void setText( String text ) {
        whiteBoardText.setText( text );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard#setReadOnly(boolean)
     */
    @SwtAsyncExec
    public void setReadOnly( boolean readOnly ) {
        whiteBoardText.setEditable( !readOnly );
        refreshRemoteAction.setEnabled( !readOnly );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.ui.views.IWhiteBoard#isReadOnly()
     */
    @SwtSyncExec
    public boolean isReadOnly() {
        return !whiteBoardText.getEditable();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave( IProgressMonitor monitor ) {

        String fileName = UiPlugin.getUIHelper().requestFileNameForSaving( "*.*" );
        if (fileName == null)
            return;

        writeLog( monitor, fileName );
    }

    /**
     * @param monitor
     * @param fileName
     */
    private void writeLog( IProgressMonitor monitor, String fileName ) {
        String textToSave = getText();

        monitor.beginTask( "Saving whiteboard log ...", 1 );

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter( new FileOutputStream( fileName ) );

            osw.write( textToSave );
            monitor.worked( 1 );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null)
                    osw.close();
            } catch (IOException e) { /* I hate checked exceptions */
            }
            monitor.done();
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#doSaveAs()
     */
    public void doSaveAs() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#isDirty()
     */
    public boolean isDirty() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
     */
    public boolean isSaveOnCloseNeeded() {
        // if the preference chosen is
        // automatically save logs in this folder is selected
        // the here we must return false; and save the logs of course
        // in the given dir, with the DEFAULT_LOG_FILENAME
        try {
            IEConferenceHelper helper = EConferencePlugin.getDefault().getHelper();
            if (helper.getBooleanPreference( IEConferenceHelper.AUTO_SAVE_LOGS ) == true) {
                System.out.println( "autosave decisions log" );
                String defaultDir = helper
                        .getStringPreference( IEConferenceHelper.AUTO_SAVE_LOGS_DIR )
                        + File.separator
                        // FIXME calling getName on context returns NULL!!!!!
                        + manager.getService().getContext().getRoom() + File.separator;
                File targetDir = new File( defaultDir );
                if (!targetDir.exists()) {
                    if (!targetDir.mkdirs()) {
                        throw new IOException( "Unable to create target dir: "
                                + targetDir.getAbsolutePath() );
                    }
                }
                System.out.println( "saving " + targetDir );
                File targetFile = new File( defaultDir, DEFAULT_LOG_FILENAME );
                if (!targetFile.exists()) {
                    if (!targetFile.createNewFile()) {
                        throw new IOException( "Unable to create target file: "
                                + targetFile.getAbsoluteFile() );
                    }
                }
                writeLog( new NullProgressMonitor(), defaultDir + DEFAULT_LOG_FILENAME );
                return false;
            } else {
                System.out.println( "must save decisions log manually" );
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println( "must save decisions log manually" );
            return true;
        }
    }
}
