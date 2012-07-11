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
package it.uniba.di.cdg.jabber.internal;

import it.uniba.di.cdg.xcore.network.model.AbstractBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IEntry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@see net.osslabs.jabber.client.model.IBuddyGroup} interface.
 */
public class BuddyGroup extends AbstractBuddyGroup {

    private String name;
    
    private Set<IBuddy> buddies;
    
    public BuddyGroup( String name ) {
        this.name = name;
        this.buddies = new HashSet<IBuddy>();
    }
    
    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyGroup#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyGroup#addBuddy(net.osslabs.jabber.client.model.IBuddy)
     */
    public void addBuddy( IBuddy buddy ) {
        buddies.add( buddy );
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyGroup#removeBuddy(net.osslabs.jabber.client.model.IBuddy)
     */
    public void removeBuddy( IBuddy buddy ) {
        buddies.remove( buddy );
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyGroup#contains(net.osslabs.jabber.client.model.IBuddy)
     */
    public boolean contains( IBuddy buddy ) {
        return buddies.contains( buddy );
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyGroup#hasBuddies()
     */
    public boolean hasBuddies() {
        return !buddies.isEmpty();
    }

    /* (non-Javadoc)
     * @see net.osslabs.jabber.client.model.IBuddyGroup#getBuddies()
     */
    public Collection<IBuddy> getBuddies() {
        return Collections.unmodifiableCollection( buddies );
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (other == null || !(other instanceof IBuddyGroup) )
            return false;
        IBuddyGroup that = (IBuddyGroup) other;
        return this.getName().equals( that.getName() );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    public int compareTo( IBuddyGroup that ) {
        return String.CASE_INSENSITIVE_ORDER.compare( this.getName(), that.getName() );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IEntry#getParent()
     */
    public IEntry getParent() {
        return null; // The roster is parent
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.model.IEntry#getChilds()
     */
    public IEntry[] getChilds() {
//        final Collection<IBuddy> buddies = ;
        IEntry[] array = new IEntry[buddies.size()];
        return buddies.toArray( array );
    }
}
