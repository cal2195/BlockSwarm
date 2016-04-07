package blockswarm.network.packets;

/**
 *
 * @author cal
 */
public class BlockPacket
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
