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
package it.uniba.di.cdg.jabber;

import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;

import java.util.Iterator;

import junit.framework.TestCase;

import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.junit.Test;

/**
 * 
 */
public class ChatTest{
    static {
        //ProviderManager.addIQProvider( TypingNotificationProvider.TYPING_ELEMENT_NAME, CDG_NAMESPACE, TypingNotificationPacket.class );
        // ProviderManager.addIQProvider( SmackTypingNotificationProvider.TYPING_ELEMENT_NAME, CDG_NAMESPACE, new SmackTypingNotificationProvider() );
    }
    
    private static final int MAX_RUNS = 5; 
    
    /**
     * Our server ...
     */
    private static final ServerContext UGRES_SERVER = new ServerContext( "jabber.org", 5222 ,false );
    
    private JabberBackend harry;
    
    public static UserContext harryContext = new UserContext( "giuseppe83", "giuseppe83" );
    
    private static JabberBackend sammy;
    
    public static UserContext sammyContext = new UserContext( "giuseppe84", "giuseppe84" );

    private ServerContext server;
   
    Visitor visitor;
    
    public static class Moderator extends JabberBot {
    	
    	
    	private MultiUserChat muc;
        /**
         * @param context
         * @param account
         */
        public Moderator() {
            super( UGRES_SERVER, MUCTest.sammyContext);
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#connect()
         */
        @Override
        protected void connect() throws Exception {
        	super.connect();

        	
        	muc = new MultiUserChat(conn, "testppcollab@conference.jabber.org");
        	muc.create("testbot");
        	
            //muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

        	//muc.join("sammy");
        	        	
        	
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#executeBot()
         */
        @Override
        protected void executeBot() throws Exception {
            for (int i = 0; i< MAX_RUNS; ++i) {
                //System.out.println( "[Sammy] Running ..." );
                sleep( 1000 );
                int count = muc.getOccupantsCount();
                System.out.println("[Moderatot] Total: "+count);
                String occupants = "[Moderator] Occupants:";
                for (Iterator<String> iterator = muc.getOccupants(); iterator
						.hasNext();) {
                	occupants += " "+(String) iterator.next()+" ";
				}
                System.out.println(occupants);
            }
            quit();
        }
    }

    public static class Visitor extends JabberBot {
        
        private MultiUserChat muc;

        /**
         * @param context
         * @param account
         */
        public Visitor() {
            super( UGRES_SERVER, MUCTest.harryContext );
        }
        
        @Override
        protected void connect() throws Exception {        	
        	super.connect();
        	
        	muc = new MultiUserChat(conn, "testppcollab@conference.jabber.org");
        	muc.join("testbot2");
        	//muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
        }


        @Override
        protected void executeBot() throws Exception {
            for (int i = 0; i< MAX_RUNS; ++i) {
                //System.out.println( "[Harry] Running ..." );
                sleep( 1000 );
                int count = muc.getOccupantsCount();
                System.out.println("[Visitor] Total: "+count);
                String occupants = "[Visitor] Occupants:";
                for (Iterator<String> iterator = muc.getOccupants(); iterator
						.hasNext();) {
                	String current = (String) iterator.next();
                	occupants += " "+current+" ";
                	Occupant occupant = muc.getOccupant(current);
                	occupants += " Role: "+occupant.getRole()+"\n ";
				}
                System.out.println(occupants);
            }
            quit();
        }

    }

    @Test
    public void botTest() throws Exception {
        Moderator moderator = new Moderator();
        visitor = new Visitor();              
        moderator.start();
        moderator.join();
        visitor.start();
        visitor.join();     
    }
}
