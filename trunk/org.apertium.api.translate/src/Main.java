import net.sf.okapi.common.LocaleId;
import net.sf.okapi.connectors.google.GoogleMTConnector;
import net.sf.okapi.lib.translation.IQuery;

import org.apertium.api.translate.actions.TranslateConfiguration;
import org.apertium.api.translate.actions.TranslateConfigurationAction;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ret = "";
		
		IQuery q = new GoogleMTConnector();
		q.setLanguages(LocaleId.fromString("it"), LocaleId.fromString("en"));
		q.open();

		q.query("ciao a tutti");

		if (q.hasNext()) {
			ret = q.next().target.toString();
		}
		
		System.out.println(ret);
	}

}
