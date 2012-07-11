package it.uniba.di.cdg.xcore.ui.formatter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class RichFormatting implements FormatListener {

	private List<AbstractMarkerPattern<TextStyleRange>>  patternMarkup;
	
	public RichFormatting(List<AbstractMarkerPattern<TextStyleRange>> pattern_markup) {
		this.patternMarkup=pattern_markup;
	}
	
	public RichFormatting()
	{	
		patternMarkup=new ArrayList<AbstractMarkerPattern<TextStyleRange>>();
		patternMarkup.add(BOLD_UNDERLINE_MARKER);	
		patternMarkup.add(BOLD_MARKER);		
		patternMarkup.add(UNDERLINE_MARKER);
		patternMarkup.add(STRIKEOUT_MARKER);
		patternMarkup.add(LATEX_MARKER);
		patternMarkup.add(ITALIC_MARKER);
		patternMarkup.add(_NO_MARKER);
		
	}
	
	
	
	
	 	@Override
		public List<StyleRange> applyFormatting(String text, int startOffset) {
		 
	 		
		 
		 Display display =  Display.getCurrent();
		 Color MARKUP_COLOR = display.getSystemColor(SWT.COLOR_GRAY);
		 Font MARKUP_FONT = new Font(display, "Courier New", 3, SWT.NORMAL);
		 
			List<StyleRange> list = new ArrayList<StyleRange>();
			int i = 0, endOffset;			
			
			while (i < text.length()) {
				for(AbstractMarkerPattern<TextStyleRange> pattern :patternMarkup)
				{
					String code = pattern.code;
					
					int patternlength=code.length();
					
					if(i+patternlength<text.length())
					{
							if (text.subSequence(i, i+patternlength) .equals(code)) {
								
								
								endOffset = text.indexOf(code, i + patternlength);
								if (endOffset != -1) {
									TextStyleRange token = new TextStyleRange(code);
									token.start = startOffset + i + patternlength;
									token.length = endOffset - i - patternlength;										
									token =  pattern.apply(token);		
									
									TextStyleRange prec= new TextStyleRange("marker");
									prec.start=token.start-patternlength ;
									prec.length = patternlength;	
									prec.foreground=  MARKUP_COLOR ;
									prec.font=MARKUP_FONT;
									
									TextStyleRange succ= new TextStyleRange("marker");
									succ.start=token.start+token.length;
									succ.length = patternlength;						 
									succ.foreground=  MARKUP_COLOR;
									succ.font=MARKUP_FONT;								
									
									list.add(prec);
									list.add(token);
									
									list.add(succ);
									
								
			
									i = endOffset;
								} else {
									i++;
								}
							} 
					}
				}
				
				i++;
			}

			return list;
		}


	 
	 
	 public static AbstractMarkerPattern<TextStyleRange> UNDERLINE_MARKER =new AbstractMarkerPattern<TextStyleRange>("__") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {			
			obj.underline=true;
			return obj;
		}
	};
	
	public static AbstractMarkerPattern<TextStyleRange> STRIKEOUT_MARKER =new AbstractMarkerPattern<TextStyleRange>("--") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {			
			obj.strikeout=true;
			return obj;
		}
	};
	
	public static AbstractMarkerPattern<TextStyleRange> BOLD_MARKER =new AbstractMarkerPattern<TextStyleRange>("**") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {			
			 obj.fontStyle=SWT.BOLD;
			return obj;
		}
	};
	
	public static AbstractMarkerPattern<TextStyleRange> BOLD_UNDERLINE_MARKER =new AbstractMarkerPattern<TextStyleRange>("*_*_") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {			
			 obj.fontStyle=SWT.BOLD;
			 obj.underline=true;
			return obj;
		}
	};

	
	public static AbstractMarkerPattern<TextStyleRange> LATEX_MARKER =new AbstractMarkerPattern<TextStyleRange>("$$") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {	
			final Device device = Display.getCurrent();		
			
			Font MARKUP_FONT = new Font(device, "Courier New", 8, SWT.ITALIC);
			obj.foreground=  device.getSystemColor(SWT.COLOR_DARK_GREEN);
			obj.font=MARKUP_FONT;				
			obj.underline=true;
			obj.underlineColor=device.getSystemColor(SWT.COLOR_DARK_GREEN);
			obj.fontStyle=SWT.ITALIC;			
			return obj;
		}
	};
	
	
	public static AbstractMarkerPattern<TextStyleRange> ITALIC_MARKER =new AbstractMarkerPattern<TextStyleRange>("%%") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {	
			obj.fontStyle=SWT.ITALIC;	
			return obj;
		}
	};
	 
	
	public static AbstractMarkerPattern<TextStyleRange> _NO_MARKER =new AbstractMarkerPattern<TextStyleRange>("_NO_") {		
		@Override
		public TextStyleRange apply(TextStyleRange obj) {
			final Device device = Display.getCurrent();	
			obj.background=device.getSystemColor(SWT.COLOR_BLACK);
			obj.foreground=device.getSystemColor(SWT.COLOR_WHITE);

			return obj;
		}
	};
	
	
	 
	 
	 
	 

}











