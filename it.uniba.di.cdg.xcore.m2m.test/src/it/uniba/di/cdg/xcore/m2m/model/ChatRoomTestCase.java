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
package it.uniba.di.cdg.xcore.m2m.model;

import it.uniba.di.cdg.xcore.m2m.model.ChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModelListener;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant;
import it.uniba.di.cdg.xcore.m2m.model.Participant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * jUnit test for {@see it.uniba.di.cdg.xcore.m2m.model.ChatRoom}. 
 */
public class ChatRoomTestCase extends MockObjectTestCase {
    
    private ChatRoomModel chatRoom;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        chatRoom = new ChatRoomModel();
    }

    public void testAddParticipant() {
        IParticipant p = new Participant( chatRoom, "tester1@test.net", "Tester1",
        		"personal role tester1", Role.PARTICIPANT );
        
        chatRoom.addParticipant( p );
        
        assertEquals( p, chatRoom.getParticipant( "tester1@test.net" ) );
    }

    public void testRemoveParticipant() {
        IParticipant p1 = new Participant( chatRoom, "tester1@test.net", "Tester1",
        		"personal role tester1", Role.PARTICIPANT );
        IParticipant p2 = new Participant( chatRoom, "tester2@test.net", "Tester2",
        		"personal role tester2", Role.PARTICIPANT );
        chatRoom.addParticipant( p1 );
        chatRoom.addParticipant( p2 );
        
        chatRoom.removeParticipant( p1 );
        
        assertTrue( chatRoom.getParticipant( "tester1@test.net" ) == null );
        assertEquals( p2, chatRoom.getParticipant( "tester2@test.net" ) );
    }
    
    public void testGetParticipants() {
        IParticipant p1 = new Participant( chatRoom, "tester1@test.net", "Tester1",
        		"personal role tester1",Role.PARTICIPANT );
        IParticipant p2 = new Participant( chatRoom, "tester2@test.net", "Tester2",
        		"personal role tester2",Role.PARTICIPANT );
        chatRoom.addParticipant( p1 );
        chatRoom.addParticipant( p2 );

        IParticipant[] participants = chatRoom.getParticipants();
        assertEquals( 2, participants.length );
        
        // We cannot be sure about the order in the [] so we do in a more tricky way ...
        assertTrue( participants[0] == p1 || participants[0] == p2 );
        assertTrue( participants[1] == p1 || participants[1] == p2 );
        assertTrue( participants[0] != participants[1] );
    }
    
    /**
     * Ensure that listeners are correctly notified.
     */
    public void testObserverNotifications() {
        IParticipant p = new Participant( chatRoom, "tester1@test.net", "Tester1",
        		"personal role tester1",Role.PARTICIPANT );

        Mock mock = mock( IChatRoomModelListener.class );
        mock.expects( once() ).method( "added" ).with( same( p ) );   // #1
        mock.expects( once() ).method( "changed" ).with( same( p ) ); // #2 
        mock.expects( once() ).method( "removed" ).with( same( p ) ); // #3
        
        chatRoom.addListener( (IChatRoomModelListener) mock.proxy() );
        
        chatRoom.addParticipant( p ); // #1

        p.setNickName( "Changed" ); // #2
        
        chatRoom.removeParticipant( p ); // #3
        
        // Now the participant is without a chat room and so I'll (hopefully) get no notification if I modify him
        p.setNickName( "Reborn" );
    }
    
    public void testNotifySubjectChanged() {
        final String newSubject = "new subject"; 
        final String modifier = "johnny"; 
        
        Mock mock = mock( IChatRoomModelListener.class );
        mock.expects( once() ).method( "subjectChanged" ).with( eq( modifier ) );   
        
        chatRoom.addListener( (IChatRoomModelListener) mock.proxy() );
        
        chatRoom.setSubject( newSubject, modifier );
        
        assertEquals( newSubject, chatRoom.getSubject() );
    }
    
    public void testNotifyLocalUserChanged() {
        Mock mock = mock( IParticipant.class );
        IParticipant localUser = (IParticipant) mock.proxy();
        
        mock = mock( IChatRoomModelListener.class );
        mock.expects( once() ).method( "localUserChanged" );   
        
        chatRoom.addListener( (IChatRoomModelListener) mock.proxy() );
        
        chatRoom.setLocalUser( localUser );
    }
}
