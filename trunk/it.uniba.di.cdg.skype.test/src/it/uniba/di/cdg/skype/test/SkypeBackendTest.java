package it.uniba.di.cdg.skype.test;

import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

public class SkypeBackendTest extends MockObjectTestCase {

	public void testGetHelper() {
		SkypeBackend backend = new SkypeBackend();
		Mock mock = mock(INetworkBackendHelper.class);
		backend.setHelper((INetworkBackendHelper)
				mock.proxy());
		assertEquals(mock.proxy(), backend.getHelper());
	}

	public void testIsConnected() {
		SkypeBackend backend = new SkypeBackend();
	
		Mock mock = mock(INetworkBackendHelper.class);
		mock.stubs().method("notifyBackendEvent");
		
		backend.setHelper((INetworkBackendHelper) mock.proxy());
		
		try {
			assertFalse(backend.isConnected());
			backend.connect(null, null);
			assertTrue(backend.isConnected());
			backend.disconnect();
			assertFalse(backend.isConnected());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetBackendId() {
		SkypeBackend backend = new SkypeBackend();
		assertEquals("it.uniba.di.cdg.skype.skypeBackend",
				backend.getBackendId());
	}

	public void testGetUserId() {
		SkypeBackend backend = new SkypeBackend();
		assertEquals(backend.getUserId(),
				backend.getUserAccount().getId());
		
	}
}
