/**
 * This file is part of the eConference project and it is distributed under the 

 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2010 Collaborative Development Group - Dipartimento di Informatica, 
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

package it.uniba.di.cdg.xcore.econference.util;

import it.uniba.di.cdg.xcore.econference.IEConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;

import java.util.Iterator;
import java.util.StringTokenizer;

public class MailFactory {

    public static String createMailBody( IEConferenceContext context, String googleDocLink ) {
        StringBuffer body = new StringBuffer();
        String passw = context.getPassword();
        body.append( String.format(
                "eConference: %s\nFrom: %s\nRoom: %s\nSchedule: %s\nPassword: %s", context
                        .getRoom().split( "@" )[0], context.getModerator(), context.getRoom(),
                context.getSchedule(), (passw != null) ? passw : "" ) );

        body.append( "\nAgenda: " );
        Iterator<IDiscussionItem> item = context.getItemList().iterator();
        if (item.hasNext()) {
            IDiscussionItem elem = item.next();
            body.append( elem.getText() );
        }
        while (item.hasNext()) {
            body.append( ", " );
            IDiscussionItem elem = item.next();
            body.append( elem.getText() );
        }
        body.append( "\nParticipants: \n" );
        String partecipants = context.getInvitees().toString();
        partecipants = partecipants.replace( "[", "" );
        partecipants = partecipants.replace( "]", "" );
        StringTokenizer tokens = new StringTokenizer( partecipants, "," );
        for (; tokens.hasMoreTokens();) {
            body.append( tokens.nextToken() + "\n\n" );
        }
        body.append( "link: " + googleDocLink );
        System.out.println( body.toString() );
        return body.toString();
    }
}
