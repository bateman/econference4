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
package it.uniba.di.cdg.jabber;

import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.IUserStatus;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.one2one.IChatService;
import it.uniba.di.cdg.jabber.JabberBackend;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.junit.Test;

/**
 * jUnit test for jabber backend. TODO Implement test cases using concrete
 * classes (for SMACK) mocks.
 */
public class JabberBackendTestCase extends MockObjectTestCase {
	private static final ServerContext GOOD_SERVER_CONTEXT = new ServerContext(
			"host@domain", 5222, false);

	private static final UserContext GOOD_USER_ACCOUNT = new UserContext(
			"user@domain", "password");

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test that the backend supports the capabilities we know it must support.
	 * In the future it is possibile that the jabber backend will have all of
	 * its services expressed as plug-in. FIXME This test excludes non-built-in
	 * capabilities :S
	 */
	public void testBuiltInCapabilities() {
		// Even the "bad" backend will be good for this test ok ;)
		final JabberBackend backend = createBackendWhichConnectsOk();

		ICapabilities capabilities = backend.getCapabilities();

		assertTrue(capabilities.contains(IChatService.CHAT_SERVICE));
	}

	/**
	 * Listeners should not get invoked if the connections fails.
	 */
	public void testNotNotifyConnectionFail() {
		// TODO Invent something ...
	}

	/**
	 * Verify that listeners get one and only one event when connection is
	 * closed.
	 */
	public void testNotifyConnectionOffline() {
		// Even the "bad" backend will be good for this test ok ;)
		// final JabberBackend backend = createBackendWhichConnectsOk();
		//
		// Mock mock = mock( IBackendStatusListener.class );
		// mock.expects( once() )
		// .method( "offline" ).with( same( backend ) );
		// IBackendStatusListener listener = (IBackendStatusListener)
		// mock.proxy();
		// backend.addConnectionStatusListener( listener );
		//
		// backend.connectionClosed();
	}

	/**
	 * If the backend is online and the buddy is right, it must be able to
	 * create a new chat service with a buddy present in the roster.
	 */
	// TODO attualmente l'implementazione di IChatService non esiste
	// TODO è gestito tutto da ChatManager
	// TODO quando sarà ricreata l'implementazione di IChat service
	// TODO si potrà riattivare questo cato di test

	/*
	 * public void testChatServiceCreationSuccess() { // Even the "bad" backend
	 * will be good for this test ok ;) final JabberBackend backend =
	 * createBackendWhichConnectsOk();
	 * 
	 * String buddyId = null; // TODO Create the buddy id
	 * IChatService.ChatContext context = new IChatService.ChatContext( buddyId
	 * );
	 * 
	 * IChatService chat = null; try { chat = (IChatService)
	 * backend.createService( IChatService.CHAT_SERVICE, context );
	 * assertNotNull( chat ); } catch (BackendException e) { fail(
	 * "Must be able to create a backend successfully!" ); } }
	 */
	/**
	 * If the connection is offline or the buddy is unknown or null, it must
	 * fail to create the chat.
	 */
	public void testChatServiceCreationFailure() {
		// Even the "bad" backend will be good for this test ok ;)
		createBackendWhichConnectsOk();

		// TODO
	}

	private JabberBackend createBackendWhichConnectsOk() {
		// We create a new backend, mocking the XMPP connection.
		return new JabberBackend() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.jabber.JabberBackend#createConnection(java.lang
			 * .String, int)
			 */
			@Override
			protected XMPPConnection createConnection(String host, int port,
					String serviceName) throws XMPPException {
				final Mock mock = mock(XMPPConnection.class);

				mock.expects(once())
				.method("login")
				.with(same(GOOD_SERVER_CONTEXT),
						same(GOOD_USER_ACCOUNT));

				mock.stubs().method("addConnectionListener");
				mock.stubs().method("addPacketListener");
				mock.stubs().method("getRoster").withNoArguments()
				.will(returnValue(createMockRoster()));

				return (XMPPConnection) mock.proxy();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * it.uniba.di.cdg.jabber.JabberBackend#createSecureConnection(java
			 * .lang.String, int)
			 */
			@Override
			protected XMPPConnection createSecureConnection(String host,
					int port, String serviceName) throws XMPPException {
				return createConnection(host, port, serviceName);
			}
		};
	}

	private Roster createMockRoster() {
		Mock mock = mock(Roster.class);
		// TODO Fill a list and return it ...
		return (Roster) mock.proxy();
	}

	/**
	 * Tests the jabber user status changes
	 */
	@Test
	public void testSetUserStatus() {

		JabberBackend backend = new JabberBackend();

		final ServerContext ugresServer = new ServerContext( "ugres.di.uniba.it", 5222 ,false );
		final UserContext testerContext = new UserContext( "tester3", "tester" );

		backend.setHelper(new NetworkBackendHelper());
		try {
			backend.connect(ugresServer, testerContext);
		} catch (BackendException e) {

			e.printStackTrace();
		}

		// case1 if we set the user status in Available
		backend.setUserStatus(IUserStatus.AVAILABLE);
		assertEquals(backend.getPresence().toString(), "available");

		// case2 if we set the user status in Away
		backend.setUserStatus(IUserStatus.AWAY);
		assertEquals(backend.getPresence().toString(), "away");

		// case2 if we set the user status in Busy
		backend.setUserStatus(IUserStatus.BUSY);
		assertEquals(backend.getPresence().toString(), "dnd");

	}

}
