package it.uniba.di.cdg.jabber;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.services.Capability;
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;
import it.uniba.di.cdg.xcore.util.IExtensionVisitor;
import it.uniba.di.cdg.xcore.util.VirtualProxyFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

public class ServiceRegistry {

	 /**
     * Descriptors mapped by backend id.
     */
    private Map<String, IServiceDescriptor> descriptors;

    /**
     * The cached backends, mapped by their descriptors. They are created in lazy way.
     */
    private Map<IServiceDescriptor, INetworkService> services;

    
    /**
     * Creates an empty registry.
     * @param context 
     * @param backend 
     */
    public ServiceRegistry(  ) {
        this.descriptors = new HashMap<String, IServiceDescriptor>();
        this.services = new HashMap<IServiceDescriptor, INetworkService>();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#processExtensions()
     */
    /*public void processExtensions( IExtensionProcessor processor ) throws Exception {
    	IExtensionVisitor visitor = new IExtensionVisitor() {
    		public void visit( IExtension extension, IConfigurationElement member ) throws Exception {
    			ServiceDescriptor descriptor = ServiceDescriptor.createFromElement( member );

    			descriptors.put( descriptor.getName(), descriptor );
    			//We have to add the capability to the backend
    			NetworkPlugin.getDefault().getHelper().getRoster().getBackend().getCapabilities().add(new Capability(descriptor.getName()));
//    			INetworkService service = VirtualProxyFactory.getProxy( INetworkService.class, member );
    			INetworkService service = (INetworkService) member.createExecutableExtension(JabberPlugin.CLASS_ATTR);
    			services.put( descriptor, service );
    		}
    	};
    	processor.process( JabberPlugin.SERVICES_EXTENSION_POINT_ID, visitor );
    }*/
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#dispose()
     */
    public void dispose() {
        services.clear();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getDescriptors()
     */
    public Collection<IServiceDescriptor> getDescriptors() {
        return Collections.unmodifiableSet( services.keySet() );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getBackend(java.lang.String)
     */
    public INetworkService getService( String serviceName ) {
        ServiceDescriptor descriptor = (ServiceDescriptor) descriptors.get( serviceName );
        if (descriptor != null) {
        	INetworkService service = services.get( descriptor );           
            return service;
        }
        return null;
    }


    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackendRegistry#getDescriptor(java.lang.String)
     */
    public IServiceDescriptor getDescriptor( String id ) {
        return descriptors.get( id );
    }

}
