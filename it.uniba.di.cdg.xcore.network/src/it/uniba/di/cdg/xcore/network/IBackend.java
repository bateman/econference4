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

import java.util.Map;

import it.uniba.di.cdg.xcore.network.action.ICallAction;
import it.uniba.di.cdg.xcore.network.action.IChatServiceActions;
import it.uniba.di.cdg.xcore.network.action.IMultiCallAction;
import it.uniba.di.cdg.xcore.network.action.IMultiChatServiceActions;
import it.uniba.di.cdg.xcore.network.model.IBuddyRoster;
import it.uniba.di.cdg.xcore.network.services.ICapabilities;
import it.uniba.di.cdg.xcore.network.services.ICapability;
import it.uniba.di.cdg.xcore.network.services.INetworkService;
import it.uniba.di.cdg.xcore.network.services.INetworkServiceContext;

import org.eclipse.core.runtime.jobs.Job;

/**
 * A network backend provides the basic services for connecting to a server endpoint and facilities that 
 * let every client to know about the supported services.
 * <p>
 * It provides event listeners registration for:
 * <ul>
 * <li>online / offline status notification {@see net.osslabs.jabber.client.network.IConnectionStatusListener}</li>
 * <li>message transmission facilities</li>
 * <li>received message notification</li>
 * </ul>
 * <p>
 * Access to implemented services is performed through {@see #createService(ICapability, INetworkServiceContext)} method:
 * clients may use {@see #getCapabilities()} method to ease the task of querying the backend for supported tasks. 
 */
public interface IBackend {
    /**
     * Perform connection to a remote server using some account info for login.
     * 
     * @param ctx the server context with information about the remote host
     * @param userAccount the user account to use for login
     * @throws BackendException if an error occurs (tipically if the login fails)
     */
    void connect( final ServerContext ctx, final UserContext userAccount ) throws BackendException;

    /**
     * Disconnect from the backend. Nothing will happen if the backend in not connected yet.
     */
    void disconnect();

    /**
     * Check wether or not this backend is connected to a remote host.
     * 
     * @return <code>true</code> if it is currently connected, <code>false</code> otherwise
     */
    boolean isConnected();

    /**
     * Returns the capabilities supported by this backend. Clients may use them for querying the
     * backend supported facilities.
     * 
     * @return the supported capabilities.
     */
    ICapabilities getCapabilities();
    
//    /**
//     * Returns the services registry for this backend.
//     * 
//     * @return the network services
//     */
//    INetworkServiceRegistry getServiceRegistry();
    
    /**
     * @return Returns the user account or <code>null</code> if not connected
     */
    UserContext getUserAccount();

    /**
     * Returns the server context associated to this backend.
     * 
     * @return the server context or <code>null</code> if not connected
     */
    ServerContext getServerContext();
    
    /**
     * Returns the current buddy roster .
     * 
     * @return the buddy roster.
     */
    IBuddyRoster getRoster();

    /**
     * Returns the network service implementation provided by this backend. 
     * 
     * @param service the required service's capability
     * @param context additional informations required by the service
     * @return a network service ready to be upcasted or <code>null</code> if the backend doesn't support such a capability
     * @throws BackendException if the backend was offline or some other error occurred 
     * @deprecated
     */
    INetworkService createService( ICapability service, INetworkServiceContext context ) throws BackendException;

    /**
     * Create new connection job, ready to be executed by clients.
     * 
     * @return the connection job
     */
    Job getConnectJob();

    
    IBackend getBackendFromProxy();
    
    /**
     * Set the helper to use when accessing to common functions.
     * 
     * @param helper
     */
    void setHelper( INetworkBackendHelper helper );
    
    /**
     * Returns the helper in use. When created this is gueranteed to be not <code>null</code>.
     * 
     * @return the helper
     */
    INetworkBackendHelper getHelper();
    
    /**
     * Changes the passwords credentials for the account connected at this moment
     * @param newpasswd
     * @throws Exception 
     */
	void changePassword(String newpasswd) throws Exception;
	
	/**
	 * Register a new account on the server
	 * @param userId the new jabber id 
	 * @param password the new password
	 * @param server the server context
	 * @param attributes the account attributes 
	 * (the user's first name, the user's last name, the user's email address, the user's city, ...) 
     * @throws Exception if an error occurs creating the account. 
	 */
	public void registerNewAccount(String userId, String password, ServerContext server, Map<String, String> attributes) throws Exception;
	
	IChatServiceActions getChatServiceAction();
	
	IMultiChatServiceActions getMultiChatServiceAction();
	
	ICallAction getCallAction();
	
	IMultiCallAction getMultiCallAction();
	
	public String getUserId();
	
	public String getBackendId();
	
}