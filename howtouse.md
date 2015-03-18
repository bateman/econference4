

# How to use eConference 4.0 #

This tutorial will show you how to use the eConference tool, according to the role you will play during the requirements workshop.


## Download ##
You can get the latest build of eConference from the [Download](https://code.google.com/p/econference4/downloads/list) page (make sure to pick the righ OS version).
Please note that eConference needs a Java 6 32bit JVM; 64bit JVM are not currently supported.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/screenshot.png' />
<br /><b>Figure 1. The eConference tool</b>
</p>



## Running the tool ##
After downloading the tool, extract it and double click on the .exe file named econference. If you have problem running it, you can try to run it from the command line:

`# econference --dubug --clean`



This does the trick with Eclipse, and so it is supposed to do as well for eConference, which is built upon the Eclipse Rich Client Platform.



## Connecting ##
Click on the Plug icon in the top-left corner of the GUI (see Figure  2).



<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/basicgui.png' />
<br /><b>Figure 2. The basic GUI</b>
</p>




When the Login Dialog is displayed (see Figure  3) fill in the fields with the data that you have received. Choose a profile name and this profile will be stored and available when you reconnect.




<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/logindlg.png' />
<br /><br /><b>Figure 3. Login dialog</b>
</p>


To check whether you are connected, see if the light bulb in the bottom-right corner is enabled and move the mouse over it: the tooltip should display all the connection details (see Figure  4).




<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/connected_new.png' />
<br /><b>Figure  4. Lights on, you are connected</b>
</p>


Don't panic if you don't see anyone in the roster: you have no buddies, and also you cannot add them to the buddy list, so you won't distract yourselves chatting with online buddies during the elicitation :)





## Joining an eConference event ##
This section will show you how to join an event, such as an elicitation or a negotiation, when you are either the moderator or a simple participant.



### The Moderator point of view ###
You are going to role-play the moderator during the session. Thus you should have received an email with a file (.ecx) in attachment. This file is necessary to create the room and invite the other participants. Go to the File > Econference > Load configuration menu (see Figure  5).

Browse and select the .ecx file wherever you saved it. Press Ok and you will see a small dialog which just shows your name and a checkbox that will ensure that the server will send invitations to all the participants registered in the configuration (Figure  6). Be sure to leave it checked, otherwise you will have to invite each of them manually=  =Press ok and you will see the GUI changing and showing the new perspective for the structured conversation.



<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/loading.png' />
<br /><b>Figure  5. Loading the .ecx file</b>
</p>


<br />


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/joiningmod.png' />
<br /><b>Figure  6. Moderator is about to create and join the event</b>
</p>




### The participant point of view ###
If you are a participant, that is not so much to do to join the event: Just be sure to connect and wait until you receive an invitation: it will show up as an item in the Events pane. . It is not a problem if the invitation was sent while you were offline. The server will store it for you and send it as soon as you get connected.

The invitations received will be displayed in this pane, and stored until you delete it. When you select an invitation, the details about the event will be show in the lower part of the Events pane.

To join an eConference event, just double-click on the item or press the button circled in the picture below.




<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/invited_part.png' />
<br /><b>Figure  7. The participant has been invited</b>
</p>


Now just click Ok and you will see the GUI change, showing the new perspective for the structured conversation.



## How to participate in an eConference Event ##
This section will briefly show what the views are meant for, and what action each participant can do on them.

Once you have entered the event, you will see the online stakeholders in the Who's on view (see picture below). The blue icon indicates who is the Moderator.




<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/whoson.png' />
<br /><b>Figure  8. Who's on list, showing online stakeholders</b>
</p>


### Selecting the Scribe ###
Before the event starts, the Moderator must grant a participant the Scribe privilege. This is accomplished by right-clicking the stakeholder and select the 'Grant scribe role' item from the popup menu (see figure below).


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/select_scribe.png' />
<br /><b>Figure  9. Selecting the scribe</b>
</p>

<br />

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/scribe.png' />
<br /><b>Figure  10. Tester 2 is the scribe</b>
</p>




It would be good to select the scribe before the conversation takes place. So the moderator should ask the stakeholders who wants to be the scribe. Also note that the scribe can be changed on the fly and that there can be multiple scribe at the same time.



#### What does being a Scribe mean? ####
If you have been selected as a Scribe, then you will see the refresh icon   in the Decisions Place view enabled now. Thus, when the discussion will take place, you will have to sum-up all the decisions that have been taken about each item, as it is being discussed. Thus you will edit an ongoing minute of the event. Be sure to press the refresh icon at every significant update, so that to show what you have written to the other stakeholders.



<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/decisions_place.png' />
<br /><b>Figure  11. Decision places view being edited</b>
</p>


### Agenda : The Participant point of view ###

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_parview.png' />
<br /><b>Figure  12. The agenda participant's  view. A) Item List - B) Status Bar</b>
</p>


Each participant can see the following sections in the Agenda:

**Discussion item list**

->This section shows the list of the items that the stakeholders have to discuss about (see Figure  12,A). This view is read only for all of the participants, except for the moderator.

**Status Bar**

->This section shows the list of the conference status (see Figure  12,B). The bar has background color green when the session is started or red when session is stopped. Furthermore, the bar shows the current discussion item.

### Agenda : The Moderator point of view ###

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_modview.png' />
<br /><b>Figure  13. The agenda moderator's  view.</b>
</p>


The moderator can perform the following actions on the Agenda:

**Append new item to discuss about**

->Clicking on the icon "plus" in the right-top corner or in right-clicking context menu, a popup is displayed so that the moderator can enter the new item (see Figure  14) that the stakeholders have to discuss about.  All the changes are propagate to all the online participants.
This function is always enabled during the conference.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_addItem.png' />
<br /><b>Figure  14. Append new item. 1) Click icon - 2) Type new Text - 3) Push OK</b>
</p>


**Edit an item of the list**

->Selecting an item and clicking on the icon "rawplug" in the right-top corner or in right-clicking context menu, a popup is displayed so that the moderator can edit the selected item (see Figure  15).  All the changes are propagate to all the online participants.
This function is always enabled during the conference.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_editItem.png' />
<br /><b>Figure  15. Edit an item of the list. 1) Select item - 2) Click icon - 3) Edit Text - 4) Push OK</b>
</p>


**Delete an item of the list**

->Selecting an item and clicking on the icon "cross" in the right-top corner or in right-clicking context menu, the moderator can delete the selected item (see Figure  16).  All the changes are propagate to all the online participants.
This function is enabled only when conference stopped.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_delItem.png' />
<br /><b>Figure  16. Delete an item of the list. 1) Select item - 2) Click icon - 3) Push YES</b>
</p>


**Move up/down an item in the list**

->Selecting an item and clicking on the icon "up arrow/down arrow"  in the right-top corner or in right-clicking context menu, the moderator can move up/down the selected item (see Figure  17).  All the changes are propagate to all the online participants.
This function is enabled only when conference is stopped.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_moveItem.png' />
<br /><b>Figure  17. Move up/down an item of the list. 1) Select item - 2) Click icon - 3) List changed</b>
</p>


**Select the current item from the list**

->Selecting an item and double-clicking on this, the moderator can select this item as the current discussion item (see Figure  18).  All the changes are propagate to all the online participants.
This function is enabled only when conference started.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_currItem.png' />
<br /><b>Figure  18. Select the current item from the list. 1) Double-Click on item - 2) Current Item changed</b>
</p>


**Start / stop the conference**

->Clicking on the button "status" in the top, the moderator can change the status of eConference session  (see Figure  19).  All the changes are propagate to all the online participants.
This function is always enabled during the conference.


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/agenda_statusButton.png' />
<br /><b>Figure  19.Started/stopped the conference. 1) Push Status Button to change the conference status</b>
</p>


**WARNING: changes made to the agenda list after the first start of the conference may break the correct flow of the threads in the messageboard.**

### The Structured Conversation ###
As soon as you enter the event the eConference is still stopped, but you can freely chat with the participants who are currently online while you wait for the others to join. When everyone is online, the moderator decides that it is time to start to work and presses the start button in the Agenda.

As soon as the moderator selects the first item in the Agenda, say Item1, you will see the talk view tab renamed from 'Free talk' into 'Item1'. Also, all the previous messages will be hidden, just to show only the newly-entered statements related to the current item (see Figure  20).




<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/threadeditem1.png' />
<br /><b>Figure  20. Conversation about "Item 1"</b>
</p>

Suppose you have reached a consensus about the current item, and the Scribe has summed up the decisions taken: It is time to move the another item. The Moderator wants edit the second item. Then, he stops the conference to edit "Item2" in "Edited2", then he restarts the conference and selects it in the agenda. All the statements about Item1 are hidden, and the talk view is again clean, ready to list only the statements related to Edited2 (see Figure 21).



<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/edited2.png' />
<br /><b>Figure  21. Edit and Conversation about "Edited 2"</b>
</p>


Suppose that someone reads what the scribe has written is in the Decisions Place and realizes he/she is not agree, the Moderator can move back to an item that has been previously discussed, all the messages already sent will appear in the talk view again. The dashes indicates the beginning of a new session in the conversation about the Item 1 (see Figure  22).


<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/threadeditem1_1.png' />
<br /><b>Figure  22.  Sessions in the conversation about "Item 1"</b>
</p>

The discussion goes on for each single item, until all have been discussed and consensus on them reached. Then the Moderator presses the Stop button and you are back in the free talk view to dismiss the stakeholders.

### Hand raising: a social protocol ###
The hand raising feature is not fundamental, but it can be useful if you see that too many stakeholders are talking simultaneously and things are getting a bit messy.

Thus the moderator can press this icon   in the Hand Raising View, and tells the stakeholders to raise hand to start a turn-taking conversation. Thus, as in real life, you raise your hand to ask someone a question or simply asking the right to speak. This is a social protocol however, as you are always allowed to jump in whenever you want. Interrupt people, but do it at your own risk.

Participants request the right to speak by pressing the following icon   in the view. Note that it is disabled until the moderator decides to enable the use of this protocol.

Fill the popup with a brief description of what you want to ask for instance, and press Ok.



<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/ask_q.png' />
<br /><b>Figure  23. Asking a question (Participant role)</b>
</p>

The questions will appear in the view and the moderator has the power to approve question or reject them right-clicking on each of them (see below). Question approved will show in the Talk view.

The moderator should avoid to reject questions as much as he/she can, or at least should provide a motivation and avoid to frustrate the other stakeholders.




<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/approve_reject_q.png' />
<br /><b>Figure  24.  Moderator can approve/reject questions</b>
</p>


### Save your logs ###
The event is over. As soon as you try to disconnect, close the perspective or even shut down the application you will be prompted to save both the conversation logs and the Decisions place content (see Figure  25). Each log will be store in a flat text file. Be sure to say yes, as you will need them to complete your task later.

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/savelog.png' />
<br /><b>Figure  25. Save your logs</b>
</p>



## Troubleshooting ##
### Accidental disconnections ###
#### As a Moderator: ####
If you get accidentally disconnected and you are the moderator, reconnect and load again the .ecx file. If you see someone disappear from the who's on list, be sure to invite him/her again pressing the icon highlighted in the picture below. Then select the id of the missing stakeholders from the drop-down list.



<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/manual_invitation.png' />
<br /><b>Figure  26. Manual invitation</b>
</p>


"NOTE"

Because of a known bug, when you see a participant reconnected after being disconnect, please press again the current item in the agenda, so than the newcomers can synchronize on the current item discussion.





#### As a Participant: ####
Simply reconnect (restart the tool if you want) and double-click on the invitation you received.