package it.uniba.di.cdg.test.projecttests;

import it.uniba.di.cdg.collaborativeworkbench.boot.test.BackendEventListenerTest;
import it.uniba.di.cdg.jabber.ConnectionTest;
import it.uniba.di.cdg.jabber.InvitationEventTest;
import it.uniba.di.cdg.jabber.JabberBackendTestCase;
import it.uniba.di.cdg.jabber.MUCTest;
import it.uniba.di.cdg.jabber.internal.BuddyGroupTestCase;
import it.uniba.di.cdg.jabber.internal.BuddyRosterTestCase;
import it.uniba.di.cdg.jabber.internal.BuddyTestCase;
import it.uniba.di.cdg.jabber.internal.XMPPUtilsTestCase;
import it.uniba.di.cdg.skype.test.SkypeBackendTest;
import it.uniba.di.cdg.xcore.econference.EConferenceHelperTestCase;
import it.uniba.di.cdg.xcore.econference.model.hr.HandRaisingModelTestCase;
import it.uniba.di.cdg.xcore.econference.model.hr.QuestionTestCase;
import it.uniba.di.cdg.xcore.econference.model.internal.ConferenceContextLoaderTestCase;
import it.uniba.di.cdg.xcore.econference.model.internal.ConferenceModelTestCase;
import it.uniba.di.cdg.xcore.econference.model.internal.ItemListTestCase;
import it.uniba.di.cdg.xcore.m2m.MultiChatHelperTestCase;
import it.uniba.di.cdg.xcore.m2m.model.ChatRoomTestCase;
import it.uniba.di.cdg.xcore.m2m.model.ParticipantTestCase;
import it.uniba.di.cdg.xcore.m2m.model.RoleTestCase;
import it.uniba.di.cdg.xcore.ui.util.TypingStatusUpdaterTestCase;
import it.uniba.di.cdg.xcore.util.MementoTestCase;
import it.uniba.di.cdg.xcore.util.MiscTestCase;
import it.uniba.di.cdg.xcore.util.VirtualProxyTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$
		// Boot test
		suite.addTestSuite(BackendEventListenerTest.class);
		// Jabber Test
		suite.addTestSuite(MUCTest.class);
		suite.addTestSuite(InvitationEventTest.class);
		suite.addTestSuite(JabberBackendTestCase.class);
		suite.addTestSuite(ConnectionTest.class);
		suite.addTestSuite(BuddyTestCase.class);
		suite.addTestSuite(BuddyGroupTestCase.class);
		suite.addTestSuite(XMPPUtilsTestCase.class);
		suite.addTestSuite(BuddyRosterTestCase.class);
		// Skype Test
		suite.addTestSuite(SkypeBackendTest.class);
		// Econference test
		suite.addTestSuite(EConferenceHelperTestCase.class);
		suite.addTestSuite(HandRaisingModelTestCase.class);
		suite.addTestSuite(QuestionTestCase.class);
		suite.addTestSuite(ConferenceContextLoaderTestCase.class);
		suite.addTestSuite(ConferenceModelTestCase.class);
		suite.addTestSuite(ItemListTestCase.class);
		// m2m test
		suite.addTestSuite(MultiChatHelperTestCase.class);
		suite.addTestSuite(ChatRoomTestCase.class);
		suite.addTestSuite(ParticipantTestCase.class);
		suite.addTestSuite(RoleTestCase.class);
		// Network test
		suite.addTestSuite(CapabilitiesTestCase.class);
		suite.addTestSuite(CapabilityTestCase.class);
		suite.addTestSuite(JabberAccountTestCase.class);
		suite.addTestSuite(NetworkServiceRegistryTestCase.class);
		suite.addTestSuite(BackendRegistryTestCase.class);
		suite.addTestSuite(EntryTestCase.class);
		suite.addTestSuite(TalkModelTestCase.class);
		// UI test
		suite.addTestSuite(TypingStatusUpdaterTestCase.class);
		// Util test
		suite.addTestSuite(MementoTestCase.class);
		suite.addTestSuite(VirtualProxyTestCase.class);
		suite.addTestSuite(MiscTestCase.class);
		// $JUnit-END$
		return suite;
	}

}
