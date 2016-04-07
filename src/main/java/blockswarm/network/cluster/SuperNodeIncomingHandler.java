package blockswarm.network.cluster;

import blockswarm.database.handlers.BlockWorker;
import blockswarm.network.packets.BlockPacket;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

/**
 *
 * @author cal
 */
public class SuperNodeIncomingHandler implements ObjectDataReply
{
    SuperNode node;

    public SuperNodeIncomingHandler(SuperNode node)
    {
        this.node = node;
    }
    
    @Override
    public Object reply(PeerAddress pa, Object packet) throws Exception
    {
        if (packet instanceof BlockPacket)
        {
            node.database.getDatabasePool().addBlock(new BlockWorker((BlockPacket) packet, node.database));
        }
        return null;
    }
}
