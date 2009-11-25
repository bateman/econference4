package it.uniba.di.cdg.jabber;

import org.jivesoftware.smack.packet.PacketExtension;

public interface IPacketExtension extends PacketExtension{
	 
    
    /**
     * Returns the provider.
     *
     * @return the provider.
     */
    public Object getProvider();

        

}
