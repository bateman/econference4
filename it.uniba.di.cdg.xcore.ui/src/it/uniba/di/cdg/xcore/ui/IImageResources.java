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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Give access to the shared images available in the application.
 */
public interface IImageResources {
    /**
     * Default symbolic image names (we use the convention that the string value is also the
     * filename part without the ".png" extension). 
     * <p>
     * All icons here are 16x16 pixels format.
     */
    public static final String ICON_USER_MODERATOR = "user_moderator"; //$NON-NLS-1$
    public static final String ICON_USER_ACTIVE = "user_active"; //$NON-NLS-1$
    public static final String ICON_USER_AWAY = "user_away"; //$NON-NLS-1$
    public static final String ICON_USER_BUSY = "user_busy"; //$NON-NLS-1$
    public static final String ICON_USER_FROZEN = "user_frozen"; //$NON-NLS-1$
    public static final String ICON_USER_OFFLINE = "user_offline"; //$NON-NLS-1$
    public static final String ICON_GROUP = "people"; //$NON-NLS-1$
    public static final String ICON_BACKEND_ONLINE = "backend_online"; //$NON-NLS-1$
    public static final String ICON_BACKEND_OFFLINE = "backend_offline"; //$NON-NLS-1$
    public static final String ICON_HELP = "help_icon";
    
	public static final String ICON_SCRIBE_VOTER = "user_scribe_voter";
	public static final String ICON_SCRIBE = "user_scribe_active";
	public static final String ICON_VOTER = "user_voter";
	public static final String ICON_MODERATOR_SCRIBE_VOTER = "user_moderator_scribe_voter";
	public static final String ICON_MODERATOR_SCRIBE = "user_moderator_scribe";
	public static final String ICON_MODERATOR_VOTER = "user_moderator_voter";

    public static final String ICON_TYPING = "icon_typing"; //$NON-NLS-1$
    
    public static final String ICON_ACTION_INCREASE_FONT_SIZE = "action_increase_font_size"; //$NON-NLS-1$
    public static final String ICON_ACTION_DECREASE_FONT_SIZE = "action_decrease_font_size"; //$NON-NLS-1$
    
    /**
     * Actions' and misc contribution items' icons (all 16x16s).
     */
    public static final String ICON_ACTION_CONNECT = "action_connect"; //$NON-NLS-1$
    public static final String ICON_ACTION_DISCONNECT = "action_disconnect"; //$NON-NLS-1$
    public static final String ICON_ACTION_RELOAD = "action_reload"; //$NON-NLS-1$

    
    /**
     * This icon is for default Buddy Image if we don't have his profile associated and is 50x50
     */
    
    public static final String BUDDYDEFAULTIMAGE = "buddy_default_image"; //$NON-NLS-1$

    
    /**
     * Returns the image descriptor for a given symbolic name (if an <code>Image</code>
     * is needed then look for {@link #getImage(String)}).
     * 
     * @param symbolicName the symbolic name for the needed image descriptor
     * @return
     */
    ImageDescriptor getImageDescriptor( String symbolicName );
    
    /**
     * Returns the image associated to the symbolic name.
     * 
     * @param symbolicName
     * @return
     */
    Image getImage( String symbolicName );
}
