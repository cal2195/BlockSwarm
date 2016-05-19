package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.PeerRequestKey;
import java.util.HashMap;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class PeerRequestManager
{
    HashMap<PeerRequestKey, RequestWorker> requests = new HashMap<>();
    Node node;

    public PeerRequestManager(Node node)
    {
        this.node = node;
    }
    
    public void processRequest(PeerAddress requester, NodeFileInfo nodeFileInfo)
    {
        PeerRequestKey key = new PeerRequestKey(requester, nodeFileInfo);
        if (requests.containsKey(key))
        {
            requests.get(key).updateRequest(nodeFileInfo);
        } else
        {
            RequestWorker request = new RequestWorker(requester, nodeFileInfo, node);
            requests.put(key, request);
            node.getWorkerPool().addWorker(request);
        }
    }
    
    public void remove(PeerAddress pa, NodeFileInfo info)
    {
        requests.remove(new PeerRequestKey(pa, info));
    }
}
