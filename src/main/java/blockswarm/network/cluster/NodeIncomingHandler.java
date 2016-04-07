package blockswarm.network.cluster;

import blockswarm.database.handlers.InsertBlockWorker;
import blockswarm.database.handlers.RequestWorker;
import blockswarm.network.packets.BlockPacket;
import blockswarm.network.packets.BlockRequestPacket;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

/**
 *
 * @author cal
 */
public class NodeIncomingHandler implements ObjectDataReply
{
    Node node;

    public NodeIncomingHandler(Node node)
    {
        this.node = node;
    }
    
    @Override
    public Object reply(PeerAddress pa, Object packet) throws Exception
    {
        if (packet instanceof BlockPacket)
        {
            node.database.getDatabasePool().addWorker(new InsertBlockWorker((BlockPacket) packet, node));
        }
        else if (packet instanceof BlockRequestPacket)
        {
            node.database.getDatabasePool().addWorker(new RequestWorker(pa, ((BlockRequestPacket) packet).nodeFileInfo, node));
        }
        return null;
    }
}
