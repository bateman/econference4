**Index**


# Introduction #

SkypeKit enables developers to create products and applications that deliver virtually the entire range of Skype’s functionality—including calling, chat, account creation, and contact management—without the need for your users to separately download Skype.


# Download #
To begin developing with SkypeKit, you’ll need to download, request, register, and obtain the following components: <br />
1. Download and extract the SkypeKit SDK.<br />
2. Request a runtime.<br />
3. Register a project.<br />
4. Obtain a key pair for your project.<br />

## SkypeKit SDK ##
SkypeKit SDK provides you with the documentation, library source code, examples, and reference implementations you need to create products and applications that incorporate Skype features.

To download it visit the <a href='http://developer.skype.com/skypekit/releases/skypekit-3-7-0'>Download</a> page.

## SkypeKit Runtime ##
SkypeKit Runtime is a “headless” version of Skype—an application that does almost everything Skype does, but has no user interface. Instead, you write your own UI components to interact with SkypeKit Runtime through the SkypeKit language wrappers. SkypeKit Runtimes provide your apps with the foundation they need to access Skype functionality. Key pairs authorize your apps to communicate with a Skype Runtime. In conjunction with the SkypeKit SDK, these two components enable you to create and distribute all kinds of innovative SkypeKit-based products and applications.<br />
To request a SkypeKit Runtime:<br />
1. Visit the <a href='http://developer.skype.com/skypekit/releases/skypekit-3-7-0'>Download</a> page. If you haven’t already done so, download the SDK and accept the development terms—the Runtimes tabs don’t appear until you do.<br />
2. Click the Request a runtime tab to display the list of available runtimes.<br />
3. Select the runtime with the OS, chipset, and A/V options that match your development environment.<br />
4. Click Request runtime.<br />
5. An acknowledgment screen informs you that your request has been submitted. Processing your request can take anywhere from a few minutes to a few hours.<br />
6. When you return to the Download page, you’ll see your request listed with a status of Building.<br />
7 .When your SkypeKit Runtime is ready, the status will change to a link that says Download.<br />

## SkypeKit Key Pairs ##
Key pairs are Base64-encoded X.509 certificates that authorize your apps to communicate with Skype Runtime (and access Skype functionality). You must request a distinct key pair for each app you create, as well as for each of their distributable versions. <br />
Key pairs come in two flavors: <br />
Development key pairs: Expire in 60 days. Good for developing and testing an app.<br />
Distribution key pairs: Don’t expire. Required for distributing an app.<br />

Requesting a distribution key pair for a registered project is similar to requesting a development key pair unless you’re planning to distribute a SkypeKit for Embedded project (see the definition for Hardware Products in your SkypeKit License Agreement). In that case, distribution key pairs become available only after your project’s hardware has passed certification testing.<br />
To request a development key pair:<br />
1. Visit the <a href='http://developer.skype.com/account/projects'>projects and programs</a> page.<br />
2. Locate your registered project, and click its Project name link.<br />
3. On your project’s detail page, click the Development key pair link:
Specify a Label for you key pair. Choose a string that is both easy to remember and easy to associate with this project.<br />
4. Type the RECAPTCHA text. <br />
5. Click Request Key Pair <br />
6. Save your certificate file to a secure location. <br />

To request a distribution key pair:

1. Sign in to your Skype Developer account as an admin. Only account admins can request a distribution key pair. To see who in your organization is an account admin, visit your organization’s Users page.<br />
2. Visit the <a href='http://developer.skype.com/account/projects'>projects and programs</a> page.<br />
3. Locate your registered project, and click its Project name link.<br />
4. Click the Distribution key pair link. If you’re working on a SkyepKit for Embedded project, your product will need to pass certification testing and be marked as certified by Skype before you can request a distribution key pair.<br />
5. Specify a Label for you key pair. Choose a string that is both easy to remember and easy to associate with this project. <br />
6. Type the RECAPTCHA text. <br />
7. Click Request key pair. <br />
8. Save your certificate file to a secure location.<br />

Only for JAVA:
Extract and convert the private key component of your key pair from RSA to PKCS8. Your original PEM certificate file and its resultant DER file must have the same filename and must reside in the same directory.

# How to Code #

## Event Handlers ##
All major Skype objects support an onPropertyChange event handler. For example: <br /> **Account Property Changes:** The Account property event handler implementation relates primarily to processing login and logout. Specifically, it interacts with the associated session’s setLoginStatus method;<br />
**Contact Property Changes:** The Contact property event handler implementation relates primarily to establishing/updating the status of the user’s Contacts;<br />
**Conversation Property Changes:** The Contact property event handler implementation relates primarily to “live status” changes;
RINGING\_FOR\_ME so we can pick up the call,
IM\_LIVE to indicate that a call is in progress,
RECENTLY\_LIVE and NONE to indicate that a call has ended;<br />
**Participant Property Changes:** The Participant property event handler tracks volume and speaker activity within a call, writing appropriate updates to the console; <br />

## Login/Logout ##
To log in to Skype:
<ul><li>invoke <font face='Courier new'>Account.loginWithPassword</font> with the password parsed from the command line;</li>
<li>periodically poll the login state (Account.Property.P_STATUS) until we see Account.Status.LOGGED_IN (success), see Account.Status.LOGGED_OUT (failure) or time-out.<br>
</li>

</ul>
To log out of Skype:
<ul><li>invoke <font face='Courier new'>Account.logout</font>;</li>
<li>periodically poll the login state (Account.Property.P_STATUS) until we see Account.Status.LOGGED_OUT (success) or time-out.</li>
</ul>

## Conversation ##
The Conversation class is a core element in SkypeKit’s view of communications. It encapsulates all interactions between Skype users, including audio & video calls, chats, SMS messages, file transfers, and so forth. A conversation’s properties store attributes such as type, participants, duration, and content.
SkypeKit provides several approaches to obtaining an account’s conversations, we illustrates using the getConversationByParticipants and getConversationList methods only.
<br />
### getConversationByParticipants ###
First, we’ll specify the Skype Name (account) of the person(s) we’re interested in:
```
public static final String[] participantNames =
{
"targetParticipant1",
"targetParticipant2",
"targetParticipant3"
};
```
Next, we’ll invoke Skype.getConversationByParticipants to obtain this account’s most recent Conversation with the participants we specified in participantNames:
```

// One Conversation (using GetConversationByParticipants)
boolean createIfNonExisting = false;
boolean ignoreBookmarkedOrNamed = false;

if ((myConversation = mySession.mySkype.getConversationByParticipants(participantNames,
createIfNonExisting, ignoreBookmarkedOrNamed)) != null) {
displayName = myConversation.getDisplayName();

}
```
### getConversationList ###
Skype.getConversationList also does exactly what its name implies—it obtains a list of the conversations associated with this account that meet criteria specified by a predefined filter.
The predefined filter is a Conversation.ListType enumerator, which recognizes the following values:
<ul>
<li>ALL_CONVERSATIONS—conversations that:<br>
<ul><li>are bookmarked</li>
<li>are stored in the Inbox</li>
<li>are live</li>
<li>have associated meta information</li>
<li>have had activity within the last 30 days</li>
</ul>
</li>
<li>INBOX_CONVERSATIONS—conversations within the last six months only (earlier conversations are not stored in the Inbox)</li>
<li>BOOKMARKED_CONVERSATIONS—conversations whose Conversation.Property.P_IS_BOOKMARKED property is true</li>
<li>LIVE_CONVERSATIONS—conversations whose Conversation.Property.P_LOCAL_LIVESTATUS property is other than NONE</li>
<li>REALLY_ALL_CONVERSATIONS—the entire conversation list; no restrictions</li>

</ul>
For example, to retrieve a list of conversations currently stored in the target account’s Inbox we use:
```

//List of Conversations (using GetConversationList)
Conversation[] myInbox = mySession.mySkype.getConversationList(Conversation.ListType.INBOX_CONVERSATIONS);
```

## Contacts ##
The first thing we need to do is retrieve a ContactGroup. In addition to user-defined groups, SkypeKit defines, manages, and automatically populates several “hardwired” groups. You can use these pre-defined groups to retrieve subsets of your Contacts on an as-needed basis:
<ul>
<li>ALL_KNOWN_CONTACTS—Our entire contact list, no exceptions.</li>
<li>ALL_BUDDIES—All authorized contacts, both Skype Name and PSTN.</li>
<li>SKYPE_BUDDIES—All authorized Skype Name contacts.</li>
<li>SKYPEOUT_BUDDIES—All PSTN contacts.</li>
<li>ONLINE_BUDDIES—All authorized Skype Name contacts that are currently online.<br>
</li>
<li>UNKNOWN_OR_PENDINGAUTH_BUDDIES—Skype Name contacts that have not yet authorized me.</li>
<li>RECENTLY_CONTACTED_CONTACTS—Contacts with whom I've had recent conversations, both Skype Name and PSTN.</li>
<li>CONTACTS_WAITING_MY_AUTHORIZATION—Skype Name contacts awaiting an authorization request from me.</li>
<li>CONTACTS_AUTHORIZED_BY_ME—All contacts that I have authorized (including unauthorized contacts).</li>
<li>CONTACTS_BLOCKED_BY_ME—All contacts that I have blocked (including unauthorized contacts).</li>
<li>UNGROUPED_BUDDIES—All authorized contacts that do not belong to any custom group.</li>
<li>CUSTOM_GROUP—Custom group.</li>
<li>EXTERNAL_CONTACTS—Contacts from external address book(s).</li>
</ul>
To retrieve a particular pre-defined group, invoke the Skype.GetHardwiredContactGroup method specifying the target group. Once you have the ContactGroup, invoke its GetContacts method to obtain an array of Contact instances describing its members.
```

Contact[] myContactList =
mySession.mySkype.getHardwiredContactGroup(ContactGroup.Type.SKYPE_BUDDIES).getContacts();
```
Now that we have the group members, we simply iterate through the list and write each member’s Contact.Property.P\_DISPLAYNAME property to the console:
```

Contact[] myContactList =
mySession.mySkype.getHardwiredContactGroup(ContactGroup.Type.SKYPE_BUDDIES).getContacts();
int i;
int j = myContactList.length;
for (i = 0; i < j; i++) {
System.out.println(myContactList[i].getDisplayName());
}
```
## Chat Message ##
While the Conversation class handles chat messages, the Message class encapsulates most of their details.
<br />For handling incoming messages, we simply catch and handle the onMessage event.
```

public void onMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage, Conversation conversation)
```
The arguments are:
<ul><li>the affected Message instance</li>
<li>whether the message caused an update to its associated Conversation’s Conversation.Property.P_INBOX_TIMESTAMP.</li>
<li>whether the affected Message instance should be added to (null) or replace an existing event (non-null) in the Account’s event history.</li>
<li>the affected Message instance’s associated Conversation.</li>
</ul>
To send a reply to any message, invoke that message’s associated Conversation’s PostText method.
```

conversation.postText(message, isXml);
```

## Calls ##
### Picking up incoming calls ###
Detecting and answering an incoming call requires:
<ul><li>recognizing changes to the list of “live” conversations, specifically when an entry’s Conversation.Property.P_LOCAL_LIVESTATUS changes to Conversation.MyStatus.RINGING_FOR_ME</li>
<li>invoking Join on that Conversation instance.</li>
</ul>
```

public boolean doPickUpCall() {
Conversation[] liveConversations = mySession.mySkype.getConversationList(Conversation.ListTypeLIVE_CONVERSATIONS);
Conversation targetConversation = liveConversations[0];

Participant[] callerList = targetConversation.getParticipants(Conversation.PARTICIPANTFILTER.OTHER_CONSUMERS);
Conversation.LOCAL_LIVESTATUS liveStatus = targetConversation.getLocalLivestatus();
switch (liveStatus) {
case RINGING_FOR_ME:
System.out.println("RING RING...");
targetConversation.JoinLiveSession(targetConversation.getJoinBlob());
return (true);
// break;
case IM_LIVE:
System.out.println("Another call is coming")
targetConversation.LeaveLiveSession(true);
break;
default:
Log.d(mySession.myTutorialTag, "Ignoring LiveStatus " + liveStatus.toString());
break;
}

return (false);
}
```
### Making Calls ###
Starting a call is easy—it’s just one method invocation: Participant.ring. You might be wondering right about now why ring is a Participant method and not a Conversation method. Since we need a Participant instance in order to invoke ring and since Participants exist only in the context of a Conversation, we first need to find (or create) a Conversation that includes our call target as a Participant. We do this by invoking Skype.getConversationByParticipants with the appropriate arguments:
<ul>
<li>A String array containing a single entry—our call target.</li>
<li>Whether the conversation should be automatically created if it doesn’t already exist. Since we need it to exist, we’ll set this argument to true.</li>
<li>Whether bookmarked or named conversations should be ignored. For our one-to-one call, we want to ignore these so we’ll set this argument to true as well.</li>
</ul>
```

String[] callTargets = {new String(myCallTarget)}; // Create & initialize the array in one step...
Conversation myConversation =
(Conversation)mySession.mySkype.getConversationByParticipants(callTargets, true, true);
```
While we have a Conversation instance that contains our call target, we don’t have its associated Participant instance—and the ring method is a Participant method. Currently, SkypeKit does not include a method that retrieves a particular Participant from a Conversation based on the target Participant’s Skype Name. Consequently, we need to retrieve all of the Conversation’s associated Participants, loop through them, and find a match between our call target and one of their Participant.Property.P\_IDENTITY values:
```

Participant[] convParticipantList = myConversation.getParticipants(Conversation.ParticipantFilter.ALL);
```
Once we have our call target’s Participant instance, we can invoke its ring
method. As you can see, it has five arguments:
<ul>
<li>Identity of the call target. The ring method determines whether it’s actually a Skype Name or a telephone number. A real-world application that displays a list of Contacts and calls whichever one the user selects typically passes the value of one of that Contact’s four “call target” properties:<br>
<ul><li>primary contact—Contact.Property.P_SKYPENAME or Contact.Property.P_PSTNNUMBER</li>
<li>Contact.Property.P_ASSIGNED_PHONE1 (for example, their mobile)</li>
<li>Contact.Property.P_ASSIGNED_PHONE2 (for example, their office phone)</li>
<li>Contact.Property.P_ASSIGNED_PHONE3 (for example, their home phone)</li></ul></li>
<li>Whether to start video when the call comes up.</li>
<li>Maximum re-dial count.</li>
<li>Re-dial period (in seconds).</li>
<li>Whether the call should go to voicemail if unanswered.</li>
</ul>
```

convParticipantList[i].ring(myCallTarget, false, 0, 10, false,
mySession.myAccount.getSkypeName());
```