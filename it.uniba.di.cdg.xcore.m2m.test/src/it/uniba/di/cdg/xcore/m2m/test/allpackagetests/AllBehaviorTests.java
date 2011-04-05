package it.uniba.di.cdg.xcore.m2m.test.allpackagetests;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    it.uniba.di.cdg.xcore.m2m.AllBehaviorTests.class
})
public class AllBehaviorTests {
    public static void main(String[] args) {
        JUnitCore.runClasses(new Class[] { AllBehaviorTests.class });
    }
}
