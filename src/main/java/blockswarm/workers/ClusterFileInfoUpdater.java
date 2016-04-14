package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.util.HashMap;
import java.util.logging.Logger;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class ClusterFileInfoUpdater extends Worker implements Runnable
{

    Node node;

    public ClusterFileInfoUpdater(Node node)
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
            for (String filehash : node.getDatabase().getFiles().getAllFileHashes())
            {
                HashMap<PeerAddress, NodeFileInfo> clusterInfo = node.getDHT().getClusterFileInfo(filehash);
                for (PeerAddress pa : clusterInfo.keySet())
                {
                    if (!pa.inetAddress().getHostAddress().equals(node.peer.peerAddress().inetAddress().getHostAddress()))
                    {
                        LOG.finest("Adding info about " + filehash + " from " + pa.inetAddress().getHostAddress());
                        NodeFileInfo fileinfo = clusterInfo.get(pa);
                        node.getDatabase().getPeers().putFileInfo(pa, fileinfo.hash, fileinfo);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static final Logger LOG = Logger.getLogger(ClusterFileInfoUpdater.class.getName());
}
