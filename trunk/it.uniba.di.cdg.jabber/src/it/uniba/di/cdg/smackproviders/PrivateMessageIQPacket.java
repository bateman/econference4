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
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

/**
 * IQ implementation of raise hand. 
 */
@Deprecated
public class PrivateMessageIQPacket extends IQ {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "pm";

    /**
     * Namespace.
     */
    public static final String ELEMENT_NS = CDG_NAMESPACE + "#private-message";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketTypeFilter( PrivateMessageIQPacket.class );
    
    /**
     * The id of who is sending the private message. 
     */
    private String who;
    
    /**
     * The message text.
     */
    private String text;

    /**
     * Default constructor that does nothing.
     */
    public PrivateMessageIQPacket() {
        this( "", "" );
    }
    
    /**
     * Constructor injection.
     * 
     * @param who the id of who has changed the whiteboard
     * @param text the actual whiteboard content 
     */
    public PrivateMessageIQPacket( String who, String text ) {
        super();
        this.who = who;
        this.text = text;
        setType( Type.SET );
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
    public String getText() {
        return text;
    }

    /**
     * @param text The text to set.
     */
    public void setText( String text ) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
     */
    @Override
    public String getChildElementXML() {
        String xml = String.format( 
                "<pm xmlns=\"%s\">" +
                "<who>%s</who>" +
                "<text>%s</text>" +
                "</pm>", ELEMENT_NS, getWho(), getText() );
        return xml;
    }
}
