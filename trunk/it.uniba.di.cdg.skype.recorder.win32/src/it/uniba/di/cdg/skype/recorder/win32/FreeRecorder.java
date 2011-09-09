/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2006 - 2011 Collaborative Development Group - Dipartimento di Informatica, 
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

package it.uniba.di.cdg.skype.recorder.win32;

//import it.uniba.di.cdg.skype.ISkypeRecorder;
import java.io.IOException;

import it.uniba.di.cdg.xcore.ui.UiPlugin;


public class FreeRecorder /*implements ISkypeRecorder*/ {
	private Process freeRecorder;
	private String freeRecorderPath;
	private ExtractIrecorder extractIrec;
	
	public FreeRecorder() {
		extractIrec = new ExtractIrecorder();
	}

	//@Override
	public void setRecorderPath(String path){
		freeRecorderPath = path;		
	}
	
	//@Override
	public String getRecorderPath(){
		return freeRecorderPath;
	}
	
	//@Override
	public void recorderStart(){						
		try { 			
			freeRecorderPath = extractIrec.extract().toString();		
			freeRecorder = Runtime.getRuntime().exec(freeRecorderPath);
			System.out.println("iFreeRecorder Started");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
		
	
	//@Override
	public void recorderStop(){
		freeRecorder.destroy();
	}
		
	//@Override
	public void recorderStartConfirmDialog(){
		String title = "iFreeRecord";
		String question = "iFreeRecorder allows you recording all Skype calls. Do you want start iFreeRecorder?";
		boolean answer = UiPlugin.getUIHelper().askYesNoQuestion(title,question);
		if (answer) recorderStart();
	}
}
