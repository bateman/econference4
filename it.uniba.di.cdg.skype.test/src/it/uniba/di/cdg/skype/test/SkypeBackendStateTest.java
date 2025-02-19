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

import org.junit.Test;

import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SkypeBackendStateTest {
    @Test
    public void testGetHelper() {
        SkypeBackend backend = new SkypeBackend();
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        backend.setHelper(backendHelper);
        assertEquals(backendHelper, backend.getHelper());
    }
    @Test
    public void testIsConnected() {
        SkypeBackend backend = new SkypeBackend();

        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
                
        backend.setHelper(backendHelper);
        
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

    @Test
    public void testGetBackendId() {
        SkypeBackend backend = new SkypeBackend();
        assertEquals("it.uniba.di.cdg.skype.skypeBackend",
                backend.getBackendId());
    }

    @Test
    public void testGetUserId() {
        SkypeBackend backend = new SkypeBackend();
        assertEquals(backend.getUserId(),
                backend.getUserAccount().getId());
        
    }
}