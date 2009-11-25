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
package it.uniba.di.cdg.xcore.m2m.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;

/**
 * Implementation of {@see it.uniba.di.cdg.xcore.m2m.model.IParticipant}.
 * <p>
 * Note that we extend <code>PlatformObject</code> to the hierarchy to make the class usable
 * by the Eclipse framework during UI notifications.
 */
public class Participant extends PlatformObject implements IParticipant {
    /**
     * The chat room this participant is attending to.
     */
    private IChatRoomModel chatRoom;
    
    /**
     * The (unique) id of this participant.
     */
    private String id;
    
    /**
     * The nickname for this participant.
     */
    private String nickName;
    
    /**
     * The role of this participant.
     */
    private Role role;
    
    /**
     * The personal status of this participant
     * 
     */
    private String personalStatus;
    
    /**
     * In addition to normal roles, each participant may have an additional capability, giving
     * him access to other software functionalities.
     * 
     * Note that to make it extensible we use string literals instead of enums, so it is client 
     * responsibility to ensure the validity of the attribute.
     */
    private List<String> privileges;
    
    /**
     * The current status of the participant.
     */
    private Status status;
    

    /**
     * Create a new participant defaulting its status to <code>NOT_JOINED</code> and its
     * role to <code>VISITOR</code>.
     * 
     * @param chatRoom
     * @param id
     * @param nickName
     * @param statusMessage 
     */
    public Participant( IChatRoomModel chatRoom, String id, String nickName ) {
        this( chatRoom, id, nickName,"", Role.VISITOR, Status.NOT_JOINED );
    }
    
    /**
     * Create a new participant defaulting its status to <code>NOT_JOINED</code>.
     *
     * @param chatRoom
     * @param id
     * @param nickName
     * @param personalStatus 
     * @param statusMessage 
     * @param string 
     * @param role
     */
    public Participant( IChatRoomModel chatRoom, String id, String nickName, String personalStatus, Role role ) {
        this( chatRoom, id, nickName, personalStatus, role, Status.NOT_JOINED );
    }

    /**
     * Convenience constructor for specifying the status only (the role will be <code>VISITOR</code>).
     * 
     * @param chatRoom
     * @param id
     * @param nickName
     * @param statusMessage 
     * @param status
     */
    public Participant( IChatRoomModel chatRoom, String id, String nickName, Status status ) {
        this( chatRoom, id, nickName, "", Role.VISITOR, status );
    }

    /**
     * Create a new participant (all properties).
     * 
     * @param chatRoom
     * @param id
     * @param nickName
     * @param personalStatus 
     * @param statusMessage 
     * @param role
     * @param status
     */
    public Participant( IChatRoomModel chatRoom, String id, String nickName, String personalStatus, 
    		Role role, Status status ) {
        super();
        this.chatRoom = chatRoom;
        this.id = id;
        this.nickName = nickName;       
        this.role = role;
        this.status = status;
        this.personalStatus = personalStatus;
        this.privileges = new ArrayList<String>();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#setChatRoom(it.uniba.di.cdg.xcore.m2m.model.IChatRoom)
     */
    public void setChatRoom( IChatRoomModel room ) {
        this.chatRoom = room;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#getChatRoom()
     */
    public IChatRoomModel getChatRoom() {
        return chatRoom;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#getNickName()
     */
    public String getNickName() {
        return nickName;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#setNickName(java.lang.String)
     */
    public void setNickName( String nickName ) {
        this.nickName = nickName;
        notifyListeners();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#getRole()
     */
    public Role getRole() {
        return role;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#setRole(it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role)
     */
    public void setRole( Role role ) {
        this.role = role;
        notifyListeners();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#getStatus()
     */
    public Status getStatus() {
        return status;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#setStatus(it.uniba.di.cdg.xcore.m2m.model.IParticipant.Status)
     */
    public void setStatus( Status status ) {
        this.status = status;
        notifyListeners();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#getSpecialRole()
     */
    public String getPersonalStatus() {
        return personalStatus;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.m2m.model.IParticipant#setSpecialRole(java.lang.String)
     */
    public void setPersonalStatus( String personalStatus ) {
        this.personalStatus = personalStatus;
        notifyListeners();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (nickName != null && nickName.length() > 0)
            return nickName;
        return id;
    }

    
    @Override
    public void addSpecialPriviliges(String privilege) {
    	if(!privileges.contains(privilege)){
    		privileges.add(privilege);
    		notifyListeners();
    	}
    }

	@Override
	public String[] getSpecialPrivileges() {
		String[] result = null;
		if(privileges.size()>0){
			result = new String[privileges.size()];
			for (int i = 0; i < privileges.size(); i++) {
				result[i] = privileges.get(i);
			}
		}
		return result;
	}

	@Override
	public void removeSpecialPrivileges(String privilege) {
		if(privileges.contains(privilege)){
			privileges.remove(privilege);
			notifyListeners();
		}		
	}

	@Override
	public boolean hasSpecialPrivilege(String privilege) {
		return privileges.contains(privilege);
	}
	
	/**
     * Use the chatroom for notifying the event about this participant.
     */
    private void notifyListeners() {
        if (chatRoom != null)
            chatRoom.changed( this );
    }
}
