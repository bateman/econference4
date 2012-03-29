/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2012 Collaborative Development Group - Dipartimento di Informatica, 
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
package it.uniba.di.cdg.xcore.econference.popup.handler;

import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.popup.agenda.SelectedItemListener;
import it.uniba.di.cdg.xcore.econference.toolbar.handler.ManagerTransport;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ListViewer;


public class DeleteItem extends AbstractHandler {
	
	public static final int DELETE_ITEM_INDEX = -15;
	
	private IEConferenceManager manager;
	private String seltectedItemIndex;
	private ListViewer viewer;
	private String oldText;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		manager = new ManagerTransport().getManager();
		seltectedItemIndex = new SelectedItemListener().getSelectedIndex();
		viewer = new SelectedItemListener().getSelectedAgendaView();
		
		
		Integer threadId = Integer.parseInt(seltectedItemIndex);
		
		if (getModel().getItemList().size() > 1) {
		Boolean deleteChoice = UiPlugin.getUIHelper().askYesNoQuestion("Delete Discussion Item",
				"Delete discussion item \""+((IDiscussionItem) getModel().getItemList().getItem(threadId)).getText()+"\"?");

		if (deleteChoice) {
		oldText = ((IDiscussionItem) getModel().getItemList().getItem(threadId)).getText();	
		getModel().getItemList().removeItem(threadId);
		viewer.getList().removeAll();
        
		for (int i=0; i<getModel().getItemList().size(); i++){
            viewer.add(((IDiscussionItem)getModel().getItemList().getItem(i)).getText() );
        		}

		getManager().notifyItemListToRemote();
		
		String discussedItem = new SelectedItemListener().getAgendaView().getDiscussedItem();
		if (discussedItem.compareTo(oldText)==0){
			new SelectedItemListener().getAgendaView().setDiscussedItem("");
			
			manager.getTalkView().setTitleText("Free talk now ...");
			String newItemId = String.format( "%d", DeleteItem.DELETE_ITEM_INDEX);
			manager.notifyCurrentAgendaItemChanged( newItemId );
			}
		
			}
		} else {
			UiPlugin.getUIHelper().showMessage("Delete Error", "Last Discussion Item, you can not erase it.\nIf you want to edit it.");
		}
		return null;
	}
	
	private IConferenceModel getModel() {
        return getManager().getService().getModel();
    }
    
    
    
    private IEConferenceManager getManager() {
        return manager;
    }
    

}