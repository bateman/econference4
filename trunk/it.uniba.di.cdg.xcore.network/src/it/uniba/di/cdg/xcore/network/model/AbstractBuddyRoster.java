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
package it.uniba.di.cdg.xcore.network.model;

import it.uniba.di.cdg.xcore.network.IBackend;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.PlatformObject;

/**
 * Just add <code>PlatformObject</code> to the hierarchy.
 */
public abstract class AbstractBuddyRoster extends PlatformObject implements
		IBuddyRoster {
	protected Map<String, IBuddyGroup> groups;
	protected Map<String, IBuddy> buddies;
	protected Collection<IBuddyRosterListener> listeners;
	protected IBackend backend;
	
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddyRoster#addListener(it.uniba.di.cdg.xcore.network.model.IBuddyRosterListener)
     */
    public void addListener( IBuddyRosterListener listener ) {
        listeners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddyRoster#removeListener(it.uniba.di.cdg.xcore.network.model.IBuddyRosterListener)
     */
    public void removeListener( IBuddyRosterListener listener ) {
        listeners.remove( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IBuddyRoster#getBackend()
     */
    public IBackend getBackend() {
        return backend;
    }
    
	/* (non-Javadoc)
	 * @see it.uniba.di.cdg.xcore.network.model.IBuddyRoster#getBuddies()
	 */
	public Collection<IBuddy> getBuddies() {
		return buddies.values();
	}

}
