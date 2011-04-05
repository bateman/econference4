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


import org.junit.Before;
import org.junit.Test;

import it.uniba.di.cdg.jabber.JabberBackend;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import static org.junit.Assert.*;

/**
 * jUnit test case for the buddy group.
 */
public class BuddyRosterStateTest {
    
    private BuddyRoster roster;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        JabberBackend jbackend = new JabberBackend();
        UserContext userc = new UserContext( "participant1", "participant1" );
        ServerContext ctx = new ServerContext( "jabber.org", 5222 ,false );
        NetworkBackendHelper helper = new NetworkBackendHelper();
        jbackend.setHelper(helper);
        jbackend.connect(ctx, userc);
        this.roster = new BuddyRoster( jbackend );
        this.roster.setJabberRoster(jbackend.getConnection().getRoster());
        }

    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.addBuddy(String id, String name, String[] gruppi)'
     */
    @Test
    public void testAddContainsBuddy() {       
        String[] gruppi = {"None"};
        roster.addBuddy("giuseppe83@jabber.org", "giuseppe83", gruppi );        
        assertTrue( roster.contains("giuseppe83@jabber.org") );

    }
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.addBuddy(String id, String name, String[] gruppi)'
     */
    @Test
    public void testEqualBuddy() {  
        String[] gruppi = {"None"};
        roster.addBuddy("giuseppe83@jabber.org", "giuseppe83", gruppi);  
        assertEquals("giuseppe83@jabber.org", roster.getBuddy("giuseppe83@jabber.org").getId());
    }
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.removeBuddy(String id)'
     */
    @Test
    public void testEqualNameBuddy() {  
        roster.addGroup("prova");
        String[] gruppi = {"prova"};
        roster.addBuddy("giuseppe83@jabber.org", "giuseppe83", gruppi);
        roster.renameBuddy("giuseppe83@jabber.org", "ale");
        roster.reload();
        assertEquals("ale" , roster.getBuddy("giuseppe83@jabber.org").getName());
    }
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.removeBuddy(String id)'
     */
    @Test
    public void testRemoveBuddy() {  
        roster.removeBuddy("giuseppe83@jabber.org");
        
        assertFalse( roster.contains("giuseppe83@jabber.org"));
    }
    
    
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.moveToGroup(String id, string newgroup)'
     */
    @Test
    public void testMoveBuddy() {  
        String[] gruppi = {"None"};
        roster.addBuddy("giuseppe83@jabber.org", "giuseppe83", gruppi); 
        roster.addGroup("prova");
        roster.moveToGroup("giuseppe83@jabber.org", "prova");
        IBuddyGroup group = (IBuddyGroup) roster.getGroups(roster.getBuddy("giuseppe83@jabber.org")).iterator().next();
        assertEquals("prova", group.getName());
    }
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.moveToGroup(String id, string newgroup)'
     */
    @Test
    public void testMoveBuddyNoGroup() {  
        String[] gruppi = {"prova"};
        roster.addBuddy("giuseppe83@jabber.org", "giuseppe83", gruppi); 
        roster.moveToGroup("giuseppe83@jabber.org", "None");
        assertTrue(roster.getGroups(roster.getBuddy("giuseppe83@jabber.org")).isEmpty());
    }

    
}
