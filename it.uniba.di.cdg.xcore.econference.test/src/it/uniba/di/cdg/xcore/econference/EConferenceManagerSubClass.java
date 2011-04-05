package it.uniba.di.cdg.xcore.econference;

import it.uniba.di.cdg.xcore.econference.internal.EConferenceManager;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.network.BackendException;

public class EConferenceManagerSubClass extends EConferenceManager {
    
    protected IEConferenceService myservice;
    
    public void setChatService(IEConferenceService mockedService){
        myservice = mockedService;
    }
    
    @Override
    protected IEConferenceService setupChatService() throws BackendException {
        return myservice;
    }
    
    @Override
    public Role getRole() {
        return Role.PARTICIPANT;
    }

}
