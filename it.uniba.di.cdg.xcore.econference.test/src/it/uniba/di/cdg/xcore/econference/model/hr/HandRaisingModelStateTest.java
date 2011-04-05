package it.uniba.di.cdg.xcore.econference.model.hr;

import it.uniba.di.cdg.xcore.econference.model.hr.IQuestion.QuestionStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.junit.Before;
import org.junit.Test;

public class HandRaisingModelStateTest {
        
    private IHandRaisingModel model;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        this.model = new HandRaisingModel();
    }
    
    @Test
    public void testAddQuestion() {
        // setup
        Question q1 = new Question( model, 1, "text", "who@unknown.net", QuestionStatus.PENDING );
        Question q2 = new Question( model, 1, "text2222", "who@unknown.net222", QuestionStatus.REJECTED );
        Question q3 = new Question( model, 2, "bla bla", "who2@unknown.net", QuestionStatus.APPROVED );
        // mock creation
        IHandRaisingModelListener mockListener = mock(IHandRaisingModelListener.class);
        
        //using mock object and verifying
        model.addListener( mockListener );        
        model.addQuestion( q1 );        
        assertEquals( 1, model.numberOfQuestions() );
        assertSame( q1, model.getQuestion( q1.getId() ) );
        verify(mockListener, times(1)).questionAdded( same(q1) );
        
        model.addQuestion( q2 );       
        assertEquals( 1, model.numberOfQuestions() );
        assertSame( q2, model.getQuestion( q2.getId() ) );
        verify(mockListener, times(1)).questionAdded( same(q2) );
        
        model.addQuestion( q3 );                   
        assertEquals( 2, model.numberOfQuestions() );
        assertSame( q3, model.getQuestion( q3.getId() ) );        
        verify(mockListener, times(1)).questionAdded( same(q3) );
    }   
    
    @Test
    public void testAddQuestion2() {
        // setup
        IQuestion q1 = mock(IQuestion.class);
        IQuestion q2 = mock(IQuestion.class);
        IQuestion q3 = mock(IQuestion.class);
        // mock creation
        IHandRaisingModelListener mockListener = mock(IHandRaisingModelListener.class);

        when(q1.getId()).thenReturn( 1 );
        when(q2.getId()).thenReturn( 1 );
        when(q3.getId()).thenReturn( 2 );
                
        //using mock object and verifying
        model.addListener( mockListener );        
        model.addQuestion( q1 );
        model.addQuestion( q2 );
        model.addQuestion( q3 );                   
        assertEquals( 3, model.numberOfQuestions() );
    }
    
    @Test
    public void testRemoveQuestion(){
        Question q1 = new Question( model, 1, "text", "who@unknown.net", QuestionStatus.PENDING );
        // mock creation
        IHandRaisingModelListener mockListener = mock(IHandRaisingModelListener.class);
        
        // Useless stub because void methods on mocks do nothing by default
        doNothing().when( mockListener).questionAdded( q1 );
        //using mock object
        model.addListener( mockListener );        
        model.addQuestion( q1 ); 
        model.removeQuestion( q1 );
        //verify
        assertEquals(0,model.numberOfQuestions());
        assertNull(model.getQuestion( q1.getId() ));
        verify(mockListener).questionRemoved( same(q1) );
    }
    
    @Test
    public void testGetQuestion() {

        Question q1 = new Question( model, 1, "text", "who@unknown.net", QuestionStatus.PENDING );
        // mock creation
        IHandRaisingModelListener mockListener = mock(IHandRaisingModelListener.class);
        model.addListener( mockListener );   

        // Useless stub because void methods on mocks do nothing by default
        doNothing().when( mockListener).questionAdded( (IQuestion)anyObject() );
        doNothing().when( mockListener).questionRemoved( (IQuestion)anyObject() );
        
        // When asking for an unknown id it will return null
        assertNull( model.getQuestion( 1 ) );
        
        model.addQuestion( q1 );
        
        // otherwise will return the question
        assertSame( q1, model.getQuestion( 1 ) );
    }
    
    @Test
    public void testIterator() {
        Question[] questions = new Question[] {
                new Question( model, 1, "one", "who@unknown.net", QuestionStatus.PENDING ),
                new Question( model, 2, "two", "who@unknown.net222", QuestionStatus.REJECTED ),
                new Question( model, 3, "three", "who2@unknown.net", QuestionStatus.APPROVED )
        };

        IHandRaisingModelListener mockListener = mock(IHandRaisingModelListener.class);
        model.addListener( mockListener );   

        // Useless stub because void methods on mocks do nothing by default
        doNothing().when( mockListener).questionAdded( (IQuestion)anyObject() );
        doNothing().when( mockListener).questionRemoved( (IQuestion)anyObject() );
                
        for (int i = 0; i < questions.length; i++)
            model.addQuestion( questions[i] );
        
        int i = 0;
        // ?
        for (IQuestion q : model) {
            assertSame( questions[i++], q );
        }
    }
    
    @Test
    public void testArrayOperation() {
        Question[] questions = new Question[] {
                new Question( model, 1, "one", "who@unknown.net", QuestionStatus.PENDING ),
                new Question( model, 2, "two", "who@unknown.net222", QuestionStatus.REJECTED ),
                new Question( model, 3, "three", "who2@unknown.net", QuestionStatus.APPROVED )
        };

        IHandRaisingModelListener mockListener = mock(IHandRaisingModelListener.class);
        model.addListener( mockListener );   

        // Useless stub because void methods on mocks do nothing by default
        doNothing().when( mockListener).questionAdded( (IQuestion)anyObject() );
        doNothing().when( mockListener).questionRemoved( (IQuestion)anyObject() );
        
        for (int i = 0; i < questions.length; i++)
            model.addQuestion( questions[i] );

        IQuestion[] retrieved = model.getQuestions();
        
        for (int i = 0; i < questions.length; i++)
            assertSame( questions[i], retrieved[i] );
    }
    
}
