package org.apertium.api.translate;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "translate";
	private static Activator plugin;

	public Activator() {
		System.out.println("Activator()");
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("Activator.start()");
		
		NetworkPlugin.getDefault().getHelper();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		System.out.println("Activator.stop()");
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
