/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package it.uniba.di.cdg.xcore.econference.model;

import it.uniba.di.cdg.xcore.econference.EConferenceContext;
import it.uniba.di.cdg.xcore.econference.model.definition.IServiceContext;
import it.uniba.di.cdg.xcore.econference.model.definition.IServiceContextLoader;
import it.uniba.di.cdg.xcore.m2m.service.Invitee;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Factory for creating or initializing {@see
 * it.uniba.di.cdg.xcore.econference.model.ConferenceModel} objects with
 * external data, like XML files.
 */
public class ConferenceContextLoader implements IServiceContextLoader {
	protected static final String XMPP_BACKENDID_VAL = "it.uniba.di.cdg.jabber.jabberBackend";
	protected static final String SKYPE_BACKENDID_VAL = "it.uniba.di.cdg.skype.skypeBackend";
	protected static final String BACKENDID_KEY = "backendId";
	protected static final String NAME_KEY = "name";
	protected static final String TOPIC_KEY = "topic";
	protected static final String ITEM_LIST_KEY = "itemList";
	protected static final String SCRIBE_KEY = "scribe";
	protected static final String MODERATOR_KEY = "moderator";

	protected EConferenceContext context = null;
	protected Document doc = null;

	public ConferenceContextLoader(EConferenceContext context) {
		this.context = context;
	}

	public ConferenceContextLoader() {
	}

	public void setContext(IServiceContext context) {
		this.context = (EConferenceContext) context;
	}

	public void load(InputStream is) throws InvalidContextException {
		if (this.context == null)
			throw new InvalidContextException(
					"Skipping file due to null context");

		try {
			doc = loadDocument(is);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();

			// Get the backend
			String backendid = xPath.evaluate("/meeting/platform/backendid",
					doc).trim();
			if (!backendid.equals(context.getBackendId())) {
				final String msg = "Skipping file due to backend mismatch:\n"
						+ "\nYour current backend: " + context.getBackendId()
						+ "\nFound: " + backendid;
				throw new InvalidContextException(msg);
			}
			// get the schedule
			String schedule = xPath.evaluate("/meeting/schedule", doc).trim();
			context.setSchedule(schedule);

			// Get the fields, one by one
			String name = xPath.evaluate("/meeting/name", doc).trim();
			context.setName(name); // Either of these two ...

			String topic = xPath.evaluate("/meeting/topic", doc).trim();
			context.setTopic(topic);

			String room = xPath.evaluate("/meeting/platform/room", doc).trim();

			if (room.equals(""))
				throw new InvalidContextException(
						"Invalid configuration file: empty roon name");
			// ////////////////////////////////////////////////////////////
			context.setRoom(room);

			IItemList itemList = getItemList(xPath, doc, context);
			context.setItemList(itemList);

			// Support team
			List<Invitee> participants = new ArrayList<Invitee>();

			Node supportTeam = (Node) xPath.evaluate("/meeting/supportTeam",
					doc, XPathConstants.NODE);
			Node node = null;
			Invitee p = null;

			// node = (Node) xPath.evaluate( "director", supportTeam,
			// XPathConstants.NODE );
			// p = readParticipantFromNode( xPath, node,
			// EConferenceContext.ROLE_DIRECTOR );
			// participants.add( p );
			// context.setDirector( p );

			node = (Node) xPath.evaluate("moderator", supportTeam,
					XPathConstants.NODE);
			p = readParticipantFromNode(xPath, node,
					EConferenceContext.ROLE_MODERATOR);
			context.setModerator(p);
			participants.add(p);

			node = (Node) xPath.evaluate("scribe", supportTeam,
					XPathConstants.NODE);
			if (null != node) {
				p = readParticipantFromNode(xPath, node,
						EConferenceContext.ROLE_SCRIBE);
				context.setScribe(p);
				participants.add(p);
			}

			// Other experts
			NodeList experts = (NodeList) xPath
					.evaluate("/meeting/participants/expert", doc,
							XPathConstants.NODESET);
			for (int i = 0; i < experts.getLength(); i++) {
				Node n = experts.item(i);

				p = readParticipantFromNode(xPath, n,
						EConferenceContext.ROLE_PARTICIPANT);
				participants.add(p);
			}
			// Add all the people to the conference
			context.setInvitees(participants);
		} catch (Exception e) {
			throw new InvalidContextException(e);
		}
	}

	public void load(String fileName) throws FileNotFoundException,
			InvalidContextException {
		load(new FileInputStream(fileName));
	}

	/**
	 * Read a participant's info, from a specific node.
	 * 
	 * @param xPath
	 * @param participantNode
	 * @param role
	 * @return the participant
	 * @throws Exception
	 *             if some XPath-related error occur or the required id is
	 *             <code>null</code>.
	 */
	private Invitee readParticipantFromNode(XPath xPath, Node participantNode,
			String role) throws Exception {
		String id = xPath.evaluate("id", participantNode);
		// String passwd = xPath.evaluate( "passwd", participantNode );
		String fullName = xPath.evaluate("fullname", participantNode).trim();
		String email = xPath.evaluate("email", participantNode).trim();
		String organization = xPath.evaluate("organization", participantNode)
				.trim();

		if (id == null || id.length() == 0)
			throw new InvalidContextException(
					"Participant id must be not empty!");

		Invitee p = new Invitee(id, fullName, email, organization, role);
		// IParticipant p = new Participant( conference, id, passwd, fullName,
		// email, organization, role );
		return p;
	}

	/**
	 * Extract the item list from the XML document.
	 * 
	 * @param xPath
	 * @param doc
	 * @param context
	 * @return
	 * @throws XPathExpressionException
	 */
	private IItemList getItemList(XPath xPath, Document doc,
			EConferenceContext context) throws XPathExpressionException {
		String expression = "/meeting/items/item";
		NodeList nodes = (NodeList) xPath.evaluate(expression, doc,
				XPathConstants.NODESET);

		IItemList il = new ItemList();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			il.addItem(new DiscussionItem(n.getTextContent()));
		}
		return il;
	}

	/**
	 * XML DOM helper.
	 * 
	 * @param is
	 * @return the XML DOM document object
	 * @throws Exception
	 */
	protected Document loadDocument(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		return doc;
	}
}
