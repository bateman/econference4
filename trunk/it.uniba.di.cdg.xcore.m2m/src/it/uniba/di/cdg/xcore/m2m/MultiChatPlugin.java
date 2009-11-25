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
package it.uniba.di.cdg.xcore.m2m;

import it.uniba.di.cdg.xcore.m2m.internal.MultiChatHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Provides Multi-chat implementation.
 * <p>
 * The duty of this plug-in is to track invitation requests and ask the user if he wants
 * to join the chat-room. 
 */
public class MultiChatPlugin extends AbstractUIPlugin {
    /**
     * Plug-in's unique id.
     */
    public final static String ID = "it.uniba.di.cdg.xcore.m2m";

	//The shared instance.
	private static MultiChatPlugin plugin;
	
    private IMultiChatHelper helper;
    
	/**
	 * The constructor.
	 */
	public MultiChatPlugin() {
		plugin = this;
        
        helper = new MultiChatHelper( UiPlugin.getUIHelper(), NetworkPlugin.getDefault().getHelper() );
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception {
        super.start( context );

        helper.init();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception {
        super.stop( context );

        helper.dispose();
        
		plugin = null;
	}
    
	/**
	 * Returns the shared instance.
	 */
	public static MultiChatPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin( ID, path );
	}

    /**
     * Returns the current helper for this multichat.
     * 
     * @return
     */
	public IMultiChatHelper getHelper() {
	    return helper;   
    }
}
