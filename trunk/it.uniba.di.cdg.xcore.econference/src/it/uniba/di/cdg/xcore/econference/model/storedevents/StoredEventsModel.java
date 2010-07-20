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
package it.uniba.di.cdg.xcore.econference.model.storedevents;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.IItemList;
import it.uniba.di.cdg.xcore.econference.model.InvalidContextException;
import it.uniba.di.cdg.xcore.econference.model.internal.ConferenceContextLoader;
import it.uniba.di.cdg.xcore.econference.util.FolderMonitor;
import it.uniba.di.cdg.xcore.econference.util.IFolderMonitorListener;
import it.uniba.di.cdg.xcore.m2m.events.ConferenceOrganizationEvent;
import it.uniba.di.cdg.xcore.m2m.events.InvitationEvent;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;
import it.uniba.di.cdg.xcore.network.IBackendDescriptor;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.events.BackendStatusChangeEvent;
import it.uniba.di.cdg.xcore.network.events.IBackendEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Provides implementation of the <code>{@link IStoredEventsModel}</code>
 * interface.
 * 
 * @see it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
 */
public class StoredEventsModel implements IStoredEventsModel,
		IFolderMonitorListener {
	private static final String SEPARATOR = ",";

	private static final String DEFAULT_FILE_PATH = System
			.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ ".econference" + System.getProperty("file.separator");

	private static final String ALGORITHM = "MD5";

	private static final String EVENT_HASH = "event_";

	private static final String CONFIGURATION_NODE_QUALIFIER = "it.uniba.di.cdg.xcore.econference.storage";

	private static final String RCVD_EVENT_PATH_NODE = "rcvd_events_for_";

	private static final String INVITER = "inviter";

	private static final String ROOM = "room";

	private static final String PASSWORD = "password";

	private static final String REASON = "reason";

	private static final String ACCOUNT_ID = "account_id";

	private static final String HASH = "hash";

	public static final String SCHEDULE = "schedule";

	public static final String EVENT_RCVD_ON = "rcvd_on";

	public static final String EVENT_TYPE = "type";

	public static final String BACKEND_ID = "backend_id";

	public static final String ITEM_LIST = "items";

	public static final String INVITEES_LIST = "invitees";

	private List<IStoredEventsModelListener> listeners;

	private Map<String, IStoredEventEntry> storedEvents;

	private Thread folderMonitorThread;

	private DateFormat dateFormat;

	/**
	 * Default constructor.
	 */
	public StoredEventsModel() {
		storedEvents = new Hashtable<String, IStoredEventEntry>();
		listeners = new Vector<IStoredEventsModelListener>();
		folderMonitorThread = new Thread(new FolderMonitor(this,
				DEFAULT_FILE_PATH));
		dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
	}

	/**
	 * Perform initialization
	 */
	public void init() {
		INetworkBackendHelper backendHelper = NetworkPlugin.getDefault()
				.getHelper();
		Collection<IBackendDescriptor> descriptors = backendHelper
				.getRegistry().getDescriptors();
		for (IBackendDescriptor d : descriptors) {
			backendHelper.registerBackendListener(d.getId(), this);
		}
		folderMonitorThread.start();
	}

	public void reloadContextFiles() {
		// get a list of the ecx files
		File dir = new File(DEFAULT_FILE_PATH);
		File[] ecxFiles = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(".ecx") ? true : false;
			}
		});
		synchronized (this) {
			// for each file generate an creation event to be stored
			for (int i = 0; i < ecxFiles.length; i++) {
				File f = ecxFiles[i];
				EConferenceContext context = new EConferenceContext();
				context.setBackendId(NetworkPlugin.getDefault().getHelper()
						.getRegistry().getDefaultBackendId());
				ConferenceContextLoader loader = new ConferenceContextLoader(
						context);
				try {
					loader.load(new FileInputStream(f));
					Iterator<Invitee> inviteeList = context.getInvitees()
							.iterator();
					String[] invitees = new String[context.getInvitees().size()];
					for (int i1 = 0; inviteeList.hasNext(); i1++) {
						Invitee invitee = (Invitee) inviteeList.next();
						invitees[i1] = invitee.toString();
					}

					IItemList itemList = context.getItemList();
					String[] items = new String[itemList.size()];
					itemList.toArray(items);
					InvitationEvent event = new ConferenceOrganizationEvent(
							context.getBackendId(), context.getRoom(), context
									.getModerator().getFullName()
									+ "("
									+ context.getModerator().getId() + ")",
							context.getSchedule(), "", context.getPassword(),
							invitees, items);
					String time = dateFormat.format(new Date(f.lastModified()));
					addStoredEventEntry(event, time);
				} catch (InvalidContextException e) {
					Logger.getLogger(
							"it.uniba.di.cdg.xcore.econference.model.storedevents")
							.log(Level.INFO, e.getMessage());
					continue;
				} catch (Exception e) {
					System.err
							.println("Failed to load eConference context file: "
									+ f.getAbsolutePath());
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #dispose()
	 */
	public void dispose() {
		// forces removal of all the registered listeners
		listeners.clear();

		INetworkBackendHelper backendHelper = NetworkPlugin.getDefault()
				.getHelper();
		Collection<IBackendDescriptor> descriptors = backendHelper
				.getRegistry().getDescriptors();
		for (IBackendDescriptor d : descriptors) {
			backendHelper.unregisterBackendListener(d.getId(), this);
		}
		folderMonitorThread.interrupt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #addStoredEventEntry(it.uniba.di.cdg.xcore.m2m.InvitationEvent)
	 */
	public void addStoredEventEntry(InvitationEvent event, String time) {
		IStoredEventEntry se = new StoredEventEntry(event);
		String accId = getUserAccountId(event.getBackendId());
		se.setAccountId(accId);
		String hash = hexEncode(hash(se));
		se.setHash(hash);
		se.setReceivedOn(time);
		storedEvents.put(hash, se);
		storeEventsPreferences();
		notifyUpdateToListeners();
	}

	/**
	 * The byte[] returned by MessageDigest does not have a nice textual
	 * representation, so some form of encoding is usually performed.
	 * 
	 * This implementation follows the example of David Flanagan's book
	 * "Java In A Nutshell", and converts a byte array into a String of hex
	 * characters.
	 * 
	 * Another popular alternative is to use a "Base64" encoding.
	 */
	private static String hexEncode(String hash) {
		byte[] aInput = hash.getBytes();
		StringBuffer result = new StringBuffer();
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		for (int idx = 0; idx < aInput.length; ++idx) {
			byte b = aInput[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}

	/**
	 * @param se
	 * 
	 */
	private String hash(IStoredEventEntry se) {
		String hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update(se.getBytes());
			hash = new String(md.digest());
			// hash = new String( se.getBytes() );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #removeStoredEventEntry
	 * (it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventEntry)
	 */
	public void removeStoredEventEntry(IStoredEventEntry event) {
		if (storedEvents.containsKey(event.getHash())) {
			storedEvents.remove(event.getHash());
			removeStoredEventPreference(event);
			if (event.getInvitationEvent().getEventType() == ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE) {
				String filename = event.getInvitationEvent().toString();
				File toRemove = new File(DEFAULT_FILE_PATH + filename + ".ecx");
				if (toRemove.exists())
					toRemove.delete();
			}

			notifyUpdateToListeners();
		}
	}

	/**
	 * @param eentry
	 * 
	 */
	private void removeStoredEventPreference(IStoredEventEntry eentry) {
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);

		String pathName = RCVD_EVENT_PATH_NODE + eentry.getAccountId();
		try {
			if (preferences.nodeExists(pathName)) {
				Preferences accountIdRootNode = preferences.node(pathName);
				// we have found the account preference node
				// now we need to find the event entry node to remove
				String entryPathName = EVENT_HASH + eentry.getHash();
				if (accountIdRootNode.nodeExists(entryPathName)) {
					accountIdRootNode.node(entryPathName).removeNode();
					System.out.println("REMOVE: path node:" + entryPathName);
				}

			}
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #removeAllStoredEventEntries()
	 */
	public void removeAllStoredEventEntries() {
		for (Iterator<IStoredEventEntry> i = storedEvents.values().iterator(); i
				.hasNext();) {
			IStoredEventEntry event = i.next();
			if (event.getInvitationEvent().getEventType() == ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE) {
				String filename = event.getInvitationEvent().toString();
				File toRemove = new File(DEFAULT_FILE_PATH + filename + ".ecx");
				if (toRemove.exists())
					toRemove.delete();
			}
		}
		storedEvents.clear();
		removeAllStoredPreferences();
		notifyUpdateToListeners();
	}

	/**
	 * Remove all the stored preferences for all the current online backends.
	 */
	private void removeAllStoredPreferences() {
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);

		Collection<IBackendDescriptor> onlineBackends = NetworkPlugin
				.getDefault().getHelper().getOnlineBackends();

		for (IBackendDescriptor descriptor : onlineBackends) {

			String pathName = RCVD_EVENT_PATH_NODE
					+ getUserAccountId(descriptor.getId());
			try {
				if (preferences.nodeExists(pathName)) {
					preferences.node(pathName).removeNode();
					System.out.println("REMOVE ALL: path node:" + pathName);
				}
				preferences.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Notifies model updates to all registered listeners.
	 */
	private void notifyUpdateToListeners() {
		for (IStoredEventsModelListener l : listeners) {
			l.notifyUpdate();
		}
	}

	/**
	 * Stores the received events as preferences, according to the user context
	 * ids of the backends currently online.
	 * 
	 * @see it.uniba.di.cdg.xcore.network.UserContext
	 */
	private void storeEventsPreferences() {
		System.out.println("Storing entries");
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);

		Collection<IBackendDescriptor> onlineBackends = NetworkPlugin
				.getDefault().getHelper().getOnlineBackends();

		for (IBackendDescriptor descriptor : onlineBackends) {
			String backendId = descriptor.getId();

			String userAccountId = getUserAccountId(backendId);
			System.out.println("STORE: id: " + userAccountId);

			Preferences connections = preferences.node(RCVD_EVENT_PATH_NODE
					+ userAccountId);
			System.out.println("STORE: path node:" + RCVD_EVENT_PATH_NODE
					+ userAccountId);

			Iterator<IStoredEventEntry> iter = storedEvents.values().iterator();
			while (iter.hasNext()) {
				IStoredEventEntry event = (IStoredEventEntry) iter.next();

				if (backendId.equals(event.getBackendId())) {

					Preferences connection = connections.node(EVENT_HASH
							+ event.getHash());

					System.out
							.println("event: " + EVENT_HASH + event.getHash());

					connection.put(ACCOUNT_ID, userAccountId);
					connection.put(EVENT_RCVD_ON, event.getReceivedOn());

					int eventType = event.getInvitationEvent().getEventType();
					connection.putInt(EVENT_TYPE, eventType);

					if (event.getInvitationEvent().getEventType() == ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE) {
						ConferenceOrganizationEvent coe = (ConferenceOrganizationEvent) event
								.getInvitationEvent();
						if (coe.getItems() != null) {
							StringBuffer bf = new StringBuffer();
							String[] items = coe.getItems();
							for (int i = 0; i < items.length; i++) {
								bf.append(items[i]);
								if (i + 1 < items.length)
									bf.append(SEPARATOR);
							}
							connection.put(ITEM_LIST, bf.toString());
						}
						if (coe.getParticipants() != null) {
							StringBuffer bf = new StringBuffer();
							String[] participants = coe.getParticipants();
							for (int i = 0; i < participants.length; i++) {
								bf.append(participants[i]);
								if (i + 1 < participants.length)
									bf.append(SEPARATOR);
							}
							connection.put(INVITEES_LIST, bf.toString());
						}
					}

					if (event.getHash() != null)
						connection.put(HASH, event.getHash());
					else
						continue;
					if (event.getInviter() != null)
						connection.put(INVITER, event.getInviter());
					else
						continue;
					if (event.getRoom() != null)
						connection.put(ROOM, event.getRoom());
					else
						continue;
					if (event.getReason() != null)
						connection.put(REASON, event.getReason());
					else
						continue;
					if (event.getPassword() != null)
						connection.put(PASSWORD, event.getPassword());
					else
						connection.put(PASSWORD, "");
					if (event.getSchedule() != null)
						connection.put(SCHEDULE, event.getSchedule());
					else
						connection.put(SCHEDULE, "");
					if (event.getBackendId() != null)
						connection.put(BACKEND_ID, event.getBackendId());
					else
						continue;

				}
			}
			try {
				connections.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param backendId
	 * @return
	 */
	private String getUserAccountId(String backendId) {
		String id = NetworkPlugin.getDefault().getRegistry()
				.getBackend(backendId).getUserAccount().getId();
		id += "@"
				+ NetworkPlugin.getDefault().getRegistry()
						.getBackend(backendId).getServerContext()
						.getServerHost();
		return id;
	}

	/**
	 * Must be executed on connection.
	 */
	private void loadStoredEventsPreferences() {
		System.out.println("Loading entries");
		try {
			Preferences preferences = new ConfigurationScope()
					.getNode(CONFIGURATION_NODE_QUALIFIER);

			Collection<IBackendDescriptor> onlineBackends = NetworkPlugin
					.getDefault().getHelper().getOnlineBackends();
			for (IBackendDescriptor descriptor : onlineBackends) {

				String id = getUserAccountId(descriptor.getId());

				Preferences connections = preferences.node(RCVD_EVENT_PATH_NODE
						+ id);
				System.out.println("LOAD: path node: " + RCVD_EVENT_PATH_NODE
						+ id);
				String[] events = connections.childrenNames();

				System.out.println("LOAD: " + events.length);
				for (int i = 0; i < events.length; i++) {
					IStoredEventEntry se;
					String event = events[i];
					Preferences node = connections.node(event);

					System.out.println(event);

					String time = node.get(EVENT_RCVD_ON, "");
					String hash = node.get(HASH, "");
					String accountid = node.get(ACCOUNT_ID, "");
					String backendid = node.get(BACKEND_ID, "");
					String room = node.get(ROOM, "");
					String inviter = node.get(INVITER, "");
					String reason = node.get(REASON, "");
					String passwd = node.get(PASSWORD, "");
					String schedule = node.get(SCHEDULE, "");
					int type = Integer.parseInt(node.get(EVENT_TYPE, "0"));
					if (type == ConferenceOrganizationEvent.ORGANIZATION_EVENT_TYPE) {
						String[] items = node.get(ITEM_LIST, "").split(
								SEPARATOR);
						String[] invitees = node.get(INVITEES_LIST, "").split(
								SEPARATOR);

						// loaded prefs are for a ConferenceOrganizationEvent
						if (items.length >= 1 && invitees.length >= 1) {
							se = new StoredEventEntry(accountid, backendid,
									room, inviter, schedule, reason, passwd,
									invitees, items, time);
							se.setHash(hash);
							storedEvents.put(hash, se);
						}
					} else {
						// loaded prefs are for a InvitationEvent
						se = new StoredEventEntry(accountid, backendid, room,
								inviter, schedule, reason, passwd, time);
						se.setHash(hash);
						storedEvents.put(hash, se);
					}

				}

			}

		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #getStoredEvents()
	 */
	public Object[] getStoredEvents() {
		return storedEvents.values().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #addListener(it.uniba.di.cdg.xcore.econference.model.storedevents.
	 * IStoredEventsModelListener)
	 */
	public void registerStoredEventsModelListener(IStoredEventsModelListener l) {
		listeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.econference.model.storedevents.IStoredEventsModel
	 * #removeListener(it.uniba.di.cdg.xcore.econference.model.storedevents.
	 * IStoredEventsModelListener)
	 */
	public void unregisterStoredEventsModelListener(IStoredEventsModelListener l) {
		listeners.remove(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.uniba.di.cdg.xcore.network.events.IBackendEventListener#onBackendEvent
	 * (it.uniba.di.cdg.xcore.network.events.IBackendEvent)
	 */
	public void onBackendEvent(IBackendEvent event) {
		if (event instanceof InvitationEvent) {
			String time = dateFormat.format(new Date());
			addStoredEventEntry((InvitationEvent) event, time);
			System.out.println("New invitation event rcvd");
		} else if (event instanceof BackendStatusChangeEvent) {
			BackendStatusChangeEvent bs = (BackendStatusChangeEvent) event;
			if (bs.isOnline()) {
				// populates the view
				reloadContextFiles();
				loadStoredEventsPreferences();
				notifyUpdateToListeners();
			} else { // going offline
				// clears the view
				storedEvents.clear();
				notifyUpdateToListeners();
			}
		}
	}

	@Override
	public void notifyFolderChange() {
		reloadContextFiles();
		notifyUpdateToListeners();
	}

}
