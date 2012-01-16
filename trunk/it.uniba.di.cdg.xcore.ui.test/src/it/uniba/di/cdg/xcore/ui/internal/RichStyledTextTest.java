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

    @Override
    @Before
    protected void setUp() {
        Shell parent = new Shell();
        Composite composite = new Composite(parent, SWT.NULL);
        instance = new RichStyledText(composite, SWT.BORDER);
    }

    /**
     * Test that a new style is applied for each of the links in the text
     * (basic cases)
     */
    public void testFindLinksBase() {
        assertEquals(instance.applyStyleLinks( "no links here", 0 ).size(), 0);
        List<StyleRange> styleList = instance.applyStyleLinks( "  http://www.google.com", 0 );
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
        assertEquals( instance.applyStyleLinks( "www.google.com", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "hello www.google.com world", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "hello world www.google.com", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "www.google.com hello world", 0 ).size(), 1 );

        assertEquals( instance.applyStyleLinks( "http://www.google.com", 0 ).size(), 1);
        assertEquals( instance.applyStyleLinks( "hello http://www.google.com world", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "hello world http://www.google.com", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "http://www.google.com hello world", 0 ).size(), 1 );

        assertEquals( instance.applyStyleLinks( "http://www.google.com", 0 ).size(), 1);
        assertEquals( instance.applyStyleLinks( "hello http://www.google.com world", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "hello world http://www.google.com", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "http://www.google.com hello world", 0 ).size(), 1 );

        assertEquals( instance.applyStyleLinks( "https://www.google.com", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "hello https://www.google.com world", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "hello world https://www.google.com", 0 ).size(), 1 );
        assertEquals( instance.applyStyleLinks( "https://www.google.com hello world", 0 ).size(), 1 );

        // multiple links in the same sentence
        assertEquals( instance.applyStyleLinks("www.google.com twice the same url www.google.com", 0 ).size(), 2 );

        assertEquals( instance.applyStyleLinks( "www.google.com and www.google.it", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "hello www.google.com and www.google.it world", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "www.google.com and www.google.it hello world", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "hello world www.google.com and www.google.it", 0 ).size(), 2 );

        assertEquals( instance.applyStyleLinks( "http://www.google.com and http://www.google.it", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "hello http://www.google.com and http://www.google.it world", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "http://www.google.com and http://www.google.it hello world", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "hello world http://www.google.com and http://www.google.it", 0 ).size(), 2 );

        assertEquals( instance.applyStyleLinks( "https://www.google.com and https://www.google.it", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "hello https://www.google.com and https://www.google.it world", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "https://www.google.com and https://www.google.it hello world", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "hello world https://www.google.com and https://www.google.it", 0 ).size(), 2 );

        // mixing link styles
        assertEquals( instance.applyStyleLinks( "http://www.google.com and https://www.google.it", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "www.google.com https://www.google.it", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "https://www.google.com and www.google.it", 0 ).size(), 2 );
        assertEquals( instance.applyStyleLinks( "http://www.google.com and www.google.it", 0 ).size(), 2 );
    }

    /**
     * Test that a new style is applied for each of the email addresses in the text
     * (basic cases)
     */
    public void testFindEmailBase() {
        assertEquals( instance.applyStyleMail( "no email", 0 ).size(), 0);
        List<StyleRange> styleList = instance.applyStyleMail( "just one email: test@gmail.com", 0 );
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
        assertEquals( instance.applyStyleMail( "test1@gmail.com test2@hotmail.it", 0 ).size(), 2);
        assertEquals( instance.applyStyleMail( "text test@uniba.it", 0 ).size(), 1);
        assertEquals( instance.applyStyleMail( "test@uniba.it text", 0 ).size(), 1);
        assertEquals( instance.applyStyleMail( "text test@uniba.it text", 0 ).size(), 1);
        assertEquals( instance.applyStyleMail( "text test1@gmail.com test2@hotmail.it", 0 ).size(), 2);
        assertEquals( instance.applyStyleMail( "test1@gmail.com text test2@hotmail.it", 0 ).size(), 2);
        assertEquals( instance.applyStyleMail( "test1@gmail.com test2@hotmail.it text", 0 ).size(), 2);
        assertEquals( instance.applyStyleMail( "text test1@gmail.com test2@hotmail.it text", 0 ).size(), 2);
        assertEquals( instance.applyStyleMail( "text test1@gmail.com text test2@hotmail.it text", 0 ).size(), 2);
    }
    
    /**
     * Test that a new style is applied for custom formatted text
     * (basic cases)
     */
    public void testFindFormattingBase() {
        List<StyleRange> boldList, underlineList, mixedList;

        assertEquals( instance.applyStyleFormatting( "no formatting", 0 ).size(), 0 );
        boldList = instance.applyStyleFormatting( "*bold formatting*", 0 );
        underlineList = instance.applyStyleFormatting( "_underline formatting_", 0 );
        
        assertEquals( boldList.size(), 1);
        assertEquals( boldList.get( 0 ).start, 0);
        assertEquals( boldList.get( 0 ).length, 17);

        assertEquals( underlineList.size(), 1 );
        assertEquals( underlineList.get( 0 ).start, 0 );
        assertEquals( underlineList.get( 0 ).length, 22 );

        mixedList = instance.applyStyleFormatting( "normal *bold* and _underline_", 0 );
        assertEquals( mixedList.size(), 2);
        assertEquals( mixedList.get( 0 ).start, 7);
        assertEquals( mixedList.get( 0 ).length, 6);
        assertEquals( mixedList.get( 1 ).start, 18);
        assertEquals( mixedList.get( 1 ).length, 11);
    }

    /**
     * Test that a new style is applied for custom formatted text
     * (complex cases)
     */
    public void testFindFormattingComplex() {
        // must match only the outer formatter
        assertEquals( instance.applyStyleFormatting( "*_mixed formatting_*", 0 ).size(), 1 );

        assertEquals( instance.applyStyleFormatting( "*mixed* _formatting_", 0 ).size(), 2 );
        assertEquals( instance.applyStyleFormatting( "**bold** formatting", 0 ).size(), 2 );
        assertEquals( instance.applyStyleFormatting( "__mixed__ formatting", 0 ).size(), 2 );
        assertEquals( instance.applyStyleFormatting( "** two asterisk highlighted", 0 ).size(), 1 );

        // invalid format tags
        assertEquals( instance.applyStyleFormatting( "invalid_", 0 ).size(), 0 );
        assertEquals( instance.applyStyleFormatting( "_invalid", 0 ).size(), 0 );
        assertEquals( instance.applyStyleFormatting( "invalid*", 0 ).size(), 0 );
        assertEquals( instance.applyStyleFormatting( "*invalid", 0 ).size(), 0 );
        assertEquals( instance.applyStyleFormatting( "*invalid_", 0 ).size(), 0 );

        // mixture of normal and formatted text
        assertEquals( instance.applyStyleFormatting( "here comes *the* bold", 0 ).size(), 1 );
        assertEquals( instance.applyStyleFormatting( "here comes _the_ underline", 0 ).size(), 1 );
        assertEquals( instance.applyStyleFormatting( "xxx _underline_ yyy *bold* zzz", 0 ).size(), 2 );
    }

    /**
     * Test that our overriden implementation of setText resets the text styles
     */
    public void testSetText() {
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
        // a valid image
        Image goodImg = instance.getImage( "http://www.di.uniba.it/dib/immagini/logo_uniba.gif" );
        assertTrue( goodImg != null );

        Image notAnImg = instance.getImage( "http://www.di.uniba.it" );
        assertTrue( notAnImg == null );

        Image invalidUrl = instance.getImage( "http://com" );
        assertTrue( invalidUrl == null );
    }
}
