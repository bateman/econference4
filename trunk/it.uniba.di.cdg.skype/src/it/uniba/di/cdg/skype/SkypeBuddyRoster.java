package it.uniba.di.cdg.skype;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.model.AbstractBuddyRoster;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.model.IBuddyRosterListener;
import it.uniba.di.cdg.xcore.network.model.IEntry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User.Status;

public class SkypeBuddyRoster extends AbstractBuddyRoster implements
		IBuddyRoster {

	public SkypeBuddyRoster(IBackend backend) {
		this.backend = backend;

		groups = new HashMap<String, IBuddyGroup>();
		buddies = new HashMap<String, IBuddy>();
		listeners = new Vector<IBuddyRosterListener>();
	}


	@Override
	public void clear() {
		groups.clear();
		buddies.clear();
	}

	@Override
	public IBuddy getBuddy(String buddyId) {
		return buddies.get(buddyId);
	}

	@Override
	public IEntry[] getChilds() {		
        // Collect all groups and buddies without a group
        final Collection<IEntry> all = new HashSet<IEntry>( groups.values() );
        
        for (IBuddy buddy : getBuddies()) {
        	all.add( buddy );
        }
        return all.toArray( new IEntry[all.size()] );
	}

	@Override
	public IEntry getParent() {		
		return null;
	}

    private void refreshBuddies() {
        buddies.clear();
        // Get all the buddies ...
        try{
	        Friend[] friends = Skype.getContactList().getAllFriends();
	        
	        for(int i=0; i<friends.length; i++){
	        	Friend friend = friends[i];
	        	boolean online = (friend.getOnlineStatus()==Status.ONLINE) ? true : false;
	        	IBuddy.Status status = null;
	        	switch (friend.getOnlineStatus()) {
				case ONLINE:
					status = IBuddy.Status.AVAILABLE;
					break;
				case AWAY:
					status = IBuddy.Status.AWAY;
					break;
				case OFFLINE:
					status = IBuddy.Status.OFFLINE;
					break;
				}
	        	IBuddy buddy = new SkypeBuddy(this, friend.getId(), friend.getFullName(),
	        			friend.getMoodMessage(), status, online);
	        	buddies.put(friend.getId(), buddy);
	        }
	        
	      }catch(SkypeException e){
	    	  e.printStackTrace();
	      }
    }
    
    public void updateRoster(){
    	
    	refreshBuddies();
    	for(IBuddyRosterListener l: listeners){
    		l.rosterChanged();
    	}
    	
    }
    
    public void disconnectRoster(){
    	clear();
    	for(IBuddyRosterListener l: listeners){
    		l.rosterChanged();
    	}
    }

    @Override
	public void addGroup(String name) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?
		
	}

	@Override
	public void addBuddy(String name, String id, String[] gruppi) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?		
	}

	@Override
	public void moveToGroup(String user, String nameNewGroup) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?		
	}

	@Override
	public void removeBuddy(String user) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?		
	}

	@Override
	public void renameBuddy(String user, String name) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?		
	}

	@Override
	public void renameGroup(String oldName, String newName) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?		
	}

	@Override
	public void removeGroup(String group, String newGroup) {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?
	}

	@Override
	public void reload() {
		// emtpy stub for compatibility with group management features
    	// in Jabber backend
    	// CHECK do these apply to skype too?
	}
	
}
