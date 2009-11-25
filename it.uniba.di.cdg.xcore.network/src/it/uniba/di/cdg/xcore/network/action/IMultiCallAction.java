package it.uniba.di.cdg.xcore.network.action;

public interface IMultiCallAction {

	public void acceptCall();
	
	public void declineCall();
	
	public void call();
	
	public boolean isCalling();
	
	public void finishCall();
}
