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
package it.uniba.di.cdg.xcore.econference;

import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.ItemList;
import it.uniba.di.cdg.xcore.econference.model.definition.IServiceContext;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.m2m.service.MultiChatContext;

/**
 * A conference has a context associated to it, reporting all the need information
 * to let the collaborators to handle the conference itself.
 * <p>
 * Clients are responsible to inject collaborators.
 */
public class EConferenceContext extends MultiChatContext implements IEConferenceContext, IServiceContext {
    public static final String ROLE_MODERATOR = "moderator";
    public static final String ROLE_SCRIBE = "scribe";
    public static final String ROLE_PARTICIPANT = "participant";

    /**
     * The discussion item list.
     */
    private IItemList itemList;

    private String name;

    private String topic;
      
    private String schedule;
    
    private Invitee scribe;

    /**
     * @param nickName
     * @param personalStatus 
     * @param invitation
     */
    public EConferenceContext( String nickName, String personalStatus, InvitationEvent invitation ) {
        super( nickName, personalStatus, invitation );
        this.itemList = new ItemList();
    }

    /**
     * Default constructor that builds an unitialized context (require caller depedency injection).
     */
    public EConferenceContext() {
        super();
    }
    
    /**
     * @return Returns the itemList.
     */
    public IItemList getItemList() {
        return itemList;
    }

    /**
     * @param itemList The itemList to set.
     */
    public void setItemList( IItemList itemList ) {
        this.itemList = itemList;
    }
    
    /**
     * Returns the conference' name.
     * 
     * @return the conference's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the conference
     * 
     * @param name
     */
    public void setName( String name ) {
        this.name = name;
    }

    
    /**
     * Returns the conference' schedule.
     * 
     * @return the conference's schedule
     */
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule( String schedule ) {
        this.schedule = schedule;
    }
    
    /**
     * Returns the conference' topic
     * 
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Set the topic for this conference.
     * 
     * @param topic
     */
    public void setTopic( String topic ) {
        this.topic = topic;
    }
    
   
    /**
     * Returns the scribe associated to the conference.
     * 
     * @return the scribe
     */
    public Invitee getScribe() {
        return scribe;
    }

    /**
     * Set the conference' scribe.
     * 
     * @param scribe
     */
    public void setScribe( Invitee scribe ) {
        this.scribe = scribe;
    }
}
