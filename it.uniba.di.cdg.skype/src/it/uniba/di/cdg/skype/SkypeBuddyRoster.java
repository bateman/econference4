package it.uniba.di.cdg.skype;

import it.uniba.di.cdg.aspects.SwtSyncExec;
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
import com.skype.Group;
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
		final Collection<IEntry> all = new HashSet<IEntry>(groups.values());

		for (IBuddy buddy : getBuddies()) {
			all.add(buddy);
		}
		return all.toArray(new IEntry[all.size()]);
	}

	@Override
	public IEntry getParent() {
		return null;
	}

	private void refreshBuddies() {
		buddies.clear();
		// Get all the buddies ...
		try {
			Friend[] friends = Skype.getContactList().getAllFriends();

			for (int i = 0; i < friends.length; i++) {

				Friend friend = friends[i];

				boolean online = (friend.getOnlineStatus() == Status.ONLINE
						|| friend.getOnlineStatus() == Status.AWAY || friend
						.getOnlineStatus() == Status.DND) ? true : false;
				IBuddy.Status status = null;
				switch (friend.getOnlineStatus()) {
				case ONLINE:
					status = IBuddy.Status.AVAILABLE;
					break;
				case NA:
					status = IBuddy.Status.EXTENDED_AWAY;
				case AWAY:
					status = IBuddy.Status.AWAY;
					break;
				case DND:
					status = IBuddy.Status.BUSY;
					break;
				case OFFLINE:
				default:
					status = IBuddy.Status.OFFLINE;
					break;
				}
				IBuddy buddy = new SkypeBuddy(this, friend.getId(),
						friend.getDisplayName(), friend.getMoodMessage(),
						status, online);
				buddies.put(friend.getId(), buddy);

			}
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	public void disconnectRoster() {
		clear();
		for (IBuddyRosterListener l : listeners) {
			l.rosterChanged();
		}
	}

	@Override
	public void addGroup(String name) {
		try {
			if (!this.containsGroup(name)) {
				Skype.getContactList().addGroup(name);
				reload();
			}

		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addBuddy(String name, String id, String[] gruppi) {
		// emtpy stub for compatibility with group management features
		// in Jabber backend
		// CHECK do these apply to skype too?
	}

	@Override
	public void moveToGroup(String user, String nameNewGroup) {
		try {
			Friend friend = Skype.getContactList().getFriend(user);
			Group[] groups = Skype.getContactList().getAllGroups();

			for (int i = 0; i < groups.length; i++) {
				Group group = groups[i];
				if (!group.hasFriend(friend)) {
					Skype.getContactList().getGroup(nameNewGroup)
							.addFriend(friend);
				}
			}
			reload();
		} catch (SkypeException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeBuddy(String user) {
		// emtpy stub for compatibility with group management features
		// in Jabber backend
		// CHECK do these apply to skype too?
	}

	@Override
	public void renameBuddy(String user, String name) {
		try {
			Skype.getContactList().getFriend(user).setDisplayName(name);
			reload();
		} catch (SkypeException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void renameGroup(String oldName, String newName) {
		try {
			if (!groups.containsKey(newName)) {
				Skype.getContactList().getGroup(oldName)
						.setDisplayName(newName);
			}
			reload();
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeGroup(String group, String newGroup) {
		try {
			Group oldGroup = Skype.getContactList().getGroup(group);
			if (!newGroup.equals("None")) {
				if (oldGroup.getAllFriends().length > 0) {
					Friend[] friends = oldGroup.getAllFriends();
					for (int j = 0; j < friends.length; j++) {
						Friend friend = friends[j];
						if (!Skype.getContactList().getGroup(newGroup)
								.hasFriend(friend)) {
							Skype.getContactList().getGroup(newGroup)
									.addFriend(friend);
						}
						oldGroup.removeFriend(friend);
					}
				}
			}
			Skype.getContactList().removeGroup(oldGroup);
			groups.remove(oldGroup);
			reload();
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reload() {
		refreshBuddies();
		refreshGroups();
		for (IBuddyRosterListener l : listeners) {
			l.rosterChanged();
		}

	}

	private void refreshGroups() {
		groups.clear();
		try {
			Group[] g = Skype.getContactList().getAllGroups();
			for (int i = 0; i < g.length; i++) {
				Group group = g[i];
				Friend[] friends = group.getAllFriends();
				SkypeBuddyGroup skypeGroup = new SkypeBuddyGroup(
						group.getDisplayName());

				for (int j = 0; j < group.getAllFriends().length; j++) {
					Friend friend = friends[j];
					skypeGroup.addBuddy(buddies.get(friend.getId()));

				}
				IBuddyGroup buddyG = skypeGroup;
				groups.put(skypeGroup.getName(), buddyG);
			}

		} catch (SkypeException e) {
			e.printStackTrace();
		}

	}

}
