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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * jUnit test case for the <code>Memento</code> implementation.
 */
public class MementoTestCase extends TestCase {

    private IMemento memento;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        memento = new Memento();
    }

    public void testStoreStrings() {
        final String s1 = new String( "a string" );
        final String s2 = new String( "another string" ); 
        
        final String key1 = "key1";
        final String key2 = "key2";
        
        memento.putString( key1, s1 );
        memento.putString( key2, s2 );
        
        assertEquals( "a string", memento.getString( key1 ) );
        assertEquals( "another string", memento.getString( key2 ) );
    }
    
    public void testStoreObjects() {
        Set<Object> set = new HashSet<Object>();
        Object obj = new Object();
        
        memento.putObject( "set", set );
        memento.putObject( "obj", obj );
        
        assertSame( set, memento.getObject( "set" ) );
        assertSame( obj, memento.getObject( "obj" ) );
    }
}
