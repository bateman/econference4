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
package it.uniba.di.cdg.xcore.network;

/**
 * A backend descriptor contains all the information about a backend: this is useful when
 * we want to query some generic information that do not requires instantiating the backend
 * and activate its plug-in. The kind of information described here are the members that 
 * compose the extensions' list of the <code>it.uniba.di.cdg.xcore.network.backends</code>
 * extension point.
 * <p>
 * This descriptors are collected by {@see it.uniba.di.cdg.xcore.network.IBackendRegistry}
 * when the plug-in activator ({@see it.uniba.di.cdg.xcore.network.NetworkPlugin} is activated.
 */
public interface IBackendDescriptor {
    
    public static final String ID_ATTR = "id";
    
    public static final String NAME_ATTR = "name";
    
    public static final String CLASS_ATTR = "class";
    
    public static final String SECURE_ATTR = "secure";
    
    /**
     * @return Returns the id.
     */
    String getId();

    /**
     * @return Returns the name.
     */
    String getName();

    /**
     * @return Returns the className.
     */
    String getClassName();

    /**
     * Check if the backend associated to this descriptor supports secure connections.
     * 
     * @return <code>true</code> if the backend supports secure connections, <code>false</code> otherwise
     */
    boolean isSecure();
}
