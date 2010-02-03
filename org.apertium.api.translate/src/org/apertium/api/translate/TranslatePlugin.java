package org.apertium.api.translate;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;

import org.apertium.api.translate.listeners.TranslateListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TranslatePlugin extends AbstractUIPlugin {

	public static final String ID = "org.apertium.api.translate";
	private static TranslatePlugin plugin;

	private IBackendEventListener translateListener = null;
	
	public TranslatePlugin() {
		System.out.println("Activator()");
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("Activator.start()");
		
		translateListener = new TranslateListener();
		
		NetworkPlugin.getDefault().getHelper().registerBackendListener(translateListener);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		System.out.println("Activator.stop()");
		
		NetworkPlugin.getDefault().getHelper().unregisterBackendListener(translateListener);
	}

	public static TranslatePlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(ID, path);
	}
}
