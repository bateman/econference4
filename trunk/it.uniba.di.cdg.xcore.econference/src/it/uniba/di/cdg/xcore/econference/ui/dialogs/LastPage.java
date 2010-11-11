package it.uniba.di.cdg.xcore.econference.ui.dialogs;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.IEConferenceContext;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page shown when the user has chosen plane as means of transport
 */

public class LastPage extends WizardPage implements Listener {

    boolean isModerator = false;

    Composite composite;

    GridData gridData;

    private Button sendInvitationsCheckBox = null;

    protected IEConferenceContext context = null;

    private Table table = null;

    private Button addRow;

    private Button removeRow;

    private boolean canSendInvitations = true;

    final static String MODERATOR = "Moderator";

    final static String PARTECIPANT = "Participant";

    final static String SCRIBE = "Scribe";

    private final static String[] roles = { PARTECIPANT, MODERATOR, SCRIBE };

    private final static String[] headers = { "Role *", "Id *", "Fullname", "Email", "Organization" };

    private List<Invitee> participants = null;

    private Invitee scribe;

    private Invitee moderator;
    
    public LastPage( String arg0 , IEConferenceContext context) {
        super( arg0 );
        setTitle( "Invite people" );
        setDescription( "Step 3: Complete the information of participants\n Click \"Add Row\" to add participants to the conference" );
        this.setContext( context );
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl( Composite parent ) {

        // create the composite to hold the widgets
        composite = new Composite( parent, SWT.NONE );
        GridLayout gridLayout = new GridLayout( 5, false );
        composite.setLayout( gridLayout );
        table = new Table( composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION
                | SWT.SINGLE | SWT.H_SCROLL );
        gridData = new GridData( 575, 200 );
        gridData.horizontalSpan = 5;
        table.setLayoutData( gridData );
        table.setHeaderVisible( true );
        table.setLinesVisible( true );
        final TableEditor editor = new TableEditor( table );
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        table.addListener( SWT.MouseDoubleClick, new Listener() {
            public void handleEvent( Event evento ) {
                modifySelectedItem( evento );
            }
        } );
        for (String i : headers)
            new TableColumn( table, SWT.NONE ).setText( i );

        setCurrentUserInformation();

        GridData gridData = new GridData();
        gridData.horizontalSpan = 5;
        gridData.grabExcessHorizontalSpace = true;
        gridData.widthHint = 85;
        gridData.heightHint = 25;
        addRow = new Button( composite, SWT.PUSH );
        addRow.setLayoutData( gridData );
        addRow.setText( "Add Row" );
        addRow.addListener( SWT.Selection, new Listener() {
            public void handleEvent( Event event ) {
                insertRow( "", "" );
            }
        } );
        removeRow = new Button( composite, SWT.PUSH );
        removeRow.setLayoutData( gridData );
        removeRow.setText( "Remove Row" );
        removeRow.addListener( SWT.Selection, new Listener() {
            public void handleEvent( Event e ) {
                removeRow();
            }
        } );

        gridData = new GridData();
        gridData.widthHint = 85;
        gridData.heightHint = 25;
        sendInvitationsCheckBox = new Button( composite, SWT.CHECK );
        sendInvitationsCheckBox.setText( "Send message and email invitations" );
        sendInvitationsCheckBox.setSelection( true );
        sendInvitationsCheckBox.setToolTipText( "send message and email to notify the invitation" );
        sendInvitationsCheckBox.addSelectionListener( new SelectionListener() {
            @Override
            public void widgetDefaultSelected( SelectionEvent e ) {
            }

            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (sendInvitationsCheckBox.getSelection())
                    canSendInvitations = true;
                else
                    canSendInvitations = false;
            }

        } );

        setControl( composite );
        setPageComplete( true );
    }

    private void setCurrentUserInformation() {
        TableColumn[] columns = table.getColumns();
        final TableItem item = new TableItem( table, SWT.NONE );
        String user = NetworkPlugin.getDefault().getRegistry().getDefaultBackend().getUserId(), name = NetworkPlugin
                .getDefault().getRegistry().getDefaultBackend().getUserAccount().getName(), email = NetworkPlugin
                .getDefault().getRegistry().getDefaultBackend().getUserAccount().getEmail();

        item.setText( 0, MODERATOR );

        item.setText( 1, user );

        item.setText( 2, name );

        if (email.isEmpty()) {
            int index = user.indexOf( "/" );
            if (index == -1) {
                index = user.length();
            }
            email = user.substring( 0, index );
        }
        item.setText( 3, email );

        item.setText( 4, "" );

        columns[0].setWidth( 90 );
        for (int i = 1; i < columns.length; i++) {
            columns[i].pack();
        }
    }

    public void init( String[] participants ) {

        if (participants != null) {
            TableColumn[] columns = table.getColumns();
            for (int i = 0; i < participants.length; i++) {
                String[] partecipant = participants[i].split( " - " );
                String idPartecipant = (partecipant.length > 1 ? partecipant[1] : partecipant[0]);
                String namePartecipant = (partecipant.length > 1 ? partecipant[0] : "");
                if (notExist( idPartecipant ))
                    insertRow( idPartecipant, namePartecipant );
            }
            columns[0].setWidth( 90 );
            for (int s = 1; s < columns.length; s++) {
                columns[s].pack();
            }
        }
    }

    private boolean notExist( String id ) {
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++)
            if (items[i].getText( 1 ).equals( id ))
                return false;
        return true;
    }

    private void insertRow( String idP, String name ) {
        final TableItem newRow = new TableItem( table, SWT.NONE );
        newRow.setText( 0, PARTECIPANT );
        newRow.setText( 1, idP );
        newRow.setText( 2, name );
        int index = idP.indexOf( "/" );
        if (index == -1) {
            index = idP.length();
        }
        newRow.setText( 3, idP.substring( 0, index ) );
    }

    public void setContext( IEConferenceContext ctx ) {
        this.context = ctx;
    }

    public boolean canFlipToNextPage() {
        return false;
    }

    /*
     * Process the events: when the user has entered all information the wizard can be finished
     */
    public void handleEvent( Event e ) {
    }

    /*
     * Sets the completed field on the wizard class when all the information is entered and the
     * wizard can be completed
     */
    public boolean isPageComplete() {
        return true;
    }

    public void saveData() {
        participants = new ArrayList<Invitee>();
        TableItem[] items = table.getItems();
        Invitee p = null;
        for (int i = 0; i < items.length; i++) {
            String id = items[i].getText( 1 );
            if (NetworkPlugin.getDefault().getRegistry().getDefaultBackendId()
                    .equals( "it.uniba.di.cdg.jabber.jabberBackend" ))
                if (id.indexOf( "/" ) != -1)
                    id = id.substring( 0, id.indexOf( "/" ) );

            p = new Invitee( id, items[i].getText( 2 ), items[i].getText( 3 ),
                    items[i].getText( 4 ), items[i].getText( 0 ).toLowerCase() );
            if (items[i].getText( 0 ).equals( "Moderator" )) {
                this.getContext().setModerator( p );
                this.setModerator( p );
            }
            if (items[i].getText( 0 ).equals( "Scribe" )) {
                this.getContext().setScribe( p );
                this.setScribe( p );
            }
            participants.add( p );
        }
        ((EConferenceContext) context).setInvitees( participants );
    }
    
    /**
     * @param p
     */
    public void setScribe( Invitee p ) {
        scribe = p;
    }

    public void setModerator( Invitee p ) {
        moderator = p;
    }

    public Invitee getScribe( ) {
        return scribe;
    }

    public Invitee getModerator( ) {
        return moderator;
    }

    public List<Invitee> getParticipants () {
        return this.participants;
    }

    public boolean getCanSendInvitations() {
        return this.canSendInvitations;
    }

    public void setCanSendInvitations( boolean canSendInvitations ) {
        this.canSendInvitations = canSendInvitations;
    }

    public IEConferenceContext getContext() {
        return this.context;
    }

    private void modifySelectedItem( Event event ) {
        TableEditor editor = new TableEditor( table );
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        Rectangle clientArea = table.getClientArea();
        Point pt = new Point( event.x, event.y );
        int index = table.getTopIndex();
        while (index < table.getItemCount()) {
            boolean visible = false;
            final TableItem item = table.getItem( index );
            for (int i = 0; i < table.getColumnCount(); i++) {
                Rectangle rect = item.getBounds( i );
                if (rect.contains( pt )) {
                    final int column = i;
                    final Control text;

                    if (column == 0) {
                        text = new CCombo( table, SWT.NONE );
                        for (String j : roles)
                            ((CCombo) text).add( j );
                        ((CCombo) text).setText( item.getText( 0 ) );
                        item.setText( 0, ((CCombo) text).getText() );
                    } else {
                        text = new Text( table, SWT.NONE );
                    }

                    Listener textListener = new Listener() {
                        public void handleEvent( Event e ) {

                            if (text instanceof CCombo) {
                                if (((CCombo) text).getText().equals( MODERATOR )) {
                                    downgradeModerator();
                                }
                            }
                            switch (e.type) {
                            case SWT.FocusOut:
                                if (text instanceof CCombo)
                                    item.setText( column, ((CCombo) text).getText() );
                                else
                                    item.setText( column, ((Text) text).getText() );
                                text.dispose();
                                break;
                            case SWT.Traverse:
                                switch (e.detail) {
                                case SWT.TRAVERSE_RETURN:
                                    if (text instanceof CCombo)
                                        item.setText( column, ((CCombo) text).getText() );
                                    else
                                        item.setText( column, ((Text) text).getText() );
                                    // FALL THROUGH
                                case SWT.TRAVERSE_ESCAPE:
                                    text.dispose();
                                    e.doit = false;
                                }
                                break;
                            }
                            boolean moderator = isModerator();
                            sendInvitationsCheckBox.setEnabled( moderator );
                            sendInvitationsCheckBox.setSelection( moderator );

                        }
                    };
                    text.addListener( SWT.FocusOut, textListener );
                    text.addListener( SWT.Traverse, textListener );
                    editor.setEditor( text, item, i );
                    if (text instanceof CCombo)
                        ((CCombo) text).setText( item.getText( i ) );
                    else {
                        ((Text) text).setText( item.getText( i ) );
                        ((Text) text).selectAll();
                    }

                    text.setFocus();
                    return;
                }
                if (!visible && rect.intersects( clientArea )) {
                    visible = true;
                }
            }
            if (!visible)
                return;
            index++;
        }
        TableColumn[] columns = table.getColumns();
        columns[0].setWidth( 90 );
        for (int i = 1; i < columns.length; i++) {
            columns[i].pack();
        }
    }

    private void downgradeModerator() {

        for (TableItem item : table.getItems()) {
            if (item.getText( 0 ).equals( MODERATOR )) {
                item.setText( 0, PARTECIPANT );
                break;
            }
        }
    }

    private void removeRow() {
        int index = table.getSelectionIndex();
        if (index != -1) {
            TableItem item = table.getSelection()[0];
            item.dispose();
        } else {
            System.out.println( "Index " + index + " out of bound!!" );
        }

    }

    public Vector<String> recipients() {

        Vector<String> to = new Vector<String>();

        for (TableItem item : table.getItems()) {
            if (!item.getText( 0 ).equals( MODERATOR )) {
                to.add( item.getText( 3 ) );
            }
        }
        return to;
    }

    private boolean isModerator() {

        boolean isModerator = false;

        for (TableItem item : table.getItems()) {
            if (item.getText( 1 ).equals(
                    NetworkPlugin.getDefault().getRegistry().getDefaultBackend().getUserId() )) {
                if (item.getText( 0 ).equals( MODERATOR )) {
                    isModerator = true;
                }
                break;
            }
        }

        return isModerator;

    }

}