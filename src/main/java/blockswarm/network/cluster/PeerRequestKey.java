package blockswarm.network.cluster;


import blockswarm.info.NodeFileInfo;
import java.util.Objects;

public class PeerRequestKey
{

    PeerAddress requester;
    String filehash;

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
