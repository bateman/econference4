package it.uniba.di.cdg.xcore.ui.formatter;

import it.uniba.di.cdg.xcore.ui.util.LinkFinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

public class LinkFormatter implements FormatListener {
    /*
     * Find links in the text just added and format them accordingly
     */
    public List<StyleRange> applyFormatting( final String text, final int startOffset ) {
        final List<String> urls = LinkFinder.extractUrls( text );
        List<StyleRange> list = new ArrayList<StyleRange>();

        int start = 0;
        for (String url : urls) {
            start = text.indexOf( url, start );

            StyleRange urlstyle = new StyleRange();
            urlstyle.underline = true;
            urlstyle.underlineStyle = SWT.UNDERLINE_LINK;
            urlstyle.underlineColor = null;
            urlstyle.foreground = null;
            urlstyle.start = startOffset + start;
            urlstyle.length = url.length();
            urlstyle.data = url.startsWith( "http" ) ? url : "http://" + url;
            list.add( urlstyle );

            start++;
        }

        return list;
    }
}
