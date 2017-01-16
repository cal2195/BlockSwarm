package blockswarm.network.cluster;

import blockswarm.workers.FileEntryWorker;
import blockswarm.workers.GetFileInfoWorker;
import blockswarm.workers.FileListWorker;
import blockswarm.workers.InsertBlockWorker;
import blockswarm.network.packets.BlockPacket;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.BlockSiteCollectionPacket;
import blockswarm.network.packets.BlockSitePacket;
import blockswarm.network.packets.BlockSitesInfoRequestPacket;
import blockswarm.network.packets.FileInfoRequestPacket;
import blockswarm.network.packets.FileListPacket;
import blockswarm.network.packets.FileListRequestPacket;
import blockswarm.workers.GetAllBlockSitesWorker;
import blockswarm.workers.PutBlockSiteWorker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class NodeIncomingHandler extends ChannelInboundHandlerAdapter {

    Node node;

    public NodeIncomingHandler(Node node) {
        this.node = node;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) 
    { 
        PeerAddress pa = new PeerAddress((InetSocketAddress) ctx.channel().remoteAddress());

        if (packet instanceof BlockPacket) {
            LOG.fine("Received block packet from " + pa.inetAddress().getHostAddress());
            node.getWorkerPool().addWorker(new InsertBlockWorker((BlockPacket) packet, node));
            node.getNetworkStats().blockReceived(((BlockPacket) packet).filehash);
        } else if (packet instanceof BlockRequestPacket) {
            LOG.fine("Received block request packet!");
            node.getPeerRequestManager().processRequest(pa, ((BlockRequestPacket) packet).nodeFileInfo);
        } else if (packet instanceof FileListPacket) {
            LOG.fine("Received file list packet!");
            node.getWorkerPool().addWorker(new FileEntryWorker(((FileListPacket) packet).files, node, pa));
        } else if (packet instanceof FileListRequestPacket) {
            LOG.fine("Received file list request packet!");
            node.getWorkerPool().addWorker(new FileListWorker(((FileListRequestPacket) packet).ignore, pa, node));
        } else if (packet instanceof FileInfoRequestPacket) {
            LOG.fine("Received a file info request packet!");
            node.getWorkerPool().addWorker(new GetFileInfoWorker(pa, node));
        } else if (packet instanceof BlockSitePacket) {
            LOG.fine("Received a blocksite packet!");
            node.getWorkerPool().addWorker(new PutBlockSiteWorker((BlockSitePacket) packet, node));
        } else if (packet instanceof BlockSiteCollectionPacket) {
            LOG.fine("Received a blocksite collection packet!");
            for (BlockSitePacket site : ((BlockSiteCollectionPacket) packet).sites) {
                node.getWorkerPool().addWorker(new PutBlockSiteWorker(site, node));
            }
        } else if (packet instanceof BlockSitesInfoRequestPacket) {
            LOG.fine("Received a blocksite info request packet!");
            node.getWorkerPool().addWorker(new GetAllBlockSitesWorker(pa, node));
        } else {
            LOG.fine("UNKNOWN BLOCK RECEIVED FROM " + pa.inetAddress().getHostAddress());
        }
    }
    private static final Logger LOG = Logger.getLogger(NodeIncomingHandler.class.getName());
}
