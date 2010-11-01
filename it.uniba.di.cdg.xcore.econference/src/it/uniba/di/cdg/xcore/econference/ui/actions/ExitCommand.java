package it.uniba.di.cdg.xcore.econference.ui.actions;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExitCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// if is online must disconnect first
		if (NetworkPlugin.getDefault().getRegistry().getDefaultBackend().isConnected()) {
			boolean answer = UiPlugin
					.getUIHelper()
					.askYesNoQuestion("Disconnect & exit?",
							"You are currently online. Do you want to disconnect and exit?");
			if (answer == true) {
				disconnect();
			} else
				return null;
		}


		HandlerUtil.getActiveWorkbenchWindow(event).close();
		return null;
	}
	
	@SwtAsyncExec
	private void disconnect() {				
		NetworkPlugin.getDefault().getHelper().shutdown();
	}
}
