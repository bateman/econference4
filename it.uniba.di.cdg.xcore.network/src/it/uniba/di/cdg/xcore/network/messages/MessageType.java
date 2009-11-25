package it.uniba.di.cdg.xcore.network.messages;

/**
 * A message type is used for runtime message identification.
 */
public class MessageType {
    /**
     * The name of this type.
     */
    final String name;

    /**
     * Constructs a new message type.
     * 
     * @param name
     */
    public MessageType( String name ) {
        this.name = name;
    }

    /**
     * Returns the name of this type
     * 
     * @return the type name
     */
    String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other ) {
        if (other == null || !(other instanceof MessageType)) 
            return false;
        final MessageType that = (MessageType) other;
        return this.getName().equals( that.getName() );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }
}
