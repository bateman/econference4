package it.uniba.di.cdg.skype.x86sdk;



import com.skype.api.Account;
import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.ContactGroup.PROPERTY;
import com.skype.api.ContactSearch;
import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Participant;
import com.skype.api.Skype;
import com.skype.api.SkypeObject;
import com.skype.api.Sms;
import com.skype.api.Transfer;
import com.skype.api.Video;
import com.skype.api.Voicemail;
import com.skype.api.Account.AccountListener;
import com.skype.api.Contact.ContactListener;
import com.skype.api.ContactGroup.ContactGroupListener;
import com.skype.api.Conversation.ConversationListener;
import com.skype.api.Conversation.LIST_TYPE;
import com.skype.api.Message.MessageListener;
import com.skype.api.Participant.DTMF;
import com.skype.api.Participant.ParticipantListener;
import com.skype.api.Skype.PROXYTYPE;
import com.skype.api.Skype.SkypeListener;
import com.skype.ipc.RootObject.ErrorListener;
import com.skype.util.Log;



public class SkypeListeners implements AccountListener, SkypeListener, ContactListener, ConversationListener, MessageListener, ParticipantListener, ContactGroupListener,  ErrorListener {

	private static final String TAG      = "SkypekitListeners";

	public boolean              loggedIn = false;

	private Skype               skype;

	private jwcObserver     mObserver;
	private SkypeBackend    mClient;

	public interface jwcObserver
	{
		public abstract void OnConversationListChange(Conversation conversation, LIST_TYPE type, boolean added);
		public abstract void OnMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation);
		public abstract void OnPropertyChange(SkypeObject obj, com.skype.api.Account.PROPERTY prop, Object value);
		public abstract void OnPropertyChange(SkypeObject obj, com.skype.api.Contact.PROPERTY prop, Object value);
		public abstract void OnPropertyChange(SkypeObject obj, com.skype.api.Conversation.PROPERTY prop, Object value);
		public abstract void OnPropertyChange(SkypeObject obj, com.skype.api.Message.PROPERTY prop, Object value);
		public abstract void OnPropertyChange(SkypeObject obj, com.skype.api.Participant.PROPERTY prop, Object value);
		public abstract void onAccountStatusChange();

	}

	public SkypeListeners(SkypeBackend skypeBackend, Skype theSkype)
	{
		this.skype = theSkype;
		this.mObserver = skypeBackend;
		this.mClient = skypeBackend;
		Log.d(TAG, "ctor time to register the listeners");
		registerAllListeners();
		mClient.skype.SetErrorListener(this);
	}

	public void registerListener(int modid)
	{
		// Register the listener with Skype service
		skype.RegisterListener(modid, this);
	}

	public void registerAllListeners()
	{
		registerListener(Skype.getmoduleid());
		registerListener(Account.moduleID());
		registerListener(Contact.moduleID());
		registerListener(ContactGroup.moduleID());
		registerListener(ContactSearch.moduleID());
		registerListener(Conversation.moduleID());
		registerListener(Message.moduleID());
		registerListener(Participant.moduleID());
		registerListener(Sms.moduleID());
		registerListener(Transfer.moduleID());
		registerListener(Video.moduleID());
		registerListener(Voicemail.moduleID());
	}

	public void unRegisterListener(int modid)
	{
		// Register the listener with Skype service
		skype.UnRegisterListener(modid, this);
	}

	public void unRegisterAllListeners()
	{
		unRegisterListener(Account.moduleID());
		unRegisterListener(Contact.moduleID());
		unRegisterListener(ContactGroup.moduleID());
		unRegisterListener(ContactSearch.moduleID());
		unRegisterListener(Conversation.moduleID());
		unRegisterListener(Message.moduleID());
		unRegisterListener(Participant.moduleID());
		unRegisterListener(Sms.moduleID());
		unRegisterListener(Transfer.moduleID());
		unRegisterListener(Video.moduleID());
		unRegisterListener(Voicemail.moduleID());
	}


	@Override
	public void OnPropertyChange(SkypeObject obj, com.skype.api.Participant.PROPERTY prop, Object value)
	{
		mObserver.OnPropertyChange(obj, prop, value);
	}


	@Override
	public void OnPropertyChange(SkypeObject obj, com.skype.api.Message.PROPERTY prop, Object value)
	{
		mObserver.OnPropertyChange(obj, prop, value);
	}


	@Override
	public void OnPropertyChange(SkypeObject obj, com.skype.api.Conversation.PROPERTY prop, Object value)
	{
		mObserver.OnPropertyChange(obj, prop, value);
	}


	@Override
	public void OnParticipantListChange(SkypeObject obj)
	{

	}


	@Override
	public void OnMessage(SkypeObject obj, Message message)
	{

	}


	@Override
	public void OnSpawnConference(SkypeObject obj, Conversation spawned)
	{

	}


	@Override
	public void OnPropertyChange(SkypeObject obj, com.skype.api.Contact.PROPERTY prop, Object value)
	{
		mObserver.OnPropertyChange(obj, prop, value);
	}


	@Override
	public void OnNewCustomContactGroup(ContactGroup group)
	{

	}


	@Override
	public void OnContactOnlineAppearance(Contact contact)
	{
		System.out.println("OnContactOnlineAppearance: " + contact.GetStrProperty(Contact.PROPERTY.skypename));
	}


	@Override
	public void OnContactGoneOffline(Contact contact)
	{
		System.out.println("SKYPE.OnContactGoneOffline: " + contact.GetStrProperty(Contact.PROPERTY.skypename));
	}


	@Override
	public void OnConversationListChange(Conversation conversation, LIST_TYPE type, boolean added)
	{
		mObserver.OnConversationListChange(conversation, type, added);
	}


	@Override
	public void OnMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage,
			Conversation conversation)
	{
		mObserver.OnMessage(message, changesInboxTimestamp, supersedesHistoryMessage, conversation);
	}


	@Override
	public void OnAvailableVideoDeviceListChange()
	{

	}


	@Override
	public void OnAvailableDeviceListChange()
	{

	}


	@Override
	public void OnNrgLevelsChange()
	{

	}


	@Override
	public void OnProxyAuthFailure(PROXYTYPE type)
	{
		System.err.println("Proxy Authorization Failure:" + type);
	}


	@Override
	public void OnPropertyChange(SkypeObject obj, com.skype.api.Account.PROPERTY prop, Object value)
	{
		mObserver.OnPropertyChange(obj, prop, value);
	}

	public void OnSkypeKitFatalError()
	{
		System.err.println("SkypeKit fatal error reported.  Continue at your own risk.");
		System.err.println("Real applications should shut down at this point.");
	}

	public void OnSkypeKitConnectionClosed()
	{
		System.err.println("The connection to the SkypeKit runtime has closed.");
		System.err.println("Recovery is possible is the runtime resumes and the user reconnects to it.");
	}

	@Override
	public void OnIncomingDTMF(SkypeObject obj, DTMF dtmf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnPropertyChange(SkypeObject obj, PROPERTY prop,
			Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnChangeConversation(SkypeObject obj,
			Conversation conversation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnChange(SkypeObject obj, Contact contact) {
		// TODO Auto-generated method stub

	}
}


