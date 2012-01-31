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
package it.uniba.di.cdg.xcore.ui.internal;

import it.uniba.di.cdg.xcore.network.model.tv.Entry;
import it.uniba.di.cdg.xcore.ui.widget.EntryRichStyledText;
import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * jUnit test for {@see it.uniba.di.cdg.xcore.ui.internal.RichStyledText}. 
 */
public class EntryRichStyledTextTest extends TestCase {
    private EntryRichStyledText instance;

    @Override
    protected void setUp() {
        Shell parent = new Shell();
        Composite composite = new Composite( parent, SWT.NULL );
        instance = new EntryRichStyledText( composite, SWT.BORDER );
    }

    public void testFormatEntry() {
        Entry entry = new Entry( "pippo", "hello" );

        entry.setType( Entry.EntryType.CHAT_MSG );
        assertEquals( instance.formatEntry(entry), "pippo> hello" );

        entry.setType( Entry.EntryType.PRIVATE_MSG );
        assertEquals( instance.formatEntry(entry), "[PM FROM pippo] hello" );

        entry.setType( Entry.EntryType.SYSTEM_MSG );
        assertEquals( instance.formatEntry(entry), "--- hello" );
    }

    /**
     * Test that the color returned is always the same for the same text and
     * (might) change for different texts
     */
    public void testSameColorForSameParticipant() {
        assertEquals(instance.getColorForText("xxx").getRGB(), instance.getColorForText("xxx").getRGB());
        // changing only one character should give a different hash value
        // but the truth is this depends on the hash function we're using,
        // so if you change the hash function this test could fail
        assertTrue(!instance.getColorForText("xxx").getRGB().equals(instance.getColorForText("xxy").getRGB()));
    }

    public void testEntryPushing() {
        Entry entry = new Entry("who", "I just added some text");
        instance.pushEntry(entry);

        /*
         * The text will contain some linefeeds added automatically, so we can't
         * check that the text is exactly the same
         */
        assertTrue(instance.getText().contains( "I just added some text") );
    }

    /**
     * Test that entries get a different style according to their type
     * (i.e.: system entries are coloured differently than chat entries)
     * 
     */
    public void testEntryAutostyling() {
        Entry entry = new Entry("user", "I just added some text that will inherit some style");

        for (Entry.EntryType entryType : Entry.EntryType.values()) {
            if (entryType == Entry.EntryType.UNKNOWN) {
                // UNKNOWN entry types don't have an associated style
                continue;
            }

            entry.setType( entryType );
            instance.pushEntry( entry );
            assertNotNull( instance.getStyleRanges()[0].foreground );
            instance.setText( "" );
        }
    }
    
    /**
     * Test that entries are styled with the correct offsets when
     * there are multiple entries added
     */
    public void testEntryAutoStylingOffsets() {
        Entry entry1 = new Entry( "user1", "text1" );
        Entry entry2 = new Entry( "user2", "text2" );
        Entry entry3 = new Entry( "user3", "longer text 3" );

        entry1.setType( Entry.EntryType.CHAT_MSG );
        entry2.setType( Entry.EntryType.CHAT_MSG );
        entry3.setType( Entry.EntryType.CHAT_MSG );

        final String finalString1 = instance.formatEntry( entry1 ) + "\n";
        final String finalString2 = instance.formatEntry( entry2 ) + "\n";
        final String finalString3 = instance.formatEntry( entry3 ) + "\n";

        instance.pushEntry( entry1 );
        instance.pushEntry( entry2 );
        instance.pushEntry( entry3 );

        StyleRange[] styles = instance.getStyleRanges();
        StyleRange style1, style2, style3;

        // one style for each entry
        assertEquals( styles.length, 3 );

        style1 = styles[2];
        style2 = styles[1];
        style3 = styles[0];

        assertEquals( style1.start, 0 );
        // -1 because the \n isn't styled
        assertEquals( style1.length, finalString1.length() - 1 );

        assertEquals( style2.start, finalString1.length());
        assertEquals( style2.length, finalString2.length() - 1 );

        assertEquals( style3.start, finalString1.length() + finalString2.length());
        assertEquals( style3.length, finalString3.length() - 1 );
    }
}