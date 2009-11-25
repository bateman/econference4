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
package it.uniba.di.cdg.jabber;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.cdg.jabber.ConnectionTest.SammyBot;
import it.uniba.di.cdg.xcore.network.ServerContext;
import it.uniba.di.cdg.xcore.network.UserContext;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 * A simple jabber bot ... 
 */
public abstract class JabberBot extends Thread {

    private final ServerContext context;
    
    private final UserContext account;

    protected XMPPConnection conn;
    
    private boolean _quit;
    
    private List<PacketListener> packetListeners;
    
    protected JabberBot( ServerContext context, UserContext account ) {
        super( "Bot: " + account.getId() );
        this.context = context;
        this.account = account;
        this._quit = false;
        this.packetListeners = new ArrayList<PacketListener>();
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public final void run() {
        try {
            connect();
            
            while (!_quit) {
                executeBot();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    /**
     * @return Returns the account.
     */
    protected UserContext getAccount() {
        return account;
    }

    /**
     * @return Returns the context.
     */
    protected ServerContext getContext() {
        return context;
    }

    /**
     * Execute the main bot code: clients will implement this.
     */
    protected abstract void executeBot() throws Exception;

    /**
     * connect the bot. 
     * @throws Exception 
     */
    protected void connect() throws Exception {    	
        //XMPPConnection.DEBUG_ENABLED = true;
    	ConnectionConfiguration config = new ConnectionConfiguration(context.getServerHost(),  context.getPort());
    	config.setReconnectionAllowed(true);
    	conn = new XMPPConnection( config );
        conn.connect();
        conn.login( account.getId(), account.getPassword() );
    }

    /**
     * Disconnect the bot.
     */
    protected void disconnect() {
        if (conn != null) {
            for (PacketListener pl : packetListeners)
                conn.removePacketListener( pl );
            conn.disconnect();
            conn = null;
        }
    }

    /**
     * Quit the bot main 
     */
    protected void quit() {
        _quit = true;
    }

    protected void sendPacket( Packet packet ) {
        conn.sendPacket( packet );
    }
    
    protected void registerPacketListener( PacketListener listener, PacketFilter filter ) {
        conn.addPacketListener( listener, filter );
        packetListeners.add( listener );
    }
    
    public static String jid( UserContext account, ServerContext context ) {
        return account.getId() + "@" + context.getServerHost() + "/Smack";
    }
}
