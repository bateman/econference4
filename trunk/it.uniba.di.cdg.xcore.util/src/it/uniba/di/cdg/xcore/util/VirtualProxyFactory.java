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
package it.uniba.di.cdg.xcore.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * A Factory for building virtual proxies, that is proxies which delays the instantiation of costly
 * objects until their services are really needed.
 * 
 * This code is tailored on Rich Client Platform approach to delayed instantiation of callback
 * objects.
 */
public class VirtualProxyFactory {
    /**
     * Attribute in <code>IConfigurationElement</code> that must be used for retrieving the
     * fully-qualified classname to instance.
     */
    public static final String CLASS_ATTR = "class";

    /**
     * A (dynamic) virtual proxy instance delays the creation of costly objects.
     * 
     * This code uses the Java Reflection API to perform the automatic generation of such proxies
     * and generics to abstract the particular interface shared by the proxy and the real
     * implementation.
     */
    private static class VirtualProxy<Interface> implements InvocationHandler {
        /** 
         * Stores all the information needed to instance the callback. 
         */
        private final IConfigurationElement configurationElement;

        /** 
         * The required interfaces that the real implementation must support. 
         */
        private final Class[] neededInterfaces;
        
        /** 
         * Reference to the real callback object. 
         */
        private Interface delegate = null;

        /**
         * Creates a new virtual proxy: for type-safe creation, a list of interface is needed so it
         * is possible to check for mismatches between what the extension point needs in terms of
         * interfaces and what the instantiated callback object really implements.
         * 
         * @param configurationElement
         *        the configuration element which callback object is being proxied.
         * @param neededInterfaces
         *        a list of interfaces that the callback object must support
         */
        public VirtualProxy( final IConfigurationElement configurationElement, final Class[] neededInterfaces ) {
            super();
            this.configurationElement = configurationElement;
            this.neededInterfaces = neededInterfaces;
        }

        /**
         * Grab a reference to the real implementation and execute the method.
         * 
         * @param proxy
         *        the proxy object the method is invoked on
         * @param method
         *        the actual method being invoked
         * @param args
         *        the method's arguments
         */
        public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
            getDelegate();

            return method.invoke( delegate, args );
        }

        /**
         * Performs the on-demand construction of the the real object so that the method invocations
         * can be delegated to it.
         * 
         * @throws Exception
         */
        @SuppressWarnings( "unchecked" )
        private void getDelegate() throws Exception {
            if (delegate != null)
                return;
            Object callback;
            try {
                callback = configurationElement.createExecutableExtension( CLASS_ATTR );
            } catch (CoreException e) {
                // TODO This may be a good place to log these exceptions ...
                e.printStackTrace();
                throw e;
            }
            checkInterfaces( callback );

            delegate = (Interface) callback;
        }

        /**
         * Check that all the required interfaces are implemented by the callback object.
         * 
         * @param callback
         */
        @SuppressWarnings("unchecked")
        private void checkInterfaces( Object callback ) {
            for (int i = 0; i < neededInterfaces.length; i++) {
                final Class itf = neededInterfaces[i];

                if (!itf.isAssignableFrom( callback.getClass() )) {
                    throw new ClassCastException( callback.getClass()
                            + " doesn't implement interface " + itf );
                }
            }
        }
    }

    /**
     * Create a virtual proxy which supports a specific interface.
     * 
     * @param intf
     *        interface to be supported
     * @param cfgElement
     *        provides information about the callback
     * @return a virtual proxy instance.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxy( Class<T> intf, IConfigurationElement cfgElement ) {
        Class[] neededInterfaces = new Class[] { intf };
        return (T) Proxy.newProxyInstance( intf.getClassLoader(),
                neededInterfaces, new VirtualProxy<T>( cfgElement, neededInterfaces ) );
    }
}
