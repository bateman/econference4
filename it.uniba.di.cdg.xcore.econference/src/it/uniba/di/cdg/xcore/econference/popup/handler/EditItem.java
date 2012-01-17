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


public class EditItem extends AbstractHandler {
	
	private IEConferenceManager manager;
	private String seltectedItemIndex;
	private ListViewer viewer;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		manager = new ManagerTransport().getManager();
		seltectedItemIndex = new SelectedItemListener().getSelectedIndex();
		viewer = new SelectedItemListener().getSelectedAgendaView();
		
				
		Integer threadId = Integer.parseInt(seltectedItemIndex);
		String oldText = ((IDiscussionItem) getModel().getItemList().getItem(threadId)).getText();
		
		String newItem = UiPlugin.getUIHelper().askFreeQuestion( "Please, type the new text referring to the selected item and press Ok." +
                "\nOtherwise press cancel to discard the operation.", 
                oldText );
         if (newItem == null) // Cancel pressed
             return null;
		
        ((IDiscussionItem) getModel().getItemList().getItem(threadId)).setText(newItem); 
		viewer.getList().removeAll();

		for (int i=0; i<getModel().getItemList().size(); i++){
            viewer.add(((IDiscussionItem)getModel().getItemList().getItem(i)).getText() );
        	}
		
		getManager().notifyItemListToRemote();
		
		String discussedItem = new SelectedItemListener().getAgendaView().getDiscussedItem();
		if (discussedItem.compareTo(oldText)==0){
			new SelectedItemListener().getAgendaView().setDiscussedItem(newItem);
			
			String newItemId = String.format( "%d", threadId);
			manager.notifyCurrentAgendaItemChanged( newItemId );
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