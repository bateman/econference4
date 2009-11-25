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

import it.uniba.di.cdg.xcore.aspects.ThreadSafetyAspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.m2m.model.IChatRoom}.
 */
public class ChatRoomModel implements IChatRoomModel {
    /**
     * The tracked participants.
     */
    private Map<String, IParticipant> participants;

    /**
     * Reference to the local participant (this user).
     */
    private IParticipant localUser;
    
    /**
     * The listeners for chat room events.
     */
    private List<IChatRoomModelListener> listeners;
    
    /**
     * The  current subject.
     */
    private String subject;

    /**
     * Create a new participant room.
     */
    public ChatRoomModel() {
        this.participants = new HashMap<String, IParticipant>();
        this.listeners = new ArrayList<IChatRoomModelListener>();
        this.subject = "";
    }
    
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#getParticipant(java.lang.String)
     */
    public IParticipant getParticipant( String participantId ) {
        return participants.get( participantId );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#addParticipant(it.uniba.di.cdg.xcore.m2m.model.IParticipant)
     */
    public void addParticipant( IParticipant participant ) {
        participants.put( participant.getId(), participant );
        participant.setChatRoom( this );
        
        for (IChatRoomModelListener l : listeners)
            l.added( participant );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#removeParticipant(it.uniba.di.cdg.xcore.m2m.model.IParticipant)
     */
    public void removeParticipant( IParticipant participant ) {
        participants.remove( participant.getId() );
        participant.setChatRoom( null );
        
        for (IChatRoomModelListener l : listeners)
            l.removed( participant );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#addListeners(it.uniba.di.cdg.xcore.m2m.model.IChatRoom.IChatRoomListener)
     */
    public void addListener( IChatRoomModelListener listener ) {
        listeners.add( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#removeListeners(it.uniba.di.cdg.xcore.m2m.model.IChatRoom.IChatRoomListener)
     */
    public void removeListener( IChatRoomModelListener listener ) {
        listeners.remove( listener );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant.IParticipantListener#changed(it.uniba.di.cdg.xcore.m2m.model.IParticipant)
     */
    public void changed( IParticipant participant ) {
        for (IChatRoomModelListener l : listeners)
            l.changed( participant );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#getParticipants()
     */
    public IParticipant[] getParticipants() {
        return participants.values().toArray( new IParticipant[participants.size()] );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#getSubject()
     */
    public String getSubject() {
        return subject;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel#setSubject(java.lang.String, java.lang.String)
     */
    public void setSubject( String subject, String who ) {
        this.subject = subject;

        for (IChatRoomModelListener l : listeners)
            l.subjectChanged( who );
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#setLocalUser(it.uniba.di.cdg.xcore.m2m.model.IParticipant)
     */
    public void setLocalUser( IParticipant p ) {
        this.localUser = p;

        for (IChatRoomModelListener l : listeners)
            l.localUserChanged();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoom#getLocalUser()
     */
    public IParticipant getLocalUser() {
        return localUser;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IChatRoomModel#getLocalUserOrParticipant(java.lang.String)
     */
    public IParticipant getLocalUserOrParticipant( String id ) {
        IParticipant p = getLocalUser();
        /*FIXME: controllare se l'ignore case va bene(c'è stato un caso in cui 
        non è riuscito a riconoscere l'utente perchè le lettere della stanza
        erano maiuscole */
        if (p == null || !id.equalsIgnoreCase( p.getId() ))
            p = getParticipant( id );
        return p;
    }
    
    /**
     * Returns an iterator to the model listeners so that derived class can notify their own events.
     * 
     * @return an iterator to listeners.
     */
    protected List<IChatRoomModelListener> listeners() {
        return listeners;
    }
    
    /**
     * Provides internal thread synchronization.
     */
    @Aspect
    public static class OwnThreadSafety extends ThreadSafetyAspect {
        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#readOperations()
         */
        @Pointcut( "execution( public * ChatRoomModel.get*(..) )" +
                "|| execution( public void ChatRoomModel.changed(..) )" +
                "|| execution( protected * ChatRoomModel.listeners() )" )
        protected void readOperations() {}

        /* (non-Javadoc)
         * @see it.uniba.di.cdg.xcore.aspects.ThreadSafety#writeOperations()
         */
        @Pointcut( "execution( public void ChatRoomModel.set*(..) )" +
                "|| execution( public void ChatRoomModel.add*(..) )" +
                "|| execution( public void ChatRoomModel.remove*(..) )" )
        protected void writeOperations() {}
    }

    
}
