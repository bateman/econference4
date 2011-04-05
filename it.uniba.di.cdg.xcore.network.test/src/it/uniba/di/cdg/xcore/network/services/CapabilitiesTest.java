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
package it.uniba.di.cdg.xcore.network.services;

import it.uniba.di.cdg.xcore.network.services.Capabilities;
import it.uniba.di.cdg.xcore.network.services.Capability;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.network.services.ICapability;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;


/**
 * jUnit test case for <code>Capabilities</code>.
 */
public class CapabilitiesTest extends TestCase {

    private ICapabilities capabilities;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        this.capabilities = new Capabilities();
    }

    public void testAddCapability() {
        final ICapability c = new Capability( "cap1" );
        capabilities.add( c );
        
        assertTrue( capabilities.contains( c ) );
    }

    public void testHasCapabilities() {
        assertFalse( capabilities.hasCapabilities() );
        
        capabilities.add( new Capability( "new" ) );
        
        assertTrue( capabilities.hasCapabilities() );
    }
    
    public void testRemoveCapability() {
        final ICapability c = new Capability( "cap1" );
        capabilities.add( c );

        capabilities.remove( c );
        assertFalse( capabilities.contains( c ) );
    }
    
    public void testIterator() {
        Set<ICapability> knownCaps = new HashSet<ICapability>(); 
        
        for (int i = 0; i < 5; i++) {
            ICapability c = new Capability( "cap" + i );
            capabilities.add( c );
            knownCaps.add( c );
        }
        
        for (ICapability c : capabilities) {
            assertTrue( knownCaps.contains( c ) );
        }
    }
}
