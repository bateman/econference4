package it.uniba.di.cdg.xcore.econference.popup.handler;

import it.uniba.di.cdg.xcore.econference.IEConferenceManager;
import it.uniba.di.cdg.xcore.econference.model.IConferenceModel;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.popup.agenda.SelectedItemListener;
import it.uniba.di.cdg.xcore.econference.toolbar.handler.ManagerTransport;
//import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ListViewer;


public class MoveDown extends AbstractHandler {
	
	private IEConferenceManager manager;
	private String seltectedItemIndex;
	private ListViewer viewer;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		manager = new ManagerTransport().getManager();
		seltectedItemIndex = new SelectedItemListener().getSelectedIndex();
		viewer = new SelectedItemListener().getSelectedAgendaView();
		
				
		Integer threadId = Integer.parseInt(seltectedItemIndex);
		
		if (threadId < getModel().getItemList().size()-1){
		String currentText = ((IDiscussionItem) getModel().getItemList().getItem(threadId)).getText();
		String DownText = ((IDiscussionItem) getModel().getItemList().getItem(threadId+1)).getText();
		
		
        ((IDiscussionItem) getModel().getItemList().getItem(threadId)).setText(DownText);
        ((IDiscussionItem) getModel().getItemList().getItem(threadId+1)).setText(currentText);
        
		viewer.getList().removeAll();

		for (int i=0; i<getModel().getItemList().size(); i++){
            viewer.add(((IDiscussionItem)getModel().getItemList().getItem(i)).getText() );
        	}
		
		getManager().notifyItemListToRemote();
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
