package blockswarm.network.cluster;

import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

/**
 *
 * @author cal
 */
public class SuperNodeIncomingHandler extends NodeIncomingHandler implements ObjectDataReply
{
    SuperNode node;

    public SuperNodeIncomingHandler(SuperNode node)
    {
        super(node);
        this.node = node;
    }
    
    @Override
    public Object reply(PeerAddress pa, Object packet) throws Exception
    {
        super.reply(pa, packet);
        return null;
    }
}
