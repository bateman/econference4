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

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

/**
 * 
 */
public class MUCTest extends TestCase {
    static {
        //ProviderManager.addIQProvider( TypingNotificationProvider.TYPING_ELEMENT_NAME, CDG_NAMESPACE, TypingNotificationPacket.class );
        // ProviderManager.addIQProvider( SmackTypingNotificationProvider.TYPING_ELEMENT_NAME, CDG_NAMESPACE, new SmackTypingNotificationProvider() );
    }
    
    private static final int MAX_RUNS = 3; 
    
    /**
     * Our server ...
     */
    private static final ServerContext UGRES_SERVER = new ServerContext( "jabber.org", 5222 ,false );
    
    private JabberBackend harry;
    
    public static UserContext harryContext = new UserContext( "participant1", "participant1" );
    
    private static JabberBackend sammy;
    
    public static UserContext sammyContext = new UserContext( "alessandrob", "studente" );

    private ServerContext server;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {   
        harry = new JabberBackend();  
        harry.setHelper(new NetworkBackendHelper());
       
        sammy = new JabberBackend(); 
        sammy.setHelper(new NetworkBackendHelper());
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        sammy.disconnect();
        harry.disconnect();
    }

    public void testConnection() throws BackendException {
        harry.connect( UGRES_SERVER, harryContext );
        sammy.connect( UGRES_SERVER, sammyContext );
    }

    public static class SammyBot extends JabberBot {
    	
    	
    	private MultiUserChat muc;
        /**
         * @param context
         * @param account
         */
        public SammyBot() {
            super( UGRES_SERVER, MUCTest.sammyContext);
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#connect()
         */
        @Override
        protected void connect() throws Exception {
        	super.connect();

        	
        	muc = new MultiUserChat(conn, "testppcollab@conference.jabber.org");
        	//muc.create("sammy");
        	muc.join("sammy");
        	        	
        	
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
                System.out.println("[Sammy] Total: "+count);
                String occupants = "[Sammy] Occupants:";
                for (Iterator<String> iterator = muc.getOccupants(); iterator
						.hasNext();) {
                	occupants += " "+(String) iterator.next()+" ";
				}
                System.out.println(occupants);
            }
            quit();
        }
    }

    public static class HarryBot extends JabberBot {
        
        private MultiUserChat muc;

        /**
         * @param context
         * @param account
         */
        public HarryBot() {
            super( UGRES_SERVER, MUCTest.harryContext );
        }
        
        @Override
        protected void connect() throws Exception {        	
        	super.connect();
        	
        	muc = new MultiUserChat(conn, "testppcollab@conference.jabber.org");
        	muc.join("harry");
        	//muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
        }


        @Override
        protected void executeBot() throws Exception {
            for (int i = 0; i< MAX_RUNS; ++i) {
                //System.out.println( "[Harry] Running ..." );
                sleep( 1000 );
                int count = muc.getOccupantsCount();
                System.out.println("[Harry] Total: "+count);
                String occupants = "[Harry] Occupants:";
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

    public static void main( String[] args ) throws Exception {
        SammyBot sammy = new SammyBot();
        HarryBot harry = new HarryBot();              
        
        
        harry.start();
        
        sammy.start();
        
        
        harry.join();
        
        sammy.join();
    }
}
