package it.uniba.di.cdg.xcore.econference.model.internal;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.IDiscussionItem;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class ConferenceContextWriter {
	private Document doc;
	private String filepath;
	private EConferenceContext context;

	public ConferenceContextWriter(String filepath, EConferenceContext context)
			throws ParserConfigurationException {
		this.filepath = filepath;
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.newDocument();
		this.context = context;
	}

	private void createRootMeetingNode() {
		Node root = doc.createElement("m:meeting");
		doc.appendChild(root);
	}

	private void createTargetPlatformNode(String backendId, String roomName) {
		Node targetPlatform = doc.createElement("target:platform");

		Node targetBackendId = doc.createElement("target:backendid");
		targetBackendId.appendChild(doc.createTextNode(backendId));
		targetPlatform.appendChild(targetBackendId);

		Node targetRoom = doc.createElement("target:room");
		targetRoom.appendChild(doc.createTextNode(roomName));
		targetPlatform.appendChild(targetRoom);

		doc.getDocumentElement().appendChild(targetPlatform);
	}

	private void createConferenceNameNode(String confName) {
		Node conferenceName = doc.createElement("conference:name");
		conferenceName.appendChild(doc.createTextNode(confName));
		doc.getDocumentElement().appendChild(conferenceName);
	}

	private void createConferenceTopicNode(String topic) {
		Node conferenceTopic = doc.createElement("conference:topic");
		conferenceTopic.appendChild(doc.createTextNode(topic));
		doc.getDocumentElement().appendChild(conferenceTopic);
	}

	private void createConferenceScheduleNode(String schedule) {
		Node conferenceSchedule = doc.createElement("conference:schedule");
		conferenceSchedule.appendChild(doc.createTextNode(schedule));
		doc.getDocumentElement().appendChild(conferenceSchedule);
	}

	private void createConferenceItemsNode(Iterator<IDiscussionItem> iterator) {
		Node conferenceItem = doc.createElement("conference:items");
		while (iterator.hasNext()) {
			IDiscussionItem item = (IDiscussionItem) iterator.next();
			Node itemNode = doc.createElement("conference:item");
			itemNode.appendChild(doc.createTextNode(item.getText()));
			conferenceItem.appendChild(itemNode);
		}

		doc.getDocumentElement().appendChild(conferenceItem);
	}

	private void createRoleSupportTeamNode(Invitee moderator, Invitee scribe,
			Iterator<Invitee> invitees) {

		Node roleSupportTeam = doc.createElement("role:supportTeam");

		Node roleModerator = doc.createElement("role:moderator");

		Node fullNameModerator = doc.createElement("role:fullname");
		fullNameModerator.appendChild(doc.createTextNode(moderator
				.getFullName()));
		roleModerator.appendChild(fullNameModerator);

		Node emailModerator = doc.createElement("role:email");
		emailModerator.appendChild(doc.createTextNode(moderator.getEmail()));
		roleModerator.appendChild(emailModerator);

		Node organizationModerator = doc.createElement("role:organization");
		organizationModerator.appendChild(doc.createTextNode(moderator
				.getOrganization()));
		roleModerator.appendChild(organizationModerator);

		Node idModerator = doc.createElement("role:id");
		idModerator.appendChild(doc.createTextNode(moderator.getId()));
		roleModerator.appendChild(idModerator);

		roleSupportTeam.appendChild(roleModerator);

		if (scribe != null) {
			Node roleScribe = doc.createElement("role:scribe");

			Node fullNameScribe = doc.createElement("role:fullname");
			fullNameScribe
					.appendChild(doc.createTextNode(scribe.getFullName()));
			roleScribe.appendChild(fullNameScribe);

			Node emailScribe = doc.createElement("role:email");
			emailScribe.appendChild(doc.createTextNode(scribe.getEmail()));
			roleScribe.appendChild(emailScribe);

			Node organizationScribe = doc.createElement("role:organization");
			organizationScribe.appendChild(doc.createTextNode(scribe
					.getOrganization()));
			roleScribe.appendChild(organizationScribe);

			Node idScribe = doc.createElement("role:id");
			idScribe.appendChild(doc.createTextNode(scribe.getId()));
			roleScribe.appendChild(idScribe);

			roleSupportTeam.appendChild(roleScribe);
		}

		Node roleParticipants = doc.createElement("role:participants");
		roleSupportTeam.appendChild(roleParticipants);

		while (invitees.hasNext()) {
			Invitee participant = (Invitee) invitees.next();
			// prevents from adding scribe and moderator again
			if (!participant.getId().equals(moderator.getId())
					&& (scribe != null && !participant.getId().equals(scribe.getId())) ) {
				Node participantNode = doc.createElement("role:expert");
				Node fullName = doc.createElement("role:fullname");
				fullName.appendChild(doc.createTextNode(participant
						.getFullName()));
				participantNode.appendChild(fullName);

				Node email = doc.createElement("role:email");
				email.appendChild(doc.createTextNode(participant.getEmail()));
				participantNode.appendChild(email);

				Node organization = doc.createElement("role:organization");
				organization.appendChild(doc.createTextNode(participant
						.getOrganization()));
				participantNode.appendChild(organization);

				Node id = doc.createElement("role:id");
				id.appendChild(doc.createTextNode(participant.getId()));
				participantNode.appendChild(id);

				roleParticipants.appendChild(participantNode);
			}
		}

		doc.getDocumentElement().appendChild(roleSupportTeam);
	}

	public void serialize() throws ParserConfigurationException,
			FileNotFoundException {
		buildDocument();
		OutputStream out = new FileOutputStream(filepath);
		prettyPrintWithDOM3LS(doc, out);
	}

	private void buildDocument() {
		createRootMeetingNode();
		createTargetPlatformNode(context.getBackendId(), context.getRoom());
		createConferenceNameNode(context.getName());
		createConferenceTopicNode(context.getTopic());
		createConferenceScheduleNode(context.getSchedule());
		createConferenceItemsNode(context.getItemList().iterator());
		createRoleSupportTeamNode(context.getModerator(), context.getScribe(),
				context.getInvitees().iterator());
	}

	/**
	 * Pretty-prints a DOM document to XML using DOM Load and Save's
	 * LSSerializer. Note that the "format-pretty-print" DOM configuration
	 * parameter can only be set in JDK 1.6+.
	 * 
	 * @param document
	 * @param fileout
	 */
	private void prettyPrintWithDOM3LS(Document document, OutputStream fileout) {
		DOMImplementation domImplementation = document.getImplementation();
		if (domImplementation.hasFeature("LS", "3.0")
				&& domImplementation.hasFeature("Core", "2.0")) {
			DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation
					.getFeature("LS", "3.0");
			LSSerializer lsSerializer = domImplementationLS
					.createLSSerializer();
			DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
			if (domConfiguration.canSetParameter("format-pretty-print",
					Boolean.TRUE)) {
				lsSerializer.getDomConfig().setParameter("format-pretty-print",
						Boolean.TRUE);
				LSOutput lsOutput = domImplementationLS.createLSOutput();
				lsOutput.setEncoding("UTF-8");
				lsOutput.setByteStream(fileout);
				lsSerializer.write(document, lsOutput);
			} else {
				throw new RuntimeException(
						"DOMConfiguration 'format-pretty-print' parameter isn't settable.");
			}
		} else {
			throw new RuntimeException(
					"DOM 3.0 LS and/or DOM 2.0 Core not supported.");
		}
	}
}
