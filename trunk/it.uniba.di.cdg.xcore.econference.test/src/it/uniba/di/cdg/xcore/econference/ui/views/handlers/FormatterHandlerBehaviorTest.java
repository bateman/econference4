package it.uniba.di.cdg.xcore.econference.ui.views.handlers;


import org.eclipse.core.commands.ExecutionException;

import org.junit.Before;
import org.junit.Test;

public class FormatterHandlerBehaviorTest {
    //private FormatterHandler fh;
    
    @Before
    public void setUp() throws Exception {
        //this.fh = new FormatterHandler();
    }
    
    @Test
    public void testFormatText() throws ExecutionException{
        //TODO Non è possibile creare unmock di classi final come Command
        // ed ExecutionEvent, quindi non è possibile effettuare il test della
        // classe FormatterHandler

     
//        // Setup
//        ExecutionEvent event = new ExecutionEvent( Command.define("","",null, null), mock(Map.class), 
//                mock(Object.class), mock(Object.class) );
//        IWorkbenchPage page = mock(IWorkbenchPage.class);
//        IViewPart view = mock(IViewPart.class);
//        String IDWB = "it.uniba.di.cdg.xcore.econference.ui.views.whiteBoardView";
//        String IDTV = "it.uniba.di.cdg.xcore.m2m.ui.views.multiChatTalkView";
//        
//        // Stub
//        when(event.getCommand().getId()).thenReturn( "bold", "underline", "strike");
//        when(HandlerUtil.getActivePart( event ).getSite().getId()).thenReturn( IDWB );
//        when(HandlerUtil.getActiveWorkbenchWindow( event ).getActivePage()).thenReturn( page );
//        when(page.findViewReference( IDWB ).getView( true )).thenReturn( view );
//        // Exercise
//        this.fh.execute( event );
//        
//        // Verify
//        verify( event.getCommand().getId() ).equals( "bold" );
    }
    
}
