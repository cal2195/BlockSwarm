package blockswarm.network.packets;

import java.io.Serializable;

/**
 *
 * @author cal
 */
public class BlockPacket implements Serializable
{
    public final String fileHash;
    public final int blockID;
    public final byte[] block;
    
    public BlockPacket(String fileHash, int blockID, byte[] block)
    {
        this.fileHash = fileHash;
        this.block = block;
        this.blockID = blockID;
    }
}
