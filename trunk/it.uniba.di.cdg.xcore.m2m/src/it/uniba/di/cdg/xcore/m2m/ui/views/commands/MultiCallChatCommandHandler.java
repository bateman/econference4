package it.uniba.di.cdg.xcore.m2m.ui.views.commands;

import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class MultiCallChatCommandHandler extends AbstractHandler implements IHandler {  
	
	IMultiChatManager multiChatManager = null;
	
	public MultiCallChatCommandHandler(IMultiChatManager multiChatManager) {
		super();
		this.multiChatManager = multiChatManager;
	}

	public IMultiChatManager getMultiChatManager() {
		return multiChatManager;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//execute the multi-chat call
     	IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
     	if(b.getMultiCallAction().isCalling())
     		b.getMultiCallAction().finishCall();
     	else
     		b.getMultiCallAction().call();
		return null;
	}
}
