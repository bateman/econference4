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

import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;
import it.uniba.di.cdg.xcore.network.internal.NetworkBackendHelper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class ConnectionTest extends TestCase {
    
    /**
     * Our server ...
     */
    private static final ServerContext UGRES_SERVER = new ServerContext( "ugres.di.uniba.it", 5222 ,false );
    
    private JabberBackend harry;
    
    public static UserContext harryContext = new UserContext( "alessandro.brucoli", "studente" );
    
    private JabberBackend sammy;
    
    public static UserContext sammyContext = new UserContext( "tester1", "tester" );

    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    protected void setUp() throws Exception {   
        harry = new JabberBackend();  
        harry.setHelper(new NetworkBackendHelper());
       
        sammy = new JabberBackend(); 
        sammy.setHelper(new NetworkBackendHelper());
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        sammy.disconnect();
        sammy.disconnect();
    }

    @Test 
    public void testConnection() throws BackendException {
		try {
			harry.connect( UGRES_SERVER, harryContext );
			assertEquals(harry.isConnected(), true);
			sammy.connect( UGRES_SERVER, sammyContext );
			assertEquals(sammy.isConnected(), true);
		} catch (BackendException e) {
			System.out.println("The problem could be the router port; you must open the 5222 port on your router");
			e.printStackTrace();
		}
	}
}
