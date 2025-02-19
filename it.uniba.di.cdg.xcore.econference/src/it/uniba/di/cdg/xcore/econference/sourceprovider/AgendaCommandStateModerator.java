package it.uniba.di.cdg.xcore.econference.sourceprovider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class AgendaCommandStateModerator extends AbstractSourceProvider {
	public final static String MY_STATE = "it.uniba.di.cdg.xcore.econference.sourceprovider.enable";
	public final static String ENABLED = "ENABLED";
	public final static String DISENABLED = "DISENABLED";
	private boolean enabled = true;


	@Override
	public void dispose() {
	}

	// We could return several values but for this example one value is sufficient
	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { MY_STATE };
	}
	
	// You cannot return NULL
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map getCurrentState() {
		Map map = new HashMap(1);
		String value = enabled ? ENABLED : DISENABLED;
		map.put(MY_STATE, value);
		
		return map;
	}

	

	// This method can be used from other commands to change the state
	// Most likely you would use a setter to define directly the state and not use this toogle method
	// But hey, this works well for my example
	public void toogleEnabled(Boolean notStarted) {
		
		String value = notStarted ? ENABLED : DISENABLED;
		fireSourceChanged(ISources.WORKBENCH, MY_STATE, value);
	}

}