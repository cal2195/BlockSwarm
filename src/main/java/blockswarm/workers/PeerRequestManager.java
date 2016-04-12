package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.util.HashMap;
import java.util.Objects;
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
            node.getWorkerPool().addWorker(requests.get(key));
        } else
        {
            RequestWorker request = new RequestWorker(requester, nodeFileInfo, node);
            requests.put(key, request);
            node.getWorkerPool().addWorker(request);
        }
    }
    
    private class PeerRequestKey
    {
        private PeerAddress requester;
        private String filehash;

        public PeerRequestKey(PeerAddress requester, NodeFileInfo nodeFileInfo)
        {
            this.requester = requester;
            this.filehash = nodeFileInfo.hash;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.requester);
            hash = 17 * hash + Objects.hashCode(this.filehash);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final PeerRequestKey other = (PeerRequestKey) obj;
            if (!Objects.equals(this.filehash, other.filehash))
            {
                return false;
            }
            if (!Objects.equals(this.requester, other.requester))
            {
                return false;
            }
            return true;
        }
    }
}
