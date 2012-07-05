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

import it.uniba.di.cdg.xcore.ui.formatter.FormatListener;
import it.uniba.di.cdg.xcore.ui.internal.UrlListener;
import it.uniba.di.cdg.xcore.ui.util.LinkFinder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * A compatible, thread-safe implementation of StyledText that allow for rich
 * text formatting. This widget is optimized for chat-like environments where
 * text appending is the most common operation.
 * 
 * In addition to the standard StyledText behaviour, this widget will
 * also automatically format http and email links as clickable items.
 *
 */
public class RichStyledText extends StyledText implements LineStyleListener,
                                                          PaintObjectListener,
                                                          UrlListener,
                                                          MouseListener {
    // inline imaged will be represented by the character here
    // ideally, it should be the unicode char \uFFFC (object replacement),
    // but when saving the text it's ugly so a space it's much better
    protected final static String IMAGE_PLACEHOLDER = " ";
    
    // suffixes that will enable image retrieval and inline placing from links
    private final static String[] VALID_IMAGE_SUFFIX = {
        ".jpg", ".jpeg", ".png", ".bmp", ".gif",
    };

    private LinkedList<StyleRange> styleList = new LinkedList<StyleRange>();

    // let the cache be shared among all RichStyledText instances
    private static Map<String,Image> urlImageCache = new HashMap<String,Image>();

    private List<UrlListener> urlListeners = new LinkedList<UrlListener>();

    private List<FormatListener> formatListeners = new LinkedList<FormatListener>();

    protected int currentTextLength;

    public RichStyledText(Composite parent, int style) {
        super(parent, style);

        currentTextLength = 0;

        addMouseListener( this );
        addUrlListener( this );
        addPaintObjectListener( this );

        /*
         * For some reason, when running through JUnit, this statement
         * crashes without throwing any errors, so it's possible
         * that the crash happens within another RCP thread.
         * Anyway, this is a huge issue because the crash only happens
         * when inheriting classes (like with EntryRichStyledText)
         * and basically prevents the inherited class' constructor
         * to run.
         * 
         */
        addLineStyleListener( this );
    }

    /**
     * Add a new URL listener.
     * This listener will be called whenever a new URL is inserted in the text.
     * 
     * @param listener listener to add
     */
    public void addUrlListener(UrlListener listener) {
        urlListeners.add( listener );
    }
    
    public void addFormatListener( FormatListener listener ) {
        formatListeners.add( listener );
    }
    
    @Override
    public void append( String string ) {
        // alter the text *before* it gets added to the widget
        string = notifyUrlListeners( string );

        int startOffset = getCharCount() - 1;
        if (startOffset < 0) {
            startOffset = 0;
        }
        super.append( string );
        currentTextLength += string.length();

        // run a set of content filters over the input text. Each of these search
        // for some particular type of textual items (ex: links) and may
        // return one or more StyleRange objects that should be added to the
        // style list (ex: link styles, one for each link)
        List<StyleRange> newStyles = new LinkedList<StyleRange>();
        for (FormatListener listener : formatListeners) {
            newStyles.addAll( listener.applyFormatting( string, startOffset ) );
        }

        addStyles (newStyles );
        redrawRange( startOffset, string.length(), true );
    }

    /**
     * Add a style to the list of styles to be applied to the text in the widget.
     * 
     * @param style style to add
     */
    public void addStyle(StyleRange style) {
        styleList.add( style );
    }

    /**
     * Add a new style to the list of styles but add it to the front queue
     * so that styles that come after this can overwrite it
     * 
     * @param style style to add
     */
    public void addStyleFirst(StyleRange style) {
        styleList.add( 0, style );
    }

    /**
     * Add a collection of styles to the list of styles to be applied
     * to the text in the widget.
     * 
     * The order to which the styles are added is the same of the
     * order returned by the iterator in input.
     * 
     * @param styles styles to add
     */
    public void addStyles(java.util.Collection<StyleRange> styles) {
        styleList.addAll( styles );
    }

    /**
     * Clear all styles for this instance
     */
    public void clearStyles() {
        currentTextLength = 0;
        styleList.clear();
    }

    protected StyleRange createStyleForImage( final Image image, final int offset ) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = 1;
        style.data = image;
        Rectangle rect = image.getBounds();
        style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
        return style;
    }

    /**
     * Load an image from the given URL
     * 
     * @param url url where to load the image from
     * @return the loaded Image instance
     */
    public Image getImage( String url ) {
        if (urlImageCache.containsKey( url )) {
            return urlImageCache.get( url );
        }

        Image image;
        ImageData imgData;
        URL imgUrl;

        try {
            imgUrl = new URL( url );
        } catch (MalformedURLException e) {
            urlImageCache.put( url, null );
            return null;
        }

        try {
            imgData = new ImageData( imgUrl.openStream() );
        } catch (IOException e) {
            // the url might not be an image
            urlImageCache.put( url, null );
            return null;
        } catch (SWTException e) {
            // the url might not be an image
            urlImageCache.put( url, null );
            return null;
        }

        image = new Image( getDisplay(), imgData );
        urlImageCache.put( url, image );

        return image;
    }
    
    /*
     * getStyleRanges(). getStyleRanges(int, int) and getStyleRangeAtOffset(int)
     * are overidden version of the original functions that replace the
     * existing one as they don't work when we add a line listener (as we do).
     */
    
    @Override
    public StyleRange[] getStyleRanges() {
        return styleList.toArray(new StyleRange[styleList.size()]);
    }

    @Override
    public StyleRange getStyleRangeAtOffset( int offset ) {
        Iterator<StyleRange> it = styleList.descendingIterator();
        while (it.hasNext()) {
            StyleRange style = it.next();

            if ((offset >= style.start) && (offset < (style.start + style.length))) {
                return style;
            }
        }
        return null;
    }

    @Override
    public StyleRange[] getStyleRanges(int start, int length) {
        List<StyleRange> styles = new LinkedList<StyleRange>();

        for (StyleRange style : styleList) {
            int stop = style.start + style.length;

            if (start >= style.start && start <= stop) {
                styles.add(style);
                continue;
            }

            if (stop >= start && stop <= (start + length)) {
                styles.add(style);
                continue;
            }
        }

        return styles.toArray( new StyleRange[styles.size()] );
    }
    
    /**
     * Return all StyleRange instances in the current widget that
     * affect the specified offset as well as those that come
     * after that
     * 
     * @param offset
     * @return an array of StyleRange that match the request
     */
    public StyleRange[] getStyleAfterOffset(int offset) {
        List<StyleRange> styles = new LinkedList<StyleRange>();

        for (StyleRange style : styleList) {
            int stop = style.start + style.length;

            if (offset >= style.start ||
                offset < style.start && offset <= stop) {
                styles.add(style);
                continue;
            }
        }

        return styles.toArray( new StyleRange[styles.size()] );
    }

    /**
     * Returns true if the string is a valid email address
     * @param emailAddress
     * @return true if the string is an email address, false otherwise
     */
    private boolean isValidEmailAddress( String emailAddress ){  
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
        CharSequence inputStr = emailAddress;  
        Pattern pattern = Pattern.compile( expression, Pattern.CASE_INSENSITIVE );  
        Matcher matcher = pattern.matcher( inputStr );  
        return matcher.matches();  
    } 

    /*
     * Apply style informations to the text in the widget
     * 
     * (non-Javadoc)
     * @see org.eclipse.swt.custom.LineStyleListener#lineGetStyle(org.eclipse.swt.custom.LineStyleEvent)
     */
    @Override
    public void lineGetStyle(LineStyleEvent event) {
        List<StyleRange> tmpStyleList = new ArrayList<StyleRange>();

        final String text = event.lineText;
        final int startOffset = event.lineOffset;

        StyleRange[] styles = getStyleRanges( startOffset, text.length() );
        for (int i = 0; i < styles.length; i++) {
            tmpStyleList.add( styles[i] );
        }

        // run the content filters on text that is currently being written
        if (startOffset >= currentTextLength) {
            for (FormatListener listener : formatListeners) {
                tmpStyleList.addAll( listener.applyFormatting( text, startOffset ) );
            }
        }

        event.styles = tmpStyleList.toArray( new StyleRange[tmpStyleList.size()] );
    }

    private String notifyUrlListeners(String text) {
        final int lineStartOffset = getCharCount();
        Set<String> urlSet = new HashSet<String>();
        urlSet.addAll( LinkFinder.extractUrls( text ) );
        // notify all the registered UrlListeners
        for (String url : urlSet) {
            for (UrlListener listener : urlListeners) {
                text = listener.urlAdded( text, url, lineStartOffset );
            }
        }

        return text;
    }

    @Override
    public void mouseDoubleClick(MouseEvent event) {}

    @Override
    public void mouseUp(MouseEvent event) {}

    /** 
     * Handle message board click on links, opening the default browser on the
     * specified link
     * @param event
     */
    @Override
    public void mouseDown(MouseEvent event) {
        Point location = new Point(event.x, event.y);
        final int offset;

        try {
            offset = this.getOffsetAtLocation(location);
        } catch (IllegalArgumentException ex) {
            // the clicked point is outside text bounds
            return;
        }

        StyleRange[] styles = getStyleRanges(offset, 1);
        if (styles == null) return;
        
        int i;
        for (i = 0; i < styles.length; i++) {
            StyleRange range = styles[i];
            if (range != null && range.underlineStyle == SWT.UNDERLINE_LINK) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                String href = (String)range.data;
                java.net.URI uri;
                try {
                    uri = new java.net.URI( href );
                } catch (URISyntaxException e1) {
                    // invalid uri
                    continue;
                }

                try {
                    if (href.startsWith("mailto://")) {
                        desktop.mail ( uri );
                    } else {
                        // assume everything else is a http url
                        desktop.browse( uri );
                    }
                } catch (IOException e) {
                    continue;
                }
            } 
        }

    }

    public void paintObject(PaintObjectEvent event) {
        StyleRange style = event.style;
        Image image;
        try {
            image = (Image)style.data;
        } catch (ClassCastException ex) {
            // it's not an image, but something else that valued .data
            return;
        }
        if (! image.isDisposed() ) {
            int x = event.x;
            int y = event.y + event.ascent - style.metrics.ascent;
            event.gc.drawImage(image, x, y);
        }
    }

    @Override
    public void setText(String text) {
        /* reset the list of styles we have on setText, they'll be
         * added again when appending
         */
        currentTextLength = 0;
        clearStyles();
        super.setText("");
        append(text);
    }

    @Override
    public String urlAdded(String text, final String url, final int lineStartOffset) {
        List<StyleRange> styles = new LinkedList<StyleRange>();

        boolean isImage = false;
        for (String suffix : VALID_IMAGE_SUFFIX) {
            if (url.toLowerCase().endsWith( suffix ) ) {
                isImage = true;
                break;
            }
        }

        if ( isImage ) {
            Image image = getImage( url );
            if (image == null) {
                // not a valid url
                return text;
            }

            String urlWithImage = url + " " + IMAGE_PLACEHOLDER;
            text = text.replace( url, urlWithImage );

            int offset = text.indexOf( url );
            while (offset != -1) {
                styles.add( createStyleForImage( image, lineStartOffset + offset + url.length() + 1 ) );
                offset = text.indexOf( url, offset + url.length() + 1 );
            }
        }
        
        addStyles( styles );
        return text;
    }
    
    
    public List<FormatListener> getFormatListeners() {
        return formatListeners;
    }
};
