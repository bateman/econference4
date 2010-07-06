package it.uniba.di.cdg.skype;

import java.util.Collection;

import it.uniba.di.cdg.xcore.network.model.AbstractBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.network.model.IBuddyGroup;
import it.uniba.di.cdg.xcore.network.model.IEntry;

public class SkypeBuddyGroup extends AbstractBuddyGroup {

	public SkypeBuddyGroup() {
		
	}

	@Override
	public void addBuddy(IBuddy buddy) {
		
	}

	@Override
	public boolean contains(IBuddy buddy) {
		return false;
	}

	@Override
	public Collection<IBuddy> getBuddies() {		
		return null;
	}

	@Override
	public String getName() {		
		return null;
	}

	@Override
	public boolean hasBuddies() {		
		return false;
	}

	@Override
	public void removeBuddy(IBuddy buddy) {
		
	}

	@Override
	public IEntry[] getChilds() {		
		return null;
	}

	@Override
	public IEntry getParent() {		
		return null;
	}

	@Override
	public int compareTo(IBuddyGroup group) {
		return 0;
	}

}
