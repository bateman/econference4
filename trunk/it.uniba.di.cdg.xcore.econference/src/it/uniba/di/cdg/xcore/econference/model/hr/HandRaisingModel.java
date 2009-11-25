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

import it.uniba.di.cdg.xcore.aspects.ThreadSafetyAspect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Implementation of the hand raising model.
 */
public class HandRaisingModel implements IHandRaisingModel {
    
    private List<IQuestion> questions;

    private Set<IHandRaisingModelListener> listeners;
    
    public HandRaisingModel() {
        this.questions = new ArrayList<IQuestion>();
        this.listeners = new HashSet<IHandRaisingModelListener>();
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#addQuestion(java.lang.String, java.lang.String)
     */
    public void addQuestion( IQuestion q ) {
        int p = questions.indexOf( q );
        // Remove the old question if already present.
        if (p > -1) {
            questions.remove( p );
        }
        questions.add( q );
        
        for (IHandRaisingModelListener l : listeners)
            l.questionAdded( q );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#removeQuestion(int)
     */
    public void removeQuestion( IQuestion q ) {
        questions.remove( q );

        for (IHandRaisingModelListener l : listeners)
            l.questionRemoved( q );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#getQuestion(int)
     */
    public IQuestion getQuestion( int id ) {
        for (IQuestion q : questions)
            if (q.getId() == id)
                return q;
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#getQuestions()
     */
    public IQuestion[] getQuestions() {
        IQuestion[] array = new IQuestion[questions.size()];
        return questions.toArray( array );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#addListener(it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModelListener)
     */
    public void addListener( IHandRaisingModelListener l ) {
        listeners.add( l );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#removeListener(it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel)
     */
    public void removeListener( IHandRaisingModelListener l ) {
        listeners.remove( l );
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<IQuestion> iterator() {
        return questions.iterator();
    }
    
    /**
     * Notify listeners that a question has changed its status.
     * <p>
     * This method is not intended to be used by clients but only by question objects 
     * to notify listeners about changes in their internal state.
     * 
     * @param q the changed question
     */
    public void notifyQuestionChanged( Question q ) {
        for (IHandRaisingModelListener l : listeners)
            l.questionModified( q );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#numberOfQuestions()
     */
    public int numberOfQuestions() {
        return questions.size();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel#dispose()
     */
    public void dispose() {
        listeners.clear();
        questions.clear();
    }
    
    /**
     * Provides internal thread synchronization.
     */
    @Aspect
    public static class OwnThreadSafety extends ThreadSafetyAspect {
        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#readOperations()
         */
        @Override
        @Pointcut( "execution( public * HandRaisingModel.get*(..) )" +
                "|| execution( public int HandRaisingModel.numberOfQuestions(..) )" +
                "|| execution( public Iterator HandRaisingModel.iterator() )")
        protected void readOperations() {}

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#writeOperations()
         */
        @Override
        @Pointcut( "execution( public void HandRaisingModel.set*(..) )" +
                "|| execution( public void HandRaisingModel.add*(..) )" +
                "|| execution( public void HandRaisingModel.remove*(..) )" )
        protected void writeOperations() {}
    }
}
