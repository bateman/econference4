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
 * Packet for notifying the current agenda item. Client are expected to synch their models upon 
 * receiving such an event.  
 */
public class CurrentAgendaItemPacket implements PacketExtension {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "current-agenda-item";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketFilter() {
        public boolean accept( Packet packet ) {
            return packet.getExtension( ELEMENT_NAME, CDG_NAMESPACE ) != null;
        }
    };
    
    /**
     * The item id identifying the current agenda item: this it usually the index in the item list
     * bu we treat it as a string (who ever knows this will change in the future ;)).
     */
    private String itemId;
    
    public CurrentAgendaItemPacket() {
        this( "" );
    }
    
    /**
     * @param itemId
     */
    public CurrentAgendaItemPacket( String currentAgendaItem ) {
        super();
        this.itemId = currentAgendaItem;
    }

    /**
     * @return Returns the itemId.
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId The itemId to set.
     */
    public void setItemId( String status ) {
        this.itemId = status;
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
                "<current-agenda-item xmlns=\"%s\">" +
                "<itemId>%s</itemId>" +
                "</current-agenda-item>", CDG_NAMESPACE, getItemId() );
        System.out.println(xml);
        return xml;
    }
}
