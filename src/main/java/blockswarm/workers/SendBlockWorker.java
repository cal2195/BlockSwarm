package blockswarm.workers;

import blockswarm.database.Database;
import blockswarm.network.cluster.Node;
import blockswarm.network.packets.BlockPacket;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class SendBlockWorker extends Worker implements Runnable
{

    final String filehash;
    final int blockID;
    final PeerAddress requester;
    final RequestWorker worker;

    public SendBlockWorker(String filehash, int blockID, PeerAddress requester, RequestWorker worker)
    {
        this.filehash = filehash;
        this.blockID = blockID;
        this.requester = requester;
        this.worker = worker;
    }

    @Override
    public void run()
    {
        if (worker.nodeFileInfo.blocks.get(blockID))
        {
            byte[] block = worker.node.getDatabase().getCache().getBlock(filehash, blockID);
            if (worker.node.send(requester, new BlockPacket(filehash, blockID, block)).awaitUninterruptibly().isSuccess())
            {
                worker.nodeFileInfo.blocks.clear(blockID);
            }
        }
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
