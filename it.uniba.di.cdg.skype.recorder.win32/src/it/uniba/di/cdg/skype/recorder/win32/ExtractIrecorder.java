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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ExtractIrecorder generate an irecorder folder and extract the program inside, 
 * this operation is necessary because you want to launch an exe file contained 
 * in a jar
 * @author Savino Saponara
 *
 */
public class ExtractIrecorder {
	private InputStream src;
	private FileOutputStream out;
	private File tempFile;
	private byte[] buffer;
	private int rc;
	private String dirTemp;

	public ExtractIrecorder(){
		buffer = new byte[32768];
		dirTemp = System.getProperty("user.dir") + "\\irecorder\\";
	}
	
	public String extract(){		
		File dir = new File(dirTemp);
		if (dir.isDirectory()) 
			return dirTemp + "irecorder.exe";
		
		System.out.println("iFreeRecorder extraction start...");
		File exe = null;
		new File(dirTemp).mkdir();
		try {
			extractFile("lame_enc.dll");
			exe = extractFile("irecorder.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exe.toString();
	}
	
	public File extractFile(String file) throws IOException{						
		System.out.println("extraction file: " + file);
		src = ExtractIrecorder.class.getResource(file).openStream(); 		
		tempFile = new File(dirTemp + file);
		out = new FileOutputStream(tempFile); 		 		 
		
		while((rc = src.read(buffer)) > 0) 
		    out.write(buffer, 0, rc); 		
		
		src.close(); 
		out.close(); 
		
		System.out.println("file extracted: " + tempFile);  								
		return tempFile;
	}	
		
}
