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
 * Test backend.
 */
public class TestBackend1 implements IBackend {
    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#connect(it.uniba.di.cdg.xcore.network.ServerContext, it.uniba.di.cdg.xcore.network.UserContext)
     */
    public void connect( ServerContext ctx, UserContext userAccount ) throws BackendException {
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#disconnect()
     */
    public void disconnect() {
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#isConnected()
     */
    public boolean isConnected() {
        return false;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#getCapabilities()
     */
    public ICapabilities getCapabilities() {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#getUserAccount()
     */
    public UserContext getUserAccount() {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#getServerContext()
     */
    public ServerContext getServerContext() {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#getRoster()
     */
    public IBuddyRoster getRoster() {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#createService(it.uniba.di.cdg.xcore.network.services.ICapability, it.uniba.di.cdg.xcore.network.services.INetworkServiceContext)
     */
    public INetworkService createService( ICapability service, INetworkServiceContext context )
            throws BackendException {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#getConnectJob(it.uniba.di.cdg.xcore.network.ServerContext, it.uniba.di.cdg.xcore.network.UserContext)
     */
    public Job getConnectJob( ServerContext serverContext, UserContext account ) {
        return null;
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#setHelper(it.uniba.di.cdg.xcore.network.INetworkBackendHelper)
     */
    public void setHelper( INetworkBackendHelper helper ) {
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.network.IBackend#getHelper()
     */
    public INetworkBackendHelper getHelper() {
        return null;
    }
    
    @Override
	public void changePassword(String newpasswd) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBackendId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICallAction getCallAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IChatServiceActions getChatServiceAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Job getConnectJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMultiChatServiceActions getMultiChatServiceAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMultiCallAction getMultiCallAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerNewAccount(String userId, String password,
			ServerContext server, Map<String, String> attributes)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBackend getBackendFromProxy() {
		// TODO Auto-generated method stub
		return this;
	}
}
