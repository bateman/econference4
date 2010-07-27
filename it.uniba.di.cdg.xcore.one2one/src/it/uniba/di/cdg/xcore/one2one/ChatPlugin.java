package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.xcore.aspects.SwtAsyncExec;
import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.call.CallEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.one2one.ChatManager.IChatStatusListener;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChatPlugin extends Plugin implements IBackendEventListener {
	/**
	 * Plug-in id.
	 */
	public static final String ID = "it.uniba.di.cdg.xcore.one2one";

	// The shared instance.
	private static ChatPlugin plugin;

	/**
	 * Tracks the open chats (buddy id --> Chat object).
	 */
	private Map<String, ChatManager> openChats;


	/**
	 * The constructor.
	 */
	public ChatPlugin() {
		this.openChats = new HashMap<String, ChatManager>();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println( "ChatUiPlugin.start()" );
		super.start(context);

		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry()
				.getDescriptors())
			NetworkPlugin.getDefault().getHelper().registerBackendListener(
					d.getId(), this);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		// System.out.println( "ChatUiPlugin.stop()" );
		super.stop(context);

		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry()
				.getDescriptors())
			NetworkPlugin.getDefault().getHelper().unregisterBackendListener(
					d.getId(), this);

		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChatPlugin getDefault() {
		return plugin;
	}

	/**
	 * Open and display a new chat window for chatting with a buddy.
	 * 
	 * @param chatContext
	 */
	public void openChatWindow(ChatContext chatContext) {
		if (openChats.containsKey(chatContext.getBuddyId())) {
			// System.err.println( "You are already chatting with " +
			// chatContext.getBuddyId() );
			return;
		}

		ChatManager chat = new ChatManager();
		chat.setBackendHelper(NetworkPlugin.getDefault().getHelper());
		chat.setUihelper(UiPlugin.getUIHelper());

		chat.addChatStatusListener(new IChatStatusListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.xcore.one2one.ui.Chat.IChatStatusListener#closed
			 * (it.uniba.di.cdg.xcore.one2one.ui.Chat)
			 */
			public void chatClosed(ChatManager chat) {
				openChats.remove(chat.getBuddyId());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.xcore.one2one.ui.Chat.IChatStatusListener#open(it
			 * .uniba.di.cdg.xcore.chat.ui.Chat)
			 */
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

	/**
	 * Track the incoming chat messages: warn the user about a new chat message
	 * if it belongs to a chat not already open.
	 * 
	 * @param event
	 *            the backend event (we are interested only in {@see ChatEvent}s
	 *            here)
	 */
	@SwtAsyncExec
	public void onBackendEvent(final IBackendEvent event) {
		// Ask the user if he wants to chat with this user.
		if (event instanceof ChatMessageReceivedEvent) {
			final ChatMessageReceivedEvent chatMessageReceivedEvent = (ChatMessageReceivedEvent) event;

			final ChatContext chatContext = new ChatContext(
					// TODO sometimes generates a NPE
					chatMessageReceivedEvent.getBuddy().getId(),
					new ChatMessage(chatMessageReceivedEvent.getBuddy().getId(), chatMessageReceivedEvent.getMessage()));
			openChatWindow(chatContext);
		}
		
		else if(event instanceof CallEvent){
			CallEvent callEvent = (CallEvent)event;
			boolean res = UiPlugin.getUIHelper().askYesNoQuestion(
					"Call Request",
					"Do you want accept a call by " + callEvent.getFrom());
			IBackend backend = NetworkPlugin.getDefault().getHelper().getRoster().getBackend();
			ICallAction callAction = backend.getCallAction();
			if(res){
				callAction.acceptCall(callEvent.getFrom());
			}else{
				callAction.declineCall(callEvent.getFrom());
			}
		}
	}
}
