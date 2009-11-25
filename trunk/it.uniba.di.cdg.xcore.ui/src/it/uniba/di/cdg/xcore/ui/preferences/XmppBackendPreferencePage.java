package it.uniba.di.cdg.xcore.ui.preferences;

import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into
 * JFace that allows us to create a page that is small and knows how to save, restore and apply
 * itself. <p> This page is used to modify preferences only. They are stored in the preference store
 * that belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */

public class XmppBackendPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {
    
    public static final String SHOW_DEBUGGER = "it.uniba.di.cdg.xcore.ui.preferences_xmppbackend_showdebugger";
  
    private ScopedPreferenceStore preferences;

    public XmppBackendPreferencePage() {
        super( GRID );
        setDescription( "This page includes all the options related to the XMPP network backend" );
        preferences = new ScopedPreferenceStore( new ConfigurationScope(), UiPlugin.ID );
        setPreferenceStore( preferences );
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to
     * manipulate various types of preferences. Each field editor knows how to save and restore
     * itself.
     */
    public void createFieldEditors() {
        addField( new BooleanFieldEditor( SHOW_DEBUGGER,
                "Show XMPP Backend &debugger (will show up at next reconnection)",
                getFieldEditorParent() ) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        try {
            preferences.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init( IWorkbench workbench ) {
        preferences.setDefault( XmppBackendPreferencePage.SHOW_DEBUGGER, false );
    }

}