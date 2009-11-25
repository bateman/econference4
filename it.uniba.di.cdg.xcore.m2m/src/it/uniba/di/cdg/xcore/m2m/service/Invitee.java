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
package it.uniba.di.cdg.xcore.m2m.service;

/**
 * A chat invitee. 
 */
public class Invitee {

    String id;
    
    String fullName;
    
    String email;
    
    String organization;

    String role;
    
    /**
     * Default constructor (initialize nothing).
     */
    public Invitee() {
        super();
    }
    
    /**
     * @param id
     * @param fullName
     * @param email
     * @param organization
     */
    public Invitee( String id, String fullName, String email, String organization, String role ) {
        super();
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.organization = organization;
        this.role = role;
    }

    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail( String email ) {
        this.email = email;
    }

    /**
     * @return Returns the fullName.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName The fullName to set.
     */
    public void setFullName( String fullName ) {
        this.fullName = fullName;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * @return Returns the organization.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization The organization to set.
     */
    public void setOrganization( String organization ) {
        this.organization = organization;
    }

    /**
     * @return Returns the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role The role to set.
     */
    public void setRole( String role ) {
        this.role = role;
    }
}