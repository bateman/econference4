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
package it.uniba.di.cdg.xcore.network;

/**
 * Aggregates the information provided by the <code>{@link ServerContext}</code> and
 * <code>{@link UserContext}</code>. <p> Thus, it provides all the information necessary to
 * create a connection profile, including userid, password, server and the like.
 * 
 * @see it.uniba.di.cdg.xcore.network.UserContext
 * @see it.uniba.di.cdg.xcore.network.ServerContext
 */
public class ProfileContext {
    /**
     * The unique identifier
     */
    private String profileName;

    private UserContext uc;

    private ServerContext sc;
    
    private boolean newaccount;

    /**
     * Default constructor. It creates an empty profile.
     * 
     */
    public ProfileContext() {
        this( "", new UserContext( "", "" ), new ServerContext( "", 5222, false ),false );
    }

    /**
     * Parametrized constructor.
     * 
     * @param profileName
     *        the unique profile id
     * @param uc
     *        the user context info
     * @param sc
     *        the server context info
     * @param newaccount
     *        is new account info
     */
    public ProfileContext( String profileName, UserContext uc, ServerContext sc, boolean newaccount) {
        this.profileName = profileName;
        this.uc = uc;
        this.sc = sc;
        this.newaccount = newaccount;
    }

    public ServerContext getServerContext() {
        return sc;
    }

    public UserContext getUserContext() {
        return uc;
    }

    public String getProfileName() {
        return profileName;
    }

    /**
     * Check the validity and completeness of the profile details.
     * 
     * @return <code>true</code> if both the <code>UserContext</code> and the
     *         <code>ServerContext</code> are valid themselves. <code>false</code>, otherwise.
     */
    public boolean isValid() {
        return (uc.isValid() && sc.isValid());
    }

    @Override
    public String toString() {
        return uc.toString() + ", " + sc.toString();
    }
    
    public boolean isNewAccount() {
		// TODO Auto-generated method stub
		return newaccount;
	}

}
