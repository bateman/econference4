package it.uniba.di.cdg.skype;

import java.util.Map;

import com.skype.Call;
import com.skype.CallStatusChangedListener;
import com.skype.SkypeException;
import com.skype.Call.Status;

public class MyCallStatusChangedListener implements CallStatusChangedListener {

	private Map<String, Call> calls;
	private String to;
		
	public MyCallStatusChangedListener(Map<String, Call> calls, String to) {
		super();
		this.calls = calls;
		this.to = to;
	}

	@Override
	public void statusChanged(Status status) throws SkypeException {
		if(status.equals(Status.FINISHED)||status.equals(Status.REFUSED)){
			calls.remove(to);
		}
	}

}
