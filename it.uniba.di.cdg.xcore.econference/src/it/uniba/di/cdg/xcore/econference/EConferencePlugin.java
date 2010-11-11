package it.uniba.di.cdg.xcore.econference;

import it.uniba.di.cdg.xcore.econference.internal.EConferenceHelper;
import it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel;
import it.uniba.di.cdg.xcore.econference.model.storedevents.StoredEventsModel;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The Econference plug-in provides support for creating moderated conferences.
 */
public class EConferencePlugin extends AbstractUIPlugin {
    /**
     * The unique id for this plug-in.
     */
    public final static String ID = "it.uniba.di.cdg.xcore.econference";

    // The shared instance.
    private static EConferencePlugin plugin;

    private IEConferenceHelper helper;
    
    private IStoredEventsModel storedEventsModel;

    /**
     * The constructor.
     */
    public EConferencePlugin() {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start( BundleContext context ) throws Exception {
        super.start( context );

        helper = new EConferenceHelper( UiPlugin.getUIHelper(), NetworkPlugin.getDefault().getHelper() );
        helper.init();
        
        storedEventsModel = new StoredEventsModel();
        storedEventsModel.init();
        NetworkPlugin.getDefault().getHelper().registerBackendListener( storedEventsModel );
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop( BundleContext context ) throws Exception {
        NetworkPlugin.getDefault().getHelper().unregisterBackendListener( storedEventsModel );
        storedEventsModel.dispose();
        
        helper.dispose();
        helper = null;
        
        super.stop( context );
        plugin = null;
    }

    /**
     * Returns the shared instance.
     */
    public static EConferencePlugin getDefault() {
        if ( plugin == null ) {
            plugin = new EConferencePlugin();
        }
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path
     *        the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor( String path ) {
        return AbstractUIPlugin.imageDescriptorFromPlugin( ID, path );
    }

    /**
     * Returns the helper for this plug-in.
     * 
     * @return
     */
    public IEConferenceHelper getHelper() {
        return helper;
    }
    
    /**
     * Set the helper for this plug-in.
     * @param helper 
     * 
     */
    public void setHelper(IEConferenceHelper helper) {     
    	
    	if(this.helper!=null)
    		this.helper.dispose();        
    	this.helper = helper;
        this.helper.init();
    }
    
    /**
     * 
     * @return
     */
    public IStoredEventsModel getStoredEventsModel() {
        return storedEventsModel;
    }
}
