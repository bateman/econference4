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
import java.util.Vector;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;

/**
 * Abstracts the common idiom for visiting the extenders of specific
 * extension points. Client need only to provide the <code>IExtensionVisitor</code> 
 * implementation for customizing the behaviour.
 * 
 * @see it.uniba.di.cdg.xcore.util.IExtensionVisitor
 */
public class ExtensionProcessor implements IExtensionProcessor {
    /** 
     * The singleton instance. 
     */
    private static IExtensionProcessor s_instance;
    
    /**
     * Returns the only instance of this processor.
     * 
     * @return the extension processor
     */
    public synchronized static IExtensionProcessor getDefault() {
        if (s_instance == null)
            s_instance = new ExtensionProcessor();
        return s_instance;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.util.IExtensionProcessor#process(java.lang.String, it.uniba.di.cdg.econference.core.util.IExtensionVisitor)
     */
    public void process( String xpid, IExtensionVisitor visitor ) throws Exception {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        
        IExtensionPoint extensionPoint = registry.getExtensionPoint( xpid );
        if (extensionPoint == null)
            return;
        
        IConfigurationElement[] members = extensionPoint.getConfigurationElements();
        HashMap<String, IConfigurationElement> backends = new HashMap<String, IConfigurationElement>();
        for (int i = 0; i < members.length; i++) {
            IConfigurationElement member = members[i];            
            backends.put(member.getAttribute( "name" ), member);
        }

        Vector<String> defaultBackand = new Vector<String>();
        
        SelectBackendDialog dlg = new SelectBackendDialog( new Shell() , defaultBackand, backends.keySet());
        dlg.open();
        
        IConfigurationElement member = backends.get(defaultBackand.get(0));
        
        IExtension extension = member.getDeclaringExtension();

        visitor.visit( extension, member );
        
        //viene inizializzato solo un Backend
        /*for (int i = 0; i < members.length; i++) {
            IConfigurationElement member = members[i];

            IExtension extension = member.getDeclaringExtension();

            visitor.visit( extension, member );
        }*/
    }

    /**
     * Avoid direct instantiation.
     */
    private ExtensionProcessor() {
    }
}
