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
