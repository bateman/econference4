package it.uniba.di.cdg.jabber;


import org.jivesoftware.smack.packet.PacketExtension;

public class PacketExtensionAdapter {

	
	public static PacketExtension adaptToTargetPacketExtension(final IPacketExtension adaptee){
		PacketExtension packetExtension = new PacketExtension(){

			@Override
			public String getElementName() {
				return adaptee.getElementName();
			}

			@Override
			public String getNamespace() {
				return adaptee.getNamespace();
			}

			@Override
			public String toXML() {
				return adaptee.toXML();
			}
    		
    	};
    	return packetExtension;
	}
	
}
