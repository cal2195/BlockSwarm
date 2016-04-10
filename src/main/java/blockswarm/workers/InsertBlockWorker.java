package blockswarm.workers;

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
        node.getDatabase().getCache().putBlock(blockPacket.filehash, blockPacket.blockID, blockPacket.block);
        if (node.getDatabase().getDownloads().flaggedForDownload(blockPacket.filehash) && node.getDatabase().getFiles().hasFullFile(blockPacket.filehash))
        {
            node.getWorkerPool().addWorker(new FileAssemblyWorker(blockPacket.filehash, node));
        }
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
