package it.uniba.di.cdg.skype.x86sdk.test;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import junit.framework.TestCase;
import it.uniba.di.cdg.skype.x86sdk.SkypeBackend;
import it.uniba.di.cdg.skype.x86sdk.SkypeBuddyRoster;
import it.uniba.di.cdg.xcore.network.BackendException;
import it.uniba.di.cdg.xcore.network.INetworkBackendHelper;
import it.uniba.di.cdg.xcore.network.UserContext;

import com.skype.api.Contact;
import com.skype.api.ContactGroup;

public class SkypeBuddyRosterTest extends TestCase {

	SkypeBackend sbackend = new SkypeBackend();
	INetworkBackendHelper backendHelper = mock(INetworkBackendHelper.class);
	UserContext userCont = new UserContext("econferencetester1",
			"econferencetester1");
	private SkypeBuddyRoster roster = new SkypeBuddyRoster(sbackend);

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		sbackend.setHelper(backendHelper);
		try {
			sbackend.connect(null, userCont);
			Thread.sleep(10000);
		} catch (BackendException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGroup() {
		roster.addGroup("prova");
		assertTrue(roster.containsGroup("prova"));
	}

	@Test
	public void testMoveToGroup() {
		roster.moveToGroup("echo123", "prova");
		assertFalse(roster.getGroups(roster.getBuddy("echo123")).isEmpty());
	}

	@Test
	public void testRenameGroup() {
		roster.renameGroup("prova", "amici");
		assertTrue(roster.containsGroup("amici"));

	}

	@Test
	public void testRenameBuddy() {
		roster.renameBuddy("echo123", "test skype");
		assertEquals("test skype", roster.getBuddy("echo123").getName());

	}

	@Test
	public void testAddContainsBuddy() {
		ContactGroup[] g = SkypeBackend.skype.GetCustomContactGroups();
		boolean trovato = false;
		for (int i = 0; i < g.length; i++) {
			ContactGroup group = g[i];
			if (group.GetStrProperty(ContactGroup.PROPERTY.given_displayname)
					.equals("amici")) {
				Contact[] friends = group.GetContacts();

				for (int j = 0; j < group.GetContacts().length; j++) {
					Contact friend = friends[j];

					if (friend.GetStrProperty(Contact.PROPERTY.skypename)
							.equals("echo123"))
						trovato = true;
				}
			}
		}
		assertTrue(trovato);
	}

	@Test
	public void testRemoveGroup() {
		roster.removeGroup("amici", "None");
		roster.removeGroup("famiglia", "None");
		boolean trovato1 = false;
		boolean trovato2 = false;
		ContactGroup[] g = SkypeBackend.skype.GetCustomContactGroups();
		for (int i = 0; i < g.length; i++) {
			ContactGroup group = g[i];
			if (group.GetStrProperty(ContactGroup.PROPERTY.given_displayname)
					.equals("amici")) {
				trovato1 = true;
			}
			if (group.GetStrProperty(ContactGroup.PROPERTY.given_displayname)
					.equals("famiglia")) {
				trovato2 = true;
			}
		}

		assertFalse(trovato1);
		assertFalse(trovato2);
	}

}
