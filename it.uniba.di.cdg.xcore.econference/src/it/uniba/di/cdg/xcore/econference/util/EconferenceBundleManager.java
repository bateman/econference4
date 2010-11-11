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

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class EconferenceBundleManager {

    public final static String PLANNINGPOKER = "planningpoker";

    static Boolean PLANNINGPOKER_ENABLED = false;

    public static String getEconferenceExtention() {

        IExtensionRegistry registry = Platform.getExtensionRegistry();

        String prefix = "it.uniba.di.cdg.econference";

        String bundleRunning = "none";
        for (String bundle : registry.getNamespaces()) {
            if (bundle.contains( prefix )) {
                System.out.println( "bundle: " + bundle );
                bundleRunning = bundle.replace( prefix + ".", "" );

            }
        }
        setService( bundleRunning );
        return bundleRunning;
    }

    public static Boolean planningPokerEnabled() {

        return PLANNINGPOKER_ENABLED;

    }

    public static void setService( String item ) {

        if (item.equals( PLANNINGPOKER )) {
            PLANNINGPOKER_ENABLED = true;
        }

    }

}
