package it.uniba.di.cdg.xcore.m2m;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    MultiChatHelperBehaviorTest.class,
    MultiChatManagerBehaviorTest.class,
    MultiChatServiceBehaviorTest.class
})
public class AllBehaviorTests {
    public static void main(String[] args) {
        JUnitCore.runClasses(new Class[] { AllBehaviorTests.class });
    }
}
