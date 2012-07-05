package it.uniba.di.cdg.xcore.ui.formatter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

public class RichFormatting implements FormatListener {

    @Override
    public List<StyleRange> applyFormatting( String text, int startOffset ) {
        List<StyleRange> list = new ArrayList<StyleRange>();
        int i = 0, endOffset;

        while (i < text.length() - 1) {
            if (text.charAt( i ) == '*') {
                endOffset = text.indexOf( '*', i + 1 );
                if (endOffset != -1) {
                    StyleRange bold = new StyleRange();
                    bold.start = startOffset + i;
                    bold.length = endOffset - i + 1;
                    bold.fontStyle = SWT.BOLD;
                    list.add( bold );

                    i = endOffset;
                } else {
                    i++;
                }
            } else 
                if (text.charAt( i ) == '-') {
                    endOffset = text.indexOf( '-', i + 1 );
                    if (endOffset != -1) {
                        StyleRange strike = new StyleRange();
                        strike.start = startOffset + i;
                        strike.length = endOffset - i + 1;
                        strike.strikeout = true;
                        list.add( strike );

                        i = endOffset;
                    } else {
                        i++;
                    }
                } else
                    if (text.charAt( i ) == '_') {
                        endOffset = text.indexOf( '_', i + 1 );
                        if (endOffset != -1) {
                            StyleRange underline = new StyleRange();
                            underline.start = startOffset + i;
                            underline.length = endOffset - i + 1;
                            underline.underline = true;
                            list.add( underline );

                            i = endOffset;
                        } else {
                            i++;
                        }
                    }
            i++;
        }

        return list;
    }

}
