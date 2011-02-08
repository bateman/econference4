package it.uniba.di.cdg.collaborativeworkbench.boot.test;

import java.util.ArrayList;
import java.util.Collection;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.util.IExtensionProcessor;

public class TestBackendRegistry implements TestIBackendRegistry {
	
	ArrayList<IBackendDescriptor> descriptors = new ArrayList<IBackendDescriptor>();
	
	public TestBackendRegistry(TestINetworkBackendHelper testBackendHelper) {
		// TODO Auto-generated constructor stub
	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public IBackend getBackend(String backendId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IBackend> getBackends() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBackend getDefaultBackend() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultBackendId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBackendDescriptor getDefaultDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBackendDescriptor getDescriptor(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void processExtensions(IExtensionProcessor processor)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public Collection<IBackendDescriptor> getDescriptors() {
		// TODO Auto-generated method stub
		return descriptors;
	}

}
