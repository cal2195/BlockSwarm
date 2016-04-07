package blockswarm.database.handlers;

import blockswarm.database.Database;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class RequestWorker extends Worker implements Runnable
{

    final NodeFileInfo nodeFileInfo;
    final PeerAddress requester;
    Node node;

    public RequestWorker(PeerAddress requester, NodeFileInfo nodeFileInfo, Node node)
    {
        this.requester = requester;
        this.nodeFileInfo = nodeFileInfo;
        this.node = node;
    }

    @Override
    public void run()
    {
        NodeFileInfo myBlocks = node.getDatabase().getCache().getFileInfo(nodeFileInfo.hash);
        myBlocks.blocks.andNot(nodeFileInfo.blocks);
        for (int i = myBlocks.blocks.nextSetBit(0); i >= 0; i = myBlocks.blocks.nextSetBit(i + 1))
        {
            node.getDatabase().getDatabasePool().addWorker(new SendBlockWorker(nodeFileInfo.hash, i, requester, node));
            if (i == Integer.MAX_VALUE)
            {
                break; // or (i+1) would overflow
            }
        }
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
