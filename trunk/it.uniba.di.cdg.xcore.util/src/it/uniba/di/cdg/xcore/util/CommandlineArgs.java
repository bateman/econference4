package it.uniba.di.cdg.xcore.util;

import java.util.Iterator;
import java.util.Set;

public class CommandlineArgs {
	private static final String XMPP_PROTOCOL = "Jabber";
	private static final String SKYPE_PROTOCOL = "Skype";
	private static final String PROTOCOL_ARG = "-p";
	private static final Object HELP_ARG = "-h";

	public static String parse(String[] args, Set<String> set) {
		String proto = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(PROTOCOL_ARG)) {
				i++;
				proto = args[i];
			}
			if(args[i].equals(HELP_ARG))
				printHelp();
		}
		
		if(proto == null)
			return proto;

		System.out.println("Loading selected backend: " + proto);
		boolean found = false;
		Iterator<String> backends = set.iterator();
		while (backends.hasNext() && !found) {
			String backend = (String) backends.next();
			if (backend.equals(proto))
				found = true;
		}
		if (!found) {
			System.err
					.println("Unknown backend protocol arg, reverting to default: "
							+ XMPP_PROTOCOL);
			proto = XMPP_PROTOCOL;
		}
		
		return proto;
	}

	private static void printHelp() {
		System.out.println("Mandatory argument(s):\n-p {Jabber|Skype}\t- selects the network backend");		
	}
}
