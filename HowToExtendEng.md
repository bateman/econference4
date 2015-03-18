**Index**


## How to extend eConference 4 ##
Unlike the previous version, eConference 4 offers a modular structure very organized and it doesn't permits that a service could enter in specific functions which are included in a specific protocol.

![http://econference4.googlecode.com/svn/wiki/img/extend1.jpg](http://econference4.googlecode.com/svn/wiki/img/extend1.jpg) <br />
**Picture 1. Interaction between plugins in eConference 3.**

As it can be seen in picture 1, in eConference 3 the Jabber plugin (which implements the XMPP protocol) has some dependencies from the Chat service; moreover the Multichat service has dependencies from Jabber and even from Smack (a package used to support XMPP) which instead should be visible only to Jabber.

The current structure, as shown in picture 2, has been developed taking into account the following principles:
  * The packages used to support the protocols (Skype4Java, Smack, ..) can be used only from the plugin which implements the same service (es. Skype, Jabber,..).
  * Every service can create dependencies only with another service and with the Network plugin, which offers some tools that all protocols expose (network events, dispatch and reception of messages, VoIP calls, .. ).
  * The protocols must be developed regardless from the system's services but just only providing an implementation of the IBackend interface and all the other interfaces linked with it.

![http://econference4.googlecode.com/svn/wiki/img/extend2.jpg](http://econference4.googlecode.com/svn/wiki/img/extend2.jpg) <br />
**Picture 2. Interaction between plugins in eConference 4**

In version 4 has been introduced a further innovation: the Chat and Multichat plugins are replaced by one2one and m2m; this decision is taken since currently to the text support is juxtapose the VoIP support and so we have to discriminate only if the communication is between two users or a group of users.

### Structure of packages ###
The classes which are part of the core of eConference are included in the _it.uniba.di.cdg.xcore.`*`_ package. All the classes which are outside the core (for example it.uniba.di.cdg.skype, it.uniba.di.cdg.jabber, org.apertium.api.translate, it.uniba.di.cdg.econference.planningpoker) can refer to core resources, hence _import_ classes _core_ but not do the contrary.

## Abstraction mechanisms ##
To make a stable abstraction layer it was necessary the use of specific mechanisms. The main idea for this solution is that all protocols have in common two things:
  1. They allow to execute actions (for example to send a message, to forward a call, to comunicate the change of nickname's status)
  1. They intercept network events (for example a received message, an incoming call, the roster's update)
For these reasons are defined all the basic actions which a protocol may make avaliable and all the events which a protocol may intercept.


### Action ###
In picture 3 are shows four classes which provide the Actions.

![http://econference4.googlecode.com/svn/wiki/img/extend3.jpg](http://econference4.googlecode.com/svn/wiki/img/extend3.jpg) <br />
**Picture 3. Implementation of actions with the Skype protocol**

Actions are divided into four interfaces:
  * ICallAction
  * IMultiCallAction
  * IChatServiceActions
  * IMultiChatServiceActions

This division has been done since it could be a chance in which a protocol offers support only to a single Action category; for example Smack doesn't support VoIP, MSN doesn't support group's VoIP and so on.

Picture 3 shows the implementations of this four interfaces.  It's possible to access the istances of this four classes only by the iBackend interface which will return the implementation provided by the protocol in that specific moment.

Picture 4 shows how the services can access actions. This scheme provides a tight abstraction from the protocol so it's always possible to add a new service therefore it will use only generic Actions and not the protocol's specific actions.

![http://econference4.googlecode.com/svn/wiki/img/extend4.jpg](http://econference4.googlecode.com/svn/wiki/img/extend4.jpg) <br />
**Picture 4. The use of Actions by services**

It's also possible to add new Actions (for example the support to 1-to-1 and/or group videochat).

### Event ###
Since there are events in common between the protocols an abstraction layer has been created to support them; as you can notice in picture 5, protocols (Skype in this example) raise an event and services (one2one in this example) intercept and manage it properly.

![http://econference4.googlecode.com/svn/wiki/img/extend5.jpg](http://econference4.googlecode.com/svn/wiki/img/extend5.jpg) <br />
**Picture 5. Abstraction of the events**

Picture 6 shows how the event system properly works. The service registers an own IBackendListener using the method registerBackendListener() exposed by INetworkBackendHelper  and afterwards the plugin which implements the protocol raises many events calling the notifyBackendEvent() event which is exposed by InetworkBackendHelper.

![http://econference4.googlecode.com/svn/wiki/img/extend6.jpg](http://econference4.googlecode.com/svn/wiki/img/extend6.jpg) <br />
**Picture 6. An example of the use of events**

### Extension ###
After the creation of the abstraction layer there was a great obstacle: the services of Multichat communicate with each other with a particular protocol which extends the basic protocol. For example the eConference service has to communicate to all participants when the conference begins and ends: it's easy to imagine that no protocol offers this function. The solution involved the creation of a mechanism to extend the basic protocol. This is allowed by:
  * An Action called sendExtensionProtocolMessage() which sends a special message that could include information formatted in any way.
  * An Event called ChatExtensionProtocolEvent which is raised when a special message is received

With this mechanism it's possible to redefine on the basic protocols infinite new protocols which are suitable to every distinct service.

## How to include a new communication protocol ##
To include a new protocol you have to create a new plugin, to extend the Network plugin by the IBackend interface and to implements some interfaces. Let's see how to perform this tasks.
As first step download the source code of eConference 4 in Eclipse using the SVN address (as shown in picture 7).

**Note: to see more details about the execution and the compilation of eConference 4, consult this [page](howtobuild.md) wiki.**

![http://econference4.googlecode.com/svn/wiki/img/extend7.png](http://econference4.googlecode.com/svn/wiki/img/extend7.png) <br />
**Picture 7. Checkout of eConference 4.0**

As second step create a new plugin by “File > Other” and select “Plug-In Project” as shown in picture 8..

![http://econference4.googlecode.com/svn/wiki/img/extend8.png](http://econference4.googlecode.com/svn/wiki/img/extend8.png) <br />
**Picture 8. Creation of a new plugin**

At this point open the “plugin.xml” file and from the extension panel click “add”, select then from the new wizard the name extension “it.uniba.di.cdg.xcore.network.backends”. If everything goes well, it will appear a panel like that shown in picture 9.

![http://econference4.googlecode.com/svn/wiki/img/extend9.png](http://econference4.googlecode.com/svn/wiki/img/extend9.png) <br />
**Picture 9. Extension point of IBackend**

Note that in picture 9 the "SkypeBackend" class is associated to the extension, so if you implement the X protocol you have to associate a “XBackend “ class to the extension. After that remains to implement all the methods exposed by the IBackend interface using the Xbackend class shown in picture 10. It's also obviously necessary to provide an implementation to all the interfaces related to IBackend (where it's possible):
  * IBuddyRoster
  * IChatServiceActions
  * IMultiChatServiceActions
  * ICallAction
  * IMultiCallAction

![http://econference4.googlecode.com/svn/wiki/img/extend10.png](http://econference4.googlecode.com/svn/wiki/img/extend10.png) <br />
**Picture 10. Methods of IBackend to implement**

## Pieces of the Skype's plugin code ##
To better understand the correct mode to implement a new protocol we analyze some methods implemented for the Skype protocol.
Let's see the IBackend.Connect() method:

```
@Override
	public void connect(ServerContext ctx, UserContext userAccount)
			throws BackendException {
		Connector.Status status = null;
		Connector conn = Connector.getInstance();
		try {
			status = conn.connect();
		} catch (ConnectorException e1) {
			e1.printStackTrace();
			throw new BackendException(e1.getMessage());
		}
		if(status != Connector.Status.ATTACHED)
			throw new BackendException(new Exception("You have to install and run Skype before running eConference.\n
Please download  Skype from www.skype.com"));
		//notifico l'avvenuta connessione
		helper.notifyBackendEvent(new BackendStatusChangeEvent( ID, true ));
		//notifico l'aggiornamento del roster
		skypeBuddyRoster.updateRoster();
		//aggiungo i listeners di Skype4Java
		try {
			Skype.addChatMessageListener(chatMessageListener);
			Skype.addCallListener(callListener);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		connected = true;
	}
```

The method shown previously is responsible to make a connection. Let's analyze in detail what it does.
The call to the connect() method of Connector returns the Skype's connection status. The status to which we refer is that of the Skype client which has to be correctly started and connected.
If the status is different from “ATTACHED” is raised an exception otherwise everything proceed.
After that, the roster is updated, listeners are registered in the skype4java package and the “connected” variable which keeps the information about the connection status, is set to true.
Let's see the IChatAction.sendExtensionProtocolMessage() method:

```
@Override
public void sendExtensionProtocolMessage(String to, String extensionName,HashMap<String, String> param) {
 param.put(ExtensionConstants.CHAT_TYPE,
     ExtensionConstants.ONE_TO_ONE);
 String message = XmlUtil.writeXmlExtension(extensionName, param);
 try {
  Skype.chat(to).send(message);
 } catch (SkypeException e) {
  e.printStackTrace();
 }
}
```

The method already seen takes care of sending an extension to the protocol. The “CHAT\_TYPE” parameter is added, with the “ONE\_TO\_ONE” value, to denote that the message in sending is an extension for the 1-to-1 chat. The name and the parameter's list of the extension are transformed in an XML string; this string is sent to the receiver.

Below is shown the management of input messages and particularly a message which extends the protocol in the SkypeBackend's processMessageReceived() method:

```
if(XmlUtil.chatType(content).equals(ExtensionConstants.ONE_TO_ONE))
 {
 if(extensionName.equals(ExtensionConstants.CHAT_MESSAGE)){
  HashMap<String, String> param = XmlUtil.readXmlExtension(content);
  String msg = param.get(ExtensionConstants.MESSAGE);
  IBackendEvent event = new ChatMessageReceivedEvent(
    senderId, msg, getBackendId());
  getHelper().notifyBackendEvent(event);
}
```

It's checked if it's a 1-a-1 chat message and particularly if it belongs to an extension; if both conditions are met an event of ChatMessageReceivedEvent will be raised. This event will be intercepted and managed by one or more services like one2one, m2m, cConference or one2oneinfo (which will be created in the next section)

## How to create a new service into eConference ##
To create a new service you have to create a new plugin (like shown in section 4.2). After that it's possible to add new graphic elements with the extensional mechanisms provided by Eclipse. In picture 11 is shown an example of that extension, it's possible to note the “Card Deck View” which has been added to the basic model of the structured MultiChat.
About the aspect of communication, it's possible to accede many mechanisms allowed by an instance of Ibackend: you can have this access with the following call: NetworkPlugin.getDefault().getRegistry().getDefaultBackend();

![http://econference4.googlecode.com/svn/wiki/img/extend11.png](http://econference4.googlecode.com/svn/wiki/img/extend11.png) <br />
**Picture 11. the eConference planning poker plugin**

### Creation of the new service ###
Now will be created a small service which extends eConference 4; with this service every user will have the possibility to receive information about the system used by another user. A new voice called “Get Info” will appear after a mouse's right button click on an user listed in the roster and so we will receive information about that user.
As first step create a new Project plugin selecting “File > New > Other” and “Plug-in Project”, then call the project "it.uniba.di.cdg.xcore.one2oneinfo" and its activator _it.uniba.di.cdg.xcore.one2oneinfo.One2OneInfoPlugin_. In the end click the voice “activator” to create the referenced class. Open the file “plugin.xml” automatically created in the project, enter in the panel “Extension”, click “add” and finally select “popupMenu” as shown in picture 12.

![http://econference4.googlecode.com/svn/wiki/img/extend12.png](http://econference4.googlecode.com/svn/wiki/img/extend12.png) <br />
**Picture 12. Adding an extension to a popupMenu**

Click with a mouse's right button on _org.eclipse.ui.popupMenu_ then on "New > objectContribution". Fill the objectClass field with _it.uniba.di.cdg.xcore.network.model.IBuddy_ which represents an user in the roster. Click now with the mouse's right button on the objectContribution just created and select "New > Action"; insert _it.uniba.di.cdg.xcore.one2oneinfo.GetUserInfo_ into the "Get Info" label and into class and finally click the voice “class” to create it.
If everything went well there will be a panel like the one shown in picture 13.

![http://econference4.googlecode.com/svn/wiki/img/extend13.png](http://econference4.googlecode.com/svn/wiki/img/extend13.png) <br />
**Picture 13. The extension of popupMenu**

Now with the same process we will extend the _org.eclipse.ui.startup_ plugin, add a new voice “startup” and insert the class field _it.uniba.di.cdg.xcore.one2oneinfo.One2OneInfoPlugin_. Finally the class _One2OneInfoPlugin_ has to implement the interface _IStartup_.
With this operation will be possible to start the plugin together eConference and not only when the plugin is called, this is necessary to start the demon which will be explained next.
In _One2OneInfoPlugin_ insert the following code:

```
/**
 * The activator class controls the plug-in life cycle
 */
public class One2OneInfoPlugin extends AbstractUIPlugin implements IStartup {
	// The plug-in ID
	public static final String PLUGIN_ID = "it.uniba.di.cdg.xcore.one2oneinfo";
	// The shared instance
	private static One2OneInfoPlugin plugin;
	/**
	 * The constructor
	 */
	public One2OneInfoPlugin() {
	}
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("One2OneInfoPlugin start..");
		plugin = this;
		new InfoDaemon ().start();
	}
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static One2OneInfoPlugin getDefault() {
		return plugin;
	}
	@Override
	public void earlyStartup() {
	}
}
```

It should be noted that at the plugin's startup is created an instance of the class _InfoDaemon_ and is called the method _start()_; this class will manage on our system the information's incoming requests  and the sending of this information to the applicant.
Create a new class called _InfoDaemon_ filled by the following code:

```
public class InfoDaemon  implements IBackendEventListener {

 public void start(){
  for (IBackendDescriptor d : NetworkPlugin.getDefault()
		  .getRegistry().getDescriptors())
   NetworkPlugin.getDefault().getHelper().registerBackendListener(
		   d.getId(), this);
 }

 @Override
 public void onBackendEvent(IBackendEvent event) {
  if (event instanceof ChatExtensionProtocolEvent) {
   IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
   IChatServiceActions chat = b.getChatServiceAction();
   ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent)event;
   if(cepe.getExtensionName().equals("GET_USER_INFO")){
    HashMap<String, String> param = new HashMap<String, String>();
	param.put("OS_NAME", System.getProperty("os.name"));
	param.put("OS_VER", System.getProperty("os.version"));
	chat.openChat(cepe.getFrom());
	chat.sendExtensionProtocolMessage(cepe.getFrom(),
			"USER_INFO", param);
	chat.closeChat(cepe.getFrom());
   }
   if(cepe.getExtensionName().equals("USER_INFO")){
	String osName = (String) cepe.getExtensionParameter("OS_NAME");
	String osVer = (String) cepe.getExtensionParameter("OS_VER");
	showMessage("One2OneInfo plugin", "OS: " + osName + " ver: " + osVer);
	chat.closeChat(cepe.getFrom());
   }
  }
 }
 
 private void showMessage(String windowTitle, String message) {
	UiPlugin.getUIHelper().showMessage(windowTitle, message);
 }
}
```

That class extends the _IBackendEventListener_ interface and with the implementation of the _onBackendEvent()_ method the class intercepts two extension's type of the protocol:
  * GET\_USER\_INFO: an user requests our information and we send them to him.
  * USER\_INFO:  an user sends its information which we previously requested.
The protocol used is the follow:

```
Utente1 > > > > > > > > > > [GET_USER_INFO, null] > > > > > > > > > Utente2
Utente1 < < < < < < < < < < < < [USER_INFO, info] < < < < < < < < < < Utente2
```

The plugin is created and incoming messages are managed, finally we have to manage the sending of the Action's request which we created at the beginning.
Open the _GetUserInfo_ class and fill in the following code:

```
public class GetUserInfo extends AbstractBuddyActionDelegate{
 @Override
 public void run(IAction action) {
  final IBuddy buddy = getSelected();
  IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
  IChatServiceActions chat = b.getChatServiceAction();
  chat.openChat(buddy.getId());
  chat.sendExtensionProtocolMessage(buddy.getId(),
		"GET_USER_INFO", new HashMap<String, String>());
 }
}
```

Finally, since it's critical that strings which represent the protocol's extension are correctly written, is useful to refactor the code so that the constant strings are collected in a single interface; doing so you will avoid mistakes and long debug sessions due to trivial write errors.

```
public interface One2OneInfoConstants {
	public static final String GET_USER_INFO = "GET_USER_INFO";
	public static final String USER_INFO =  "USER_INFO";
	public static final String OS_NAME = "OS_NAME";
	public static final String OS_VER = "OS_VER";	
}
```

### Rendering of the new service ###
That code will be executed whenever you select the voice “Get Info” from the roster's popupMenu; it receives an instance of  _IChatServiceActions_ and sends to the selected user a request for information.
The service is concluded!
Now it's all ready to test what has been created. To do that you have to add by the “Run Configuration” panel the _it.uniba.di.cdg.xcore.one2oneinfo_ plugin then you can begin the application.
If all steps have been executed rightly, after the request of an user's information there will be displayed a screen like that in picture 14:

![http://econference4.googlecode.com/svn/wiki/img/extend14a.png](http://econference4.googlecode.com/svn/wiki/img/extend14a.png)
![http://econference4.googlecode.com/svn/wiki/img/extend14b.png](http://econference4.googlecode.com/svn/wiki/img/extend14b.png)<br />
**Picture 14. Test of the plugin it.uniba.di.cdg.xcore.one2oneinfo**

In this example is used only the _IChatServiceActions_ interface which exposes the basic functions for a 1-to-1 chat; with _IBackend_ it's even possible accessing to:
  * _IMultichatActions_ which exposes methods for group chats;
  * _ICallAction_ which provides methods for 1-to-1calls;
  * _IMultiCallAction_ which provides methods for group calls;