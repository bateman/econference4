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

import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * jUnit testcase for {@see it.uniba.di.cdg.xcore.econference.model.hr.HandRaisingModel}.
 */
public class HandRaisingModelTestCase extends MockObjectTestCase {

    private IHandRaisingModel model;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        this.model = new HandRaisingModel();
    }

    public void testAddQuestion() {
        Question q = new Question( model, 1, "text", "who@unknown.net", QuestionStatus.PENDING );
        Question qe = new Question( model, 1, "text2222", "who@unknown.net222", QuestionStatus.REJECTED );
        Question qne = new Question( model, 2, "bla bla", "who2@unknown.net", QuestionStatus.APPROVED );
        
        Mock mock = mock( IHandRaisingModelListener.class );
        mock.expects( once() ).method( "questionAdded" ).with( same( q ) );
        mock.expects( once() ).method( "questionAdded" ).with( same( qe ) );
        mock.expects( once() ).method( "questionAdded" ).with( same( qne ) );
        IHandRaisingModelListener listener = (IHandRaisingModelListener) mock.proxy();
        model.addListener( listener );
        
        model.addQuestion( q ); // Will be added
        assertEquals( 1, model.numberOfQuestions() );
        assertSame( q, model.getQuestion( q.getId() ) );
        
        model.addQuestion( qe ); // Will Replace the previous
        assertEquals( 1, model.numberOfQuestions() );
        assertSame( qe, model.getQuestion( qe.getId() ) );
        
        model.addQuestion( qne ); // Will be added
        assertEquals( 2, model.numberOfQuestions() );
        assertSame( qne, model.getQuestion( qne.getId() ) );
    }
    
    public void testRemoveQuestion() {
        Question q = new Question( model, 1, "text", "who@unknown.net", QuestionStatus.PENDING );

        Mock mock = mock( IHandRaisingModelListener.class );
        mock.stubs().method( "questionAdded" ); // We already tested this
        mock.expects( once() ).method( "questionRemoved" ).with( same( q ) );
        IHandRaisingModelListener listener = (IHandRaisingModelListener) mock.proxy();
        model.addListener( listener );
        
        model.addQuestion( q );
        
        model.removeQuestion( q );
        assertEquals( 0, model.numberOfQuestions() );
    }

    public void testGetQuestion() {
        Question q = new Question( model, 1, "text", "who@unknown.net", QuestionStatus.PENDING );

        Mock mock = mock( IHandRaisingModelListener.class );
        mock.stubs().method( "questionAdded" ); // We already tested this
        mock.stubs().method( "questionRemoved" ); // We already tested this
        IHandRaisingModelListener listener = (IHandRaisingModelListener) mock.proxy();
        model.addListener( listener );

        // When asking for an unknown id it will return null
        assertNull( model.getQuestion( 1 ) );
        
        model.addQuestion( q );
        
        // otherwise will return the questiont
        assertSame( q, model.getQuestion( 1 ) );
    }

    /**
     * The iterator will let the clients to walk through the clients in the insertion-order. 
     */
    public void testIterator() {
        Question[] questions = new Question[] {
                new Question( model, 1, "one", "who@unknown.net", QuestionStatus.PENDING ),
                new Question( model, 2, "two", "who@unknown.net222", QuestionStatus.REJECTED ),
                new Question( model, 3, "three", "who2@unknown.net", QuestionStatus.APPROVED )
        };

        Mock mock = mock( IHandRaisingModelListener.class );
        mock.stubs().method( "questionAdded" ); // We already tested this
        mock.stubs().method( "questionRemoved" ); // We already tested this
        IHandRaisingModelListener listener = (IHandRaisingModelListener) mock.proxy();
        model.addListener( listener );
        
        for (int i = 0; i < questions.length; i++)
            model.addQuestion( questions[i] );
        
        int i = 0;
        for (IQuestion q : model) {
            assertSame( questions[i++], q );
        }
    }

    /**
     * Requesting an array will give the question in insertion order.
     */
    public void testArrayOperation() {
        Question[] questions = new Question[] {
                new Question( model, 1, "one", "who@unknown.net", QuestionStatus.PENDING ),
                new Question( model, 2, "two", "who@unknown.net222", QuestionStatus.REJECTED ),
                new Question( model, 3, "three", "who2@unknown.net", QuestionStatus.APPROVED )
        };

        Mock mock = mock( IHandRaisingModelListener.class );
        mock.stubs().method( "questionAdded" ); // We already tested this
        mock.stubs().method( "questionRemoved" ); // We already tested this
        IHandRaisingModelListener listener = (IHandRaisingModelListener) mock.proxy();
        model.addListener( listener );
        
        for (int i = 0; i < questions.length; i++)
            model.addQuestion( questions[i] );

        IQuestion[] retrieved = model.getQuestions();
        
        for (int i = 0; i < questions.length; i++)
            assertSame( questions[i], retrieved[i] );
    }
}
