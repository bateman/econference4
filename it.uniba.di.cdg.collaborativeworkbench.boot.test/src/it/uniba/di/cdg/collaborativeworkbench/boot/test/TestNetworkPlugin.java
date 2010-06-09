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

import org.osgi.framework.BundleContext;

/**
 * The network plug-ins handles zero, one or more network backends. 
 * <p>
 * It provides a set of essential or useful features that otherwise, other plug-ins should care about themselves:
 * <ul>
 * <li><b>Major task]</b>: it tracks down the backend event listeners and acts as an event dispatcher for them. This is 
 * required since we want to implement lazy plug-in activation and don't want the the 
 * unused backends to be waked up without having nothing to do for them.</li> 
 * <li><b>Minor task</b> it tracks the backends online / offline status and let other clients to query it</li>
 * </ul>
 * This is required since we want to implement lazy plug-in activation and don't want the the 
 * unused backends to be waked up without having nothing to do for them.
 */
public class TestNetworkPlugin  {
    /**
     * The unique id for this backends.
     */
    public static final String ID = "test.it.uniba.di.cdg.xcore.network"; 
    
	//The shared instance.
	private static TestNetworkPlugin plugin;

    /**
     * Helper functions.
     */
    private final TestINetworkBackendHelper testBackendHelper;
    
	/**
	 * The constructor.
	 */
	public TestNetworkPlugin() {
		plugin = this;
        this.testBackendHelper = new TestNetworkBackendHelper();
        
        testBackendHelper.setRegistry( new TestBackendRegistry( testBackendHelper ) );

	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		
	}

	/**
	 * Returns the shared instance.
	 */
	public TestNetworkPlugin getDefault() {
		return plugin;
	}

    /**
     * Returns the helper for this plugin.
     *  
     * @return
     */
    public TestINetworkBackendHelper getHelper() {
        return testBackendHelper;
    }
    
    /**
     * Returns the network backends registry.
     *  
     * @return Returns the registry.
     */
    public TestBackendRegistry getRegistry() {
        return getHelper().getRegistry();
    }
}
