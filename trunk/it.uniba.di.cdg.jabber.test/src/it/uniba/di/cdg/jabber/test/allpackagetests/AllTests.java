package it.uniba.di.cdg.jabber.test.allpackagetests;

import it.uniba.di.cdg.jabber.*;
import it.uniba.di.cdg.jabber.internal.BuddyGroupTestCase;
import it.uniba.di.cdg.jabber.internal.BuddyRosterTestCase;
import it.uniba.di.cdg.jabber.internal.BuddyTestCase;
import it.uniba.di.cdg.jabber.internal.XMPPUtilsTestCase;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	it.uniba.di.cdg.jabber.AllTests.class,
	it.uniba.di.cdg.jabber.internal.AllTests.class
})
public class AllTests {

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 07/02/11 12.23
	 */
	public static void main(String[] args) {
		JUnitCore.runClasses(new Class[] { AllTests.class });
	}
}
