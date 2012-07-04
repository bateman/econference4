package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.call.CallEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.one2one.ChatManager.IChatStatusListener;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.IUIHelper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ChatHelper implements IChatHelper {

	/**
	 * Tracks the open chats (buddy id --> Chat object).
	 */
	public Map<String, ChatManager> openChats;
	protected final IUIHelper uihelper;
	protected final INetworkBackendHelper backendHelper;
	private MessageDialog call_alert=null;

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

			IBuddy buddy = chatMessageReceivedEvent.getBuddy();
			if (null != buddy) {
				final ChatContext chatContext = new ChatContext(
						// FIXME sometimes generates a NPE, see issue 48
						// http://code.google.com/p/econference4/issues/detail?id=48
						buddy.getId(),
						new ChatMessage(buddy.getId(),
								chatMessageReceivedEvent.getMessage()));
				openChatWindow(chatContext);
			}
		}

		else if (event instanceof CallEvent) {

			final CallEvent callEvent = (CallEvent) event;
			if (!((CallEvent) event).getFrom().equals("conference")){
				String[] button = {"Yes","No"};
				call_alert = new MessageDialog(null, "Incoming Call", null, "Do you want accept call from "+callEvent.getFrom()+" ?", 0, button, 0);
				IBackend backend = backendHelper.getRoster().getBackend();
				final ICallAction callAction = backend.getCallAction();
				ringThread t = new ringThread();
				t.setUser(callEvent.getFrom());
				t.start();

				int res=call_alert.open();
				if (res==0) {
					callAction.acceptCall(callEvent.getFrom());
				} else {
					callAction.declineCall(callEvent.getFrom());
				}

			}else{

				boolean res = uihelper.askYesNoQuestion("Call Request",
						"Do you want join to conference?");
				IBackend backend = backendHelper.getRoster().getBackend();
				IMultiCallAction callAction = backend.getMultiCallAction();
				if (res) {
					callAction.acceptCall();
				} else {
					callAction.declineCall();
				}


			}
		}
	}


	class ringThread extends Thread {

		String user="";

		public void setUser(String user) {

			this.user=user;
		}

		public void run() {
			IBackend backend = backendHelper.getRoster().getBackend();
			ICallAction callAction = backend.getCallAction();
			while (callAction.isCalling(user)){

			}
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					call_alert.close();    
				}

			});
		}
	}


}
