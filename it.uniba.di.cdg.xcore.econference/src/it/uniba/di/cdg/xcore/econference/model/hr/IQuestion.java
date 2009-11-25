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
package it.uniba.di.cdg.xcore.econference.model.hr;

import org.eclipse.core.runtime.IAdaptable;

/**
 * A question signaled my an user when using hand raising view.
 * <p>
 * A question is usually requested by some remote user to a moderator.
 */
public interface IQuestion extends IAdaptable {
    /**
     * Statuses that each question can assume (initially it is <code>PENDING</code>). 
     */
    enum QuestionStatus {
        PENDING, REJECTED, APPROVED
    };

    /**
     * Returns the unique id of question. The id is assigned once at construction time by
     * implementor's factories and will never change in a question's life.
     * 
     * @return the unique id
     */
    int getId();

    /**
     * A text containing the participant-formulated question.
     * 
     * @return the question's text
     */
    String getText();
    
    /**
     * Set the question text.
     * 
     * @param text
     */
    void setText( String text );
    
    /**
     * The id of requestor.
     * 
     * @return the requestor's id.
     */
    String getWho();
    
    /**
     * Set the id of the requestor.
     * 
     * @param userId
     */
    void setWho( String userId );

    /**
     * Changes the state of this question.
     * 
     * @param qs
     */
    void setStatus( QuestionStatus qs );
    
    /**
     * Returns the question's status.
     * 
     * @return the question state.
     */
    QuestionStatus getStatus();
    
    /**
     * A question doesn't live on its own and belongs to a model. 
     * 
     * @param model
     */
    void setModel( IHandRaisingModel model );
    
    /**
     * Returns the parent model. If this method returns a <b>not</b> <code>null</code> value then this 
     * question will notify the parent model about changes in its status. 
     * See {@see IHandRaisingModelListener}.
     * 
     * @return the model or <code>null</code> if no model is set.
     */
    IHandRaisingModel getModel();
}
