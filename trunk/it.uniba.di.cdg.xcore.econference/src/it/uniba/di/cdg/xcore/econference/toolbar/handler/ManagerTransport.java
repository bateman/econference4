package it.uniba.di.cdg.xcore.econference.toolbar.handler;

import it.uniba.di.cdg.xcore.econference.IEConferenceManager;

public class ManagerTransport {
	
	static private IEConferenceManager manager;
	
	public void setManager( IEConferenceManager m_manager ) {
        manager = m_manager;
    }

    public IEConferenceManager getManager() {
        return manager;
    }
}
