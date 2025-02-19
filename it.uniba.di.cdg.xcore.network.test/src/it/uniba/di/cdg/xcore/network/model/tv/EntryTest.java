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

import junit.framework.TestCase;

public class EntryTest extends TestCase {
    public void testToString() {
        final Date now = Calendar.getInstance().getTime();

        Entry entry = new Entry(now, "pippo", "This is my text", Entry.EntryType.UNKNOWN);

        String expected = String.format("[%1$tH:%1$tM:%1$tS] %2$s: %3$s", now, "pippo", "This is my text");

        assertEquals(expected, entry.toString());
    }

    public void testToStringWhenNoWhoIsProvided() {
        Entry entry = new Entry("This is my text");
        String expected = String.format("%s", "This is my text");

        assertEquals(expected, entry.toString());
    }

    public void testIsSystemEntry() {
        final Date now = Calendar.getInstance().getTime();

        Entry normal = new Entry(now, "pippo", "This is my text", Entry.EntryType.UNKNOWN);
        assertFalse(normal.isSystemEntry());

        Entry system = new Entry("This is a system entry");
        assertTrue(system.isSystemEntry());
    }

    public void testEntryType() {
        final Date now = Calendar.getInstance().getTime();

        Entry unknown = new Entry(now, "pippo", "This is an unknown message", Entry.EntryType.UNKNOWN);
        assertEquals(unknown.getType(), Entry.EntryType.UNKNOWN);

        Entry system = new Entry(now, "pippo", "This is a system message", Entry.EntryType.SYSTEM_MSG);
        assertEquals(system.getType(), Entry.EntryType.SYSTEM_MSG);

        Entry chatmsg = new Entry(now, "pippo", "This is my chat message", Entry.EntryType.CHAT_MSG);
        assertEquals(chatmsg.getType(), Entry.EntryType.CHAT_MSG);
        
        Entry privatemsg = new Entry(now, "pippo", "This is my private message", Entry.EntryType.PRIVATE_MSG);
        assertEquals(privatemsg.getType(), Entry.EntryType.PRIVATE_MSG);

        /* a constructor that takes only a `who' and a `message' must be
         * a chat message (the timestamp must be set automatically)
         */
        Entry entry = new Entry("pippo", "message");
        assertEquals(entry.getWho(), "pippo");
        assertEquals(entry.getText(), "message");
        assertEquals(entry.getType(), Entry.EntryType.CHAT_MSG);
        assertTrue(now.getTime() - entry.getTimestamp().getTime() < 2000);
    }

    public void testEntryTypeChange() {
        final Date now = Calendar.getInstance().getTime();

        Entry entry = new Entry(now, "pippo", "This is an unknown message", Entry.EntryType.UNKNOWN);
        assertEquals(entry.getType(), Entry.EntryType.UNKNOWN);

        entry.setType(Entry.EntryType.CHAT_MSG);
        assertEquals(entry.getType(), Entry.EntryType.CHAT_MSG);

        entry.setType(Entry.EntryType.SYSTEM_MSG);
        assertEquals(entry.getType(), Entry.EntryType.SYSTEM_MSG);

        entry.setType(Entry.EntryType.PRIVATE_MSG);
        assertEquals(entry.getType(), Entry.EntryType.PRIVATE_MSG);
    }
}
