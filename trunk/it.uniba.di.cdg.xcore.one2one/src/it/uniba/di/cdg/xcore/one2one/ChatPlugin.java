package it.uniba.di.cdg.xcore.one2one;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.one2one.IChatService.ChatContext;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChatPlugin extends Plugin implements IStartup {
	
	/**
	 * Plug-in id.
	 */
	public static final String ID = "it.uniba.di.cdg.xcore.one2one";

	// The shared instance.
	private static ChatPlugin plugin;

	private IChatHelper helper;

	private static boolean isSet = false;
	
	/**
	 * Tracks the open chats (buddy id --> Chat object).
	 */
	/*
	 * private Map<String, ChatManager> openChats;
	 */

	/**
	 * The constructor.
	 */
	public ChatPlugin() {
		if (!isSet) { 			
			// this.openChats = new HashMap<String, ChatManager>();
			plugin = this;
			helper = new ChatHelper(NetworkPlugin.getDefault().getHelper(),
					UiPlugin.getUIHelper());
			isSet = true;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("ChatUiPlugin.start()");
		super.start(context);
		// Code for cicle is moved in ChatHelper.init()
		helper.init();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println( "ChatUiPlugin.stop()" );
		super.stop(context);
		// Code is moved in ChatHelper.dispose()
		helper.dispose();
		plugin = null;
		isSet = false;
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
		helper.openChatWindow(chatContext);
	}

	/**
	 * Returns the helper for this plug-in.
	 * 
	 * @return
	 */
	public IChatHelper getHelper() {
		return helper;
	}

	/**
	 * Set the helper for this plug-in.
	 * 
	 * @param helper
	 * 
	 */
	public void setHelper(IChatHelper helper) {

		if (this.helper != null)
			this.helper.dispose();
		this.helper = helper;
		this.helper.init();
	}

	@Override
	public void earlyStartup() {

	}
}
