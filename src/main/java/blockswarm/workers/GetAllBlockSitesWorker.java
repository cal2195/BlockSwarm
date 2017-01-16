package blockswarm.workers;

import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.PeerAddress;
import blockswarm.network.packets.BlockSiteCollectionPacket;
import blockswarm.network.packets.BlockSitePacket;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class GetAllBlockSitesWorker extends Worker implements Runnable
{
    final PeerAddress requester;
    Node node;

    public GetAllBlockSitesWorker(PeerAddress requester, Node node)
    {
        this.requester = requester;
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
        ArrayList<BlockSitePacket> sites = node.getDatabase().getSites().getAllBlockSites();
        node.send(requester, new BlockSiteCollectionPacket(sites));
    }
}
