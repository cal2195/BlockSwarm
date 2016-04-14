package blockswarm.workers.supernode;

import blockswarm.network.cluster.supernode.SuperNode;
import blockswarm.network.packets.FileInfoRequestPacket;
import blockswarm.workers.Worker;
import java.util.logging.Logger;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class ClusterFileInfoWorker extends Worker implements Runnable
{
    SuperNode node;

    public ClusterFileInfoWorker(SuperNode node)
    {
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
        try
        {
            LOG.info("Updating DHT file info!");
            for (String filehash : node.getDatabase().getFiles().getAllFileHashes())
            {
                node.getDHT().putClusterFileInfo(filehash, node.getClusterFileInfoHandler().getClusterInfo(filehash));
            }
            node.getClusterFileInfoHandler().clear();
            LOG.info("Asking all peers for new file info!");
            for (PeerAddress pa : node.getPeer().peerBean().peerMap().all())
            {
                node.send(pa, new FileInfoRequestPacket());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static final Logger LOG = Logger.getLogger(ClusterFileInfoWorker.class.getName());
}
