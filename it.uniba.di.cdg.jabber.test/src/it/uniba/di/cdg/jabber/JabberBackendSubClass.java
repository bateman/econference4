package it.uniba.di.cdg.jabber;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class JabberBackendSubClass extends JabberBackend {
    
    private XMPPConnection mockedConnection;
    
    public void setMockConnection(XMPPConnection conn){
        mockedConnection = conn;
    }
    
    @Override
    protected XMPPConnection createConnection( String host, int port, String serviceName ) throws XMPPException {
        return mockedConnection;
    }
    
    @Override
    protected XMPPConnection createSecureConnection( String host, int port, String serviceName ) throws XMPPException {
        return mockedConnection;
    }

}
