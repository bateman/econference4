/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2012 Collaborative Development Group - Dipartimento di Informatica, 
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
package it.uniba.di.cdg.xcore.econference.toolbar.handler;

import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


public class AddAgendaItem extends AbstractHandler {

	
	private IEConferenceManager manager;
	
	/**
	 * 
	 */
	public AddAgendaItem() {
		super();
		}

	
	private boolean privilege_check(){
		this.setBaseEnabled(false);
		
		manager = new ManagerTransport().getManager();
		if (Role.MODERATOR.equals( getModel().getLocalUser().getRole()) ){
	    	this.setBaseEnabled(true);
	    	return true;
		}
		return false;
	}    
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean privilage = this.privilege_check();
		
		if (privilage){
		String newItem = UiPlugin.getUIHelper().askFreeQuestion( "Please, type the text referring to the new item and press Ok." +
                "\nOtherwise press cancel to discard the operation.", 
                "Add a new item" );
         if (newItem == null) // Cancel pressed
             return null;
         
         getModel().getItemList().addItem( newItem );
         getManager().notifyItemListToRemote();
		}else {
			UiPlugin.getUIHelper().showMessage("Privilage Error", "You are not moderator");
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
