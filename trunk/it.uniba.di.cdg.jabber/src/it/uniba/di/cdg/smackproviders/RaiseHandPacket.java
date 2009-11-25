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
 * Packet for notifying that a user has raise his hands in an attempt to speak. This packet
 * is designed to be notified by clients to some moderator of their choice.
 * 
 * <b>This class has been deprecated: use {@see RaiseHandPacket} instead</b>.
 */
@Deprecated
public class RaiseHandPacket implements PacketExtension {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "raise-hand";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketFilter() {
        public boolean accept( Packet packet ) {
            return packet.getExtension( ELEMENT_NAME, CDG_NAMESPACE ) != null;
        }
    };

    /**
     * The Jabber conference id of the moderator.
     */
    private String moderator;
    
    /**
     * The id of who has changed the whiteboard. 
     */
    private String who;
    
    /**
     * The question text.
     */
    private String questionText;

    /**
     * Default constructor that does nothing.
     */
    public RaiseHandPacket() {
        this( "", "", "" );
    }
    
    /**
     * Constructor injection.
     * 
     * @param who the id of who has changed the whiteboard
     * @param text the actual whiteboard content 
     */
    public RaiseHandPacket( String moderator, String who, String text ) {
        super();
        this.moderator = moderator;
        this.who = who;
        this.questionText = text;
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
                "<raise-hand xmlns=\"%s\">" +
                "<moderator>%s</moderator>" +
                "<who>%s</who>" +
                "<questionText>%s</questionText>" +
                "</raise-hand>", CDG_NAMESPACE, getModerator(), getWho(), getQuestionText() );
        return xml;
    }

    /**
     * @return Returns the moderator.
     */
    public String getModerator() {
        return moderator;
    }

    /**
     * @param moderator The moderator to set.
     */
    public void setModerator( String moderator ) {
        this.moderator = moderator;
    }
}
