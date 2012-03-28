package it.uniba.di.cdg.xcore.ui.views;
/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2012 Collaborative Development Group - Dipartimento di Informatica, 
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import it.uniba.di.cdg.jabber.internal.Buddy;

import org.junit.Before;
import org.junit.Test;

public class BuddyListViewTest {
    // added dependency for plugin xcg.jabber in order to istantiate the Buddy obj.
    Buddy buddytest = null;

    @Before
    public void setUp() throws Exception {
        buddytest = new Buddy( null, "idpippo@gmail.com/blabla", "pippo", "statuspippo" );
    }

    @Test
    public void testAddBuddy() {
        BuddyListView.addBuddy( "pippo", "pippourl" );
        assertEquals( "pippourl", BuddyListView.getBuddy( "pippo" ) );
    }

    @Test
    public void testGetBuddyId() {
        String buddyid = BuddyListView.getBuddyId( buddytest );
        assertEquals( "idpippo@gmail.com", buddyid );
    }

    @Test
    public void testGetBuddy() {
        BuddyListView.addBuddy( buddytest.getId(), "pippourl" );
        String pippourl = BuddyListView.getBuddy( buddytest.getId() );
        assertEquals( "pippourl", pippourl );
    }

    @Test
    public void testGetBuddyEmpty() {
        assertNull( BuddyListView.getBuddy( "nonesisto" ) );
    }

    @Test
    public void testUpdateBuddy() {

        BuddyListView.addBuddy( BuddyListView.getBuddyId( buddytest ), "pippourl" );
        buddytest.setStatusMessage( "provaupdate" );
        BuddyListView.updateBuddy( buddytest );
        // assure that it don't update if buddy already present
        assertEquals( "pippourl", BuddyListView.getBuddy( BuddyListView.getBuddyId( buddytest ) ) );

    }

}
