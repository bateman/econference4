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
package it.uniba.di.cdg.xcore.ui.adapters;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddy.Status;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.Collection;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Adapts {@see it.uniba.di.cdg.xcore.network.model.IEntry} objects to workbench objects.
 * This is needed for connecting model objects (buddy, group and roster) to the UI.
 * <p> 
 * We also offer limited support for action enablements through the use of <code>IActionFilter</code>s
 * only "<b>status</b>" property is supported ({@see it.uniba.di.cdg.xcore.network.model.IBuddy.Status}).
 */
public class BuddyAdapterFactory implements IAdapterFactory {
    /**
     * Standard adapter for roster objects: we want the roster to be transparent to the 
     * user interface, still to be the root object for all groups. So this adapter will
     * only provide the childs and keep a low profile for all other activities.
     */
    private final IWorkbenchAdapter rosterAdapter = new IWorkbenchAdapter() {
        public Object[] getChildren( Object o ) {
            return ((IBuddyRoster) o).getChilds();
        }

        public ImageDescriptor getImageDescriptor( Object object ) {
            return null;
        }

        public String getLabel( Object o ) {
            return "";
        }

        public Object getParent( Object o ) {
            return null;
        }
    };

    /**
     * Adapter for groups of buddies.
     */
    private final IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter() {
        public Object getParent( Object o ) {
            return null; // buddy groups have the roster parent
        }

        public Object[] getChildren( Object o ) {
            Object[] childs = ((IBuddyGroup) o).getChilds(); 
            return childs;
        }

        public ImageDescriptor getImageDescriptor( Object object ) {
            return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_GROUP );
        }

        public String getLabel( Object o ) {
            return ((IBuddyGroup) o).getName();
        }
    };

    /**
     * Adapter for buddy entries.
     */
    private final IWorkbenchAdapter buddyAdapter = new IWorkbenchAdapter() {
        public Object[] getChildren( Object o ) {
            return new Object[0]; // No child
        }

        public ImageDescriptor getImageDescriptor( Object object ) {
            IBuddy buddy = (IBuddy) object;
            if (Status.AVAILABLE.equals( buddy.getStatus() ))
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_ACTIVE );
            else if (Status.CHAT.equals( buddy.getStatus() ))
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_ACTIVE );
            else if (Status.BUSY.equals( buddy.getStatus() ))
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_BUSY );
            else if (Status.AWAY.equals( buddy.getStatus() ))
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_AWAY );
            else if (Status.EXTENDED_AWAY.equals( buddy.getStatus() ))
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_AWAY );
            return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_OFFLINE );
        }

        public String getLabel( Object o ) {
            return ((IBuddy) o).getPrintableLabel();
        }

        public Object getParent( Object o ) {
            IBuddy buddy = (IBuddy) o;
            
            Collection<IBuddyGroup> groups = buddy.getRoster().getGroups( buddy );
            if (groups.size() > 0)
                return groups.toArray()[0];
            else 
                return new Object[0];
        }
    };

    /**
     * Permits action enablements.
     */
    private final IActionFilter actionFilterAdapter = new IActionFilter() {
        public boolean testAttribute( Object target, String name, String value ) {
            IBuddy buddy = (IBuddy) target;
//          System.out.println( String.format( "*** name == %s, value == %s", name, value ) );
          
            if ("status".equals( name )) {
                final Status wanted = Status.valueOf( value );
                return wanted.equals( buddy.getStatus() );
            } 
            return false;
        }
    };
    
    @Override
    public Object getAdapter( Object adaptableObject, Class adapterType ) {
//        System.out.println( String.format( "BuddyAdapterFactory.getAdapter( %s, %s )", adaptableObject, adapterType ) );
        if (adapterType == IWorkbenchAdapter.class 
                && adaptableObject instanceof IBuddyRoster)
            return rosterAdapter;
        else if (adapterType == IWorkbenchAdapter.class 
                && adaptableObject instanceof IBuddyGroup)
            return groupAdapter;
        else if (adapterType == IWorkbenchAdapter.class 
                && adaptableObject instanceof IBuddy)
            return buddyAdapter;
        else if (adapterType == IActionFilter.class 
                && adaptableObject instanceof IBuddy)
            return actionFilterAdapter;
        // No adapter otherwise
        return null;
    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class, IActionFilter.class };
    } 
}
