import java.net.URL;
import java.util.*;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;

public class Main {

	private static String _translate(String text, String src, String dest, Object c) throws InterruptedException, ApertiumXMLRPCClientException {
		String ret = text;
		
		if (c instanceof IQuery) {
			IQuery q = (IQuery) c;
			if (c instanceof MicrosoftMTConnector) {
				net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
				p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
				q.setParameters(p);
			}

			q.setLanguages(LocaleId.fromString(src), LocaleId.fromString(dest));

			int hits = q.query(text);

			System.out.println("Hits: " + hits);

			if (q.hasNext()) {
				ret = q.next().target.toString();
			}
		} else if (c instanceof ApertiumXMLRPCClient) {
			ApertiumXMLRPCClient a = (ApertiumXMLRPCClient)c;
			ret = a.translate(text, src, dest).get("translation");
		}
		
		return ret;
	}
	
	public static long bench(String text, Object c) throws InterruptedException, ApertiumXMLRPCClientException {
		long startTime = System.currentTimeMillis();

		_translate(text, "en", "it", c);
		
		long stopTime = System.currentTimeMillis();
		return stopTime - startTime;
	}

	
	public static void main(String[] args) throws Exception {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://www.neuralnoise.com:6173/RPC2"));
		IQuery qg = new GoogleMTConnector();

		List<Utterance> utterances = Utils.readUtterances("trainingset/log.xml");
		List<String> strings = new LinkedList<String>();
		
		for (Utterance u : utterances) {
			strings.add(u.getUtterance());
		}
		
		Collections.sort(strings, new StringLengthComparator());
		
		

	}
}
