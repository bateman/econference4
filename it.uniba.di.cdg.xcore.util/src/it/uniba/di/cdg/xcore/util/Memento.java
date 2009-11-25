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

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of a simple memory memento.
 */
public class Memento implements IMemento {

    private Map<String,String> strings = new HashMap<String,String>();

    private Map<String,Object> objects = new HashMap<String,Object>();
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.util.IMemento#putString(java.lang.String, java.lang.String)
     */
    public void putString( String key, String string ) {
        strings.put( key, string );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.util.IMemento#getString(java.lang.String)
     */
    public String getString( String key ) {
        return strings.get( key );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.util.IMemento#putObject(java.lang.String, java.lang.Object)
     */
    public void putObject( String key, Object object ) {
        objects.put( key, object );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.util.IMemento#getObject(java.lang.String)
     */
    public Object getObject( String key ) {
        return objects.get( key );
    }
}
