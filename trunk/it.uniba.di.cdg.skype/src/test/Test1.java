package test;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;
import com.skype.connector.ConnectorException;

public class Test1 {

	/**
	 * @param args
	 * @throws ConnectorException 
	 * @throws SkypeException 
	 */
	public static void main(String[] args) throws ConnectorException, SkypeException {
		Connector.useJNIConnector(true);
		Connector c = Connector.getInstance();
		c.connect();

		//Skype.getUser("danielaweb").setData("role", "MODERATOR");
		
		System.out.println(Skype.getUser("danielaweb").getData("role"));
        	
	}

}
