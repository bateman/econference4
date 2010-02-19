import java.net.URL;
import java.util.*;

import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.connectors.microsoft.MicrosoftMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

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

    public static List<String> campiona(List<String> l) {
        List<String> ret = new LinkedList<String>();
        long u = new Long(l.get(l.size() - 2).length()) / 10;
        long count = 1;
        for (String s : l) {
                long t = u * count;
                if (s.length() >= t) {
                        ret.add(s);
                        count += 1;
                }
        }
        return ret;
    }
	
	public static void main(String[] args) throws Exception {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://www.neuralnoise.com:6173/RPC2"));
		IQuery g = new GoogleMTConnector();
		g.open();
		
		List<Utterance> utterancestr = Utils.readUtterances("trainingset/log.xml");
		
		List<Utterance> utterancests1 = Utils.readUtterances("testset/testset_log_1.xml");
		List<Utterance> utterancests2 = Utils.readUtterances("testset/testset_log_2.xml");
		List<Utterance> utterancests3 = Utils.readUtterances("testset/testset_log_3.xml");
		List<Utterance> utterancests4 = Utils.readUtterances("testset/testset_log_4.xml");
		List<Utterance> utterancests5 = Utils.readUtterances("testset/testset_log_5.xml");
		
		List<String> strings = new LinkedList<String>();
		
		for (Utterance u : utterancestr) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests1) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests2) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests3) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests4) {
			strings.add(u.getUtterance());
		}
		
		for (Utterance u : utterancests5) {
			strings.add(u.getUtterance());
		}
		
		Collections.sort(strings, new StringLengthComparator());
		
		List<String> camp = campiona(strings);

		System.out.println(camp.size());
		
		
		
		for (String s : camp) {
			System.out.println("Length: " + s.length());
			System.out.println("Apertium: " + bench(s, a));
			System.out.println("Google: " + bench(s, g));
		}
	}
}
