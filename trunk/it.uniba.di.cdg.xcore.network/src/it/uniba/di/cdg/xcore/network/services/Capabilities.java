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
package it.uniba.di.cdg.xcore.network.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set of capabilities: only one instance of each capability is admitted. 
 */
public class Capabilities implements ICapabilities {
    /**
     * The set of capabilities (because we can only have one capability of each kind).
     */
    private Set<ICapability> caps;

    /**
     * Creates an empty set of capabilities.
     */
    public Capabilities() {
        super();
        this.caps = new HashSet<ICapability>();
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.network.ICapabilities#hasCapabilities()
     */
    public boolean hasCapabilities() {
        return !caps.isEmpty();
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.network.ICapabilities#add(net.osslabs.jabber.client.network.ICapability)
     */
    public void add( ICapability capability ) {
        caps.add( capability );
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.network.ICapabilities#contains(net.osslabs.jabber.client.network.ICapability)
     */
    public boolean contains( ICapability c ) {
        return caps.contains( c );
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.network.ICapabilities#remove(net.osslabs.jabber.client.network.ICapability)
     */
    public void remove( ICapability c ) {
        caps.remove( c );
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<ICapability> iterator() {
        return caps.iterator();
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.network.ICapabilities#size()
     */
    public int size() {
        return caps.size();
    }
}
