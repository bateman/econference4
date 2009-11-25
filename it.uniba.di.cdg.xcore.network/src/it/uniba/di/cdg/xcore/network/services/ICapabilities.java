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

/**
 * Interface for the capabilities set supported by each backend.
 */
public interface ICapabilities extends Iterable<ICapability> {
    /**
     * Check if there capabilities stored. 
     * 
     * @return <code>true</code> if there any capabilities, <code>false</code> otherwise
     */
    boolean hasCapabilities();

    /**
     * Add a new capability.
     * 
     * @param capability
     */
    void add( ICapability capability );

    /**
     * Check if the specified capability is contained in this set (it is intended for querying 
     * a backend about its supported capabilities).
     * 
     * @param c the capability to check
     * @return <code>true</code> if the capability is present, <code>false</code> otherwise
     */
    boolean contains( ICapability c );

    /**
     * Remove the capability from this set.
     * 
     * @param c 
     */
    void remove( ICapability c );
}