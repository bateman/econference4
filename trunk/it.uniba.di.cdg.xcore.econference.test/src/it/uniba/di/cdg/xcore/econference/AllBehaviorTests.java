package it.uniba.di.cdg.xcore.econference;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    EConferenceHelperBehaviorTest.class,
    EConferenceManagerBehaviorTest.class,
    EConferenceServiceBehaviorTest.class
})

public class AllBehaviorTests {
    public static void main(String[] args) {
        JUnitCore.runClasses(new Class[] { AllBehaviorTests.class });
    }

}
