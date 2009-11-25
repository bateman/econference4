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
package it.uniba.di.cdg.xcore.ui;

import it.uniba.di.cdg.xcore.ui.internal.UIHelper;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The <i>User Interface</i> plug-in provides a set of shared utilities and resources 
 * (like images and image descriptors) among all the graphical plug-ins. 
 */
public class UiPlugin extends AbstractUIPlugin implements IImageResources {
    /**
     * Plug-in's id.
     */
    public static final String ID = "it.uniba.di.cdg.xcore.ui";

    private static final String ICON_PATH = "icons/";

    private static final String ICON_FILES_EXT = ".png";

    // The shared instance.
    private static UiPlugin plugin;

    private IUIHelper uihelper;
    
    /**
     * The constructor.
     */
    public UiPlugin() {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start( BundleContext context ) throws Exception {
        super.start( context );
        
        uihelper = new UIHelper();
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop( BundleContext context ) throws Exception {
        super.stop( context );
        
        uihelper = null;
        
        plugin = null;
    }

    /**
     * Returns the shared instance.
     * @return the instance of UIPlugin
     */
    public static UiPlugin getDefault() {
        return plugin;
    }

    /**
     * Actually loads the image.
     * FIXME And what happens if the image file is not found?
     * 
     * @param symbolicName
     * @return the image descriptor
     */
    private ImageDescriptor loadAndRegisterIconImage( String symbolicName ) {
        Bundle bundle = Platform.getBundle( ID );

        URL url = bundle.getResource( ICON_PATH + symbolicName + ICON_FILES_EXT );
        ImageDescriptor descr = ImageDescriptor.createFromURL( url );
        getImageRegistry().put( symbolicName, descr );

        return descr;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.ui.util.IEConferenceImages#getImageDescriptor(java.lang.String)
     */
    public ImageDescriptor getImageDescriptor( String symbolicName ) {
        ImageDescriptor descr = getImageRegistry().getDescriptor( symbolicName );
        if (descr == null) {
            descr = loadAndRegisterIconImage( symbolicName );
        }
        return descr;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.ui.util.IEConferenceImages#getImage(java.lang.String)
     */
    public Image getImage( String symbolicName ) {
        Image img = getImageRegistry().get( symbolicName );
        if (img == null) {
            loadAndRegisterIconImage( symbolicName );
            img = getImageRegistry().get( symbolicName );
        }
        return img;
    }

    /**
     * Returns the current UI helper.
     * 
     * @return the UI helper
     */
    public IUIHelper getHelper() {
        return uihelper;
    }
    
    /**
     * Set the current UI helper.
     * 
     * @param uihelper an UI helper implementation
     *  
     */
    public void setHelper(IUIHelper uihelper) {
        this.uihelper = uihelper;
    }
    
    /**
     * Returns the default UI helper.
     * 
     * @return the ui helper
     */
    public static IUIHelper getUIHelper() {
        return getDefault().getHelper();
    }
}
