package it.uniba.di.cdg.skype.action;

import it.uniba.di.cdg.skype.MyCallStatusChangedListener;
import it.uniba.di.cdg.xcore.network.action.ICallAction;

import java.util.Map;
import java.util.TreeMap;

import com.skype.Call;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeCallAction implements ICallAction {
	
	private Map<String, Call> calls;

	@Override
	public void acceptCall(String from) {
		Call call = calls.get(from);
		if(call != null){
			try {
				call.answer();
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void call(String to) {
		try {
			Call call = Skype.call(to);
			addCall(to, call);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		
	}
	
	public SkypeCallAction() {
		super();
		calls = new  TreeMap<String, Call>();
	}

	public void addCall(String from, Call call){
		calls.put(from, call);
		call.addCallStatusChangedListener(
				new MyCallStatusChangedListener(calls, from));
	}

	@Override
	public void finishCall(String user) {
		Call call = calls.get(user);
		if(call != null){
			try {
				call.finish();
				//call.removeCallStatusChangedListener(callStatusChangedListener);
				calls.remove(user);
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isCalling(String user) {
		return (calls.containsKey(user));
	}

	@Override
	public void declineCall(String from) {
		Call call = calls.get(from);
		if(call != null){
			try {
				call.cancel();
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		}
	}

}
