package it.uniba.di.cdg.collaborativeworkbench.boot.test;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;

import java.util.Collection;

/**
 * A Registry which tracks down every backend connected to the <code>it.uniba.di.cdg.xcore.network.backends</code>
 * extension point.
 */
public interface TestIBackendRegistry {
    /**
     * The extension point that this registry handles.
     */
   // public static final String XP_BACKENDS = NetworkPlugin.ID + ".backends";
    
    public IBackend getDefaultBackend();
    
    /**
     * Returns the descriptors for the currently known backends.
     * 
     * @return the backends' descriptors
     */
    

    /**
     * Returns all the registered backends.
     * 
     * @return the registry.
     */
    Collection<IBackend> getBackends();

    /**
     * Returns the descriptor associated to a specific id.
     * 
     * @param id the backend id
     * @return the descriptor or <code>null</code> if the id is invalid or unknown
     */
    IBackendDescriptor getDescriptor( String id );
    
    IBackendDescriptor getDefaultDescriptor();
    
    /**
     * Returns the backend object identified by the identifier.
     * 
     * @param backendId the identifier of the wanted backend
     * @return the backend object or <code>null</code> if the id is unknown
     */
    IBackend getBackend( String backendId );
    
    /**
     * Process the extension point's extensions. 
     * @throws Exception 
     */
    void processExtensions( IExtensionProcessor processor ) throws Exception;

    /**
     * Dispose all backends (close open connections and perform clean-ups).  
     */
    void dispose();
    
    public String getDefaultBackendId();
}
