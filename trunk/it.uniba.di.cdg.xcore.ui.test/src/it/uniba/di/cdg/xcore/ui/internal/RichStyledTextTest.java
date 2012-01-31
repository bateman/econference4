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

import it.uniba.di.cdg.xcore.ui.formatter.EmailFormatter;
import it.uniba.di.cdg.xcore.ui.formatter.LinkFormatter;
import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;
import it.uniba.di.cdg.xcore.ui.widget.RichStyledText;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import junit.framework.TestCase;

import org.junit.*;

/**
 * jUnit test for {@see it.uniba.di.cdg.xcore.ui.internal.RichStyledText}. 
 */
public class RichStyledTextTest extends TestCase {
    private RichStyledText instance;
    private LinkFormatter linkFormatter;
    private EmailFormatter emailFormatter;
    private RichFormatting richFormatting;
    @Override
    @Before
    protected void setUp() {
        Shell parent = new Shell();
        Composite composite = new Composite(parent, SWT.NULL);
        instance = new RichStyledText(composite, SWT.BORDER);
        linkFormatter = new LinkFormatter();
        emailFormatter = new EmailFormatter();
        richFormatting = new RichFormatting();
    }

    /**
     * Test that a new style is applied for each of the links in the text
     * (basic cases)
     */
    public void testFindLinksBase() {
        assertEquals(linkFormatter.applyFormatting( "no links here", 0 ).size(), 0);
        List<StyleRange> styleList = linkFormatter.applyFormatting( "  http://www.google.com", 0 );
        assertEquals( styleList.size(), 1 );
        assertEquals( styleList.get( 0 ).start, 2 );
        assertEquals( styleList.get( 0 ).underlineStyle, SWT.UNDERLINE_LINK );
        assertTrue( styleList.get( 0 ).underline );
    }

    /**
     * Test that a new style is applied for each of the links in the text
     * (complex cases)
     */
    public void testFindLinksComplex() {
        // a single link in a sentence
        assertEquals( linkFormatter.applyFormatting( "www.google.com", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "hello www.google.com world", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "hello world www.google.com", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "www.google.com hello world", 0 ).size(), 1 );

        assertEquals( linkFormatter.applyFormatting( "http://www.google.com", 0 ).size(), 1);
        assertEquals( linkFormatter.applyFormatting( "hello http://www.google.com world", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "hello world http://www.google.com", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "http://www.google.com hello world", 0 ).size(), 1 );

        assertEquals( linkFormatter.applyFormatting( "http://www.google.com", 0 ).size(), 1);
        assertEquals( linkFormatter.applyFormatting( "hello http://www.google.com world", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "hello world http://www.google.com", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "http://www.google.com hello world", 0 ).size(), 1 );

        assertEquals( linkFormatter.applyFormatting( "https://www.google.com", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "hello https://www.google.com world", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "hello world https://www.google.com", 0 ).size(), 1 );
        assertEquals( linkFormatter.applyFormatting( "https://www.google.com hello world", 0 ).size(), 1 );

        // multiple links in the same sentence
        assertEquals( linkFormatter.applyFormatting("www.google.com twice the same url www.google.com", 0 ).size(), 2 );

        assertEquals( linkFormatter.applyFormatting( "www.google.com and www.google.it", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "hello www.google.com and www.google.it world", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "www.google.com and www.google.it hello world", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "hello world www.google.com and www.google.it", 0 ).size(), 2 );

        assertEquals( linkFormatter.applyFormatting( "http://www.google.com and http://www.google.it", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "hello http://www.google.com and http://www.google.it world", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "http://www.google.com and http://www.google.it hello world", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "hello world http://www.google.com and http://www.google.it", 0 ).size(), 2 );

        assertEquals( linkFormatter.applyFormatting( "https://www.google.com and https://www.google.it", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "hello https://www.google.com and https://www.google.it world", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "https://www.google.com and https://www.google.it hello world", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "hello world https://www.google.com and https://www.google.it", 0 ).size(), 2 );

        // mixing link styles
        assertEquals( linkFormatter.applyFormatting( "http://www.google.com and https://www.google.it", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "www.google.com https://www.google.it", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "https://www.google.com and www.google.it", 0 ).size(), 2 );
        assertEquals( linkFormatter.applyFormatting( "http://www.google.com and www.google.it", 0 ).size(), 2 );
    }

    /**
     * Test that a new style is applied for each of the email addresses in the text
     * (basic cases)
     */
    public void testFindEmailBase() {
        assertEquals( emailFormatter.applyFormatting( "no email", 0 ).size(), 0);
        List<StyleRange> styleList = emailFormatter.applyFormatting( "just one email: test@gmail.com", 0 );
        assertEquals( styleList.size(), 1 );
        assertEquals( styleList.get( 0 ).start, 16 );
        assertEquals( styleList.get( 0 ).length, 14 );
        assertEquals( styleList.get( 0 ).underlineStyle, SWT.UNDERLINE_LINK );
        assertTrue( styleList.get( 0 ).underline );
    }

    /**
     * Test that a new style is applied for each of the email addresses in the text
     * (complex cases)
     */
    public void testFindEmailComplex() {
        assertEquals( emailFormatter.applyFormatting( "test1@gmail.com test2@hotmail.it", 0 ).size(), 2);
        assertEquals( emailFormatter.applyFormatting( "text test@uniba.it", 0 ).size(), 1);
        assertEquals( emailFormatter.applyFormatting( "test@uniba.it text", 0 ).size(), 1);
        assertEquals( emailFormatter.applyFormatting( "text test@uniba.it text", 0 ).size(), 1);
        assertEquals( emailFormatter.applyFormatting( "text test1@gmail.com test2@hotmail.it", 0 ).size(), 2);
        assertEquals( emailFormatter.applyFormatting( "test1@gmail.com text test2@hotmail.it", 0 ).size(), 2);
        assertEquals( emailFormatter.applyFormatting( "test1@gmail.com test2@hotmail.it text", 0 ).size(), 2);
        assertEquals( emailFormatter.applyFormatting( "text test1@gmail.com test2@hotmail.it text", 0 ).size(), 2);
        assertEquals( emailFormatter.applyFormatting( "text test1@gmail.com text test2@hotmail.it text", 0 ).size(), 2);
    }
    
    /**
     * Test that a new style is applied for custom formatted text
     * (basic cases)
     */
    public void testFindFormattingBase() {
        List<StyleRange> boldList, underlineList, strikeList, mixedList;

        assertEquals( richFormatting.applyFormatting( "no formatting", 0 ).size(), 0 );
        boldList = richFormatting.applyFormatting( "*bold formatting*", 0 );
        underlineList = richFormatting.applyFormatting( "_underline formatting_", 0 );
        strikeList = richFormatting.applyFormatting( "-strike formatting-", 0 );
        
        assertEquals( boldList.size(), 1);
        assertEquals( boldList.get( 0 ).start, 0);
        assertEquals( boldList.get( 0 ).length, 17);

        assertEquals( underlineList.size(), 1 );
        assertEquals( underlineList.get( 0 ).start, 0 );
        assertEquals( underlineList.get( 0 ).length, 22 );

        assertEquals( strikeList.size(), 1 );
        assertEquals( strikeList.get( 0 ).start, 0 );
        assertEquals( strikeList.get( 0 ).length, 19 );
        
        mixedList = richFormatting.applyFormatting( "normal *bold* and _underline_ and -strike-", 0 );
        assertEquals( mixedList.size(), 3);
        assertEquals( mixedList.get( 0 ).start, 7);
        assertEquals( mixedList.get( 0 ).length, 6);
        assertEquals( mixedList.get( 1 ).start, 18);
        assertEquals( mixedList.get( 1 ).length, 11);
        assertEquals( mixedList.get( 2 ).start, 34);
        assertEquals( mixedList.get( 2 ).length, 8);
    }

    /**
     * Test that a new style is applied for custom formatted text
     * (complex cases)
     */
    public void testFindFormattingComplex() {
        // must match only the outer formatter
        assertEquals( richFormatting.applyFormatting( "*_mixed formatting_*", 0 ).size(), 1 );
        assertEquals( richFormatting.applyFormatting( "-_mixed formatting_-", 0 ).size(), 1 );
        
        assertEquals( richFormatting.applyFormatting( "*mixed* _formatting_", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "*mixed* -formatting-", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "-mixed- _formatting_", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "**bold** formatting", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "__mixed__ formatting", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "--mixed-- formatting", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "** two asterisk highlighted", 0 ).size(), 1 );

        // invalid format tags
        assertEquals( richFormatting.applyFormatting( "invalid_", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "invalid-", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "_invalid", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "-invalid", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "invalid*", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "*invalid", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "*invalid_", 0 ).size(), 0 );
        assertEquals( richFormatting.applyFormatting( "_invalid-", 0 ).size(), 0 );

        // mixture of normal and formatted text
        assertEquals( richFormatting.applyFormatting( "here comes *the* bold", 0 ).size(), 1 );
        assertEquals( richFormatting.applyFormatting( "here comes _the_ underline", 0 ).size(), 1 );
        assertEquals( richFormatting.applyFormatting( "xxx _underline_ yyy *bold* zzz", 0 ).size(), 2 );
        assertEquals( richFormatting.applyFormatting( "the answer to -life- the _universe and everything_: *42*", 0 ).size(), 3 );
    }

    /**
     * Test that our overriden implementation of setText resets the text styles
     */
    public void testSetText() {
        instance.addFormatListener( linkFormatter );
        instance.setText( "this entry has a link style associated: http://www.google.com\n" );
        instance.redraw(); // force a lineStyleEvent so text styling is applied
        assertEquals(instance.getStyleRanges().length, 1);

        instance.setText( "this text has no style associated" );
        instance.redraw();
        assertEquals( instance.getStyleRanges().length, 0 );
    }

    /**
     * Make sure that images are inlined within text
     */
    public void testBasicImageInlining() {
        instance.addFormatListener( linkFormatter );
        String text = "this url ends with .gif so an image will be added: http://www.di.uniba.it/dib/immagini/logo_uniba.gif";
        instance.setText( text );
        instance.redraw();
        StyleRange[] styles = instance.getStyleRanges();
        // the url will also be highlighted and the image will come afterwards
        assertEquals( styles.length, 2 );
        // the second style (styles[1]) is the url styling, the first is the image
        assertEquals( styles[0].start, text.length() + 1);
        assertEquals( styles[0].length, 1 );
        
        try {
            @SuppressWarnings("unused")
            Image image = (Image)styles[0].data;
        } catch (ClassCastException ex) {
            fail( "embedded data in style is not an image" );
        }
    }
    
    /**
     * Test complex cases of image inlining
     */
    public void testComplexImageInlining() {
        instance.addFormatListener( linkFormatter );
        // twice the same image
        instance.setText( "http://www.di.uniba.it/dib/immagini/build.gif http://www.di.uniba.it/dib/immagini/build.gif" );
        StyleRange[] styles = instance.getStyleRanges();
        // 2 url stylings + 2 images
        assertEquals( styles.length, 4 );

        // the first two styles (styles[0] and styles[1]) must be the images
        // (we can't know who's the first and who's the second)
        assertTrue( styles[0].start == 46 || styles[0].start == 94 );
        assertTrue( styles[1].start == 46 || styles[1].start == 94 );

        assertEquals( styles[0].length, 1 );
        assertEquals( styles[1].length, 1 );

        // different images
        instance.setText( "http://www.di.uniba.it/dib/immagini/logo_uniba.gif http://www.di.uniba.it/dib/immagini/build.gif" );
        styles = instance.getStyleRanges();
        // 2 url stylings + 2 images
        assertEquals( styles.length, 4 );

        // the first two styles (styles[0] and styles[1]) must be the images
        // (we can't know who's the first and who's the second)
        assertTrue( styles[0].start == 51 || styles[0].start == 97 );
        assertTrue( styles[1].start == 51 || styles[1].start == 97 );

        assertEquals( styles[0].length, 1 );
        assertEquals( styles[1].length, 1 );
    }

    /**
     * Test that images are retrieved correctly
     */
    public void testImageGet() {
        instance.addFormatListener( linkFormatter );
        // a valid image
        Image goodImg = instance.getImage( "http://www.di.uniba.it/dib/immagini/logo_uniba.gif" );
        assertTrue( goodImg != null );

        Image notAnImg = instance.getImage( "http://www.di.uniba.it" );
        assertTrue( notAnImg == null );

        Image invalidUrl = instance.getImage( "http://com" );
        assertTrue( invalidUrl == null );
    }
    
    public void testAddFormatListenet(){        
        instance.addFormatListener( emailFormatter );
        instance.addFormatListener( linkFormatter );
        instance.setText( "" );
        
        instance.setText( "foo@mail.com" );
        StyleRange [] styles = instance.getStyleRanges( 0, 11 );
        assertTrue( styles[0].underline == true && styles[0].underlineStyle == SWT.UNDERLINE_LINK );
        instance.setText( "" );
        
        instance.setText( "www.google.com" );
        styles = instance.getStyleRanges( 0, 14 );
        assertTrue( styles[0].underline == true && styles[0].underlineStyle == SWT.UNDERLINE_LINK );
        instance.setText( "" );
        
        instance.setText( "*prova*" );
        styles = instance.getStyleRanges( 0, 7 );
        assertTrue( styles.length == 0);
        instance.setText( "" );
        
        instance.addFormatListener( richFormatting );
        instance.setText( "*prova*" );
        styles = instance.getStyleRanges( 0, 7 );
        assertTrue( styles[0].fontStyle == SWT.BOLD );
    }
}
