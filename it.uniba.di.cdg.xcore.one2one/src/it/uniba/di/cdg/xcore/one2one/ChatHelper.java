package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.call.CallEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.one2one.ChatManager.IChatStatusListener;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.IUIHelper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ChatHelper implements IChatHelper {

	/**
	 * Tracks the open chats (buddy id --> Chat object).
	 */
	public Map<String, ChatManager> openChats;
	protected final IUIHelper uihelper;
	protected final INetworkBackendHelper backendHelper;

	public ChatHelper(INetworkBackendHelper helper, IUIHelper uiHelper) {
		this.openChats = new HashMap<String, ChatManager>();
		this.uihelper = uiHelper;
		this.backendHelper = helper;
	}

	public void init() {
		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry()
				.getDescriptors())
			backendHelper.registerBackendListener(d.getId(), this);
	}

	public void dispose() {
		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry()
				.getDescriptors())
			backendHelper.unregisterBackendListener(d.getId(), this);
	}

	/**
	 * Open and display a new chat window for chatting with a buddy.
	 * 
	 * @param chatContext
	 */
	public void openChatWindow(ChatContext chatContext) {
		if (openChats.containsKey(chatContext.getBuddyId())) {
			return;
		}

		ChatManager chat = new ChatManager();
		chat.setBackendHelper(backendHelper);
		chat.setUihelper(uihelper);

		chat.addChatStatusListener(new IChatStatusListener() {

			@Override
			public void chatClosed(ChatManager chat) {
				openChats.remove(chat.getBuddyId());
			}

			@Override
			public void chatOpen(ChatManager chat) {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				Point size = shell.getSize();
				if (size.x < 640 || size.y < 480)
					shell.setSize(640, 480);
			}
		});
		// Track the chat if it open ok
		if (chat.open(chatContext))
			openChats.put(chatContext.getBuddyId(), chat);
	}

	@SwtAsyncExec
	public void onBackendEvent(final IBackendEvent event) {
		// Ask the user if he wants to chat with this user.
		if (event instanceof ChatMessageReceivedEvent) {
			final ChatMessageReceivedEvent chatMessageReceivedEvent = (ChatMessageReceivedEvent) event;

			final ChatContext chatContext = new ChatContext(
					// FIXME sometimes generates a NPE, see issue 48
					// http://code.google.com/p/econference4/issues/detail?id=48
					chatMessageReceivedEvent.getBuddy().getId(),
					new ChatMessage(
							chatMessageReceivedEvent.getBuddy().getId(),
							chatMessageReceivedEvent.getMessage()));
			openChatWindow(chatContext);
		}

		else if (event instanceof CallEvent) {
			CallEvent callEvent = (CallEvent) event;
			boolean res = uihelper.askYesNoQuestion("Call Request",
					"Do you want accept a call by " + callEvent.getFrom());
			IBackend backend = backendHelper.getRoster().getBackend();
			ICallAction callAction = backend.getCallAction();
			if (res) {
				callAction.acceptCall(callEvent.getFrom());
			} else {
				callAction.declineCall(callEvent.getFrom());
			}
		}
	}

}
