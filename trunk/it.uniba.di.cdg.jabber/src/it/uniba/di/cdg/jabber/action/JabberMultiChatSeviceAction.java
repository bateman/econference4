package it.uniba.di.cdg.jabber.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import it.uniba.di.cdg.jabber.JabberBackend;
import it.uniba.di.cdg.jabber.internal.XMPPUtils;
import it.uniba.di.cdg.jabber.ui.JoinChatRoomDialog;
import it.uniba.di.cdg.smackproviders.TypingNotificationPacket;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatExtensionProtocolEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatInvitationDeclinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatMessageEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatModeratorGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatModeratorRevokedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatNameChangedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatOwnershipGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatOwnershipRevokedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatSubjectUpdatedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatTypingEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserJoinedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatUserLeftEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceGrantedEvent;
import it.uniba.di.cdg.xcore.network.events.multichat.MultiChatVoiceRevokedEvent;
import it.uniba.di.cdg.xcore.network.services.IRoomInfo;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.DefaultParticipantStatusListener;
import org.jivesoftware.smackx.muc.DefaultUserStatusListener;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jivesoftware.smackx.muc.UserStatusListener;

public class JabberMultiChatSeviceAction implements IMultiChatServiceActions{

	private JabberBackend backend;
	private MultiUserChat smackMultiChat;
	private String userId;
	
	/* Definizione di tutti i listeners usati per la stanza*/
	PacketListener messageListener = new PacketListener() {
		
		@Override
		public void processPacket(Packet packet) {
			Message message = (Message)packet;
			IBackendEvent event = new MultiChatMessageEvent(JabberBackend.ID, message.getBody(), message.getFrom());
			backend.getHelper().notifyBackendEvent(event);
		}
	};
	
	
	UserStatusListener  smackUserStatusListener = new DefaultUserStatusListener() {
        /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultUserStatusListener#moderatorGranted()
         */
        @Override
        public void moderatorGranted() {
        	IBackendEvent event = new MultiChatModeratorGrantedEvent(JabberBackend.ID, userId);
        	backend.getHelper().notifyBackendEvent(event);
        }

        /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultUserStatusListener#moderatorRevoked()
         */
        @Override
        public void moderatorRevoked() {
            IBackendEvent event = new MultiChatModeratorRevokedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }

        /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultUserStatusListener#ownershipGranted()
         */
        @Override
        public void ownershipGranted() {
            IBackendEvent event = new MultiChatOwnershipGrantedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }

        /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultUserStatusListener#ownershipRevoked()
         */
        @Override
        public void ownershipRevoked() {
            IBackendEvent event = new MultiChatOwnershipRevokedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }

        /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultUserStatusListener#voiceGranted()
         */
        @Override
        public void voiceGranted() {
            IBackendEvent event = new MultiChatVoiceGrantedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }

        /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultUserStatusListener#voiceRevoked()
         */
        @Override
        public void voiceRevoked() {
            IBackendEvent event = new MultiChatVoiceRevokedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }
    };
	
	ParticipantStatusListener participantStatusListener = new DefaultParticipantStatusListener(){
		 /* (non-Javadoc)
         * @see org.jivesoftware.smackx.muc.DefaultParticipantStatusListener#nicknameChanged(java.lang.String, java.lang.String)
         */
        @Override
        public void nicknameChanged( String userId, String userName ) {
            IBackendEvent event = new MultiChatNameChangedEvent(JabberBackend.ID, userId, userName);
            backend.getHelper().notifyBackendEvent(event);
        }

        @Override
        public void moderatorGranted(String userId){
        	IBackendEvent event = new MultiChatModeratorGrantedEvent(JabberBackend.ID, userId);
        	backend.getHelper().notifyBackendEvent(event);
        }
        
        @Override
        public void moderatorRevoked(String userId) {
            IBackendEvent event = new MultiChatModeratorRevokedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }
        
        @Override
        public void ownershipGranted(String userId) {
            IBackendEvent event = new MultiChatOwnershipGrantedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }
        
        @Override
        public void ownershipRevoked(String userId) {
            IBackendEvent event = new MultiChatOwnershipRevokedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }

        @Override
        public void joined( String userId ) {         
        	String name = XMPPUtils.getUserNameFromChatString( userId );
        	String role = smackMultiChat.getOccupant(userId).getRole();
            IBackendEvent event = new MultiChatUserJoinedEvent(JabberBackend.ID, userId, name, role);
            backend.getHelper().notifyBackendEvent(event);
        }
                    

        @Override
        public void left( String userId ) {
        	IBackendEvent event = new MultiChatUserLeftEvent(JabberBackend.ID, userId);
        	backend.getHelper().notifyBackendEvent(event);
        }

        @Override
        public void voiceGranted( String userId ) {
        	IBackendEvent event = new MultiChatVoiceGrantedEvent(JabberBackend.ID, userId);
        	backend.getHelper().notifyBackendEvent(event);
        }

        @Override
        public void voiceRevoked( String userId ) {
            IBackendEvent event = new MultiChatVoiceRevokedEvent(JabberBackend.ID, userId);
            backend.getHelper().notifyBackendEvent(event);
        }
	};
	
	SubjectUpdatedListener subjectUpdatedListener = new SubjectUpdatedListener() {
		
		@Override
		public void subjectUpdated(String subject, String from) {
			IBackendEvent event = new MultiChatSubjectUpdatedEvent(JabberBackend.ID, from, subject);
			backend.getHelper().notifyBackendEvent(event);
		}
	};
	
	InvitationRejectionListener invitationRejectionListener = new InvitationRejectionListener() {
		
		@Override
		public void invitationDeclined(String invitee, String reason) {
			IBackendEvent event = new MultiChatInvitationDeclinedEvent(invitee, reason, JabberBackend.ID);
			backend.getHelper().notifyBackendEvent(event);
		}
	};
	
	PacketListener multiChatTypingListener = new PacketListener() {
		
		@Override
		public void processPacket(Packet packet) {
			final TypingNotificationPacket typingPacket = (TypingNotificationPacket) packet
            .getExtension( TypingNotificationPacket.ELEMENT_NAME,
            		TypingNotificationPacket.ELEMENT_NS );
			IBackendEvent event = new MultiChatTypingEvent(JabberBackend.ID, typingPacket.getWho());
			backend.getHelper().notifyBackendEvent(event);
		}
	};
	
	PacketListener extensionProtocolListener = new PacketListener() {
		
		@Override
		public void processPacket(Packet packet) {
			Message message = (Message)packet;
			
			if(message.getType() == Type.groupchat){
				if(message.getProperty(JabberBackend.EXTANSION_NAME) != null){
					HashMap<String, String> prop = new HashMap<String, String>();
	    			Collection<String> propName = message.getPropertyNames();
	    			Iterator<String> it = propName.iterator();
	    			while(it.hasNext()){
	    				String val = it.next();
	    				prop.put(val, (String)message.getProperty(val));
	    			}
					IBackendEvent event = new MultiChatExtensionProtocolEvent(
							prop, (String)message.getProperty(JabberBackend.EXTANSION_NAME)
								, JabberBackend.ID, message.getFrom());
					backend.getHelper().notifyBackendEvent(event);
					
				}
			}
		}
	};
	
    /**
     * Filters typing packets for multichats. We need this since that typing packet is implemented 
     * as a Message and we need a way to distinguish them from normal packets.
     */
    private static class MultiChatTypingFilter extends AndFilter {
        /**
         * Makes up a new filter for monitoring typing events in multichats. The parameter <code>from</code\ 
         * is needed since the server will notify the same emitter of the notification packet :O and
         * we don't want this to happen.
         * 
         * @param acceptTo the user id to accept in incoming packet field <code>to</code>
         * @param ignoreFrom the user id to ignore in incoming packet field <code>from</code>
         */
        public MultiChatTypingFilter( final String acceptTo, final String ignoreFrom ) {
            super();
            addFilter( new PacketTypeFilter( Message.class ) );
            addFilter( TypingNotificationPacket.FILTER );
            addFilter( new PacketFilter() {
                public boolean accept( Packet packet ) {
                    return acceptTo.equals( packet.getTo() );
                }
            } );
            addFilter( new PacketFilter() {
                public boolean accept( Packet packet ) {
                    return !ignoreFrom.equals( packet.getFrom() );
                }
            } );
        }
    }
    
    /**
     * Jabber/XMPP adapter implementation.
     */
    private static class RoomInfoAdapter implements IRoomInfo {
    	
        /**
         * Wrapped smack room info.
         */
        private RoomInfo smackRoomInfo;

        public RoomInfoAdapter( RoomInfo smackRoomInfo ) {
            this.smackRoomInfo = smackRoomInfo;
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService.IRoomInfo#getDescription()
         */
        public String getDescription() {
            return smackRoomInfo.getDescription();
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService.IRoomInfo#getRoomId()
         */
        public String getRoomId() {
            return smackRoomInfo.getRoom();
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService.IRoomInfo#getSubject()
         */
        public String getSubject() {
            return smackRoomInfo.getSubject();
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService.IRoomInfo#isMembersOnly()
         */
        public boolean isMembersOnly() {
            return smackRoomInfo.isMembersOnly();
        }

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.m2m.IMultiChatService.IRoomInfo#isModerated()
         */
        public boolean isModerated() {
            return smackRoomInfo.isModerated();
        }
    }
    
    private PacketFilter extensionPacketFilter = new PacketFilter() {
		
		@Override
		public boolean accept(Packet packet) {
			return (String)packet.getProperty(JabberBackend.EXTANSION_NAME) != null;
		}
	}; 
	
    
	public JabberMultiChatSeviceAction(JabberBackend backend) {
		super();
		this.backend = backend;
	}

	@Override
	public void sendMessage(String room, String message) {
		
		try {
			smackMultiChat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	public void grantVoice(String room, String to ){
		
		try {
			smackMultiChat.grantVoice(to);
		} catch (XMPPException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void revokeVoice(String room, String to) {
		
		try {
			smackMultiChat.revokeVoice(to);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void invite(String room, String to, String reason) {	
		smackMultiChat.invite(to, reason);
	}

	@Override
	public void join(String roomName, String password, String nickName, String userId, boolean moderator) {
        
		if(!roomName.equals("")){
			this.userId = userId;
			smackMultiChat = new MultiUserChat( backend.getConnection(), roomName);
		}else{
			final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			JoinChatRoomDialog dlg = new JoinChatRoomDialog(window);
			if(dlg.open() == Window.OK){
				MultiChatContext context = dlg.getContext();
				smackMultiChat = new MultiUserChat( backend.getConnection(), context.getRoom());
			}else{
				return;
			}
			
		}
		
        //Add all listeners for this room
		smackMultiChat.addMessageListener(messageListener);
		smackMultiChat.addUserStatusListener(smackUserStatusListener);
        smackMultiChat.addParticipantStatusListener(participantStatusListener);
        smackMultiChat.addSubjectUpdatedListener(subjectUpdatedListener);
		smackMultiChat.addInvitationRejectionListener(invitationRejectionListener);
		backend.getConnection().addPacketListener(multiChatTypingListener, 
				new MultiChatTypingFilter(backend.getUserJid(), userId));
		backend.getConnection().addPacketListener(extensionProtocolListener, extensionPacketFilter );
        
		
		try {
			smackMultiChat.create(nickName);
			smackMultiChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			try {
				smackMultiChat.join(nickName);
				if (moderator == true)
		        	smackMultiChat.grantModerator(nickName);
			} catch (XMPPException e1) {
				e1.printStackTrace();
			}
		}
		
        /*DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(Integer.MAX_VALUE);
    	try {
	        if (password != null )
					smackMultiChat.join( nickName, password,
							history, SmackConfiguration.getPacketReplyTimeout() );
			else
	            smackMultiChat.join( nickName, "", history, 
	            		SmackConfiguration.getPacketReplyTimeout() );  
	        
	        if (moderator == true)
	        	smackMultiChat.grantModerator(nickName);
	
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
		
	}


	@Override
	public String getUserRole(String userId){
		// XXX Wait a few time for the server to update its internal status
        Occupant me = null;
                
        int tentatives = 0;

        while (me == null && tentatives++ < 5) { // Let's hope the server will give us an aswer!
            try {
                Thread.sleep( 100 );
            } catch (InterruptedException e) { /* Silence */
            }

            me = smackMultiChat.getOccupant( userId );
        }
        
        return me == null ? null : me.getRole();
	}

	@Override
	public void sendTyping(String userName) {
        TypingNotificationPacket notification = new TypingNotificationPacket();
        notification
                .setWho( StringUtils.escapeForXML( userName ) );

        Message msg = smackMultiChat.createMessage();
        msg.setTo( smackMultiChat.getRoom() );
        msg.addExtension( notification );
        backend.getConnection().sendPacket( msg );
	}

	@Override
    public void leave() {
        if (backend.isConnected()) {
            //remove all listeners for this room
    		smackMultiChat.removeMessageListener(messageListener);
    		smackMultiChat.removeUserStatusListener(smackUserStatusListener);
            smackMultiChat.removeParticipantStatusListener(participantStatusListener);
            smackMultiChat.removeSubjectUpdatedListener(subjectUpdatedListener);
    		smackMultiChat.removeInvitationRejectionListener(invitationRejectionListener);
    		backend.getConnection().removePacketListener(multiChatTypingListener);
    		backend.getConnection().removePacketListener(extensionProtocolListener);
            smackMultiChat.leave();
        }
        smackMultiChat = null;
    }
	
	@Override
	public void SendExtensionProtocolMessage(String extensionName,
			HashMap<String, String> param){

		Message message = new Message();
		
		message.setType(Type.groupchat);
		message.setTo(smackMultiChat.getRoom());
		message.setProperty("ExtensionName", extensionName);
		
		Iterator<String> it = param.keySet().iterator();
		while(it.hasNext()){
			String val = it.next();
			message.setProperty(val, param.get(val));
		}

		try {
			smackMultiChat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void declineInvitation(String room, String inviter, String reason) {
        MultiUserChat.decline(backend.getConnection(), room, inviter, reason);
	}

	@Override
	public IRoomInfo getRoomInfo(String room) {
		try {
			return new RoomInfoAdapter(MultiUserChat.getRoomInfo( backend.getConnection(), room ));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void changeSubject(String room, String subject) {
		try {
			smackMultiChat.changeSubject(subject);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	


}
