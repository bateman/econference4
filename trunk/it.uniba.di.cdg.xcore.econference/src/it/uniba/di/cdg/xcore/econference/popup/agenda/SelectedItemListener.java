package it.uniba.di.cdg.xcore.econference.popup.agenda;

import it.uniba.di.cdg.xcore.econference.ui.views.AgendaView;

import org.eclipse.jface.viewers.ListViewer;


public class SelectedItemListener {
	
	static private String selectedIndex;
	static private ListViewer selectedView;
	static private AgendaView viewAgenda;

	
	public void setSelectedIndex( String index ) {
        selectedIndex = index;
    }

    public String getSelectedIndex() {
        return selectedIndex;
    }
    
	public void setSelectedAgendaView( ListViewer viewer ) {
        selectedView = viewer;
    }

    public ListViewer getSelectedAgendaView() {
        return selectedView;
    }
	public void setAgendaView( AgendaView view ) {
        viewAgenda = view;
    }

    public AgendaView getAgendaView() {
        return viewAgenda;
    }
}