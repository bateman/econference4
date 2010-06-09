/**
 * 
 */
package it.uniba.di.cdg.collaborativeworkbench.boot.test;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;


import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;



public class BackendEventListenerTest extends TestCase {

	/**
	 * Test method for {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#onBackendEvent(it.uniba.di.cdg.xcore.network.events.IBackendEvent)}.
	 */
	@Test
	public void testOnBackendEvent() {
		BackendEventListener daemon = new BackendEventListener();
		assertFalse(daemon.getMessageIncoming());
		daemon.onBackendEvent(new ChatMessageReceivedEvent(new TestBuddy(),"TestMessaggio","ID_TEST"));
		assertTrue(daemon.getMessageIncoming());
		
		daemon.setNewMessageIncoming(false);
		assertFalse(daemon.getMessageIncoming());
		daemon.onBackendEvent(new MultiChatMessageEvent("ID_TEST","Test_MultiChatMessage", "From: Test_Multichat"));
		assertTrue(daemon.getMessageIncoming());
		
	}

	/**
	 * Test method for {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#getMessageIncoming()}.
	 */
	@Test
	public void testGetMessageIncoming() {
		BackendEventListener daemon = new BackendEventListener();
		assertFalse(daemon.getMessageIncoming());
		daemon.setNewMessageIncoming(true);
		assertTrue(daemon.getMessageIncoming());
		daemon.setNewMessageIncoming(false);
		assertFalse(daemon.getMessageIncoming());
	}

	/**
	 * Test method for {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#setNewMessageIncoming(boolean)}.
	 */
	@Test
	public void testSetNewMessageIncoming() {
		BackendEventListener daemon = new BackendEventListener();
		daemon.setNewMessageIncoming(true);
		assertTrue(daemon.getMessageIncoming());
		daemon.setNewMessageIncoming(false);
		assertFalse(daemon.getMessageIncoming());

	}

	/**
	 * Test method for {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#start()}.
	 */
	@Test
	public void testStart() {
		TestNetworkPlugin testNetworkPlugin = new TestNetworkPlugin();
		
		assertTrue(testNetworkPlugin.getDefault() instanceof TestNetworkPlugin);
		assertTrue(testNetworkPlugin.getDefault().getRegistry() instanceof TestBackendRegistry);
		assertTrue(testNetworkPlugin.getDefault().getRegistry().getDescriptors() instanceof Collection<?>);
		 
	}

}
