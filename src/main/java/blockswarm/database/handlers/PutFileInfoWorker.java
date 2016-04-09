package blockswarm.database.handlers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class PutFileInfoWorker extends Worker implements Runnable
{
    final PeerAddress peer;
    final NodeFileInfo info;
    Node node;

    public PutFileInfoWorker(PeerAddress peer, NodeFileInfo info, Node node)
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
        node.getDatabase().getPeers().putFileInfo(peer, info.hash, info);
        node.getGui().updateFileList();
    }
}
