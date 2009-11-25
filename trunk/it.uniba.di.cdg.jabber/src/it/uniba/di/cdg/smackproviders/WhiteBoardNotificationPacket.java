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
 * Packet for notifying the new whiteboard content (yeah, _all_ the content: no diff for now).  
 */
public class WhiteBoardNotificationPacket implements PacketExtension {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "whiteboard-changed";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketFilter() {
        public boolean accept( Packet packet ) {
            return packet.getExtension( ELEMENT_NAME, CDG_NAMESPACE ) != null;
        }
    };

    /**
     * New whiteboard text.
     */
    private String text;

    /**
     * The id of who has changed the whiteboard. 
     */
    private String who;

    /**
     * Default constructor that does nothing.
     */
    public WhiteBoardNotificationPacket() {
        this( "", "" );
    }
    
    /**
     * Constructor injection.
     * 
     * @param who the id of who has changed the whiteboard
     * @param text the actual whiteboard content 
     */
    public WhiteBoardNotificationPacket( String who, String text ) {
        super();
        this.who = who;
        this.text = text;
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text The text to set.
     */
    public void setText( String text ) {
        this.text = text;
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
                "<whiteboard-changed xmlns=\"%s\">" +
                "<who>%s</who>" +
                "<text>%s</text>" +
                "</whiteboard-changed>", CDG_NAMESPACE, getWho(), getText() );
        return xml;
    }
}
