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
package it.uniba.di.cdg.xcore.m2m.model;

import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.ui.IImageResources;
import it.uniba.di.cdg.xcore.ui.UiPlugin;
import it.uniba.di.cdg.xcore.ui.views.IActivatableView;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Adapter for {@see it.uniba.di.cdg.xcore.m2m.model.IParticipant}: this will provide a 
 * way for the framework to wire context actions to our model.
 * <p>
 * Note that the action filter currently accept several test properties:
 * <ul>
 * <li><b>status</b>, for testing the current online status {@link IParticipant.Status})</li>
 * <li><b>role</b>, for testing the role of selected participant ({@see it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role})</li>
 * <li><b>myRole</b>, for testing the role of the current local user ({@see it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role})</li>
 * <li><b>myStatus</b>, for testing the status of the current local user ({@see it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status})</li>
 * <li><b>specialRole</b>, for testing the special role of the user (this is a String implementation-dependent, non an enum)</li>
 * <li><b>viewIsReadOnly</b>, for testing if a specific view is read-only</li>
 * </ul>   
 */
public class ParticipantAdapterFactory implements IAdapterFactory {
    /**
     * Participants adapter.
     */
    private final IWorkbenchAdapter participantAdapter = new IWorkbenchAdapter() {
        public Object[] getChildren( Object o ) {
            return new Object[0]; // No child
        }

        public ImageDescriptor getImageDescriptor( Object object ) {
            IParticipant participant = (IParticipant) object;

            if (Role.MODERATOR.equals( participant.getRole() )){
            	if(participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)
            			&& participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)){
            		return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_MODERATOR_SCRIBE_VOTER );            		
            	}else if(participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)){
            		return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_MODERATOR_SCRIBE );
            	}else if(participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)){
            		return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_MODERATOR_VOTER );
            	}
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_MODERATOR );
            }else if (Status.JOINED.equals( participant.getStatus() )){
            	if(participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)
            			&& participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)){
            		return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_SCRIBE_VOTER );            		
            	}else if(participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.SCRIBE)){
            		return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_SCRIBE );
            	}else if(participant.hasSpecialPrivilege(ParticipantSpecialPrivileges.VOTER)){
            		return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_VOTER );
            	}
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_ACTIVE );
            }else if (Status.FROZEN.equals( participant.getStatus() ))
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_FROZEN );
            else // NOT_JOINED otherwise
                return UiPlugin.getDefault().getImageDescriptor( IImageResources.ICON_USER_OFFLINE );
        }

        public String getLabel( Object o ) {
            IParticipant p = (IParticipant) o;
            if (p.getPersonalStatus()!=null && !p.getPersonalStatus().equals(""))
                return String.format( "%s (%s)", p.getNickName(), p.getPersonalStatus() );
            else
                return p.getNickName();
        }

        public Object getParent( Object o ) {
            return null;
        }
    };
        
    /**
     * This is needed by the UI for filtering actions based on the property of the participant.
     */
    private final IActionFilter contextMenuActionFilter = new IActionFilter() {
        public boolean testAttribute( Object target, String name, String value ) {
            IParticipant p = (IParticipant) target;
            
//            System.out.println( String.format( "*** name == %s, value == %s", name, value ) );
            
            if ("status".equals( name ))
                return value.equals( p.getStatus().toString() );
            // Discriminate upon special role
            else if ("specialRole".equals( name ))
                return p.hasSpecialPrivilege(value);
            else if ("role".equals( name )) {
//                System.out.println( String.format( "Role of %s is %s", p.getId(), p.getRole() ) );
                return value.equals( p.getRole().toString() );
            }else if ("service".equals( name )) {
                ICapabilities capabilities = NetworkPlugin.getDefault().getHelper().getRoster().getBackend().getCapabilities();
                for (Iterator<ICapability> iterator = capabilities.iterator(); iterator
						.hasNext();) {
					ICapability capability = iterator.next();
					if(capability.getName().equals(value))
						return true;				
				}
                return false;
            }else if ("myRole".equals( name )) {
                IParticipant thisLocalUser = p.getChatRoom().getLocalUser();
                return thisLocalUser.getRole().equals( Role.valueOf( value ) );
            } else if ("myStatus".equals( name )) {
                IParticipant thisLocalUser = p.getChatRoom().getLocalUser();
                return thisLocalUser.getStatus().equals( Status.valueOf( value ) );
            } else if ("viewIsReadOnly".equals( name )) {
                final String viewId = value;
                IViewPart view = findView( viewId );
                
                boolean result = false;
                if (view instanceof IActivatableView) {
                    result = ((IActivatableView) view).isReadOnly();
                }
                return result;
            }

//            try {
//                final Field f = IParticipant.class.getDeclaredField( name );
//                final Object fValue = f.get( p );
//                return value.equals( fValue );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            
            return false;
        }
    };
    
    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
     */
    public Object getAdapter( Object adaptableObject, Class adapterType ) {
//        System.out.println( String.format( "ParticipantAdapterFactory.getAdapter( %s, %s )", adaptableObject, adapterType ) );
        if (!(adaptableObject instanceof IParticipant))
            return null;
        
        if (adapterType == IWorkbenchAdapter.class)
            return participantAdapter;
        else if (adapterType == IActionFilter.class)
            return contextMenuActionFilter;
        
        // No adapter otherwise
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
     */
    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class, IActionFilter.class };
    }

    private IViewPart findView( String viewId ) {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView( viewId );
    }
}
