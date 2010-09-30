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
package it.uniba.di.cdg.smackproviders;

import static it.uniba.di.cdg.smackproviders.SmackCommons.CDG_NAMESPACE;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Packet for notifying tha a question status must be updated. This kind of packet is usually
 * dispatched by some moderator to update the hand raising model of the other clients. 
 */
@Deprecated
public class QuestionUpdatePacket implements PacketExtension {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "question-status-update";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketFilter() {
        public boolean accept( Packet packet ) {
            return packet.getExtension( ELEMENT_NAME, CDG_NAMESPACE ) != null;
        }
    };

    /**
     * The id of the question.
     */
    private int id;
    
    /**
     * The id of who has changed the whiteboard. 
     */
    private String who;
    
    /**
     * The question text.
     */
    private String questionText;

    /**
     * The status of the question.
     */
    private String status;
    
    /**
     * Default constructor that does nothing.
     */
    public QuestionUpdatePacket() {
        this( 0, "", "", "" );
    }
    
    /**
     * Constructor injection.
     * 
     * @param who the id of who has changed the whiteboard
     * @param text the actual whiteboard content 
     */
    public QuestionUpdatePacket( int id, String who, String text, String status ) {
        super();
        this.id = id;
        this.who = who;
        this.questionText = text;
        this.status = status;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId( int id ) {
        this.id = id;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus( String status ) {
        this.status = status;
    }

    /**
     * @return Returns the who.
     */
    public String getWho() {
        return who;
    }

    /**
     * @param who The who to set.
     */
    public void setWho( String who ) {
        this.who = who;
    }

    /**
     * @return Returns the text.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @param text The text to set.
     */
    public void setQuestionText( String text ) {
        this.questionText = text;
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.packet.PacketExtension#getElementName()
     */
    public String getElementName() {
        return ELEMENT_NAME;
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.packet.PacketExtension#getNamespace()
     */
    public String getNamespace() {
        return CDG_NAMESPACE;
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.packet.PacketExtension#toXML()
     */
    public String toXML() {
        String xml = String.format( 
                "<question-status-update xmlns=\"%s\">" +
                "<id>%d</id>" +
                "<who>%s</who>" +
                "<questionText>%s</questionText>" +
                "<status>%s</status>" +
                "</question-status-update>", CDG_NAMESPACE, getId(), getWho(), getQuestionText(), getStatus() );
        return xml;
    }
}
