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
package it.uniba.di.cdg.jabber;

import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;

import org.junit.Test;

import junit.framework.TestCase;

public class InvitationEventTest extends TestCase {
	static InvitationEvent ie1, ie2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ie1 = new InvitationEvent("", "", "johndoe@googlemail.com/Smack3A61CA00", "", "", null);
		ie2 = new InvitationEvent("", "", "johndoe@googlemail.com/Smack91CCA155", "", "", null);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	// fixed issue#1 http://code.google.com/p/econference/issues/detail?id=1
	// skips the inviter resource so that
	// invitations to the same event from the following inviter
	// johndoe@googlemail.com/Smack3A61CA00
	// johndoe@googlemail.com/Smack91CCA155
	// are treated as the same invitation w/ the same inviter
	@Test
	public void testNewInvitationEvent() {
		assertNotSame(ie1.getInviter(), ie2.getInviter());
	}
}
