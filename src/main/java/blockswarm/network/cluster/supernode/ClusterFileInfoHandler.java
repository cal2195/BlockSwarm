package blockswarm.network.cluster.supernode;

import blockswarm.info.NodeFileInfo;
import java.util.HashMap;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class ClusterFileInfoHandler
{

    HashMap<PeerAddress, HashMap<String, NodeFileInfo>> clusterInfo = new HashMap<>();

    public void updatePeer(PeerAddress pa, HashMap<String, NodeFileInfo> info)
    {
        clusterInfo.put(pa, info);
    }
    
    public void clear()
    {
        clusterInfo.clear();
    }

    public HashMap<PeerAddress, NodeFileInfo> getClusterInfo(String filehash)
    {
        HashMap<PeerAddress, NodeFileInfo> fileinfo = new HashMap<>();
        for (PeerAddress pa : clusterInfo.keySet())
        {
            NodeFileInfo peerInfo = clusterInfo.get(pa).get(filehash);
            if (peerInfo != null)
            {
                fileinfo.put(pa, peerInfo);
            }
        }
        return fileinfo;
    }
}
