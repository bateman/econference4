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
package it.uniba.di.cdg.collaborativeworkbench.boot.ui;

import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEventListener;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;

public class BackendEventListener implements IBackendEventListener {

	private boolean messageIncoming = false; 

	private boolean activeMenu = false;

	@Override
	public void onBackendEvent(IBackendEvent event) {
		// TODO Auto-generated method stub
		if (event instanceof MultiChatMessageEvent) {
			messageIncoming=true;
		}

		if (event instanceof ChatMessageReceivedEvent) {
			messageIncoming=true;
		}

		if (event instanceof BackendStatusChangeEvent) {
			BackendStatusChangeEvent changeEvent = (BackendStatusChangeEvent) event;
			activeMenu=changeEvent.isOnline();
		}
	}
	public boolean getMessageIncoming() {
		return messageIncoming;
	}
	public void setNewMessageIncoming(boolean value) {
		messageIncoming= value;
	}
	public boolean getActiveMenu(){
		return activeMenu;
	}
	public void start() {
		// TODO Auto-generated method stub
		for (IBackendDescriptor d : NetworkPlugin.getDefault().getRegistry().getDescriptors())
			NetworkPlugin.getDefault().getHelper().registerBackendListener(d.getId(), this); 
	}

}
