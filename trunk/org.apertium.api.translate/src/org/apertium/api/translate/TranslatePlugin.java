package org.apertium.api.translate;

import java.util.LinkedList;
import java.util.List;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;

import org.apertium.api.translate.listeners.TranslateListener;
import org.apertium.api.translate.views.TranslateView;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TranslatePlugin extends AbstractUIPlugin {

	public static final String ID = "org.apertium.api.translate";
	private static TranslatePlugin plugin;

	private List<IBackendEventListener> translateListeners = null;
	
	public TranslatePlugin() {
		System.out.println("TranslatePlugin()");
		plugin = this;
		
		translateListeners = new LinkedList<IBackendEventListener>();
		translateListeners.add(new TranslateListener());
	}
	
	public void addListener(IBackendEventListener listener) {
		translateListeners.add(listener);
		NetworkPlugin.getDefault().getHelper().registerBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("TranslatePlugin.start()");
		
		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin.getDefault().getHelper().registerBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
		}
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		System.out.println("TranslatePluginr.stop()");
		
		for (IBackendEventListener listener : translateListeners) {
			NetworkPlugin.getDefault().getHelper().unregisterBackendListener(NetworkPlugin.getDefault().getRegistry().getDefaultBackendId(), listener);
		}
	}

	public static TranslatePlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(ID, path);
	}
}
