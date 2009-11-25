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
package it.uniba.di.cdg.xcore.m2m.ui.actions.popup;

import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.ui.views.IMultiChatView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Shared stuff among context actions related to a participant. 
 */
public abstract class AbstractParticipantActionDelegate implements IObjectActionDelegate {
    /**
     * The selected buddies.
     */
    private IWorkbenchPart targetPart;
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
     */
    public void setActivePart( IAction action, IWorkbenchPart targetPart ) {
        this.targetPart = targetPart;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged( IAction action, ISelection selection ) {
    }

    /**
     * Returns the manager associated to this action.
     * 
     * @return hte multichat manager
     */
    public IMultiChatManager getManager() {
        return ((IMultiChatView) getTargetPart()).getManager();
    }
    
    /**
     * Returns the active viewer this action has been called upon.
     * 
     * @return the target part.
     */
    protected IWorkbenchPart getTargetPart() {
        return targetPart;
    }
    
    /**
     * Returns the selected participants.
     * 
     * @param selection
     */
    protected List<IParticipant> getSelectedParticipants() {
        IStructuredSelection items = (IStructuredSelection) getSelection();
        
        List<IParticipant> selected = new ArrayList<IParticipant>();
        for (Iterator it = items.iterator(); it.hasNext(); ) {
            IParticipant p = (IParticipant) it.next();
            selected.add( p );
        }
        return selected;
    }

    /**
     * Convenience method that returns an array instead of a collection.
     * 
     * @return an array containing the selected participants 
     */
    protected IParticipant[] getSelectedParticipantsAsArray() {
        List<IParticipant> selected = getSelectedParticipants();
        IParticipant[] array = new IParticipant[selected.size()];
        return selected.toArray( array );
    }
    
    /**
     * Returns the first of the selected participants.
     * 
     * @return the first selected participant
     */
    protected IParticipant getFirstParticipant() {
        return (IParticipant) ((IStructuredSelection) getSelection()).getFirstElement();
    }
    
    /**
     * Returns the current selection.
     * 
     * @return the selection
     */
    protected ISelection getSelection() {
        return targetPart.getSite().getSelectionProvider().getSelection();
    }
}
