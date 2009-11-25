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
package it.uniba.di.cdg.xcore.util;


/**
 * Base interface for implementation of memementos: they are used to store the internal state
 * of an object without clients knowing anything about it (saving and restore around an operation's
 * execution). 
 * <p>
 * Information are stored information as (key,info) pairs: interface and implementation may change
 * as need arise (explicit different types needed). 
 */
public interface IMemento {
    /**
     * Store a string.
     * 
     * @param key 
     * @param string
     */
    void putString( String key, String string );

    /**
     * Retrieve a string from the memento.
     * 
     * @param key
     * @return the string or <code>null</code> if the key was not found
     */
    String getString( String key );

    /**
     * Store a generic object: this is useful for collections and other objects.
     * 
     * @param key
     * @param object
     */
    void putObject( String key, Object object );

    /**
     * Returns a stored object.
     * 
     * @param key
     * @return the object or <code>null</code> if the key was not found
     */
    Object getObject( String key );
}
