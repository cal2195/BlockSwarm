package blockswarm.network.packets;

/**
 *
 * @author cal
 */
public class BlockPacket
{
    final String fileHash;
    final int blockID;
    final byte[] block;
    
    public BlockPacket(String fileHash, int blockID, byte[] block)
    {
        this.fileHash = fileHash;
        this.block = block;
        this.blockID = blockID;
    }
}
