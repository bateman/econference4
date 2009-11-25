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
package it.uniba.di.cdg.xcore.network;

/**
 * A server context captures all the information neede for establishing a connection.
 */
public class ServerContext {
    public static final ServerContext GOOGLE_TALK = new ServerContext( "talk.google.com", true, false, 5222, "googlemail.com" );
    public static final ServerContext JABBER = new ServerContext( "jabber.com", true, false, 5222, "jabber.com" );
    
    public static final int DEFAULT_PORT = 5222;

	
    
    /**
     * A server's name or ip.
     */
    private String serverHost = "";

    /**
     * The server port number.
     */
    private int port;
    
    /**
     * The service name. 
     * For example: gmail.com, jabber.org
     */
    private String serviceName ="";

    /**
     * Server connection must be secure (encrypted), if possible.
     */
    private boolean secure;

    /**
     * Default server port or custom port
     */
    private boolean usingDefaultPort = true;

    public ServerContext( String serverHost ) {
        this( serverHost, true, false, DEFAULT_PORT );
    }
    
    public ServerContext( String serverHost, int port, boolean secure ) {
        this( serverHost, true, secure, port );
    }

    public ServerContext( String serverHost, boolean usingDefaultPort, boolean secure, int port ) {
        this.serverHost = serverHost;
        this.usingDefaultPort = usingDefaultPort;
        this.port = port;
        this.secure = secure;
        this.serviceName = serverHost;
    }
    
    public ServerContext( String serverHost, boolean usingDefaultPort, boolean secure, int port, String serviceName ) {
        this.serverHost = serverHost;
        this.usingDefaultPort = usingDefaultPort;
        this.port = port;
        this.secure = secure;
        this.serviceName = serviceName;
    }

    /**
     * Returns the server's host name or ip.
     * 
     * @return Returns the server host
     */
    public String getServerHost() {
        return serverHost;
    }
    
    /**
     * Returns the service name of the connection.
     * 
     * @return Returns the service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Check if all the information are complete.
     * 
     * @return check if the parameters are valid.
     */
    public boolean isValid() {
        return serverHost != null && serverHost.length() > 0 && port > 0 && port < 65536;
    }

    /**
     * Returns the port number.
     * 
     * @return Returns the port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return Returns the secure.
     */
    public boolean isSecure() {
        return secure;
    }

    public boolean isUsingDefaultPort() {
        return usingDefaultPort;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	String serviceString =  String.format( "%s:%d, ", getServerHost(), getPort());
    	if(serviceName != "")
    		serviceString+= String.format( "service name: %s, ", getServiceName());
    	
       	serviceString+= String.format("default port: %b, secure: %b", isUsingDefaultPort(), isSecure() );
        return serviceString;
    }
}
