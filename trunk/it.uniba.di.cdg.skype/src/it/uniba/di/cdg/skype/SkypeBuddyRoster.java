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
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User.Status;

public class SkypeBuddyRoster extends AbstractBuddyRoster implements
		IBuddyRoster {

    private final Map<String, IBuddyGroup> groups;
    private final Map<String, IBuddy> buddies;
    private final Collection<IBuddyRosterListener> listeners;
    private IBackend backend;

	public SkypeBuddyRoster(IBackend backend) {
		this.backend = backend;

		groups = new HashMap<String, IBuddyGroup>();
		buddies = new HashMap<String, IBuddy>();
		listeners = new Vector<IBuddyRosterListener>();
	}

	@Override
	public void addListener(IBuddyRosterListener listener) {
		listeners.add(listener);
	}

	@Override
	public void clear() {
		groups.clear();
		buddies.clear();
	}

	@Override
	public boolean contains(String buddyId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<IBuddyGroup> getAllGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBackend getBackend() {
		return backend;
	}

	@Override
	public Collection<IBuddy> getBuddies() {
		return buddies.values();
	}

	@Override
	public IBuddy getBuddy(String buddyId) {
		return buddies.get(buddyId);
	}

	@Override
	public Collection<IBuddyGroup> getGroups(IBuddy buddy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBuddies() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasGroups() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(IBuddyRosterListener listener) {
		listeners.remove(listener);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<IBuddy> iterator() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBuddy(String name, String id, String[] gruppi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToGroup(String user, String nameNewGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBuddy(String user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameBuddy(String user, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameGroup(String oldName, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGroup(String group, String newGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}
	
   

	
}
