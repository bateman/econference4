package it.uniba.di.cdg.xcore.one2oneinfo;

import java.util.HashMap;

import it.uniba.di.cdg.xcore.network.IBackend;
import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.model.IBuddy;
import it.uniba.di.cdg.xcore.ui.actions.AbstractBuddyActionDelegate;

import org.eclipse.jface.action.IAction;

public class GetUserInfo extends AbstractBuddyActionDelegate{
 @Override
 public void run(IAction action) {
  final IBuddy buddy = getSelected();
  IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
  IChatServiceActions chat = b.getChatServiceAction();
  chat.OpenChat(buddy.getId());
  chat.SendExtensionProtocolMessage(buddy.getId(),
			"GET_USER_INFO", new HashMap<String, String>());
 }
}
