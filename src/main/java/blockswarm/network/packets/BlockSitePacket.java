package blockswarm.network.packets;

import java.io.Serializable;

/**
 *
 * @author cal
 */
public class BlockSitePacket implements Serializable
{
    public final int version;
    public final byte[] signature, publicKey;
    public final String domain, filehash;

    public BlockSitePacket(String domain, String filehash, int version, byte[] signature, byte[] publicKey)
    {
        this.version = version;
        this.signature = signature;
        this.publicKey = publicKey;
        this.domain = domain;
        this.filehash = filehash;
    }
    
    public byte[] getData()
    {
        return (domain + ":" + filehash + ":" + version).getBytes();
    }
}
