package blockswarm.database.handlers;

import blockswarm.database.Database;
import blockswarm.files.FileHandler;
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
        node.getDatabase().getCache().putBlock(blockPacket.fileHash, blockPacket.blockID, blockPacket.block);
        if (node.getDatabase().getFiles().hasFullFile(blockPacket.fileHash))
        {
            FileHandler fileHandler = new FileHandler(node);
            fileHandler.assembleFile(blockPacket.fileHash);
        }
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
