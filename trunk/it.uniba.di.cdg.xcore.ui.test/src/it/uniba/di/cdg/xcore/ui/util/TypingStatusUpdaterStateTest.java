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
package it.uniba.di.cdg.xcore.ui.util;

import org.junit.Test;

import it.uniba.di.cdg.xcore.ui.views.ITalkView;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import static it.uniba.di.cdg.xcore.ui.util.TypingStatusUpdater.ONE_SECOND;
import static it.uniba.di.cdg.xcore.ui.util.TypingStatusUpdater.UPDATE_PAUSE;;


/**
 * jUnit test for {@see it.uniba.di.cdg.xcore.ui.util.TypingStatusUpdater}. 
 */
public class TypingStatusUpdaterStateTest {
    /**
     * Check that the text is composed as we expect by the thread.
     */
    @Test
    public void testComposeStatusText() {
        System.out.println( "*** Warning: this test will take about 10 seconds to complete ***" );
        
        ITalkView talkView = mock(ITalkView.class);
        
        TypingStatusUpdater t = new TypingStatusUpdater( talkView );
        t.start();

        // First a bunch of static tests ...
        t.queueUser( "mario", 5 );
        assertEquals( "mario is typing ...", t.composeStatusText() );

        t.queueUser( "pippo", 2 );
        assertEquals( "mario, pippo are typing ...", t.composeStatusText() );

        sleep( 4 * UPDATE_PAUSE ); // just wait more than pippo needs to die ...
        // Ensure that only mario stands ...
        assertEquals( "mario is typing ...", t.composeStatusText() );

        t.queueUser( "toto", 4 );
        assertEquals( "mario, toto are typing ...", t.composeStatusText() );

        sleep( 2 * UPDATE_PAUSE );
        assertEquals( "toto is typing ...", t.composeStatusText() );

        // Ensure that all died now ...
        sleep( 2 * UPDATE_PAUSE );
        assertEquals( "", t.composeStatusText() );

        t.quit();
        
        verify(talkView,atLeast(1)).updateStatus( anyString() );
        
        System.out.println( "*** Test completed ***" );
    }

    /**
     * Ensure that when we queue another user it doesn't appear as duplicated in the composed
     * string.
     */
    @Test
    public void testComposeStatusTextHasNotDuplicates() {
        System.out.println( "*** Warning: this test will take about 3-5 seconds to complete ***" );

        ITalkView talkView = mock(ITalkView.class);
                
        TypingStatusUpdater t = new TypingStatusUpdater( talkView );
        t.start();

        // First a bunch of static tests ...
        t.queueUser( "mario", 5 );
        assertEquals( "mario is typing ...", t.composeStatusText() );

        sleep( UPDATE_PAUSE );

        t.queueUser( "mario", 4 );
        // Ensure that only ONE mario stands ...
        assertEquals( "mario is typing ...", t.composeStatusText() );

        sleep( 1 * ONE_SECOND );
        
        t.quit();
                
        System.out.println( "*** Test completed ***" );
    }

    private void sleep( int msec ) {
        try {
            Thread.sleep( msec );
        } catch (InterruptedException e) { e.printStackTrace(); /* I hate checked exceptions */ }
    }
}
