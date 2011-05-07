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
package it.uniba.di.cdg.aspects;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.aspectj.lang.Signature;

public aspect ExceptionLoggingAspect {

	private Logger logger;
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	private FileHandler handler;

	pointcut exceptionTraced()
		: execution(* *.*(..)) && !within( it.uniba.di.cdg.aspects..*);

	after() throwing(Throwable ex) : exceptionTraced() {
		this.logger = Logger.getLogger(thisJoinPoint.getClass()
				.getName());
		try {
			File f = new File("logs");
			if (!f.exists()) {
				f.mkdir();
			}
			handler = new FileHandler("logs/"
					+ thisJoinPoint.getThis().getClass().getPackage().getName()
					+ ".exceptionTrace.log", true);

			handler.setFormatter(new SimpleFormatter());
			this.logger.addHandler(handler);
		} catch (IOException e) {
			System.out.println("Cannot write log file");
		}

		if (lastLoggedException.get() != ex) {
			lastLoggedException.set(ex);
			Signature sig = thisJoinPointStaticPart.getSignature();
			logger.log(Level.INFO,
					"Exception trace aspect [" + sig.toShortString() + "]", ex);
		}

	}
}
