package org.apertium.api.translate;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;

import org.apertium.api.translate.listeners.TranslateListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TranslatePlugin extends AbstractUIPlugin {

	public static final String ID = "org.apertium.api.translate";
	private List<IBackendEventListener> translateListeners = null;
	
	private static TranslatePlugin plugin = null;
	//private static TranslateHelper translateHelper = null;
	
	public TranslatePlugin() {
		System.out.println("TranslatePlugin()");
		plugin = this;
		
		//translateHelper = new TranslateHelper();
		
		translateListeners = new LinkedList<IBackendEventListener>();
		translateListeners.add(new TranslateListener());
	}
	
	public void addListener(IBackendEventListener listener) {
		translateListeners.add(listener);
		NetworkPlugin.getDefault().getHelper().registerBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
	}
	
	public void removeListener(IBackendEventListener listener) {
		translateListeners.remove(listener);
		NetworkPlugin.getDefault().getHelper().unregisterBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("TranslatePlugin.start()");
		
		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin.getDefault().getHelper().registerBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
		}
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("TranslatePlugin.stop()");
		
		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin.getDefault().getHelper().unregisterBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
		}
		
		plugin = null;
		super.stop(context);
	}

	public static TranslatePlugin getDefault() {
		return plugin;
	}

	//public TranslateHelper getTranslateHelper() {
	//	return translateHelper;
	//}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(ID, path);
	}
}
