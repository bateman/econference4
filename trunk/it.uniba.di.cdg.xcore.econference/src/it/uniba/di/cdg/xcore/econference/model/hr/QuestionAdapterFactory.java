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
package it.uniba.di.cdg.xcore.econference.model.hr;

import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;
import it.uniba.di.cdg.xcore.m2m.IRoleProvider;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Adapter for {@see it.uniba.di.cdg.xcore.econference.model.hr.IQuestion} objects and 
 * filtering actions based on questions' properties. The supported properties are:
 * <ul>
 * <li><b>myRole</b>, for testing against current local user role (moderator, participant ...)</li>
 * <li><b>status</b>, for testing against question status (APPROVED, REJECTED, ...)</li>
 * </ul>
 */
public class QuestionAdapterFactory implements IAdapterFactory {

    private final IWorkbenchAdapter workbenchAdapter = new IWorkbenchAdapter() {
        public Object[] getChildren( Object o ) {
            return new Object[0];
        }

        public ImageDescriptor getImageDescriptor( Object object ) {
            return null;
        }

        public String getLabel( Object o ) {
            final IQuestion q = (IQuestion) o;
            return q.getText();
        }

        public Object getParent( Object o ) {
            return null;
        }
    };

    /**
     * This adapter only provides the "myRole" property for checking action enabling 
     * agains local user's current role.
     */
    private final IActionFilter contextMenuActionFilter = new IActionFilter() {
        public boolean testAttribute( Object target, String name, String value ) {
//            System.out.println( String.format( "*** name == %s, value == %s", name, value ) );

            // XXX We gain access to current part, being confident that it is a role 
            // provider (the current HR view is).
            final IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
            final IQuestion q = (IQuestion) target;

            if ("myRole".equals( name ) && part instanceof IRoleProvider) {
                IRoleProvider roleProvider = (IRoleProvider) part;
                Role wantedRole = Role.valueOf( value );
                return roleProvider.getRole().compareTo( wantedRole ) >= 0;
            } else if ("status".equals( name )){
                QuestionStatus wanted = QuestionStatus.valueOf( value );
                return wanted.equals( q.getStatus() );
            }
            return false;
        }
    };
    
    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
     */
    public Object getAdapter( Object adaptableObject, Class adapterType ) {
//        System.out.println( String.format( "QuestionAdapterFactory.getAdapter( %s, %s )", adaptableObject, adapterType ) );
        if (adaptableObject instanceof IQuestion) {
            if (adapterType == IWorkbenchAdapter.class)
                return workbenchAdapter;
            else if (adapterType == IActionFilter.class)
                return contextMenuActionFilter;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
     */
    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class, IActionFilter.class };
    }
}
