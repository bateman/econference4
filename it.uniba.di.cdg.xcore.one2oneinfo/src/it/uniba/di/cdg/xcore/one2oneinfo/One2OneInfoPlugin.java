package it.uniba.di.cdg.xcore.one2oneinfo;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class One2OneInfoPlugin extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "it.uniba.di.cdg.xcore.one2oneinfo";

	// The shared instance
	private static One2OneInfoPlugin plugin;
	
	/**
	 * The constructor
	 */
	public One2OneInfoPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("One2OneInfoPlugin start..");
		plugin = this;
		new InfoDeamon().start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static One2OneInfoPlugin getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		
	}

}
