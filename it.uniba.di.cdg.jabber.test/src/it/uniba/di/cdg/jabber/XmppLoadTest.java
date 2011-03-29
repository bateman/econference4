package it.uniba.di.cdg.jabber;
import java.util.Date;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smackx.muc.*;
import org.junit.Test;

public class XmppLoadTest{

	static public class ChatListener implements PacketListener {
		public void processPacket(Packet packet) {
			System.out.println(packet.toXML());
		}
	}

	@Test
	public void xmppTest() {
		try {

			ConnectionConfiguration config = new ConnectionConfiguration("jabber.org", 5222);
			XMPPConnection conn1 = new XMPPConnection(config);
			conn1.connect();
			conn1.login("giuseppe83", "giuseppe83");
			MultiUserChat chat = new MultiUserChat(conn1, "testppcollab@conference.jabber.org");
			chat.join("giuseppe83");

			ChatListener cl = new ChatListener();
			chat.addMessageListener(cl);

			String msg;
			for (int i = 0; i < 50; ++i) {
				msg = (new Date()).toString() + " " + (new Double( Math.random())).toString();
				chat.sendMessage(msg);
				Thread.sleep(50);
			}
			conn1.disconnect();

		} catch (Exception e) { e.printStackTrace(); }
	}
}