package it.uniba.di.cdg.xcore.network.action;

public interface ICallAction {
	
	public void acceptCall(String from);
	
	public void declineCall(String from);
	
	public void call(String to);
	
	public boolean isCalling(String user);
	
	public void finishCall(String user);

}
