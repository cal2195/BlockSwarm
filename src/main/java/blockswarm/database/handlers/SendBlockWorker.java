package blockswarm.database.handlers;

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
    Node node;

    public SendBlockWorker(String filehash, int blockID, PeerAddress requester, Node node)
    {
        this.filehash = filehash;
        this.blockID = blockID;
        this.requester = requester;
        this.node = node;
    }

    @Override
    public void run()
    {
        byte[] block = node.getDatabase().getCache().getBlock(filehash, blockID);
        while (!node.send(requester, new BlockPacket(filehash, blockID, block))) {}
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
