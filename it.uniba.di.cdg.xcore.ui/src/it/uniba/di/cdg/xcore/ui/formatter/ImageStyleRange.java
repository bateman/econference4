package it.uniba.di.cdg.xcore.ui.formatter;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextStyle;

public class ImageStyleRange extends StyleRange {

	public ImageStyleRange() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImageStyleRange(int start, int length, Color foreground,
			Color background, int fontStyle) {
		super(start, length, foreground, background, fontStyle);
		// TODO Auto-generated constructor stub
	}

	public ImageStyleRange(int start, int length, Color foreground,
			Color background) {
		super(start, length, foreground, background);
		// TODO Auto-generated constructor stub
	}

	public ImageStyleRange(TextStyle style) {
		super(style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		return super.equals(object);
	}
	
	

}


