package it.uniba.di.cdg.xcore.network.internal;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BackendRegistryTestCase.class,
	NetworkServiceRegistryTestCase.class
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