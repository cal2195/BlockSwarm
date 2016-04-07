package blockswarm.database.handlers;

import blockswarm.database.Database;
import blockswarm.network.packets.BlockPacket;

/**
 *
 * @author cal
 */
public class BlockWorker extends Worker implements Runnable
{
    final BlockPacket blockPacket;
    Database database;
    
    public BlockWorker(BlockPacket blockPacket, Database database)
    {
        this.blockPacket = blockPacket;
        this.database = database;
    }

    @Override
    public void run()
    {
        database.getCache().putBlock(blockPacket.fileHash, blockPacket.blockID, blockPacket.block);
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
