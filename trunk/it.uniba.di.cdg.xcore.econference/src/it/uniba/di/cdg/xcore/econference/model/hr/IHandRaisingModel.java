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

/**
 * Provides a model for handling the incoming and outgoing questions. 
 * TODO perhaps we should provide a method for  
 */
public interface IHandRaisingModel extends Iterable<IQuestion> {
    /**
     * Add a question without specifying the
     *  
     * @param q
     */
    void addQuestion( IQuestion q );
    
    void removeQuestion( IQuestion q );
    
    IQuestion getQuestion( int id );
    
    IQuestion[] getQuestions();
    
    int numberOfQuestions();
    
    void addListener( IHandRaisingModelListener l );
    
    void removeListener( IHandRaisingModelListener l );

    /**
     * Perform clean-up, both of questions' list and listeners. This method is intended to 
     * be called by the {@see it.uniba.di.cdg.xcore.econference.IEConferenceService} and not
     * to be used by user code directly.
     */
    void dispose();
    
    /**
     * Notify listeners that a question has changed its status.
     * <p>
     * This method is not intended to be used by clients but only by question objects 
     * to notify listeners about changes in their internal state.
     * 
     * @param q the changed question
     */
    void notifyQuestionChanged( Question q );
}
