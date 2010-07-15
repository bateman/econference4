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
package it.uniba.di.cdg.xcore.econference.ui.actions;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;


import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.ParticipantSpecialPrivileges;
import it.uniba.di.cdg.xcore.m2m.model.SpecialPrivilegesAction;
import it.uniba.di.cdg.xcore.m2m.ui.actions.popup.AbstractParticipantActionDelegate;

/**
 * Action for clearing special role (currently unmaking a man as scribe. 
 */
public class UnMakeScribeAction extends AbstractParticipantActionDelegate {
    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run( IAction action ) {
        // This action is for conferences only ... 
        if (!(getManager() instanceof IEConferenceManager))
            return;
        final IEConferenceManager manager = (IEConferenceManager) getManager();
        final IParticipant p = getFirstParticipant();
        Assert.isNotNull( p );
        
        manager.notifySpecialPrivilegeChanged( p, ParticipantSpecialPrivileges.SCRIBE, SpecialPrivilegesAction.REVOKE );
    }
}
