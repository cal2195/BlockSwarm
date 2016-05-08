package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.util.HashMap;
import java.util.logging.Logger;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class ClusterFileInfoUpdater extends Worker implements Runnable
{

    Node node;
    int time = 0;

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
                FutureGet futureGet = node.getDHT().getClusterFileInfo(filehash);
                futureGet.addListener(new BaseFutureAdapter<FutureGet>()
                {
                    @Override
                    public void operationComplete(FutureGet f)
                    {
                        try
                        {
                            if (f.isSuccess())
                            {
                                HashMap<PeerAddress, NodeFileInfo> clusterInfo = (HashMap<PeerAddress, NodeFileInfo>) f.data().object();
                                for (PeerAddress pa : clusterInfo.keySet())
                                {
                                    if (!pa.inetAddress().getHostAddress().equals(node.peer.peerAddress().inetAddress().getHostAddress()))
                                    {
                                        NodeFileInfo fileinfo = clusterInfo.get(pa);
                                        LOG.finest("Adding info about " + fileinfo.hash + " from " + pa.inetAddress().getHostAddress());
                                        node.getDatabase().getPeers().putFileInfo(pa, fileinfo.hash, fileinfo);
                                    }
                                }
                            }
                        } catch (Exception ex)
                        {
                            LOG.fine("Couldn't get info on " + filehash);
                        }
                    }
                });
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static final Logger LOG = Logger.getLogger(ClusterFileInfoUpdater.class.getName());
}
