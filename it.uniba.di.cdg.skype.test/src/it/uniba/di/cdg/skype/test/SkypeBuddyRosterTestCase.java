package it.uniba.di.cdg.skype.test;

import it.uniba.di.cdg.skype.SkypeBackend;
import it.uniba.di.cdg.skype.SkypeBuddyRoster;

import org.jmock.MockObjectTestCase;

import com.skype.Friend;
import com.skype.Group;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeBuddyRosterTestCase extends MockObjectTestCase {
    
    private SkypeBuddyRoster roster;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SkypeBackend sbackend = new SkypeBackend();
        this.roster = new SkypeBuddyRoster( sbackend );
    
        }
 
    public void testAddGroup() throws SkypeException {   
    	roster.addGroup("prova");
    	assertTrue( roster.containsGroup("prova")); 
    }
    
    public void testMoveToGroup() throws SkypeException {       
    	roster.moveToGroup("echo123", "prova");
    	assertFalse(roster.getGroups(roster.getBuddy("echo123")).isEmpty());
    }

    public void testRenameGroup()throws SkypeException {       
    	roster.renameGroup("prova", "amici");
    	assertTrue( roster.containsGroup("amici"));
    }
    
    public void testRenameBuddy() {  
    	roster.renameBuddy("echo123", "test skype");
    	assertEquals("test skype" , roster.getBuddy("echo123").getName());
    }
    
    public void testAddContainsBuddy() throws SkypeException {
    	roster.addGroup("famiglia");
    	roster.moveToGroup("echo123", "famiglia");
    	Group group = Skype.getContactList().getGroup("famiglia");
        Friend friend = Skype.getContactList().getFriend("echo123");
        assertTrue( group.hasFriend(friend) );
    }
    
    public void testRemoveGroup() throws SkypeException {       
    	Group group= Skype.getContactList().getGroup("luna");
    	roster.removeGroup("amici","None");
    	roster.removeGroup("famiglia","None");
    	Group group2= Skype.getContactList().getGroup("famiglia");
    	assertFalse(roster.getAllGroups().equals(group)); 
    	assertFalse(roster.getAllGroups().equals(group2));
    }
       
}
