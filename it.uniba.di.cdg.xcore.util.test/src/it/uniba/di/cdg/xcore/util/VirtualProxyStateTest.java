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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * jUnit test for the <code>VirtualProxy</code> facility.
 * 
 * We try to proxy a dummy interface and its implementation and 
 * check that it works ok. 
 */
public class VirtualProxyStateTest {
    // Interface shared among the dynamic proxy and the real implementation
    public static interface IProxyWannabe {
        void costlyMethod1();
        int getDummy();
        Object costlyMethod2();
    }
    
    // This can be proxied ok (implements the right interface
    public static class ProxyWannabe implements IProxyWannabe {
        private int dummy;
        
        public void costlyMethod1() {
            dummy = 1;
        }
        
        public Object costlyMethod2() {
            return new Object();
        }

        public int getDummy() {
            return dummy;
        }
    }
    
    // This doesn't implement the interface so should not be proxied ...
    public static class BadProxyWannabe {}
    
    /**
     * Check that proxy works when on-demand instantiation creates
     * an object which implement the right interface.
     */
    @Test
    public void testProxyWorksOk() {
        IConfigurationElement mockCfgElement = mock( IConfigurationElement.class );
                
        try {
            when(mockCfgElement.createExecutableExtension( eq( VirtualProxyFactory.CLASS_ATTR ) )).thenReturn( new ProxyWannabe() );
        } catch (CoreException e) { }
        
        IProxyWannabe proxy = VirtualProxyFactory.getProxy( IProxyWannabe.class, 
                mockCfgElement );
        
        assertEquals( 0, proxy.getDummy() );
        proxy.costlyMethod1();
        assertEquals( 1, proxy.getDummy() );
        assertNotNull( proxy.costlyMethod2() );       
        
    }

    /**
     * Proxy should check that the interface is implemented by the 
     * real instance when it has been instantiated.
     */
    @Test
    public void testProxyWorksWithBadInterface() {
        IConfigurationElement mockCfgElement = mock( IConfigurationElement.class );
                
        try {
            when(mockCfgElement.createExecutableExtension( eq( VirtualProxyFactory.CLASS_ATTR ) )).thenReturn( new BadProxyWannabe() );
        } catch (CoreException e) { }
        
        IProxyWannabe proxy = VirtualProxyFactory.getProxy( IProxyWannabe.class, 
                mockCfgElement );

        try {
            proxy.costlyMethod1();
            fail( "Proxy doesn't fail with a bad interface" );
        } catch (ClassCastException e) {
          
        }
    }
}