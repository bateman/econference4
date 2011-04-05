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

import java.util.HashMap;

import org.junit.Test;

import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * jUnit test for <code>Buddy</code>.
 */
public class BuddyStateTest {
    /**
     * Check that two buddies are referring to the same event if they have a different resource field.
     */
    @Test
    public void testEquals() {
        IBuddyRoster roster = mock( IBuddyRoster.class );

        Buddy b1 = new Buddy( roster, "tester1@jabber.org","status tester1", "Smack" );
        Buddy b2 = new Buddy( roster, "tester1@jabber.org","status tester1", "Kopete" );

        assertEquals( b1, b2 );
    }

    /**
     * Check that we can put and retrieve buddies from collections, without caring about the resource field.
     */
    @Test
    public void testHash() {
        IBuddyRoster roster = mock( IBuddyRoster.class );

        HashMap<String, IBuddy> buddies = new HashMap<String, IBuddy>();

        Buddy b1 = new Buddy( roster, "tester1@jabber.org","status tester1", "Smack" );
        Buddy b2 = new Buddy( roster, "tester1@jabber.org","status tester1", "Kopete" );

        buddies.put( b1.getId(), b1 );
        buddies.put( b2.getId(), b2 );

        // Ensure that b2 replaced b1
        assertTrue( b2 == buddies.get( "tester1@jabber.org" ) );
        assertTrue( b1 != buddies.get( "tester1@jabber.org" ) );
    }

    /**
     * Check that the constructor properly separates id from resource.
     */
    @Test
    public void testConstructor() {
            IBuddyRoster roster = mock( IBuddyRoster.class );
            Buddy b1 = new Buddy( roster, "tester1@jabber.org/Smack" );
            Buddy b2 = new Buddy( roster, "tester1@jabber.org/Kopete" );
            Buddy b3 = new Buddy( roster, "tester1@jabber.org" );
            assertEquals( "tester1@jabber.org/Smack", b1.getId()  );
            assertEquals( "tester1@jabber.org", b1.getCleanJid()  );
            assertEquals( "Smack", b1.getResource() );
            assertEquals( "tester1@jabber.org/Kopete", b2.getId()  );
            assertEquals( "tester1@jabber.org", b2.getCleanJid()  );
            assertEquals( "Kopete", b2.getResource() );
            assertEquals( "tester1@jabber.org", b3.getId()  );
            assertEquals( "tester1@jabber.org", b3.getCleanJid()  );
            assertEquals( "", b3.getResource() );
    }
}
