package it.uniba.di.cdg.xcore.econference.ui.views;

import junit.framework.TestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class WhiteBoardViewUITest extends TestCase{
    
    private WhiteBoardViewUI wbui;
    
    @org.junit.Before
    @Override
    protected void setUp() {
        Shell parent = new Shell();
        Composite composite = new Composite(parent, SWT.NULL);
        wbui = new WhiteBoardViewUI();
        wbui.createPartControl( composite ); 
    }
    
    public void testApplyFormatting(){
        wbui.setWhiteBoardTextContent( "sator arepo tenet opera rotas" );
        wbui.setSelectionRangWhiteBoardText( 0, 29 );
        wbui.applyFormatting( "*" );
        assertEquals( true, wbui.getWhiteBoardTextContent().equals( "*sator arepo tenet opera rotas*" ) );
        
        wbui.setWhiteBoardTextContent( "sator arepo tenet opera rotas" );
        wbui.setSelectionRangWhiteBoardText( 0, 29 );
        wbui.applyFormatting( "-" );
        assertEquals( true, wbui.getWhiteBoardTextContent().equals( "-sator arepo tenet opera rotas-" ) );
        
        wbui.setWhiteBoardTextContent( "sator arepo tenet opera rotas" );
        wbui.setSelectionRangWhiteBoardText( 0, 29 );
        wbui.applyFormatting( "_" );
        assertEquals( true, wbui.getWhiteBoardTextContent().equals( "_sator arepo tenet opera rotas_" ) );
    }
}
