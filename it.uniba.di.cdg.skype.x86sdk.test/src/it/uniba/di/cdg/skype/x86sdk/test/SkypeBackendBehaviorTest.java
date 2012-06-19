package it.uniba.di.cdg.skype.x86sdk.test;


import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;
import it.uniba.di.cdg.skype.x86sdk.util.ExtensionConstants;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.UserContext;
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
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SkypeBackendBehaviorTest {
	// SUT
	private SkypeBackend backend;

	INetworkBackendHelper backendHelper;

	private static final String senderName = "pippo pappi";
	private static final String senderId = "1";
	private static final String skypeId = "ppp";
	UserContext userCont = new UserContext( "econferencetester1", "econferencetester1" );


	@Before
	public void setUp() throws Exception {
		backend=new SkypeBackend();
		backendHelper = mock(INetworkBackendHelper.class);
		backend.setHelper(backendHelper);
		backend.connect(null,userCont);
	}

	@After
	public void tearDown() throws Exception {
		backend.disconnect();

		Thread.sleep(10000);

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName,backend.skype.GetConversationByParticipants(participants, true, false));

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

		// Verify
		verify(backendHelper).notifyBackendEvent( (MultiChatUserLeftEvent)anyObject() );        
	}

	@Test(timeout=6000)
	public void testHelperM2MNotificationOnPresenceMessageAvailable(){
		// Setup
		INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
		String content = buildAPresenceMessage(ExtensionConstants.PRESENCE_MESSAGE, ExtensionConstants.M_TO_M, "available");        
		backend.setHelper( backendHelper );
		// Exercise
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

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
		String [] participants={ skypeId };
		backend.processMessageReceived( content, senderId, senderName, backend.skype.GetConversationByParticipants(participants, true, false) );

		// Verify
		verify(backendHelper).notifyBackendEvent( (ChatMessageReceivedEvent)anyObject() );        
	}
}
