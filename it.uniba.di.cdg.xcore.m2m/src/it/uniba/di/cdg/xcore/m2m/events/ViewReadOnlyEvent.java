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
package it.uniba.di.cdg.xcore.m2m.events;

/**
 * This event is notified by the service: the receiving listener should (must) enable / disable
 * the views based on the specified id. 
 */
public class ViewReadOnlyEvent implements IManagerEvent {
    /**
     * The view id, conforming the plugin.xml definition (i.e. "it.uniba.di.cdg.xcore.m2m.ui.views.multiChatTalkView").
     */
    private String viewId;
    
    /**
     * A flag indicating if the view must
     */
    private boolean readOnly;

    /**
     * @param viewId
     * @param readOnly
     */
    public ViewReadOnlyEvent( String viewId, boolean readOnly ) {
        super();
        this.viewId = viewId;
        this.readOnly = readOnly;
    }

    /**
     * @return Returns the enable.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @return Returns the viewId.
     */
    public String getViewId() {
        return viewId;
    }
}
