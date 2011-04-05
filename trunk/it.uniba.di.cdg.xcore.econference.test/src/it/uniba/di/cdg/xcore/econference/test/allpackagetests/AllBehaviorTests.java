package it.uniba.di.cdg.xcore.econference.test.allpackagetests;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.framework.TestSuite;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/* All behavior tests in it.uniba.di.cdg.xcore.econference.test
 * are in one package because at this moment we found no need to create
 * a it.uniba.di.cdg.xcore.econference.service package just for one test case
 * */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    it.uniba.di.cdg.xcore.econference.AllBehaviorTests.class
})
public class AllBehaviorTests {

    /**
     * Launch the test.
     *
     * @param args the command line arguments
     *
     * @generatedBy CodePro at 07/02/11 12.23
     */
    public static void main(String[] args) {
        JUnitCore.runClasses(new Class[] { AllBehaviorTests.class });
    }
}
