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
package it.uniba.di.cdg.xcore.econference.util;

import java.io.IOException;
import java.util.List;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;

public class FolderMonitor implements Runnable {

	private String folder;
	private WatchService watchService;
	private IFolderMonitorListener listener;

	public FolderMonitor(IFolderMonitorListener listener, String folder) {
		this.listener = listener;
		this.folder = folder;
		init();
	}

	private void init() {
		watchService = FileSystems.getDefault().newWatchService();
		Path path = Paths.get(folder);
		try {
			path.register(watchService, StandardWatchEventKind.ENTRY_CREATE,
					StandardWatchEventKind.ENTRY_MODIFY);
		} catch (UnsupportedOperationException uox) {
			System.err.println("File watching not supported!\nPath " + folder
					+ "won't be monitored.");
		} catch (IOException iox) {
			System.err.println("I/O errors in monitoring folder " + folder
					+ "(won't be monitored)");
		}
	}

	@Override
	public void run() {
		for (;;) {
			// take() will block until a file has been created/deleted
			WatchKey signalledKey;
			try {
				signalledKey = watchService.take();
			} catch (InterruptedException ix) {
				// we'll ignore being interrupted
				continue;
			} catch (ClosedWatchServiceException cwse) {
				// other thread closed watch service
				System.out.println("watch service closed, terminating.");
				break;
			}

			// get list of events from key
			List<WatchEvent<?>> list = signalledKey.pollEvents();

			// VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
			// key to be reported again by the watch service
			signalledKey.reset();

			// we'll simply print what has happened; real applications
			// will do something more sensible here
			for (WatchEvent<?> e : list) {
				String message = "";
				if (e.kind() == StandardWatchEventKind.ENTRY_CREATE
						|| e.kind() == StandardWatchEventKind.ENTRY_DELETE
						|| e.kind() == StandardWatchEventKind.ENTRY_MODIFY) {
					Path context = (Path) e.context();
					message = context.toString() + " change occurred.";
					System.out.println(message);
					// hold on a sec before notifying
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					listener.notifyFolderChange();
				} else if (e.kind() == StandardWatchEventKind.OVERFLOW) {
					message = "OVERFLOW: more changes happened than we could retreive";
					System.out.println(message);
				}
			}
		}

	}

}
