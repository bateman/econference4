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

import it.uniba.di.cdg.xcore.econference.model.hr.IHandRaisingModel;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion;
import it.uniba.di.cdg.xcore.econference.model.hr.Question;
import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

/**
 * jUnit test case for {@see it.uniba.di.cdg.xcore.econference.model.hr.Question}.
 */
public class QuestionStateTest {
    /**
     * Check that constructions do what they are designed to do.
     */
    @Test
    public void testFieldsInitialized() {
        IQuestion q1 = new Question( 2 );
        
        assertEquals( 2, q1.getId() );
        assertEquals( "", q1.getText() );
        assertEquals( "", q1.getWho() );
        
        IQuestion q2 = new Question( null, 3, "Could I ask something?", "room@conference.192.168.0.5/Pippo", QuestionStatus.PENDING );
        assertEquals( 3, q2.getId() );
        assertEquals( "Could I ask something?", q2.getText() );
        assertEquals( "room@conference.192.168.0.5/Pippo", q2.getWho() );
    }

    /**
     * Check that equality works by id.
     */
    @Test
    public void testEquals() {
        IQuestion q = new Question( 3 );
        IQuestion qe = new Question( 3 );
        
        IQuestion qne = new Question( 4 );
        
        assertEquals( q, qe ); 
        assertFalse( q.equals( qne ) );
    }

    /**
     * Check that questions are hashed by their id.
     */
    @Test
    public void testHash() {
        Set<IQuestion> questions = new HashSet<IQuestion>();
        
        // Questions are hashed by their id: so different id, different questions
        questions.add( new Question( 1 ) );
        questions.add( new Question( 2 ) );
        assertEquals( 2, questions.size() );
        
        // If I put in a set a question with the same Id it will replace the existing one.
        questions.add( new Question( 1 ) );
        assertEquals( 2, questions.size() );
    }
    
    /**
     * Changing properties must produce the right events when they must be produced!
     */
    @Test
    public void testStatusChangeNotification() {
        // When no model is specified the notification for changed properties is disabled
        Question q = new Question( null, 1, "To be or not to be?", "hamleth@shakespeare.net", QuestionStatus.PENDING  );

        IHandRaisingModel model = mock(IHandRaisingModel.class);
        
        // Setting the model will _never_ produce event notification
        q.setModel( model );
        
        q.setStatus( QuestionStatus.APPROVED );
        q.setWho( "pippo@server.net" );
        q.setText( "It cannot be!" );
        verify(model,times(3)).notifyQuestionChanged( q );
    }
}