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
	
	public final int deleteIndex = -15;
	
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
			String newItemId = String.format( "%d", -15);
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