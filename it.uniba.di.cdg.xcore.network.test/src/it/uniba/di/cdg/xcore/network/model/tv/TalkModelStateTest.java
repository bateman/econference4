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
package it.uniba.di.cdg.xcore.network.model.tv;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TalkModelStateTest {
    
    private ITalkModel model;
    
    @Before
    public void setUp() throws Exception {
        this.model = new TalkModel();
    }
    @Test
    public void testAddEntry() {
        Entry[] thread1 = new Entry[] {
                new Entry( getCurrentTime(), "pippo", "This is what I've to say", Entry.EntryType.CHAT_MSG ),
                new Entry( getCurrentTime(), "pluto", "This are my .02 cents", Entry.EntryType.CHAT_MSG )
        };
        
        Entry[] thread2 = new Entry[] {
                new Entry( getCurrentTime(), "mario", "Nothing to say", Entry.EntryType.CHAT_MSG )
        };

        // Note that we added thread2 before thread 1 since we want to test that the getAllEntries()
        // method still returns the entries sorted by timestamp (that is, {t1[0], t1[1], t2[0]})
        addEntriesToModel( "thread2", thread2 );
        addEntriesToModel( "thread1", thread1 );
        
        List<Entry> t1 = model.getEntries( "thread1" );
        List<Entry> t2 = model.getEntries( "thread2" );
        
        assertEquals( 2, t1.size() );
        assertEquals( 1, t2.size() );
        
        List<Entry> allEntries = model.getAllEntries();
        assertEquals( 3, allEntries.size() );
        assertEquals( t1.get( 0 ), allEntries.get( 0 ) );
        assertEquals( t1.get( 1 ), allEntries.get( 1 ) );
        assertEquals( t2.get( 0 ), allEntries.get( 2 ) );
    }

    private Date getCurrentTime() {
        Date timestamp = Calendar.getInstance().getTime();
        // Just introduce a small delay so the timestamp are a bit different (computers are damn fast)
        try {
            Thread.sleep( 20 );
        } catch (InterruptedException e) { /* I hate checked exceptions. */ }
        return timestamp;
    }

    private void addEntriesToModel( String threadId, Entry[] entries ) {
        for (int i = 0; i < entries.length; i++) {
            model.addEntry( threadId, entries[i] );
        }
    }

    @Test
    public void testAddFreeTalkEntry() {
        Date now = Calendar.getInstance().getTime();
        
        Entry entry = new Entry( now, "pippo", "This is what I've to say", Entry.EntryType.CHAT_MSG );
        
        model.addEntry( entry );
        
        List<Entry> freeTalk = model.getCurrentThreadEntries();
        assertNotNull( freeTalk );
        assertEquals( 1, freeTalk.size() );
    }

    @Test
    public void testUnknownThreadWillReturnEmptyList() {
        List<Entry> entries = model.getEntries( "unknown" );
        assertEquals( 0, entries.size() );
    }

    @Test
    public void testNotification() {
        Entry entry = new Entry( getCurrentTime(), "pippo", "This is what I've to say", Entry.EntryType.CHAT_MSG );
        ITalkModelListener listener = mock(ITalkModelListener.class);
        
        model.addListener( listener );
        
        model.addEntry( entry );
        
        verify(listener).entryAdded( eq( ITalkModel.FREE_TALK_THREAD_ID ), eq( entry ));
    }
    
    /**
     * By default the entries are queued into / fetched from the <code>FREE_TALK_THREAD_ID</code> thread:
     * using <code>setCurrentThread</code> method will change the working discussion thread.
     */
    @Test
    public void testSwitchCurrentThread() {
        ITalkModelListener listener = mock(ITalkModelListener.class);
                
        model.addListener( listener );
        
        Entry e1 = new Entry( getCurrentTime(), "pippo", "This is what I've to say", Entry.EntryType.CHAT_MSG );
        model.addEntry( e1 );

        assertEquals( ITalkModel.FREE_TALK_THREAD_ID, model.getCurrentThread() );
        assertEquals( 1, model.getEntries( ITalkModel.FREE_TALK_THREAD_ID ).size() );
        
        model.setCurrentThread( "thread1" );
        model.addEntry( e1 );

        assertEquals( "thread1", model.getCurrentThread() );
        assertEquals( 1, model.getEntries( "thread1" ).size() );
        
        verify(listener).currentThreadChanged( eq( ITalkModel.FREE_TALK_THREAD_ID ), eq( "thread1" ) );
    }
}