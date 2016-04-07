package blockswarm.network.cluster;

import blockswarm.network.packets.BlockPacket;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

/**
 *
 * @author cal
 */
public class NodeIncomingHandler implements ObjectDataReply
{
    @Override
    public Object reply(PeerAddress pa, Object packet) throws Exception
    {
        if (packet instanceof BlockPacket)
        {
            
        }
        return null;
    }
}
