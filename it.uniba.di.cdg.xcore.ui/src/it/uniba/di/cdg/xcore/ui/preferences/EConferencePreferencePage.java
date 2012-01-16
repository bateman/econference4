package it.uniba.di.cdg.xcore.ui.preferences;

import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ComboFieldEditor;
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

public class EConferencePreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

    public static final String AUTO_SAVE_LOGS = "it.uniba.di.cdg.xcore.ui.preferences_econference_autosavelogs";

    public static final String AUTO_SAVE_LOGS_DIR = "it.uniba.di.cdg.xcore.ui.preferences_econference_autosavelogsdir"; 

    public static final String SHOW_TIMESTAMP = "it.uniba.di.cdg.xcore.ui.preferences_econference_showtimestamp";

    public static final String URL_SHORTENER = "it.uniba.di.cdg.xcore.ui.preferences_econference_urlshortener";

    private ScopedPreferenceStore preferences;

    public EConferencePreferencePage() {
        super( GRID );
        setDescription( "This page includes all the options related to the eConference extension" );
        preferences = new ScopedPreferenceStore( new ConfigurationScope(), UiPlugin.ID );
        setPreferenceStore( preferences );
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to
     * manipulate various types of preferences. Each field editor knows how to save and restore
     * itself.
     */
    public void createFieldEditors() {
        // TODO listen to changes
        BooleanFieldEditor booleanFieldEditorAutoSaveLogs = new BooleanFieldEditor( AUTO_SAVE_LOGS,
                "Automatically save &logs at the end of the event", getFieldEditorParent() );
        addField( booleanFieldEditorAutoSaveLogs );
        DirectoryFieldEditor directoryFieldEditorStoreLogs = new DirectoryFieldEditor( AUTO_SAVE_LOGS_DIR, "Store logs in:", getFieldEditorParent() );
        addField( directoryFieldEditorStoreLogs );
        addField( new BooleanFieldEditor( SHOW_TIMESTAMP,
                "Show &timestamp beside statements in the talk view",
                getFieldEditorParent() ) );
        addField( new ComboFieldEditor(URL_SHORTENER, "Select which URL shortening service to use:",
                    new String[][] {
                        { "None", "none" }, 
                        { "TinyURL", "tinyurl" },
                    }, getFieldEditorParent()));
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
        preferences.setDefault(EConferencePreferencePage.AUTO_SAVE_LOGS, false );
        preferences.setDefault(EConferencePreferencePage.AUTO_SAVE_LOGS_DIR, System.getProperty("user.dir"));
        preferences.setDefault(EConferencePreferencePage.URL_SHORTENER, "none");
    }

}