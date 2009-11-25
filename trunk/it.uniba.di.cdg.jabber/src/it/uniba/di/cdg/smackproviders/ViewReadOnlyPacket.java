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
 * Notify manager event regarding the change in the view read-only status.   
 */
public class ViewReadOnlyPacket implements PacketExtension {
    /**
     * Just one element in this packet.
     */
    public static final String ELEMENT_NAME = "view-read-only";

    /**
     * Filter this kind of packets.
     */
    public static final PacketFilter FILTER = new PacketFilter() {
        public boolean accept( Packet packet ) {
            return packet.getExtension( ELEMENT_NAME, CDG_NAMESPACE ) != null;
        }
    };
    
    /**
     * The conference status: this string must be compatible with the <code>ConferenceStatus</code>
     * enumeration.
     */
    private String viewId;
    
    private boolean readOnly;

    public ViewReadOnlyPacket() {
        this( "", false );
    }
    
    /**
     * @param viewId
     */
    public ViewReadOnlyPacket( String viewId, boolean readOnly ) {
        super();
        this.viewId = viewId;
        this.readOnly = readOnly;
    }
    
    /**
     * @return Returns the readOnly.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly The readOnly to set.
     */
    public void setReadOnly( boolean readOnly ) {
        this.readOnly = readOnly;
    }

    /**
     * @return Returns the viewId.
     */
    public String getViewId() {
        return viewId;
    }

    /**
     * @param viewId The viewId to set.
     */
    public void setViewId( String viewId ) {
        this.viewId = viewId;
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
                "<view-read-only xmlns=\"%s\">" +
                "<viewId>%s</viewId>" +
                "<readOnly>%b</readOnly>" +
                "</view-read-only>", CDG_NAMESPACE, getViewId(), isReadOnly() );
        return xml;
    }
}
