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

import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;

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
		// only typing notification, econference protocol extensions are treated elsewhere
		manager.addExtensionProvider( TypingNotificationPacket.ELEMENT_NAME, TypingNotificationPacket.ELEMENT_NS, TypingNotificationPacket.class );
	}

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
