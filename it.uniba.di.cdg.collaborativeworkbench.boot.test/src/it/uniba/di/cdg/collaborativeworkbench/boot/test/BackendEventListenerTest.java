/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package it.uniba.di.cdg.collaborativeworkbench.boot.test;

import it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;

public class BackendEventListenerTest extends TestCase {

	/**
	 * Test method for
	 * {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#onBackendEvent(it.uniba.di.cdg.xcore.network.events.IBackendEvent)}
	 * .
	 */
	@Test
	public void testOnBackendEvent() {
		BackendEventListener daemon = new BackendEventListener();
		assertFalse(daemon.getIncomingMessage());
		daemon.onBackendEvent(new ChatMessageReceivedEvent(new TestBuddy(),
				"TestMessaggio", "ID_TEST"));
		assertTrue(daemon.getIncomingMessage());

		daemon.setNewMessageIncoming(false);
		assertFalse(daemon.getIncomingMessage());
		daemon.onBackendEvent(new MultiChatMessageEvent("ID_TEST",
				"Test_MultiChatMessage", "From: Test_Multichat"));
		assertTrue(daemon.getIncomingMessage());

	}

	/**
	 * Test method for
	 * {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#getMessageIncoming()}
	 * .
	 */
	@Test
	public void testGetMessageIncoming() {
		BackendEventListener daemon = new BackendEventListener();
		assertFalse(daemon.getIncomingMessage());
		daemon.setNewMessageIncoming(true);
		assertTrue(daemon.getIncomingMessage());
		daemon.setNewMessageIncoming(false);
		assertFalse(daemon.getIncomingMessage());
	}

	/**
	 * Test method for
	 * {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#setNewMessageIncoming(boolean)}
	 * .
	 */
	@Test
	public void testSetNewMessageIncoming() {
		BackendEventListener daemon = new BackendEventListener();
		daemon.setNewMessageIncoming(true);
		assertTrue(daemon.getIncomingMessage());
		daemon.setNewMessageIncoming(false);
		assertFalse(daemon.getIncomingMessage());

	}

	/**
	 * Test method for
	 * {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#getNumberOfOngoingChats(int)}
	 * .
	 */
	public void testGetNumberOfOngoingChats() {
		BackendEventListener daemon = new BackendEventListener();
		assertEquals(0, daemon.getNumberOfOngoingChats());
		TestBuddy tester1 = new TestBuddy();
		TestBuddy tester2 = new TestBuddy();
		tester1.setId("1");
		tester2.setId("2");
		daemon.onBackendEvent(new ChatMessageReceivedEvent(tester1,
				"TestMessaggio", "ID_TEST"));
		assertEquals(1, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new ChatMessageReceivedEvent(tester1,
				"TestMessaggio", "ID_TEST"));
		assertEquals(1, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new ChatMessageReceivedEvent(tester2,
				"TestMessaggio", "ID_TEST"));
		assertEquals(2, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new ChatMessageReceivedEvent(tester1,
				"TestMessaggio", "ID_TEST"));
		assertEquals(2, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new MultiChatMessageEvent("ID_TEST",
				"Test_MultiChatMessage", "From: Test_Multichat"));
		assertEquals(3, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new MultiChatMessageEvent("ID_TEST",
				"Test_MultiChatMessage", "From: Test_Multichat"));
		assertEquals(3, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new MultiChatMessageEvent("ID_TEST1",
				"Test_MultiChatMessage", "From: Test_Multichat"));
		assertEquals(4, daemon.getNumberOfOngoingChats());
		daemon.onBackendEvent(new MultiChatMessageEvent("ID_TEST",
				"Test_MultiChatMessage", "From: Test_Multichat"));
		assertEquals(4, daemon.getNumberOfOngoingChats());
	}

	/**
	 * Test method for
	 * {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#emptyHistory(void)}
	 * .
	 */
	public void testEmptyHistory() {
		BackendEventListener daemon = new BackendEventListener();
		daemon.onBackendEvent(new ChatMessageReceivedEvent(new TestBuddy(),
				"TestMessaggio", "ID_TEST"));
		assertEquals(1, daemon.getNumberOfOngoingChats());
		daemon.emptyHistory();
		assertEquals(0, daemon.getNumberOfOngoingChats());
		// TODO history is emptied when the eC windows gets the focus
		// this behavior should be more properly tested using mock test
	}

	/**
	 * Test method for
	 * {@link it.uniba.di.cdg.collaborativeworkbench.boot.ui.BackendEventListener#start()}
	 * .
	 */
	@Test
	public void testStart() {
		TestNetworkPlugin testNetworkPlugin = new TestNetworkPlugin();

		assertTrue(testNetworkPlugin.getDefault() instanceof TestNetworkPlugin);
		assertTrue(testNetworkPlugin.getDefault().getRegistry() instanceof TestBackendRegistry);
		assertTrue(testNetworkPlugin.getDefault().getRegistry()
				.getDescriptors() instanceof Collection<?>);

	}

}
