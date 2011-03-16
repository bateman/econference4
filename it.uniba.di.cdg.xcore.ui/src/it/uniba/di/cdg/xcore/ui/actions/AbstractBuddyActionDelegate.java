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
package it.uniba.di.cdg.xcore.ui.actions;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Common base for all <code>IBuddy</code> action delegates. Clients must
 * implement <code>run()</code> at the minimum.
 */
public abstract class AbstractBuddyActionDelegate implements
		IObjectActionDelegate {
	/**
	 * The selected buddies.
	 */
	private IWorkbenchPart targetPart;

	/**
	 * Enforce that this must not be used alone.
	 */
	protected AbstractBuddyActionDelegate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.
	 * action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// Nothing is done.
	}

	/**
	 * @return Returns the targetPart.
	 */
	protected IWorkbenchPart getTargetPart() {
		return targetPart;
	}

	/**
	 * Returns the currently selected buddies.
	 * 
	 * @param selection
	 */
	@SuppressWarnings("unchecked")
	protected Collection<IBuddy> getSelectedBuddies() {
		IStructuredSelection selected = (IStructuredSelection) getSelection();

		final Collection<IBuddy> selectedBuddies = new ArrayList<IBuddy>();
		Iterator<IBuddy> it = selected.iterator();
		while (it.hasNext()) {
			IBuddy buddy = it.next();
			selectedBuddies.add(buddy);
			System.out.println("You selected: " + buddy);
		}

		return selectedBuddies;
	}

	/**
	 * Returns the first selected buddy.
	 * 
	 * @return the first selected buddy
	 */
	protected IBuddy getSelected() {
		return (IBuddy) getSelection().getFirstElement();
	}

	/**
	 * Returns the first selected group.
	 * 
	 * @return the first selected group
	 */
	protected IBuddyGroup getSelectedGroup() {
		return (IBuddyGroup) getSelection().getFirstElement();
	}

	/**
	 * Returns the ids of the selected buddies.
	 * 
	 * @return the ids of the selected buddies
	 */
	public Collection<String> getSelectedBuddiesId() {
		Collection<String> strings = new ArrayList<String>();
		for (IBuddy b : getSelectedBuddies())
			strings.add(b.getId());
		return strings;
	}

	/**
	 * Returns the current selection.
	 * 
	 * @return the current selection
	 */
	protected IStructuredSelection getSelection() {
		return (IStructuredSelection) targetPart.getSite()
				.getSelectionProvider().getSelection();
	}
}
