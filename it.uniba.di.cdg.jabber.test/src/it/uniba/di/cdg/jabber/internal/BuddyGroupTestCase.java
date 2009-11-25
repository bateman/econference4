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
package it.uniba.di.cdg.jabber.internal;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * jUnit test case for the buddy group.
 */
public class BuddyGroupTestCase extends MockObjectTestCase {
    
    private IBuddyGroup group;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.group = new BuddyGroup( "friends" );
    }

    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyGroup.getName()'
     */
    public void testGetName() {
        IBuddyGroup aGroup = new BuddyGroup( "group" );
        
        assertEquals( "group", aGroup.getName() );
    }

    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyGroup.addBuddy(IBuddy)'
     */
    public void testAddContainsBuddy() {
        Mock mock = mock( IBuddy.class );
        IBuddy buddy = (IBuddy) mock.proxy();
        
        group.addBuddy( buddy );
        
        assertTrue( group.contains( buddy ) );
    }

    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyGroup.removeBuddy(IBuddy)'
     */
    public void testRemoveBuddy() {
        Mock mock = mock( IBuddy.class );
        IBuddy buddy = (IBuddy) mock.proxy();
        
        group.addBuddy( buddy );
        group.removeBuddy( buddy );

        assertFalse( group.contains( buddy ) );
    }
}
