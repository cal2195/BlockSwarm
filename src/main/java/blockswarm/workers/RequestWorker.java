package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.packets.BlockPacket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class RequestWorker extends Worker implements Runnable
{

    NodeFileInfo nodeFileInfo;
    final PeerAddress requester;
    Node node;
    Random random = new Random();

    public RequestWorker(PeerAddress requester, NodeFileInfo nodeFileInfo, Node node)
    {
        this.requester = requester;
        this.nodeFileInfo = nodeFileInfo;
        this.node = node;
    }

    public void updateRequest(NodeFileInfo info)
    {
        this.nodeFileInfo = info;
    }

    @Override
    public void run()
    {
        NodeFileInfo myBlocks = node.getDatabase().getCache().getFileInfo(nodeFileInfo.hash);
        LOG.log(Level.FINE, "I have {0}:{1}", new Object[]
        {
            myBlocks.hash, myBlocks.blocks.toString()
        });
        myBlocks.blocks.and(nodeFileInfo.blocks);
        LOG.log(Level.FINE, "I should send {0}:{1}", new Object[]
        {
            myBlocks.hash, myBlocks.blocks.toString()
        });
        LOG.log(Level.FINE, "Got request for {0}:{1}", new Object[]
        {
            nodeFileInfo.hash, nodeFileInfo.blocks.toString()
        });
        int sent = 0;
        for (int i = myBlocks.blocks.nextSetBit(0); i >= 0 && sent++ < 5; i = myBlocks.blocks.nextSetBit(i + 1))
        {
            LOG.log(Level.FINE, "Sending block {0}:{1} to {2}", new Object[]
            {
                nodeFileInfo.hash, i, requester.inetAddress()
            });

            byte[] block = node.getDatabase().getCache().getBlock(nodeFileInfo.hash, i);
            if (node.send(requester, new BlockPacket(nodeFileInfo.hash, i, block)).awaitUninterruptibly().isSuccess())
            {
                nodeFileInfo.blocks.clear(i);
            }

            //node.getWorkerPool().addWorker(new SendBlockWorker(nodeFileInfo.hash, i, requester, this));
            if (i == Integer.MAX_VALUE)
            {
                break; // or (i+1) would overflow
            }
        }
        if (myBlocks.blocks.cardinality() != 0)
        {
            node.getWorkerPool().addDelayedWorker(this, 10);
        }
    }
    private static final Logger LOG = Logger.getLogger(RequestWorker.class.getName());

    @Override
    public int getPriority()
    {
        return 0;
    }
}
