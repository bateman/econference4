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
