package blockswarm.network.cluster;

import blockswarm.database.handlers.FileEntryWorker;
import blockswarm.database.handlers.FileListWorker;
import blockswarm.database.handlers.InsertBlockWorker;
import blockswarm.database.handlers.RequestWorker;
import blockswarm.network.packets.BlockPacket;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.FileListPacket;
import blockswarm.network.packets.FileListRequestPacket;
import java.util.logging.Logger;
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
            LOG.fine("Received block packet!");
            node.database.getDatabasePool().addWorker(new InsertBlockWorker((BlockPacket) packet, node));
        }
        else if (packet instanceof BlockRequestPacket)
        {
            LOG.fine("Received block request packet!");
            node.database.getDatabasePool().addWorker(new RequestWorker(pa, ((BlockRequestPacket) packet).nodeFileInfo, node));
        }
        else if (packet instanceof FileListPacket)
        {
            LOG.fine("Received file list packet!");
            node.database.getDatabasePool().addWorker(new FileEntryWorker(((FileListPacket) packet).files, node));
        }
        else if (packet instanceof FileListRequestPacket)
        {
            LOG.fine("Received file list request packet!");
            node.database.getDatabasePool().addWorker(new FileListWorker(((FileListRequestPacket) packet).ignore, pa, node));
        }
        return null;
    }
    private static final Logger LOG = Logger.getLogger(SuperNodeIncomingHandler.class.getName());
}
