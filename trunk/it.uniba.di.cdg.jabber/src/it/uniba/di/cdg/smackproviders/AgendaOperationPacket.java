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
 * A packet containing the operation  
 */
@Deprecated
public class AgendaOperationPacket implements PacketExtension {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "agenda-operation";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketFilter() {
        public boolean accept( Packet packet ) {
            return packet.getExtension( ELEMENT_NAME, CDG_NAMESPACE ) != null;
        }
    };
    
    /**
     * The operation to perform. 
     */
    private String operation; 
    
    /**
     * The operation context (this is encoded as a string: clients must figure conversion
     * on their own).
     */
    private String context;
    
    public AgendaOperationPacket() {
        this( "", "" );
    }
    
    /**
     * @param operation
     * @param context
     */
    public AgendaOperationPacket( String operation, String context ) {
        super();
        this.operation = operation;
        this.context = context;
    }

    /**
     * @return Returns the itemId.
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context The itemId to set.
     */
    public void setContext( String status ) {
        this.context = status;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation( String operation ) {
        this.operation = operation;
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
                "<agenda-operation xmlns=\"%s\">" +
                "<operation>%s</operation>" +
                "<context>%s</context>" +
                "</agenda-operation>", CDG_NAMESPACE, getOperation(), getContext() );
        System.out.println(xml);
        return xml;
    }
}
