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
 * A user account contains the information needed for authenticating the user. TODO Clean up the
 * names: they seems too much jabber-ish ;)
 */
public class UserContext {
    static public final String AT = "@";

    /**
     * The user id.
     */
    private String id = "";

    private String password = "";
    
    private String name = "";
    
    private String email = "";
    
   
    public UserContext( String id, String password ) {
        super();
        this.id = id;
        this.password = password;
    }
    
    public UserContext( String id, String password, String name, String email ) {
        super();
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *        The id to set.
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *        The password to set.
     */
    public void setPassword( String password ) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        The name to set.
     */
    public void setName( String name ) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *        The email to set.
     */
    public void setEmail( String email ) {
        this.email = email;
    }
    
    public boolean isGmail(){
    	return id.contains("@gmail") || id.contains("@googlemail");
    }
    
    /**
     * Check whethere or not this buddy has valid data.
     * 
     * @return
     */
    public boolean isValid() {
        return (null != id && null != password && id.length() > 0 && password.length() > 0
//                && id.indexOf( AT ) == -1 && // no '@' in jid
                && id.indexOf( AT ) != id.length() - 1 && // 'pippo@'
        !id.startsWith( AT )); // '@gmail.com');
    }

    @Override
    public String toString() {    	
    	//password is hidden by asterisks
        return String.format("id: %s, password: %s", id, password.replaceAll(".", "*"));
    }   
    
}
