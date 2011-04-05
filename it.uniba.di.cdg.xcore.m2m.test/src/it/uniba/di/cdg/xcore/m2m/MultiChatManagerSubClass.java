package it.uniba.di.cdg.xcore.m2m;

import it.uniba.di.cdg.xcore.m2m.internal.MultiChatManager;
import it.uniba.di.cdg.xcore.m2m.service.IMultiChatService;
import it.uniba.di.cdg.xcore.network.BackendException;

public class MultiChatManagerSubClass extends MultiChatManager {
    
    public void setChatService(IMultiChatService mockedService){
        service = mockedService;        
    }
        
    protected IMultiChatService setupChatService() throws BackendException {
        return service;
    }

}
