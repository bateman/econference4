package it.uniba.di.cdg.xcore.one2oneinfo;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.HashMap;

public class InfoDaemon implements IBackendEventListener {

	public void start() {
		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry()
				.getDescriptors())
			NetworkPlugin.getDefault().getHelper()
					.registerBackendListener(d.getId(), this);
	}

	@Override
	public void onBackendEvent(IBackendEvent event) {
		if (event instanceof ChatExtensionProtocolEvent) {
			IBackend b = NetworkPlugin.getDefault().getRegistry()
					.getDefaultBackend();
			IChatServiceActions chat = b.getChatServiceAction();

			ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent) event;

			if (cepe.getExtensionName().equals(One2OneInfoConstants.GET_USER_INFO)) {
				HashMap<String, String> param = new HashMap<String, String>();
				param.put(One2OneInfoConstants.OS_NAME, System.getProperty("os.name"));
				param.put(One2OneInfoConstants.OS_VER, System.getProperty("os.version"));
				chat.OpenChat(cepe.getFrom());
				chat.SendExtensionProtocolMessage(cepe.getFrom(), One2OneInfoConstants.USER_INFO,
						param);
				chat.CloseChat(cepe.getFrom());
			}
			if (cepe.getExtensionName().equals(One2OneInfoConstants.USER_INFO)) {
				String osName = (String) cepe.getExtensionParameter(One2OneInfoConstants.OS_NAME);
				String osVer = (String) cepe.getExtensionParameter(One2OneInfoConstants.OS_VER);
				showMessage("Extension Demo plugin", "OS: " + osName + " ver: "
						+ osVer);
				chat.CloseChat(cepe.getFrom());
			}
		}
	}

	private void showMessage(String windowTitle, String message) {
		UiPlugin.getUIHelper().showMessage(windowTitle, message);
	}
}
