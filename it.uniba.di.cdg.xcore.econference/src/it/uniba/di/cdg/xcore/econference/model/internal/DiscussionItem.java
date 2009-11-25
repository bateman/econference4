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
package it.uniba.di.cdg.xcore.econference.model.internal;

import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;

/**
 * Implementation of a discussion item.
 */
public class DiscussionItem implements IDiscussionItem {

    private String text;

    /**
     * Makes up a new discussion item with the specified text.
     * 
     * @param text
     */
    public DiscussionItem( String text ) {
        this.text = text;
    }
    

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IDiscussionItem#setText(java.lang.String)
     */
    public void setText( String text ) {
        this.text = text;
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.econference.core.mvc.IDiscussionItem#getText()
     */
    public String getText() {
        
        return text;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object o ) {
        if (o == null || !(o instanceof IDiscussionItem)) 
            return false;
        IDiscussionItem other = (IDiscussionItem) o;
        return this.getText().equals( other.getText() );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getText().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getText();
    }
}
