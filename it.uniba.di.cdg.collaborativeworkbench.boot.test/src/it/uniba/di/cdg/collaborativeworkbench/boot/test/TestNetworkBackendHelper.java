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
