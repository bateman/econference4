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
package it.uniba.di.cdg.xcore.econference.model.internal;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.ConferenceContextLoader;
import it.uniba.di.cdg.xcore.econference.model.DiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.InvalidContextException;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;

import java.io.FileNotFoundException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * jUnit test for the {@see it.uniba.di.cdg.xcore.econference.model.internal.ConferenceLoader}. 
 */
public class ConferenceContextLoaderTestCase extends TestCase {
    private static final String VALID_CONFERENCE_FNAME = "conference_factory_test3.ecx";
    private static final String INVALID_CONFERENCE_FNAME = "conference_factory_test2.ecx";
    private static final String UNEXISTING_CONFERENCE_FNAME = "I_DO_NOT_EXIST.ecx";
    
    private static final String EXPECTED_TOPIC = "Test topic";
    private static final String EXPECTED_NAME = "Test conference";
    
    private static final IDiscussionItem EXPECTED_ITEM1 = new DiscussionItem( "Item 1" );
    private static final IDiscussionItem EXPECTED_ITEM2 = new DiscussionItem( "Item 2" );
    
//    private static final String EXPECTED_DIRECTOR_ID = "director@jabber.organization.com";
//    private static final String EXPECTED_DIRECTOR_PASSWORD = "secret";
//    private static final String EXPECTED_DIRECTOR_FULLNAME = "Director";
//    private static final String EXPECTED_DIRECTOR_EMAIL = "director@organization.com";
//    private static final String EXPECTED_DIRECTOR_ORGANIZATION = "Organization";
    
    private static final String EXPECTED_MODERATOR_FULLNAME = "Fabio Calefato";
    private static final String EXPECTED_SCRIBE_FULLNAME = "diavoletto";

//    private static final String EXPECTED_BACKEND_ID = "it.uniba.di.cdg.jabber.jabberBackend"; 
    private static final String EXPECTED_ROOM_NAME = "myroom@conference.ugres.di.uniba.it"; 
    
    private EConferenceContext context;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        this.context = new EConferenceContext();
    }
    
    public void testLoadFromGoodInputStream() {
        // 1. Load the model
        ConferenceContextLoader loader = new ConferenceContextLoader( context );
        try {
            InputStream is = ConferenceContextLoaderTestCase.class.getResourceAsStream( VALID_CONFERENCE_FNAME );
            loader.load( is );
        } catch (InvalidContextException e) {
            e.printStackTrace();
            fail( "Model is valid and should be read ok!" );
        }
        // 2. Ensure all the expected fields are loaded.
        assertEquals( EXPECTED_TOPIC, context.getTopic() );
        assertEquals( EXPECTED_NAME, context.getName() );
        
        IItemList itemList = context.getItemList();
        assertEquals( 2, itemList.size() );
        assertEquals( EXPECTED_ITEM1, itemList.getItem( 0 ) );
        assertEquals( EXPECTED_ITEM2, itemList.getItem( 1 ) );
        // Ensure that the first element in the discussion is selected
        assertEquals( IItemList.NO_ITEM_SELECTED, itemList.getCurrentItemIndex() );
        
        // Check the support team
//        Invitee director = context.getDirector();
//        assertEquals( EConferenceContext.ROLE_DIRECTOR, director.getRole() );
//        assertEquals( EXPECTED_DIRECTOR_ID, director.getId() );
////        assertEquals( EXPECTED_DIRECTOR_PASSWORD, director.getPassword() );
//        assertEquals( EXPECTED_DIRECTOR_FULLNAME, director.getFullName() );
//        assertEquals( EXPECTED_DIRECTOR_EMAIL, director.getEmail() );
//        assertEquals( EXPECTED_DIRECTOR_ORGANIZATION, director.getOrganization() );
        
        Invitee moderator = context.getModerator();
        assertEquals( EConferenceContext.ROLE_MODERATOR, moderator.getRole() );
        assertEquals( EXPECTED_MODERATOR_FULLNAME, moderator.getFullName() );

        Invitee scribe = context.getScribe();
        assertEquals( EConferenceContext.ROLE_SCRIBE, scribe.getRole() );
        assertEquals( EXPECTED_SCRIBE_FULLNAME, scribe.getFullName() );
        
        // Check the other participants (just ensure they the expected number of people)
        // 3 for support team + 3 other expert
        assertEquals( 3 + 3, context.getInvitees().size() );
        for (Invitee i : context.getInvitees()) {
            assertNotNull( i.getId() );
            final String r = i.getRole(); 
            assertTrue( r.equals( EConferenceContext.ROLE_PARTICIPANT ) 
//                    || r.equals( EConferenceContext.ROLE_DIRECTOR )
                    || r.equals( EConferenceContext.ROLE_MODERATOR )
                    || r.equals( EConferenceContext.ROLE_SCRIBE ) );
        }
        
        //assertEquals( EXPECTED_BACKEND_ID, context.getBackendId() );
        assertEquals( EXPECTED_ROOM_NAME, context.getRoom() );
    }
    
    public void testLoadFromUnexistingFile() {
        ConferenceContextLoader loader = new ConferenceContextLoader( context );
        try {
            loader.load( UNEXISTING_CONFERENCE_FNAME );
            fail( "If the file doesn't exist an exception must be thrown!!" );
        } catch (FileNotFoundException e) {
            // Ok
        } catch (InvalidContextException e) {
            // Ok
        } catch (Exception e ) {
            fail( "FileNotFoundException || InvalidModelException only!!" );
        }
    }
    
    public void testLoadFromInvalidFile() throws Exception {
        ConferenceContextLoader loader = new ConferenceContextLoader( context );
        try {
            loader.load( INVALID_CONFERENCE_FNAME );
            fail( "If the file has invalid XML within, then it must be rejected!!" );
        } catch (FileNotFoundException e) {
            // Ok
        } catch (InvalidContextException e) {
            // Ok
        } catch (Exception e ) {
            fail( "FileNotFoundException || InvalidModelException only!!" );
        }
    }
}
