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

	private static String _translate(String text, String src, String dest, Object c) throws Exception {
		String ret = text;
		
		if (c != null) {
			if (c instanceof IQuery) {
				IQuery q = (IQuery) c;
				if (c instanceof MicrosoftMTConnector) {
					net.sf.okapi.connectors.microsoft.Parameters p = new net.sf.okapi.connectors.microsoft.Parameters();
					p.setAppId("28AEB40E8307D187104623046F6C31B0A4DF907E");
					q.setParameters(p);
				}

				q.setLanguages(LocaleId.fromString(src), LocaleId
						.fromString(dest));

				int hits = q.query(text);

				// System.out.println("Hits: " + hits);

				if (q.hasNext()) {
					ret = q.next().target.toString();
				}
			} else if (c instanceof ApertiumXMLRPCClient) {
				ApertiumXMLRPCClient a = (ApertiumXMLRPCClient) c;
				ret = a.translate(text, src, dest).get("translation");
			}
		} else { 
			ret = Translate.execute(text, Language.fromString(src), Language.fromString(dest));
			
			System.out.println(ret);
		}
		
		return ret;
	}
	
	public static long bench(String text, Object c, int cycles) throws Exception {
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < cycles; ++i) {
			_translate(text, "en", "it", c);
		}
		
		long stopTime = System.currentTimeMillis();
		return stopTime - startTime;
	}

    public static List<String> campiona(List<String> l) {
        List<String> ret = new LinkedList<String>();
        long u = new Long(l.get(l.size() - 1).length()) / 20;
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
    
    public static String showArray(String mark, List<Long> l) {
    	String ret = mark + " = [";
    	boolean first = true;
    	for (Long v : l) {
    		if (!first) {
    			ret += ", ";
    		}
    		ret += v;
    		first = false;
    	}
    	ret += "];";
    	return ret;
    }
	
	public static void main(String[] args) throws Exception {
		ApertiumXMLRPCClient a = new ApertiumXMLRPCClient(new URL("http://localhost:6173/RPC2"));
		
		//IQuery g = new GoogleMTConnector();
		//g.open();
		Object g = null;
		
		Translate.setHttpReferrer("http://www.neuralnoise.com");
		
		List<String> strings = Utils.getAllSortedStrings();
		
		strings.remove(strings.size() - 1);
		strings.remove(strings.size() - 1);
		strings.remove(strings.size() - 1);
		
		List<String> camp = campiona(strings);
		
		List<Long> len = new LinkedList<Long>();
		List<Long> msa = new LinkedList<Long>();
		List<Long> msg = new LinkedList<Long>();
		
		for (String s : camp) {
			len.add(new Long(s.length()));
			msa.add(bench(s, a, 32));
			msg.add(bench(s, g, 32));
		}
		
		System.out.println(showArray("len", len));
		System.out.println(showArray("msa", msa));
		System.out.println(showArray("msg", msg));
		
		String bigger = strings.get(strings.size() - 1);
		
		List<Long> threads = new LinkedList<Long>();
		List<Long> msat = new LinkedList<Long>();
		List<Long> msgt = new LinkedList<Long>();
		
		for (int i = 1; i <= 8; ++i) {
			long at = Timing.bench(a, bigger, 32, i) / i;
			long gt = Timing.bench(g, bigger, 32, i) / i;
			
			threads.add(new Long(i));
			msat.add(at);
			msgt.add(gt);
		}
		
		System.out.println(showArray("threads", len));
		System.out.println(showArray("msat", msat));
		System.out.println(showArray("msgt", msgt));
	}
}
