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

import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.ui.views.IEConferenceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Base class for implementing all <code>IQuestion</code>-based UI action delegates. 
 */
public abstract class AbstractQuestionActionDelegate implements IObjectActionDelegate {
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
    public IEConferenceManager getManager() {
        return ((IEConferenceView) getTargetPart()).getManager();
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
     * Returns the selected questions.
     * 
     * @param selection
     */
    protected List<IQuestion> getSelectedParticipants() {
        IStructuredSelection items = (IStructuredSelection) getSelection();
        
        List<IQuestion> selected = new ArrayList<IQuestion>();
        for (Iterator it = items.iterator(); it.hasNext(); ) {
            IQuestion q = (IQuestion) it.next();
            selected.add( q );
        }
        return selected;
    }

    /**
     * Convenience method that returns an array instead of a collection.
     * 
     * @return an array containing the selected questions 
     */
    protected IQuestion[] getSelectedQuestionsAsArray() {
        List<IQuestion> selected = getSelectedParticipants();
        IQuestion[] array = new IQuestion[selected.size()];
        return selected.toArray( array );
    }
    
    /**
     * Returns the first of the selected questions.
     * 
     * @return the first selected question
     */
    protected IQuestion getFirstQuestion() {
        return (IQuestion) ((IStructuredSelection) getSelection()).getFirstElement();
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
