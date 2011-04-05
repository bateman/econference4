package it.uniba.di.cdg.skype.test;


import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.skype.util.ExtensionConstants;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.chat.ChatMessageReceivedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatComposingEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatInvitationDeclinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceRevokedEvent;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeBackendBehaviorTest {
    // SUT
    private SkypeBackend backend;
    
    private static final String senderName = "pippo pappi";
    private static final String senderId = "1";
    private static final String skypeId = "pippo";
    
    @Before
    public void setUp() throws Exception {
        backend = new SkypeBackend();
    }
    /*
     * Since we can't mock the connection, we can't stub it to throw specific exceptions
     * So, to run this test you need to have Skype running or it will fail throwing a FileNotFoundException
     * that cannot be catched here or it will hang on if you run it as a JUnit Plugin Test because the Skype component
     * will initialize and try to find a running Skype program
     * */
    @Test(timeout=10000)
    public void testSkypeConnection(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        ServerContext scon = mock(ServerContext.class);
        UserContext ucon = mock(UserContext.class);
        // Installation        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.connect( scon, ucon );
        } catch (BackendException e) {fail("Connection fail or Skype not running");}
          catch (Exception e) {fail("Only BackendException");}
       // Verify
       verify(backendHelper).notifyBackendEvent( (IBackendEvent)anyObject() );
    }
    
    @Test(timeout=10000)
    public void testSkypeDisconnection(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        ServerContext scon = mock(ServerContext.class);
        UserContext ucon = mock(UserContext.class);
        // Installation        
        backend.setHelper( backendHelper );
        // Exercise        
        try {
            backend.connect( scon, ucon );
            backend.disconnect();
        } catch (BackendException e) {fail("Connection fail or Skype not running");}
          catch (Exception e) {fail("Only BackendException");}
       // Verify
       verify(backendHelper, times(2)).notifyBackendEvent( (IBackendEvent)anyObject() );
    }

    private String buildAMessage(String extensionName, String chatType){
       String content =    "<?xml version=\"1.0\"?>";
       content = content + "<extension>";
       content = content + "<extensionName>" + extensionName + "</extensionName>";
       content = content + "<chatType>" + chatType + "</chatType>";
       content = content + "</extension>"; 
       return content;
    }
    
    private String buildAPresenceMessage(String extensionName, String chatType, String presence){
        String content =    "<?xml version=\"1.0\"?>";
        content = content + "<extension>";
        content = content + "<extensionName>" + extensionName + "</extensionName>";
        content = content + "<chatType>" + chatType + "</chatType>";
        content = content + "<type>" + presence + "</type>";
        content = content + "</extension>"; 
        return content;
     }
    
    private String buildARegularSkypeMessage(){
        String content = "Hello!";
        return content;
     }
    
    @Test(timeout=5000)
    public void testHelperOne2OneNotificationOnChatComposing(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);        
        String content = buildAMessage(ExtensionConstants.CHAT_COMPOSING, ExtensionConstants.ONE_TO_ONE);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (ChatComposingEvent)anyObject() );        
    }
    
    @Test(timeout=5000)
    public void testHelperOne2OneNotificationOnRoomInvite(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);       
        String content = buildAMessage(ExtensionConstants.ROOM_INVITE, ExtensionConstants.ONE_TO_ONE);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (InvitationEvent)anyObject() );        
    }
    
    @Test(timeout=5000)
    public void testHelperOne2OneNotificationOnRoomInviteDecline(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);       
        String content = buildAMessage(ExtensionConstants.ROOM_INVITE_DECLINE, ExtensionConstants.ONE_TO_ONE);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatInvitationDeclinedEvent)anyObject() );        
    }    

    @Test(timeout=5000)
    public void testHelperOne2OneNotificationOnChatMessage(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);       
        String content = buildAMessage(ExtensionConstants.CHAT_MESSAGE, ExtensionConstants.ONE_TO_ONE);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (ChatMessageReceivedEvent)anyObject() );        
    }
    
    @Test(timeout=5000)
    public void testHelperOne2OneNotificationOnCoreExtension(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        // We force a ChatExtensionProtocolEvent with an unknown ExtensionName
        String content = buildAMessage("unknown", ExtensionConstants.ONE_TO_ONE);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (ChatExtensionProtocolEvent)anyObject() );        
    }

    @Test(timeout=5000)
    public void testHelperM2MNotificationOnChatComposing(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        String content = buildAMessage(ExtensionConstants.CHAT_COMPOSING, ExtensionConstants.M_TO_M);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatComposingEvent)anyObject() );        
    }

    @Test(timeout=5000)
    public void testHelperM2MNotificationOnPresenceMessageUnavailable(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        String content = buildAPresenceMessage(ExtensionConstants.PRESENCE_MESSAGE, ExtensionConstants.M_TO_M, ExtensionConstants.PRESENCE_UNAVAILABLE);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatUserLeftEvent)anyObject() );        
    }
    
    @Test(timeout=5000)
    public void testHelperM2MNotificationOnPresenceMessageAvailable(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        String content = buildAPresenceMessage(ExtensionConstants.PRESENCE_MESSAGE, ExtensionConstants.M_TO_M, "available");        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper, never()).notifyBackendEvent( (MultiChatUserLeftEvent)anyObject() );        
    }

    @Test(timeout=5000)
    public void testHelperM2MNotificationOnChatMessage(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);       
        String content = buildAMessage(ExtensionConstants.CHAT_MESSAGE, ExtensionConstants.M_TO_M);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatMessageEvent)anyObject() );        
    }

    @Test(timeout=5000)
    public void testHelperM2MNotificationOnRevokeVoice(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);       
        String content = buildAMessage(ExtensionConstants.REVOKE_VOICE, ExtensionConstants.M_TO_M);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatVoiceRevokedEvent)anyObject() );        
    }

    @Test(timeout=5000)
    public void testHelperM2MNotificationOnGrantVoice(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);       
        String content = buildAMessage(ExtensionConstants.GRANT_VOICE, ExtensionConstants.M_TO_M);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatVoiceGrantedEvent)anyObject() );        
    }
    
    @Test(timeout=5000)
    public void testHelperM2MNotificationOnCoreExtension(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        // We force a ChatExtensionProtocolEvent with an unknown ExtensionName
        String content = buildAMessage("unknown", ExtensionConstants.M_TO_M);        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (MultiChatExtensionProtocolEvent)anyObject() );        
    }
    

    @Test(timeout=5000)
    public void testHelperM2MNotificationOnRegularSkypeMessage(){
        // Setup
        INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
        String content = buildARegularSkypeMessage();        
        backend.setHelper( backendHelper );
        // Exercise
        try {
            backend.processMessageReceived( content, senderId, senderName, Skype.chat( skypeId ) );
        } catch (SkypeException e) { }
        // Verify
        verify(backendHelper).notifyBackendEvent( (ChatMessageReceivedEvent)anyObject() );        
    }
}
