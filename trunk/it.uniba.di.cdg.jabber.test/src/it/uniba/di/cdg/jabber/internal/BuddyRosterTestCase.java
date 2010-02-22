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

import java.util.List;

import it.uniba.di.cdg.jabber.JabberBackend;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.IBackendRegistry;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * jUnit test case for the buddy group.
 */
public class BuddyRosterTestCase extends MockObjectTestCase {
    
    private BuddyRoster roster;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

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
    public void testAddContainsBuddy() {       
    	String[] gruppi = {"None"};
        roster.addBuddy("alessandrob@jabber.org", "alessandrob", gruppi );        
        assertTrue( roster.contains("alessandrob@jabber.org") );

    }
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.addBuddy(String id, String name, String[] gruppi)'
     */
    public void testEqualBuddy() {  
    	String[] gruppi = {"None"};
    	roster.addBuddy("alessandrob@jabber.org", "alessandrob", gruppi);  
        
        assertEquals("alessandrob@jabber.org", roster.getBuddy("alessandrob@jabber.org").getId());
    }
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.removeBuddy(String id)'
     */
    public void testEqualNameBuddy() {  
    	roster.addGroup("prova");
    	String[] gruppi = {"prova"};
    	roster.addBuddy("alessandrob@jabber.org", "alessandrob", gruppi);
    	roster.renameBuddy("alessandrob@jabber.org", "ale");
    	roster.reload();
        assertEquals("ale" , roster.getBuddy("alessandrob@jabber.org").getName());
    }
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.removeBuddy(String id)'
     */
    public void testRemoveBuddy() {  
    	roster.removeBuddy("alessandrob@jabber.org");
        
        assertFalse( roster.contains("alessandrob@jabber.org"));
    }
    
    
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.moveToGroup(String id, string newgroup)'
     */
    public void testMoveBuddy() {  
    	String[] gruppi = {"None"};
    	roster.addBuddy("alessandrob@jabber.org", "alessandrob", gruppi); 
    	roster.addGroup("prova");
    	roster.moveToGroup("alessandrob@jabber.org", "prova");
    	IBuddyGroup group = (IBuddyGroup) roster.getGroups(roster.getBuddy("alessandrob@jabber.org")).iterator().next();
        assertEquals("prova", group.getName());
    }
    
    /*
     * Test method for 'net.osslabs.jabber.client.model.BuddyRoster.moveToGroup(String id, string newgroup)'
     */
    public void testMoveBuddyNoGroup() {  
    	String[] gruppi = {"prova"};
    	roster.addBuddy("alessandrob@jabber.org", "alessandrob", gruppi); 
    	roster.moveToGroup("alessandrob@jabber.org", "None");
        assertTrue(roster.getGroups(roster.getBuddy("alessandrob@jabber.org")).isEmpty());
    }

    
}
