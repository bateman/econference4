/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2010 Collaborative Development Group - Dipartimento di Informatica, 
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
package it.uniba.di.cdg.xcore.ui.service;

import it.uniba.di.cdg.xcore.ui.formatter.RichFormatting;

import java.util.ArrayList;
import java.util.List;

public class LatexService {

	public static String DEFAULT_SERVICE_BASE_URL = "http://latex.codecogs.com/gif.latex?";

	public final static String PATTERN_MARKER =RichFormatting.LATEX_MARKER.getCode();
	private final static int PATTERN_MARKER_LENGTH =PATTERN_MARKER.length();
	

	public String url;
	public String expression;

	public static List<String> extractPattern(String text) {
		List<String> result = new ArrayList<String>();



		int offset = text.indexOf(PATTERN_MARKER);
		while(offset!=-1)
		{
		
			int endoffset = text.indexOf(PATTERN_MARKER,offset+PATTERN_MARKER.length());
			if(endoffset!=-1)
			{
				result.add(text.substring(offset,endoffset+PATTERN_MARKER.length()));
			}
			offset=text.indexOf(PATTERN_MARKER,endoffset+PATTERN_MARKER.length());
		}
		
		
		
		return result;
	}
	


	public static String getUrlFor(String pattern) {

		return DEFAULT_SERVICE_BASE_URL
				+ pattern.substring(PATTERN_MARKER_LENGTH, pattern.length() - PATTERN_MARKER_LENGTH)
						.replace("%", "%25").replace("&", "%26")
						.replace(" ", "%20");
	}

}
