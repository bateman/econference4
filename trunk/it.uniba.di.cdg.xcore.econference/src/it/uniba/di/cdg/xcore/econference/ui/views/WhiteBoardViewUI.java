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
package it.uniba.di.cdg.xcore.econference.ui.views;

import it.uniba.di.cdg.xcore.ui.formatter.EmailFormatter;
import it.uniba.di.cdg.xcore.ui.formatter.LinkFormatter;
import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;
import it.uniba.di.cdg.xcore.ui.formatter.TextStyleRange;
import it.uniba.di.cdg.xcore.ui.widget.RichStyledText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * UI of the whiteboard view.
 */
public class WhiteBoardViewUI extends ViewPart {

    private Composite top = null;

    protected RichStyledText whiteBoardText = null;
 
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl( Composite parent ) {
        FillLayout layout = new FillLayout();
        top = new Composite( parent, SWT.NONE );
        top.setLayout( layout );
        whiteBoardText = new RichStyledText( top, SWT.BORDER | SWT.V_SCROLL );
        // Adding formatter to the Whiteboard
        whiteBoardText.addFormatListener( new LinkFormatter() );
        whiteBoardText.addFormatListener( new EmailFormatter() );
        whiteBoardText.addFormatListener( new RichFormatting() );
    }
    
    /**
     * This method applies the formatting style that corresponds to the string passed as parameter
     * 
     * @param s a String that represents a formatting style
     */
		  public void applyFormatting( String s ) {
		  Point sel = whiteBoardText.getSelectionRange();
		  String text=whiteBoardText.getText();
		  String newtext = new String();
		 
		  StyleRange[] styles = whiteBoardText.getStyleRanges(sel.x, sel.x+sel.y);
		  for(StyleRange sr: styles)
		  {
			  
		  	if(sr instanceof TextStyleRange && ((TextStyleRange)sr).code.equals(s))
		  	{
		  		if(sel.x>sr.start)
		  		{
		  			if(sel.y+sel.x<sr.length+sr.start)
		  			{
		  				//case **abSa**  => **ab**S**a**
		  				System.out.println("case **abSa**  => **ab**S**a**");
		  				
		  				newtext = s + whiteBoardText.getSelectionText() + s;
		  				whiteBoardText.replaceTextRange( sel.x, sel.y, newtext );
		  			}else
		  			{
		  				//case **abSS**  => **ab**SS
		  				System.out.println("case **abSS**  => **ab**SS");
		  				 text = whiteBoardText.getText();
		  				newtext=text.substring(0,sel.x)+s+text.substring(sel.x,sel.x+sel.y);
		  				int caretOffset = newtext.length();	  						 
		  				newtext+=((sel.x+sel.y+s.length()<whiteBoardText.getText().length())?text.substring(sel.x+sel.y+s.length()):"");
		  				whiteBoardText.setText(newtext);
		  				whiteBoardText.setCaretOffset(caretOffset);
		  				
		  			}
		  		}
		  		else
		  		{
		  			if(sel.y+sel.x<sr.length+sr.start)
		  			{
		  			//case **Sa**  => S**a**
		  				System.out.println("case **Sa**  => S**a**");
		  				 text = whiteBoardText.getText();
		  				newtext=((sel.x-s.length()>0)?text.substring(0,sel.x-s.length()):"")+text.substring(sel.x,sel.x+sel.y)+s+text.substring(sel.x+sel.y);
		  				int caretOffset = sel.x+sel.y-s.length();	 
		  				 whiteBoardText.setText(newtext);
		  				whiteBoardText.setCaretOffset(caretOffset);
		  			}else
		  			{
		  				//case a**S**a = > aSa
		  				System.out.println("case a**S**a = > aSa");
		  				 text = whiteBoardText.getText();
		  				newtext=((sel.x-s.length()>0)?text.substring(0,sel.x-s.length()):"")+text.substring(sel.x,sel.x+sel.y);
		  				int caretOffset =newtext.length();
		  				newtext+=((sel.x+sel.y+s.length()<whiteBoardText.getText().length())?text.substring(sel.x+sel.y+s.length()):"");
		  				whiteBoardText.setText(newtext);		  					
		  				whiteBoardText.setCaretOffset(caretOffset);
		  				
		  			}
		  		}
		  		
		  		return;
		  	}
		  }
		  
		  System.out.println("case S ==> **S**");
		  newtext = s + whiteBoardText.getSelectionText() + s;
		
		  if ((sel == null) || (sel.y == 0))
		      return;
		  
		  whiteBoardText.replaceTextRange( sel.x, sel.y, newtext );
		}
    
    public String getWhiteBoardTextContent() {
        return whiteBoardText.getText();
    }

    public void setWhiteBoardTextContent( String text ) {
        this.whiteBoardText.setText( text );
    }

    public void setSelectionRangWhiteBoardText(int start, int lenght){
        this.whiteBoardText.setSelectionRange( start, lenght );
    } 
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        whiteBoardText.setFocus();
    }
}