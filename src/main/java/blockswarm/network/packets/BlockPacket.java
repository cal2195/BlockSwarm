package blockswarm.network.packets;

import java.io.Serializable;

/**
 *
 * @author cal
 */
public class BlockPacket implements Serializable
{
    public final String filehash;
    public final int blockID;
    public final byte[] block;
    
    public BlockPacket(String fileHash, int blockID, byte[] block)
    {
        this.filehash = fileHash;
        this.block = block;
        this.blockID = blockID;
    }
}
