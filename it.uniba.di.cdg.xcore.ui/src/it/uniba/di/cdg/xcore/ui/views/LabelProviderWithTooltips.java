/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2011 Collaborative Development Group - Dipartimento di Informatica, 
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

package it.uniba.di.cdg.xcore.ui.views;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class LabelProviderWithTooltips extends ColumnLabelProvider implements IWorkbenchAdapter {

    private class WorkbenchLabelProviderForTooltips extends WorkbenchLabelProvider {
        public IWorkbenchAdapter getAdapterFT( Object x ) {
            return this.getAdapter( x );
        }
    }

    WorkbenchLabelProviderForTooltips wbadapter = new WorkbenchLabelProviderForTooltips();

    public String getToolTipText( Object element ) {
        // show tooltip only for IBuddy interface
        if (element instanceof IBuddy) {
            if (((IBuddy) element).isOnline()) {
                // buddy is online
                IBuddyRoster iroster = ((IBuddy) element).getRoster();
                // String id2 = "useraccount.getid = " + iroster.getBackend().getUserAccount(). +
                // "\n";
                String id = ((IBuddy) element).getId();
                ArrayList<String> presencesStr = iroster.getPresencesStr( id );
                // System.out.println("Buddy: " + id + " accountId: " + ((IBuddy)
                // element).getAccountId());
                String tooltipText = id + "\n";
                if (!presencesStr.isEmpty()) {
                    tooltipText += "Connected from:\n";
                }
                for (Iterator<String> prStrIt = presencesStr.iterator(); prStrIt.hasNext();) {
                    String prStr = prStrIt.next();
                    tooltipText = tooltipText
                            + prStr.substring( prStr.indexOf( '/' ) + 1, prStr.length() - 1 )
                            + "\n";
                }
                if (tooltipText.endsWith( "\n" )) {
                    tooltipText = tooltipText.substring( 0, tooltipText.length() - 1 );
                }
                return tooltipText;
            } else {
                // buddy is not online
                String tooltipText = "Not connected";
                return tooltipText;
            }
        } else {
            // might be a group
            return null;
        }
    }

    /**
     * Auth: Malerba Francesco
     * 
     * overrided from superclass, this method provide the image associated with the tooltip that
     * comes for every buddy showing his image.
     */
    public Image getToolTipImage( Object element ) {
        if (element instanceof IBuddy) {
            IBuddy i = (IBuddy) element;
            String URLL = BuddyListView.getBuddy( i.getId() );
            if (URLL != null) {
                System.out.print( "URL OF " + i.getId() + " IS " + URLL + " AND HIS BUDDYNAME IS "
                        + i.getName() );
                ImageDescriptor des;
                try {
                    des = ImageDescriptor.createFromURL( new URL( URLL ) );
                    Image w = des.createImage();
                    return w;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.BUDDYDEFAULTIMAGE )
                        .createImage();
            }

        }
        return null;

    }

    public Image getImage( Object element ) {
        return wbadapter.getImage( element );
    }

    public String getText( Object element ) {
        return wbadapter.getText( element );
    }

    public Object[] getChildren( Object element ) {
        IWorkbenchAdapter iwb = wbadapter.getAdapterFT( element );
        return iwb.getChildren( element );
    }

    public ImageDescriptor getImageDescriptor( Object element ) {
        IWorkbenchAdapter iwb = wbadapter.getAdapterFT( element );
        return iwb.getImageDescriptor( element );
    }

    public String getLabel( Object element ) {
        IWorkbenchAdapter iwb = wbadapter.getAdapterFT( element );
        return iwb.getLabel( element );
    }

    public Object getParent( Object element ) {
        IWorkbenchAdapter iwb = wbadapter.getAdapterFT( element );
        return iwb.getParent( element );
    }

}
