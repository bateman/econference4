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
package it.uniba.di.cdg.jabber.internal;

import org.jivesoftware.smack.util.StringUtils;


/**
 * Collection of useful methods when dealing with XMPP/SMACK messages ...
 */
public abstract class XMPPUtils {
    /**
     * Remove the "/<resource-id>" from the jid (i.e. "xman@ugres.di.uniba.it/Gaim" becomes "xman@ugres.di.uniba.it").
     * This will spare us some headaches.
     * 
     * @param jid 
     * @return the cleaned jid
     */
    public static String cleanJid( String jid ) {
        return StringUtils.parseBareAddress( jid ); 
//        if (jid.indexOf( "/" ) == -1 )
//            return jid; // Ok
//        return jid.substring( 0, jid.indexOf( "/" ) );
    }
    
    /**
     * Group chats usually dispatch messages using strings link
     * <pre><code>
     * from="chatroom@conference.ugres.di.uniba.it/Mario"
     * </code></pre>
     *  
     * @param chatString
     * @return the username that has generated the message
     */ 
    public static String getUserNameFromChatString( String chatString ) {
        return StringUtils.parseResource( chatString );
//        return chatString.substring( chatString.indexOf( "/" ) + 1, chatString.length() );
    }



    /**
     * @param string
     * @return
     */
    public static String getRoomName( String string ) {
        return StringUtils.parseName( string );
    }
}
