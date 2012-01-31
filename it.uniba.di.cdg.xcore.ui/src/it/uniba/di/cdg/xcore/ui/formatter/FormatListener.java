package it.uniba.di.cdg.xcore.ui.formatter;

import java.util.List;

import org.eclipse.swt.custom.StyleRange;

public interface FormatListener {
    /**
     * A public interface that defines the behavior for formatting a string
     * 
     * @param text
     *        the text to apply a formatting style
     * @param startOffset
     *        the offset from where apply a formatting style
     * @return a list of styles
     */
    public List<StyleRange> applyFormatting( final String text, final int startOffset );
}
