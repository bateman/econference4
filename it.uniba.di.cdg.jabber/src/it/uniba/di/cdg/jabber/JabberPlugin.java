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
package it.uniba.di.cdg.jabber;

import it.uniba.di.cdg.smackproviders.AgendaItemListPacket;
import it.uniba.di.cdg.smackproviders.AgendaOperationPacket;
import it.uniba.di.cdg.smackproviders.ConferenceStatusPacket;
import it.uniba.di.cdg.smackproviders.CurrentAgendaItemPacket;
import it.uniba.di.cdg.smackproviders.MUCPersonalStatusChangedPacket;
import it.uniba.di.cdg.smackproviders.PrivateMessageIQPacket;
import it.uniba.di.cdg.smackproviders.QuestionUpdatePacket;
import it.uniba.di.cdg.smackproviders.RaiseHandIQPacket;
import it.uniba.di.cdg.smackproviders.SpecialPrivilegeNotificationPacket;
import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;
import it.uniba.di.cdg.smackproviders.ViewReadOnlyPacket;
import it.uniba.di.cdg.smackproviders.WhiteBoardNotificationPacket;

import org.eclipse.core.runtime.Plugin;
import org.jivesoftware.smack.provider.ProviderManager;
import org.osgi.framework.BundleContext;

/**
 * Plug-in for handling the Jabber/XMPP backend. It provides access to extension registry
 * and the backend's helper.
 */
public class JabberPlugin extends Plugin {
    /**
     * The string id.
     */
    public static final String ID = "it.uniba.di.cdg.jabber";
    
    public static final String CDG_NAMESPACE = "http://cdg.di.uniba.it/xcore/jabber";
    
    public static final String PACKETS_EXTENSION_POINT_ID = ID + ".packets";
	
	public static final String CLASS_ATTR = "class";
	
	public static final String SERVICES_EXTENSION_POINT_ID = ID + ".services";
	
	public static final String SERVICES_NAME_ATTR = "name";
    
	//The shared instance.
	private static JabberPlugin plugin;

	private ServiceRegistry registry;
	
	private ProviderManager manager;

	
	/**
	 * The constructor.
	 */
	public JabberPlugin() {
		plugin = this;
		registry = new ServiceRegistry();
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);		
		//registry.processExtensions( ExtensionProcessor.getDefault() );
		
        // This is not the better place to add the provider (it will need to be removed in
        // stop() but since SMACK gives no way to remove providers ...
		manager = ProviderManager.getInstance();
		
		//TODO: se uno vuole può usare lo stesso meccaniscmo per la parte base di eConference
		manager.addExtensionProvider( TypingNotificationPacket.ELEMENT_NAME, TypingNotificationPacket.ELEMENT_NS, TypingNotificationPacket.class );
		manager.addExtensionProvider( ConferenceStatusPacket.ELEMENT_NAME, CDG_NAMESPACE, ConferenceStatusPacket.class );
		manager.addExtensionProvider( WhiteBoardNotificationPacket.ELEMENT_NAME, CDG_NAMESPACE, WhiteBoardNotificationPacket.class );
		manager.addExtensionProvider( SpecialPrivilegeNotificationPacket.ELEMENT_NAME, CDG_NAMESPACE, SpecialPrivilegeNotificationPacket.class );
		manager.addExtensionProvider( MUCPersonalStatusChangedPacket.ELEMENT_NAME, CDG_NAMESPACE, MUCPersonalStatusChangedPacket.class);
		manager.addExtensionProvider( QuestionUpdatePacket.ELEMENT_NAME, CDG_NAMESPACE, QuestionUpdatePacket.class );
		manager.addExtensionProvider( ViewReadOnlyPacket.ELEMENT_NAME, CDG_NAMESPACE, ViewReadOnlyPacket.class );
		manager.addExtensionProvider( CurrentAgendaItemPacket.ELEMENT_NAME, CDG_NAMESPACE, CurrentAgendaItemPacket.class );
		manager.addExtensionProvider( AgendaItemListPacket.ELEMENT_NAME, CDG_NAMESPACE, AgendaItemListPacket.class );
		manager.addExtensionProvider( AgendaOperationPacket.ELEMENT_NAME, CDG_NAMESPACE, AgendaOperationPacket.class );
        
		manager.addIQProvider( RaiseHandIQPacket.ELEMENT_NAME, RaiseHandIQPacket.ELEMENT_NS, RaiseHandIQPacket.class );
		manager.addIQProvider( PrivateMessageIQPacket.ELEMENT_NAME, PrivateMessageIQPacket.ELEMENT_NS, PrivateMessageIQPacket.class );		
		
		//addPacketExtensionFromExtensionPoint();
		//processExtensions(ExtensionProcessor.getDefault());
		//Adding extension point packets
//		manager.addExtensionProvider(DefaultBacklogPacket.ELEMENT_NAME, DefaultBacklogPacket.ELEMENT_NS, new DefaultBacklogPacket.Provider());		
//		manager.addExtensionProvider(DefaultCardSelectionPacket.ELEMENT_NAME, DefaultCardSelectionPacket.ELEMENT_NS, new DefaultCardSelectionPacket.Provider());
//		manager.addExtensionProvider(EstimateSessionStatusPacket.ELEMENT_NAME, EstimateSessionStatusPacket.ELEMENT_NS, EstimateSessionStatusPacket.class);
//		manager.addExtensionProvider(DefaultDeckPacket.ELEMENT_NAME, DefaultDeckPacket.ELEMENT_NS, new DefaultDeckPacket.Provider());
//		manager.addExtensionProvider(EstimateAssignedPacket.ELEMENT_NAME, EstimateAssignedPacket.ELEMENT_NS, EstimateAssignedPacket.class);
		
	}

	/**
	 * TODO: descrivere
	 * @param manager
	 */
//	private void addPacketExtensionFromExtensionPoint() {
//		try {
//			IConfigurationElement[] config = Platform.getExtensionRegistry()
//					.getConfigurationElementsFor(PACKETS_EXTENSION_POINT_ID);
//			for (IConfigurationElement e : config) {
//				final Object o = e.createExecutableExtension(CLASS_ATTR);
//				if (o instanceof IPacketExtension) {
//					ISafeRunnable runnable = new ISafeRunnable() {
//						@Override
//						public void handleException(Throwable exception) {
//							System.out.println("Exception in client");
//						}
//
//						@Override
//						public void run() throws Exception {
//							IPacketExtension packetExtension = (IPacketExtension) o;
//							manager.addExtensionProvider(packetExtension.getElementName(), 
//									packetExtension.getElementName(), packetExtension.getProvider());					
//						}
//					};
//					SafeRunner.run(runnable);
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			//System.out.println(ex.getMessage());
//		}
//
//	}
	
	/**
	 * This method looking for all packet extension point in order to add the Extension Provider to the manager
	 * @param processor
	 * @throws Exception
	 */
	/*public void processExtensions( IExtensionProcessor processor ) throws Exception {
    	IExtensionVisitor visitor = new IExtensionVisitor() {
    		public void visit( IExtension extension, IConfigurationElement member ) throws Exception {
//    			PacketExtension packet = VirtualProxyFactory.getProxy( PacketExtension.class, member );
//    			PacketExtensionProvider provider = VirtualProxyFactory.getProxy( PacketExtensionProvider.class, member );
    			IPacketExtension packet = (IPacketExtension) member.createExecutableExtension(JabberPlugin.CLASS_ATTR);
    			//PacketExtensionProvider provider = (PacketExtensionProvider)member.createExecutableExtension("provider");
    			manager.addExtensionProvider(packet.getElementName(), 
    					packet.getNamespace(), packet.getProvider());
    		}
    	};
    	processor.process( JabberPlugin.PACKETS_EXTENSION_POINT_ID, visitor );
    }*/

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 * @return plugin instance
	 */
	public static JabberPlugin getDefault() {
		return plugin;
	}
	
	public ServiceRegistry getRegistry(){
		return registry;
	}
}
