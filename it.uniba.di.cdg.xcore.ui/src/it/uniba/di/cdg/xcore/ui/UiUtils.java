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
package it.uniba.di.cdg.xcore.ui;

import it.uniba.di.cdg.xcore.util.RunnableWithReturn;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * User interface utilities.
 * TODO Remove this class
 */
@Deprecated
public class UiUtils {
    @Deprecated
    public static void swtAsyncExec( Runnable runnable ) {
        getDisplay().asyncExec( runnable );
    }

    @Deprecated
    public static void swtSyncExec( Runnable runnable ) {
        getDisplay().syncExec( runnable );
    }

    @Deprecated
    public static Object swtSyncExec( RunnableWithReturn runnable ) {
        getDisplay().syncExec( runnable );
        return runnable.getReturnValue();
    }
    
    public static Display getDisplay() {
        Display display = Display.getCurrent();
        if (display == null)
            display = PlatformUI.getWorkbench().getDisplay();
        return display;
    }
    
    /**
     * Activate the application: this should usually translate in app icon blinking in the window
     * manager task bar.
     * 
     * For more information see https://bugs.eclipse.org/bugs/show_bug.cgi?id=96735 .
     */
    public static void demandAttention() {
        swtAsyncExec( new Runnable() {
            public void run() {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setActive();            
            }
        } );
    }
}
