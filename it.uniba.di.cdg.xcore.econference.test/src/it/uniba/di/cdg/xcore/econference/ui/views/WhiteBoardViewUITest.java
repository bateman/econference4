package it.uniba.di.cdg.xcore.econference.ui.views;

import junit.framework.Assert;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

public class WhiteBoardViewUITest {
    
    private WhiteBoardViewUI wbui;
    
    
    //@Override
    @Before
    public void setUp() {
    	
        Shell parent = new Shell();
       
        Composite composite = new Composite(parent, SWT.NULL);
      
        wbui = new WhiteBoardViewUI();
       
        wbui.createPartControl( composite ); 
    }
    
    @Test
    public void testApplyFormatting(){
    	
    	String text= "sator arepo tenet opera rotas";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText( 0, text.length() );
        wbui.applyFormatting( "*" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "*sator arepo tenet opera rotas*" ) );
        
       
        
        wbui.setWhiteBoardTextContent( "sator arepo tenet opera rotas" );
        wbui.setSelectionRangWhiteBoardText(  0, text.length() );
        wbui.applyFormatting( "-" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "-sator arepo tenet opera rotas-" ) );
        
        wbui.setWhiteBoardTextContent( "sator arepo tenet opera rotas" );
        wbui.setSelectionRangWhiteBoardText(  0, text.length() );
        wbui.applyFormatting( "_" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "_sator arepo tenet opera rotas_" ) );
    
        
        text= "**abSS**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  4, 2 );        
        wbui.applyFormatting( "**");        
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "**ab**SS" ) );
        
        
        text= "aaaa**abSS**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  8, 2 );        
        wbui.applyFormatting( "**");        
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aaaa**ab**SS" ) );
        
        text= "**abSS**aaaa";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  4, 2 );        
        wbui.applyFormatting( "**");        
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "**ab**SSaaaa" ) );
        
        
        text= "**abSa**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  4, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "**ab**S**a**" ) );
        
        text= "aaaa**abSa**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  8, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aaaa**ab**S**a**" ) );
        
        text= "**abSa**aaaa";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  4, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "**ab**S**a**aaaa" ) );
        
        
        text= "**Sa**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  2, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "S**a**" ) );
        
        text= "aaaa**Sa**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  6, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aaaaS**a**" ) );
        
        text= "**Sa**aaaa";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  2, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "S**a**aaaa" ) );
        
        
        text= "a**S**a";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  3, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aSa" ) );
        
        text= "a**S**aaaaa";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  3, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aSaaaaa" ) );
        
        text= "aaaaa**S**a";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  7, 1 );
        wbui.applyFormatting( "**" );
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aaaaaSa" ) );
        
        text= "aaaa**abSS**";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  8, 2 );        
        wbui.applyFormatting( "**");        
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "aaaa**ab**SS" ) );
        
        text= "**abSS**aaaa";
        wbui.setWhiteBoardTextContent( text );
        wbui.setSelectionRangWhiteBoardText(  4, 2 );        
        wbui.applyFormatting( "**");        
        Assert.assertEquals( true, wbui.getWhiteBoardTextContent().equals( "**ab**SSaaaa" ) );
        
    }
}
