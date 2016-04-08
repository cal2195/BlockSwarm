package blockswarm.database.handlers;

import blockswarm.database.Database;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        LOG.log(Level.FINE, "I have {0}:{1}", new Object[]{myBlocks.hash, myBlocks.blocks.toString()});
        myBlocks.blocks.and(nodeFileInfo.blocks);
        LOG.log(Level.FINE, "I should send {0}:{1}", new Object[]{myBlocks.hash, myBlocks.blocks.toString()});
        LOG.log(Level.FINE, "Got request for {0}:{1}", new Object[]{nodeFileInfo.hash, nodeFileInfo.blocks.toString()});
        for (int i = myBlocks.blocks.nextSetBit(0); i >= 0; i = myBlocks.blocks.nextSetBit(i + 1))
        {
            LOG.log(Level.FINE, "Queued sending block {0}:{1} to {2}", new Object[]{nodeFileInfo.hash, i, requester.inetAddress()});
            node.getDatabase().getDatabasePool().addWorker(new SendBlockWorker(nodeFileInfo.hash, i, requester, node));
            if (i == Integer.MAX_VALUE)
            {
                break; // or (i+1) would overflow
            }
        }
    }
    private static final Logger LOG = Logger.getLogger(RequestWorker.class.getName());

    @Override
    public int getPriority()
    {
        return 0;
    }
}
