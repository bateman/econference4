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

import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import junit.framework.TestCase;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

/**
 * 
 */
public class ConnectionTest extends TestCase {
    static {
        //ProviderManager.addIQProvider( TypingNotificationProvider.TYPING_ELEMENT_NAME, CDG_NAMESPACE, TypingNotificationPacket.class );
        // ProviderManager.addIQProvider( SmackTypingNotificationProvider.TYPING_ELEMENT_NAME, CDG_NAMESPACE, new SmackTypingNotificationProvider() );
    }
    
    private static final int MAX_RUNS = 10; 
    
    /**
     * Our server ...
     */
    private static final ServerContext UGRES_SERVER = new ServerContext( "ugres.di.uniba.it", 5222 ,false );
    
    private JabberBackend harry;
    
    public static UserContext harryContext = new UserContext( "alessandro.brucoli", "studente" );
    
    private JabberBackend sammy;
    
    public static UserContext sammyContext = new UserContext( "aaaaa", "a" );

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
        sammy.disconnect();
    }

    public void testConnection() throws BackendException {
        harry.connect( UGRES_SERVER, harryContext );
        sammy.connect( UGRES_SERVER, sammyContext );
    }

    public static class SammyBot extends JabberBot {
        /**
         * @param context
         * @param account
         */
        public SammyBot() {
            super( UGRES_SERVER, ConnectionTest.sammyContext);
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#connect()
         */
        @Override
        protected void connect() throws Exception {
        	super.connect();

        	PacketListener pl = new PacketListener() {
        		public void processPacket( Packet packet ) {
        			System.out.println( "[Sammy] " + packet.toXML() );
        		}            
        	};
        	registerPacketListener( pl, TypingNotificationPacket.FILTER );


//        	PacketListener ml = new PacketListener() {
//        		public void processPacket( Packet packet ) {
//        			System.out.println( "[Sammy] processing backlog...");
//        			Message m = (Message) packet;
//        			DefaultBacklogPacket ext = (DefaultBacklogPacket) m.getExtension(DefaultBacklogPacket.ELEMENT_NAME,
//        					DefaultBacklogPacket.ELEMENT_NS);
//
//        			System.out.println( String.format( "[Harry] from %s : %s", m.getFrom(), ext.getBacklog().toString()) );
//        		}
//        	};
//        	registerPacketListener( ml, DefaultBacklogPacket.FILTER );
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#executeBot()
         */
        @Override
        protected void executeBot() throws Exception {
            for (int i = 0; i< MAX_RUNS; ++i) {
                System.out.println( "[Sammy] Running ..." );
                sleep( 1000 );
            }
            quit();
        }
    }

    public static class HarryBot extends JabberBot {
        
        private String target;

        /**
         * @param context
         * @param account
         */
        public HarryBot() {
            super( UGRES_SERVER, ConnectionTest.harryContext );
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#connect()
         */
      /*  @Override
        protected void connect() throws Exception {
            super.connect();
            
            PacketListener il = new PacketListener() {
                public void processPacket( Packet packet ) {
                    //System.out.println( "[Harry] " + packet.toXML() );
                }
            };
            registerPacketListener( il, TypingNotificationPacket.FILTER  );
        }*/

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.jabber.JabberBot#executeBot()
         */
        @Override
        protected void executeBot() throws Exception {
            for (int i = 0; i< MAX_RUNS; ++i) {
                System.out.println( "[Harry] Running ..." );
                sleep( 1000 );
                
                if (i % 2 == 0) {
//                	Message message = new Message();
//                    TypingNotificationPacket notification = new TypingNotificationPacket();
//                    notification.setWho( "harry" ); 
//                    message.addExtension(notification);
//                    sendPacket( message );
//               } else {
//                	DefaultBacklogPacket backlogPacket = new DefaultBacklogPacket();
//                	Backlog backlog = new Backlog();
//                	backlog.addUserStory(new DefaultUserStory("id1","story1", "Closed", "description1", "8"));
//                	backlog.addUserStory(new DefaultUserStory("id2","story2", "Open", "description2", "Unknown"));
//                	backlogPacket.setBacklog(backlog);
                    Message message = new Message();
                    message.setFrom( getAccount().getId() );
                    message.setTo( target );
//                    message.setBody( "Hello, Sammy: this is Harry's attempt nr. " + i );
                   // message.setType( Message.Type.groupchat );
//                    message.addExtension(backlogPacket);
                    sendPacket( message );
//                    System.out.println("[Harry] Sending message to "+ message.getTo() +": "+ message.getExtension(DefaultBacklogPacket.ELEMENT_NS).toXML());
               }
            }
            quit();
        }

        /**
         * @return Returns the target.
         */
        protected String getTarget() {
            return target;
        }

        /**
         * @param target The target to set.
         */
        protected void setTarget( String target ) {
            this.target = target;
        }
    }

    public static void main( String[] args ) throws Exception {
        SammyBot sammy = new SammyBot();
        HarryBot harry = new HarryBot();
        
        ProviderManager manager = ProviderManager.getInstance();
//        manager.addExtensionProvider(DefaultBacklogPacket.ELEMENT_NAME, DefaultBacklogPacket.ELEMENT_NS, new DefaultBacklogPacket.Provider());        
        harry.setTarget( JabberBot.jid( sammy.getAccount(), sammy.getContext() ) );
        //sammy.setTarget( JabberBot.jid( harry.getAccount(), harry.getContext() ) );
        
        sammy.start();
        harry.start();
        
        sammy.join();
        harry.join();
    }
}
