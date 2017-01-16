package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.PeerAddress;
import blockswarm.network.cluster.supernode.SuperNode;
import java.util.HashMap;

/**
 *
 * @author cal
 */
public class PutFileInfoWorker extends Worker implements Runnable
{
    final PeerAddress peer;
    final HashMap<String, NodeFileInfo> info;
    SuperNode node;

    public PutFileInfoWorker(PeerAddress peer, HashMap<String, NodeFileInfo> info, SuperNode node)
    {
        this.peer = peer;
        this.info = info;
        this.node = node;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void run()
    {
        node.getClusterFileInfoHandler().updatePeer(peer, info);
    }
}
