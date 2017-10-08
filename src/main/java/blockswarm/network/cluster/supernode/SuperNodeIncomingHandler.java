package blockswarm.network.cluster.supernode;

import blockswarm.network.cluster.PeerAddress;
import blockswarm.workers.FileEntryWorker;
import blockswarm.workers.FileListWorker;
import blockswarm.workers.InsertBlockWorker;
import blockswarm.workers.PutFileInfoWorker;
import blockswarm.workers.RequestWorker;
import blockswarm.network.packets.BlockPacket;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.FileInfoPacket;
import blockswarm.network.packets.FileListPacket;
import blockswarm.network.packets.FileListRequestPacket;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class SuperNodeIncomingHandler //implements ObjectDataReply
{
    SuperNode node;

    public SuperNodeIncomingHandler(SuperNode node)
    {
        this.node = node;
    }
    
//    @Override
    public Object reply(PeerAddress pa, Object packet)
    {
        if (packet instanceof BlockPacket)
        {
            LOG.fine("Received block packet!");
            node.getWorkerPool().addWorker(new InsertBlockWorker((BlockPacket) packet, node));
        }
        else if (packet instanceof BlockRequestPacket)
        {
            LOG.fine("Received block request packet!");
            node.getWorkerPool().addWorker(new RequestWorker(pa, ((BlockRequestPacket) packet).nodeFileInfo, node));
        }
        else if (packet instanceof FileListPacket)
        {
            LOG.fine("Received file list packet!");
            node.getWorkerPool().addWorker(new FileEntryWorker(((FileListPacket) packet).files, node, pa));
        }
        else if (packet instanceof FileListRequestPacket)
        {
            LOG.fine("Received file list request packet!");
            node.getWorkerPool().addWorker(new FileListWorker(((FileListRequestPacket) packet).ignore, pa, node));
        }
        else if (packet instanceof FileInfoPacket)
        {
            LOG.fine("Received a file info packet!");
            node.getWorkerPool().addWorker(new PutFileInfoWorker(pa, ((FileInfoPacket) packet).info, node));
        }
        return null;
    }
    private static final Logger LOG = Logger.getLogger(SuperNodeIncomingHandler.class.getName());
}
