package it.uniba.di.cdg.skype.x86sdk;


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

import com.skype.api.Contact;
import com.skype.api.ContactGroup;


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
	public IEntry[] getChildren() {
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

		ContactGroup cg = SkypeBackend.skype.GetHardwiredContactGroup(ContactGroup.TYPE.ALL_BUDDIES);
		Contact[] contacts = cg.GetContacts();


		for (int i = 0; i < contacts.length; i++) {

			Contact contact = contacts[i];

			String availability = Contact.AVAILABILITY.get(contact.GetIntProperty(Contact.PROPERTY.availability)).toString();

			String id = contact.GetIdentity();
			String display_name = contact.GetStrProperty(Contact.PROPERTY.displayname);
			String mood_message = contact.GetStrProperty(Contact.PROPERTY.mood_text);

			boolean online = (availability == "ONLINE"
					|| availability == "AWAY" ||  availability == "DO_NOT_DISTURB ") ? true : false;
			IBuddy.Status status = null;

			if (availability== "ONLINE")
				status = IBuddy.Status.AVAILABLE;

			if (availability== "NOT_AVAILABLE ")
				status = IBuddy.Status.EXTENDED_AWAY;

			if (availability== "AWAY")
				status = IBuddy.Status.AWAY;

			if (availability== "DO_NOT_DISTURB")
				status = IBuddy.Status.BUSY;

			if (availability== "OFFLINE")
				status = IBuddy.Status.OFFLINE;


			IBuddy buddy = new SkypeBuddy(this, id, display_name, mood_message,	status, online);
			buddies.put(id, buddy);

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

		if (!this.containsGroup(name)) {
			ContactGroup new_group = SkypeBackend.skype.CreateCustomContactGroup(); 
			new_group.GiveDisplayName(name);
			reload();
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


		Contact friend = SkypeBackend.skype.GetContact(user);

		ContactGroup[] groups = SkypeBackend.skype.GetCustomContactGroups();

		for (int i = 0; i < groups.length; i++) {
			ContactGroup group = groups[i];
			if ((group.GetStrProperty(ContactGroup.PROPERTY.given_displayname).equals(nameNewGroup))){
				if (!friend.IsMemberOf(group)) {
					group.AddContact(friend);

				}
			}
		}
		reload();

	}

	@Override
	public void removeBuddy(String user) {
		// emtpy stub for compatibility with group management features
		// in Jabber backend
		// CHECK do these apply to skype too?
	}

	@Override
	public void renameBuddy(String user, String name) {
		Contact friend = SkypeBackend.skype.GetContact(user);
		friend.GiveDisplayName(name);
		reload();
	}

	@Override
	public void renameGroup(String oldName, String newName) {

		ContactGroup[] groups = SkypeBackend.skype.GetCustomContactGroups();

		for (int i = 0; i < groups.length; i++) {
			ContactGroup group = groups[i];

			if ((group.GetStrProperty(ContactGroup.PROPERTY.given_displayname).equals(oldName))) {
				group.GiveDisplayName(newName);
			}
		}
		reload();
	}

	@Override
	public void removeGroup(String group, String newGroup) {

		ContactGroup[] groups_ex = SkypeBackend.skype.GetCustomContactGroups();

		for (int i = 0; i < groups_ex.length; i++) {
			ContactGroup old_group = groups_ex[i];

			if (old_group.GetStrProperty(ContactGroup.PROPERTY.given_displayname).equals(group)) {

				if (!newGroup.equals("None")) {
					if (old_group.GetContacts().length > 0) {
						Contact[] friends = old_group.GetContacts();
						for (int j = 0; j < friends.length; j++) {
							Contact friend = friends[j];
							this.moveToGroup(friend.GetIdentity(), newGroup);
							old_group.RemoveContact(friend);
						}
					}
				}

				old_group.Delete();	
				groups.remove(old_group.GetStrProperty(ContactGroup.PROPERTY.given_displayname));
				reload();
			}
		}
	}

	@Override
	public  void reload() {
		refreshBuddies();
		refreshGroups();
		for (IBuddyRosterListener l : listeners) {
			l.rosterChanged();
		}

	}


	private void refreshGroups() {
		groups.clear();

		ContactGroup[] g = SkypeBackend.skype.GetCustomContactGroups();
		for (int i = 0; i < g.length; i++) {
			ContactGroup group = g[i];				
			Contact[] friends=group.GetContacts();
			SkypeBuddyGroup skypeGroup = new SkypeBuddyGroup(
					group.GetStrProperty(ContactGroup.PROPERTY.given_displayname));

			for (int j = 0; j < group.GetContacts().length; j++) {
				Contact friend = friends[j];
				skypeGroup.addBuddy(buddies.get(friend.GetIdentity()));

			}
			IBuddyGroup buddyG = skypeGroup;
			groups.put(skypeGroup.getName(), buddyG);

		}



	}

}
