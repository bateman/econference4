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
package it.uniba.di.cdg.skype.test;

import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.IUserStatus;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.junit.Test;

import com.skype.Skype;
import com.skype.SkypeException;

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
	
	/**
	 * Tests the Skype user status changes
	 */
	@Test
	public void testSetUserStatus() throws SkypeException, InterruptedException{
		SkypeBackend backend = new SkypeBackend();

		//case1 if we set the user status in Available
		backend.setUserStatus(IUserStatus.AVAILABLE);
		Thread.sleep(2000);
		assertEquals(Skype.getProfile().getStatus().toString(),"ONLINE");
	
		//case2 if we set the user status in Away
		backend.setUserStatus(IUserStatus.AWAY);
		Thread.sleep(2000);
		assertEquals(Skype.getProfile().getStatus().toString(),"AWAY");
			
		//case3 if we set the user status in Busy
		backend.setUserStatus(IUserStatus.BUSY);
		Thread.sleep(2000);
		assertEquals(Skype.getProfile().getStatus().toString(),"DND");

	}
}
