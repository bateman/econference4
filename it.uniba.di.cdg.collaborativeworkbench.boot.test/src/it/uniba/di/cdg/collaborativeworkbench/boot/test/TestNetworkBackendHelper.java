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

import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

import java.util.List;

public class TestNetworkBackendHelper implements TestINetworkBackendHelper {
	TestBackendRegistry testBackendRegistry;
	@Override
	public List<IBackendDescriptor> getOnlineBackends() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestBackendRegistry getRegistry() {
		// TODO Auto-generated method stub
		return testBackendRegistry;
	}

	@Override
	public IBuddyRoster getRoster() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBackendOnline(String backendId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyBackendEvent(IBackendEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerBackendListener(String backendId,
			BackendEventListenerTest backendEventListenerTest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerBackendListener(IBackendEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRegistry(TestBackendRegistry registry) {
		// TODO Auto-generated method stub
		testBackendRegistry = registry;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterBackendListener(String backendId,
			IBackendEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterBackendListener(IBackendEventListener listener) {
		// TODO Auto-generated method stub
		
	}

}
