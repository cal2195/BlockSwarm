package blockswarm.workers;

import blockswarm.network.cluster.Node;
import blockswarm.network.packets.BlockPacket;

/**
 *
 * @author cal
 */
public class InsertBlockWorker extends Worker implements Runnable
{
    final BlockPacket blockPacket;
    Node node;
    
    public InsertBlockWorker(BlockPacket blockPacket, Node node)
    {
        this.blockPacket = blockPacket;
        this.node = node;
    }

    @Override
    public void run()
    {
        node.getDatabase().getCache().putBlock(blockPacket.getFilehash(), blockPacket.getBlockID(), blockPacket.getBlock());
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
