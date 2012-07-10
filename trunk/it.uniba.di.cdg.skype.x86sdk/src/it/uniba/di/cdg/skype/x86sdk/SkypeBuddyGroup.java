package it.uniba.di.cdg.skype.x86sdk;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import it.uniba.di.cdg.xcore.network.model.AbstractBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IEntry;

public class SkypeBuddyGroup extends AbstractBuddyGroup {
	
	private String name;
    
    private Set<IBuddy> buddies;
    
	public SkypeBuddyGroup( String name ) {
        this.name = name;
        this.buddies = new HashSet<IBuddy>();
    }

	@Override
	public void addBuddy(IBuddy buddy) {
		buddies.add(buddy);
		
	}

	@Override
	public boolean contains(IBuddy buddy) {
		 return buddies.contains( buddy );
	}

	@Override
	public Collection<IBuddy> getBuddies() {		
		return Collections.unmodifiableCollection( buddies );
	}

	@Override
	public String getName() {		
		 return name;
	}

	@Override
	public boolean hasBuddies() {		
		 return !buddies.isEmpty();
	}

	@Override
	public void removeBuddy(IBuddy buddy) {
		 buddies.remove( buddy );
	}
	
	@Override
	public synchronized IEntry[] getChildren() {		
        IEntry[] array = new IEntry[buddies.size()];
        return buddies.toArray( array );
	}

	@Override
	public IEntry getParent() {		
		 return null; 
	}

	@Override
	public int compareTo(IBuddyGroup group) {
		return String.CASE_INSENSITIVE_ORDER.compare( this.getName(), group.getName() );
	}

}
