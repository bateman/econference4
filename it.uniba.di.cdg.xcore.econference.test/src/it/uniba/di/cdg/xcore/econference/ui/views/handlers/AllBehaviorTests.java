package it.uniba.di.cdg.xcore.econference.ui.views.handlers;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    FormatterHandlerBehaviorTest.class
})

public class AllBehaviorTests {
    public static void main(String[] args) {
        JUnitCore.runClasses(new Class[] { AllBehaviorTests.class });
    }

}