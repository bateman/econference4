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

package it.uniba.di.cdg.xcore.ui.widget;

import it.uniba.di.cdg.xcore.network.model.tv.Entry;
import it.uniba.di.cdg.xcore.ui.formatter.EntryStyleRange;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Extends RichStyledText in order to allow support for Entry instances,
 * automatically handling rich formatting and colouring.
 * 
 * @see RichStyledText
 *
 */
public class EntryRichStyledText extends RichStyledText {
    public EntryRichStyledText(Composite parent, int style) {
        super(parent, style);
    }
    
    /**
     * Apply a specific style for the entry that is going to be added
     * @param entry the entry instance added
     * @param text the real text added
     * @param start the offset where the entry has been added
     * @param length length of the entry added (may differ from that of the original entry)
     */
    private void applyStyleForEntry(final Entry entry, final String text, final int start, final int length) {
        final Display display = this.getShell().getDisplay();
        EntryStyleRange style = new EntryStyleRange();
 
        style.start = start;      
        
        switch (entry.getType()) {
        case CHAT_MSG:
            // let the color depend from who sent the message
        	 style.length = entry.getWho().length();
            style.foreground = getColorForText(entry.getWho());
            break;
        case PRIVATE_MSG:       	
        	//+1 because include "]"
        	style.length=text.indexOf("]",start)-start+1;
            style.background = display.getSystemColor(SWT.COLOR_GRAY);
            style.foreground = display.getSystemColor(SWT.COLOR_DARK_YELLOW);
            break;
        case SYSTEM_MSG:
        	style.length=text.length()-start-1;        	
            style.background = display.getSystemColor(SWT.COLOR_GRAY);          
            style.foreground = display.getSystemColor(SWT.COLOR_DARK_RED);
            break;
        case UNKNOWN:
            System.err.println("WARNING: unknown message type");
        default:
            style.foreground = display.getSystemColor(SWT.COLOR_BLACK);
            break;
        }

        // add entry styling as first items as they should be overriden by
        // other things (such as link styling) which have higher priority
        addStyleFirst( style );
        //redrawRange( style.start, style.length, true );
        redrawRange( style.start, length, true );
        
    }
    

    public String formatEntry( Entry entry ) {
        switch (entry.getType()) {
        case CHAT_MSG:
            return String.format( "%s> %s", entry.getWho(), entry.getText() );
        case SYSTEM_MSG:
            return String.format( "=== %s", entry.getText() );
        case PRIVATE_MSG:
        	if(entry.getWho()!=null)
            return String.format( "[PM FROM %s] %s", entry.getWho(), entry.getText() );
        	else
        	return entry.getText();
        case UNKNOWN:
            System.err.println("WARNING: unknown message type (" + entry.getText().trim() + ")");
        default:
            return entry.getText();
        }
    }

    /**
     * Return always the same color for the given text
     */
    public Color getColorForText(String text) {
        final Display display = this.getShell().getDisplay();

        Color[] COLORS = {
            display.getSystemColor( SWT.COLOR_DARK_MAGENTA ),
            display.getSystemColor( SWT.COLOR_BLACK ),
            display.getSystemColor( SWT.COLOR_DARK_GRAY ),
            display.getSystemColor( SWT.COLOR_BLUE ),
            display.getSystemColor( SWT.COLOR_DARK_BLUE ),
            display.getSystemColor( SWT.COLOR_CYAN ),
            display.getSystemColor( SWT.COLOR_DARK_CYAN ),
            display.getSystemColor( SWT.COLOR_DARK_RED ),
            display.getSystemColor( SWT.COLOR_DARK_YELLOW ),
        };

        int mod = 0;
        for (char ch : text.toCharArray()) {
            /* casting to char ensures that the xor stays positive (the cast
             * to char is guaranteed to be unsigned)
             */
            mod += (char)(ch ^ 0xFEE1DEAD);
        }
        return COLORS[mod % COLORS.length];
    }

    /**
     * Add a new entry to the text widget
     * @param entry entry to add
     */
    public void pushEntry(Entry entry) {
        String textToAppend = formatEntry ( entry ) + "\n";
        int oldCharCount = getCharCount();

        append( textToAppend );
        
        textToAppend=getText();

        // text can be altered when appending, so the length may be different
        // from that of the original entry
        final int length = getCharCount() - oldCharCount - 1; // -1 because we don't style the \n

        applyStyleForEntry( entry, textToAppend, oldCharCount, length );
    }
};
