package blockswarm.network.cluster;

import blockswarm.workers.FileEntryWorker;
import blockswarm.workers.GetFileInfoWorker;
import blockswarm.workers.FileListWorker;
import blockswarm.workers.InsertBlockWorker;
import blockswarm.network.packets.BlockPacket;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.BlockSitePacket;
import blockswarm.network.packets.FileInfoRequestPacket;
import blockswarm.network.packets.FileListPacket;
import blockswarm.network.packets.FileListRequestPacket;
import java.util.logging.Logger;
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
    public Object reply(PeerAddress pa, Object packet)
    {
        if (packet instanceof BlockPacket)
        {
            LOG.fine("Received block packet from " + pa.inetAddress().getHostAddress());
            node.getWorkerPool().addWorker(new InsertBlockWorker((BlockPacket) packet, node));
            node.getNetworkStats().blockReceived(((BlockPacket) packet).filehash);
        }
        else if (packet instanceof BlockRequestPacket)
        {
            LOG.fine("Received block request packet!");
            node.getPeerRequestManager().processRequest(pa, ((BlockRequestPacket) packet).nodeFileInfo);
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
        else if (packet instanceof FileInfoRequestPacket)
        {
            LOG.finer("Received a file info request packet!");
            node.getWorkerPool().addWorker(new GetFileInfoWorker(pa, node));
        }
        else if (packet instanceof  BlockSitePacket)
        {
            LOG.finer("Received a blocksite packet!");
            
        }
        else 
        {
            LOG.fine("UNKNOWN BLOCK RECEIVED FROM " + pa.inetAddress().getHostAddress());
        }
        return null;
    }
    private static final Logger LOG = Logger.getLogger(NodeIncomingHandler.class.getName());
}
