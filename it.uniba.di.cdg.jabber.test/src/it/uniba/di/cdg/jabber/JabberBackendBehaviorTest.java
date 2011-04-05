package it.uniba.di.cdg.jabber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.junit.Before;
import org.junit.Test;

public class JabberBackendBehaviorTest {
    
    // SUT
    private JabberBackendSubClass backend;
    
    private static final int PRESENCE_PRIORITY_MAX = 128;

    @Before
    public void setUp() throws Exception {
        backend = new JabberBackendSubClass();
    }
    
    @Test
    public void testXMPPConnectionOk(){        
        // Setup
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);
        XMPPConnection conn = mock(XMPPConnection.class);        
        Roster roster = mock(Roster.class);
        ServerContext scon = mock(ServerContext.class);
        UserContext ucon = mock(UserContext.class);
        Presence presencePriorityMax = new Presence(Presence.Type.available);
        presencePriorityMax.setPriority(PRESENCE_PRIORITY_MAX);
        // Stub
        when(conn.getRoster()).thenReturn( roster );
        // Installation
        backend.setMockConnection( conn );
        backend.setHelper( helper );
        // Exercise
        try{
        backend.connect( scon, ucon );
        }catch (BackendException e){fail("BackendException shouldn't be thrown");}
         catch (Exception e){fail("Only BackendException");}
        backend.disconnect();
        //Verify
        try{
        verify(conn).connect();
        } catch (XMPPException e){ }        
        verify(conn).addConnectionListener( backend );
        verify(conn).sendPacket( presencePriorityMax );
        // A new PacketFilter is created during the connection so it can't be mocked
        // Note that smack will add a packet listener for his own so we just check ours is added
        verify(conn, atLeast(1)).addPacketListener( eq(backend), (PacketFilter)anyObject() );
        // A new event is created during the notification so it can't be mocked
        // Note that when everything goes good there should be 2 status change events
        verify(helper, times(2)).notifyBackendEvent( (BackendStatusChangeEvent)anyObject() );
        verify(conn).disconnect();
    }
    
    @Test
    public void testXMPPConnectionWhileConnected(){        
        // Setup
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);
        XMPPConnection conn = mock(XMPPConnection.class);        
        Roster roster = mock(Roster.class);
        ServerContext scon = mock(ServerContext.class);
        UserContext ucon = mock(UserContext.class);
        Presence presencePriorityMax = new Presence(Presence.Type.available);
        presencePriorityMax.setPriority(PRESENCE_PRIORITY_MAX);
        // Stub
        when(conn.getRoster()).thenReturn( roster );
        // Installation
        backend.setMockConnection( conn );
        backend.setHelper( helper );
        // Exercise
        try{
        backend.connect( scon, ucon );
        backend.connect( scon, ucon );
        }catch (BackendException e){fail("BackendException shouldn't be thrown");}
         catch (Exception e){fail("Only BackendException");}
        backend.disconnect();
        //Verify
        try{
           verify(conn, times(2)).connect();
        } catch (XMPPException e){ }        
        verify(conn, times(2)).addConnectionListener( backend );
        verify(conn, times(2)).sendPacket( presencePriorityMax );
        // A new PacketFilter is created during the connection so it can't be mocked
        // Note that smack will add a packet listener for his own so we just check ours is added
        verify(conn, atLeast(2)).addPacketListener( eq(backend), (PacketFilter)anyObject() );
        // Note that if XMPPConnection is already connected, it will be disconnected before
        // a new connection can be done
        verify(helper, times(4)).notifyBackendEvent( (BackendStatusChangeEvent)anyObject() );
        verify(conn, times(2)).disconnect();
    }
    
    
    /* Note that the connection is mocked so we don't care about contexts specific data
     * but we care about exception handling when smack connect() fails
     */
    @Test
    public void testXMPPConnectionFails() {        
        // Setup
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);
        XMPPConnection conn = mock(XMPPConnection.class);        
        Roster roster = mock(Roster.class);
        ServerContext scon = mock(ServerContext.class);
        UserContext ucon = mock(UserContext.class);
        // Stub
        when(conn.getRoster()).thenReturn( roster );
        try {
            doThrow(new XMPPException()).when(conn).connect();
        } catch (XMPPException e) { }
        // Installation
        backend.setMockConnection( conn );
        backend.setHelper( helper );
        // Exercise
        try{
        backend.connect( scon, ucon );
        fail("BackendException should be thrown because smack connect will fail");
        }catch (BackendException e){}
         catch (Exception e){fail("Only BackendException");}                 
    }
    
    @Test
    public void testProcessPacketOnExtensionProtocolEventNullName(){  
        // Setup
        Message msg = mock(Message.class);
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);     
        // Stub   
        when(msg.getType()).thenReturn( Message.Type.chat );
        // We just need a not null extension name to fire the event
        when(msg.getProperty( JabberBackend.EXTENSION_NAME )).thenReturn(null);
        // Installation
        backend.setHelper( helper );
        // Exercise
        backend.processPacket( msg );
        // Verify        
        verify(helper, never()).notifyBackendEvent( (ChatExtensionProtocolEvent)anyObject() );        
    }
    
    @Test
    public void testProcessPacketOnChatComposingEvent(){  
        // Setup
        Message msg = mock(Message.class);
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);
        PacketExtension ext = mock(PacketExtension.class);
        // Stub   
        when(msg.getType()).thenReturn( Message.Type.chat );
        // We just need a not null packet extension
        when(msg.getExtension(TypingNotificationPacket.ELEMENT_NAME,
                    TypingNotificationPacket.ELEMENT_NS)).thenReturn( ext );
        // Installation
        backend.setHelper( helper );
        // Exercise
        backend.processPacket( msg );
        // Verify        
        verify(helper).notifyBackendEvent( (ChatComposingEvent)anyObject() );        
    }
    
    @Test
    public void testProcessPacketOnNullMessage(){  
        // Setup
        Message msg = mock(Message.class);
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);
        // Stub   
        when(msg.getType()).thenReturn( Message.Type.chat );
        when(msg.getBody()).thenReturn( "" );        
        // Installation
        backend.setHelper( helper );
        // Exercise
        backend.processPacket( msg );
        // Verify        
        verify(helper, never()).notifyBackendEvent( (IBackendEvent)anyObject() );        
    } 
    
    @Test
    public void testProcessPacketOnGoodMessage(){  
        // Setup
        Message msg = mock(Message.class);
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);
        // Stub   
        when(msg.getType()).thenReturn( Message.Type.chat );
        when(msg.getBody()).thenReturn( "message" );        
        // Installation
        backend.setHelper( helper );
        // Exercise
        backend.processPacket( msg );
        // Verify        
        verify(helper).notifyBackendEvent( (ChatMessageReceivedEvent)anyObject() );        
    }
    

    /* We can't test this until the buddy roster becomes visible
    @Test
    public void testProcessPacketOnExtensionProtocolEventGoodName(){  
        // Setup
        Message msg = mock(Message.class);
        INetworkBackendHelper helper = mock(INetworkBackendHelper.class);     
        // Stub   
        when(msg.getType()).thenReturn( Message.Type.chat );
        // We just need a not null extension name to fire the event
        when(msg.getProperty( JabberBackend.EXTENSION_NAME )).thenReturn( "name" );
        when(msg.getFrom()).thenReturn( "io" );
        // Installation
        backend.setHelper( helper );
        // Exercise
        backend.processPacket( msg );
        // Verify        
        verify(helper).notifyBackendEvent( (ChatExtensionProtocolEvent)anyObject() );        
    }*/
}
