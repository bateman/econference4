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

import org.junit.Test;

import it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel;
import it.uniba.di.cdg.xcore.m2m.model.Participant;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * jUnit test for {@see it.uniba.di.cdg.xcore.m2m.model.Participant}.
 */
public class ParticipantStateTest {
    /**
     * Check that calls to <code>set*()</code> methods trigger notification to clients. Notification
     * from participants is performed thru the parent chatroom object, if this parent != null.
     */
    @Test
    public void testNotification() {
        Participant p = new Participant( null, "tester1@test.net", "Tester1",
                "personal status tester1", Role.VISITOR );
        IChatRoomModel model = mock(IChatRoomModel.class);
        p.setChatRoom( model );
        
        // Now try to change something and cross fingers ...
        p.setNickName( "Changed" );
        p.setRole( Role.PARTICIPANT );
        p.setStatus( Status.FROZEN );
        p.addSpecialPriviliges( "New special" );
        assertEquals( "Changed", p.getNickName() );
        assertEquals( Role.PARTICIPANT, p.getRole() );
        assertEquals( Status.FROZEN, p.getStatus() );
        verify(model, times(4)).changed( same(p) );
    }

    @Test
    public void testToString() {
        IChatRoomModel model = mock(IChatRoomModel.class);

        String id = "chatroom@conference.ugres.di.uniba.it/diavoletto";
        Participant p = new Participant( model, id, "diavoletto" );
        
        assertEquals( "diavoletto", p.toString() );
    }
}