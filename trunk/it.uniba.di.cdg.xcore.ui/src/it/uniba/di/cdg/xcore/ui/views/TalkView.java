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
package it.uniba.di.cdg.xcore.ui.views;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.aspects.SwtSyncExec;
import it.uniba.di.cdg.xcore.network.events.ITypingEvent;
import it.uniba.di.cdg.xcore.network.events.ITypingListener;
import it.uniba.di.cdg.xcore.network.messages.IMessage;
import it.uniba.di.cdg.xcore.network.model.tv.Entry;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModel;
import it.uniba.di.cdg.xcore.network.model.tv.ITalkModelListener;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.util.TypingStatusUpdater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * A view which includes an input area (for typing the message to send) and a message board text
 * (containing the exchanged messages).
 */
public class TalkView extends TalkViewUI implements ITalkView {
    /**
     * Unique id for the view.
     */
    public static final String ID = UiPlugin.ID + ".views.talkView"; //$NON-NLS-1$

    private static final String SYSTEM_CR = System.getProperty( "line.separator" );;

    private static final int MINIMUM_FONT_HEIGHT = 8;

    private static final int MAXIMUM_FONT_HEIGHT = 16;

    /**
     * Listeners for notifying the 'send message' events.
     */
    private final List<ISendMessagelListener> listeners;

    private ITalkModel model;

    private Map<String, StringBuffer> cachedTalks;

    protected IAction increaseFontSize;

    protected IAction decreaseFontSize;

    /**
     * The model listener will append text messages addded to the model and replace the currently
     * displayed text with the one belonging to the newly selected thread id.
     */
    private ITalkModelListener modelListener = new ITalkModelListener() {
        public void entryAdded( String threadId, Entry entry ) {
            appendMessage( formatEntry( entry ) );
        }

        public void currentThreadChanged( String oldThread, String newThread ) {
            // Add a separator to the old threading (when this event is fired the current
            // thread in the talk model has not been changed yet, so it safe to 
            // call appendMessage() here and have it attached the message to "oldThread"
            putSeparator( oldThread );
            showThread( newThread );
        }
    };

    /**
     * Listeners for typing notifications.
     */
    private final List<ITypingListener> typingListeners;

    /**
     * Updates the chat status line about typing status.
     */
    private TypingStatusUpdater typingStatusUpdater;

    private Image typingImage;

    protected boolean shiftMaskEnabled;

    protected int timeout;

    /**
     * Default constructor.
     */
    public TalkView() {
        this.listeners = new ArrayList<ISendMessagelListener>();
        this.typingListeners = new ArrayList<ITypingListener>();
        // Setup the status updater to receive typing notifications ...
        typingStatusUpdater = new TypingStatusUpdater( this );
        typingStatusUpdater.start();
        shiftMaskEnabled = false;
        timeout = 0;

        cachedTalks = new HashMap<String, StringBuffer>();
    }

    @SwtAsyncExec
    protected void showThread( String threadId ) {
        StringBuffer sb = getOrCreateCachedText( threadId );
        messageBoardText.setText( sb.toString() );
        scrollToEnd();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        super.createPartControl( parent );

        this.typingImage = UiPlugin.getDefault().getImage( IImageResources.ICON_TYPING );

        userInputText.addKeyListener( new KeyAdapter() {
            public void keyPressed( KeyEvent e ) {
                if (e.character == SWT.CR && !isReadOnly()) {
                    // raises send button action
                    sendButton.notifyListeners( SWT.Selection, new Event() );
                    // FIXME doesnt work: still have to strip it
                    // ignore the CR and don't add to text control
                    e.doit = false;
                    // forces focus gain of input text
                    userInputText.forceFocus();
                }
            }
        } );

        // When the user press return or click 'send', notify listeners
        sendButton.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected( SelectionEvent e ) {
                if (isShiftMaskEnabled()) {
                    userInputText.append( SYSTEM_CR );
                    userInputText.setCaretOffset( userInputText.getText().length() );
                } else {
                    notifyListeners();
                    userInputText.setText( "" ); // Clear text area                    
                }
                userInputText.forceFocus(); //  forces focus gain of input text
            }
        } );

        userInputText.addKeyListener( new KeyAdapter() {

            @Override
            public void keyPressed( KeyEvent e ) {
                setShiftPressedEnabled( e );
                // Notify typing listeners
                if (timeout == 0) {
                    for (ITypingListener l : typingListeners)
                        l.typing();
                }
                timeout++;
                if (timeout > 10)
                    timeout = 0;
            }
        } );

        //makeActions();
        increaseFontSize = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.IAction#run()
             */
            @Override
            public void run() {
                Font oldFont = messageBoardText.getFont();
                FontData[] fd = oldFont.getFontData();
                for (int i = 0; i < fd.length; i++) {
                    int height = fd[i].getHeight();
                    if (height == MAXIMUM_FONT_HEIGHT) {
                        return;
                    } else {
                        fd[i].setHeight( height + 1 );
                        messageBoardText.setFont( new Font( getViewSite().getShell().getDisplay(),
                                fd ) );
                        // XXX how to dispose new fonts?
                        // add pref option
                        //                      oldFont.dispose();
                    }
                }
            }
        };
        increaseFontSize.setText( "Increase font size" );
        increaseFontSize.setToolTipText( "Increase font size of the chat window" );
        increaseFontSize.setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_ACTION_INCREASE_FONT_SIZE ) );

        decreaseFontSize = new Action() {
            /* (non-Javadoc)
             * @see org.eclipse.jface.action.IAction#run()
             */
            @Override
            public void run() {
                Font oldFont = messageBoardText.getFont();
                FontData[] fd = oldFont.getFontData();
                for (int i = 0; i < fd.length; i++) {
                    int height = fd[i].getHeight();
                    if (height == MINIMUM_FONT_HEIGHT) {
                        return;
                    } else {
                        fd[i].setHeight( height - 1 );
                        messageBoardText.setFont( new Font( getViewSite().getShell().getDisplay(),
                                fd ) );
                        // XXX how to dispose new fonts?
                        // add pref option
                        //oldFont.dispose();
                    }
                }
            }
        };
        decreaseFontSize.setText( "Decrease font size" );
        decreaseFontSize.setToolTipText( "Decrease font size of the chat window" );
        decreaseFontSize.setImageDescriptor( UiPlugin.getDefault().getImageDescriptor(
                IImageResources.ICON_ACTION_DECREASE_FONT_SIZE ) );
        //contributeToActionBars
        getViewSite().getActionBars().getToolBarManager().add( increaseFontSize );
        getViewSite().getActionBars().getToolBarManager().add( decreaseFontSize );

        getViewSite().getActionBars().getMenuManager().add( increaseFontSize );
        getViewSite().getActionBars().getMenuManager().add( decreaseFontSize );
    }

    private String formatEntry( Entry entry ) {
        if (entry.isSystemEntry())
            return entry.getText();
        return String.format( "%s > %s", entry.getWho(), entry.getText() );
    }

    private void setShiftPressedEnabled( KeyEvent e ) {
        if (e.keyCode == SWT.SHIFT) {
            shiftMaskEnabled = true;
        } else {
            shiftMaskEnabled = false;
        }
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IInputPanel#setReadOnly(boolean)
     */
    @SwtAsyncExec
    public void setReadOnly( final boolean readOnly ) {
        if (sendButton.isDisposed())
            return;
        sendButton.setEnabled( !readOnly );
        //        messageBoardText.setEnabled( !readOnly );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#isReadOnly()
     */
    @SwtSyncExec
    public boolean isReadOnly() {
        return !sendButton.isEnabled();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IMessageBoard#appendMessage(it.uniba.di.cdg.xcore.ui.views.IMessageBoard.LookType,java.lang.String)
     */
    public void appendMessage( final IMessage message ) {
        // Avoid empty text ...
        if (message == null || message.getText() == null || message.getText().length() == 0)
            return;

        model.addEntry( makeEntry( message ) );
    }

    /**
     * Format the message, adding the appropriate fields like time date and who has said the message.
     * @param message
     * @return
     */
    private Entry makeEntry( IMessage message ) {
        final Date now = Calendar.getInstance().getTime();

        Entry entry = new Entry();
        entry.setTimestamp( now );
        entry.setText( message.getText() );

        if (IMessage.SYSTEM.equals( message.getType() )) {
            entry.setWho( "***" );
        } else if (IMessage.CHAT.equals( message.getType() )
                || IMessage.GROUP_CHAT.equals( message.getType() )) {
            if (message.getFrom() == null)
                entry.setWho( "Me" );
            else
                // Other buddy than local user
                entry.setWho(this.getTitle()); //message.getFrom() );
        }
        return entry;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#appendMessage(java.lang.String)
     */
    @SwtAsyncExec
    public void appendMessage( String text ) {
        String textToAppend = text + "\n";
        messageBoardText.append( textToAppend );
        cacheMessage( getModel().getCurrentThread(), textToAppend );
        scrollToEnd();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#getChatLogText()
     */
    @SwtSyncExec
    public String getChatLogText() {
        return messageBoardText.getText();
    }

    /**
     * Notifies all listeners about the incoming message.
     */
    private void notifyListeners() {
        String message = getMessage();

        // Workaround
        // strips trailing carriage return and line feed
        if (message.endsWith( SYSTEM_CR )) {
            message = message.substring( 0, message.length() - SYSTEM_CR.length() );
        }

        if (message.length() > 0) {
            for (ISendMessagelListener l : listeners) {
                l.notifySendMessage( message );
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        typingStatusUpdater.quit();
        typingStatusUpdater = null;

        super.dispose();
    }

    /**
     * Returns the text that the user wants to send. TODO Is the sync exec needed here?
     * 
     * @return the text to send
     */
    @SwtSyncExec
    private String getMessage() {
        return userInputText.getText();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#updateStatus(java.lang.String)
     */
    @SwtAsyncExec
    public void updateStatus( final String status ) {
        if (statusLabel.isDisposed())
            return;

        if (status == null || status.length() == 0)
            statusLabel.setImage( null );
        else
            statusLabel.setImage( typingImage );

        statusLabel.setText( status );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IMessageBoard#setTitleText(java.lang.String)
     */
    @SwtAsyncExec
    public void setTitleText( final String title ) {
        setPartName( title );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IInputPanel#addListener(it.uniba.di.cdg.xcore.ui.views.IInputPanel.IListener)
     */
    public void addListener( ISendMessagelListener listener ) {
        this.listeners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.IInputPanel#removeListener(it.uniba.di.cdg.xcore.ui.views.IInputPanel.IListener)
     */
    public void removeListener( ISendMessagelListener listener ) {
        this.listeners.remove( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#addTypingListener(it.uniba.di.cdg.xcore.ui.views.ITalkView.ITypingListener)
     */
    public void addTypingListener( ITypingListener listener ) {
        this.typingListeners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#removeTypingListener(it.uniba.di.cdg.xcore.ui.views.ITalkView.ITypingListener)
     */
    public void removeTypingListener( ITypingListener listener ) {
        this.typingListeners.remove( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.events.ITypingEventListener#onTyping(it.uniba.di.cdg.xcore.network.events.ITypingEvent)
     */
    public void onTyping( ITypingEvent event ) {
        typingStatusUpdater.queueUser( event.getWho() );
        updateStatus( typingStatusUpdater.composeStatusText() );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave( IProgressMonitor monitor ) {
        System.out.println( "doSave()" );

        String fileName = UiPlugin.getUIHelper().requestFileNameForSaving( "*.*" );
        if (fileName == null)
            return;

        List<Entry> allEntries = model.getAllEntries();

        monitor.beginTask( "Saving chat log ...", allEntries.size() / 10 );

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter( new FileOutputStream( fileName ) );

            int i = 0, worked = 10;
            for (Entry e : allEntries) {
                final String line = e.toString();
                osw.write( String.format( "% 5d %s\n", i++, line ) );
                if (--worked == 0) {
                    monitor.worked( 1 );
                    worked = 10;
                }
            }
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
        return false;
    }

    /**
     * @return Returns the shiftMaskEnabled.
     */
    public boolean isShiftMaskEnabled() {
        return shiftMaskEnabled;
    }

    public void setModel( ITalkModel model ) {
        // Switch model ;)
        if (this.model != null)
            this.model.removeListener( modelListener );
        this.model = model;
        model.addListener( modelListener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#putSeparator(java.lang.String)
     */
    public void putSeparator( String threadId ) {
        getModel().addEntry( threadId, new Entry( "---------------------" ) );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#getModel()
     */
    public ITalkModel getModel() {
        return model;
    }

    private void cacheMessage( String threadId, String text ) {
        StringBuffer sb = getOrCreateCachedText( threadId );
        sb.append( text );
    }

    private StringBuffer getOrCreateCachedText( String threadId ) {
        StringBuffer sb = cachedTalks.get( threadId );
        if (sb == null) {
            sb = new StringBuffer();
            cachedTalks.put( threadId, sb );
        }
        return sb;
    }

	@Override
	public void refresh() {
		// do nothing
	}
}
