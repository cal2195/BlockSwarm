package blockswarm.network.cluster

import blockswarm.workers.FileEntryWorker
import blockswarm.workers.GetFileInfoWorker
import blockswarm.workers.FileListWorker
import blockswarm.workers.InsertBlockWorker
import blockswarm.network.packets.BlockPacket
import blockswarm.network.packets.BlockRequestPacket
import blockswarm.network.packets.BlockSiteCollectionPacket
import blockswarm.network.packets.BlockSitePacket
import blockswarm.network.packets.BlockSitesInfoRequestPacket
import blockswarm.network.packets.FileInfoRequestPacket
import blockswarm.network.packets.FileListPacket
import blockswarm.network.packets.FileListRequestPacket
import blockswarm.workers.GetAllBlockSitesWorker
import blockswarm.workers.PutBlockSiteWorker
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.net.InetSocketAddress
import java.util.logging.Logger

/**
 *
 * @author cal
 */
class NodeIncomingHandler(internal var node: Node) : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, packet: Any) {
        val pa = PeerAddress(ctx.channel().remoteAddress() as InetSocketAddress)

        when (packet) {
            is BlockPacket -> {
                LOG.fine("Received block packet from " + pa.inetAddress().address.hostAddress)
                node.getWorkerPool().addWorker(InsertBlockWorker(packet, node))
                node.getNetworkStats().blockReceived(packet.filehash)
            }
            is BlockRequestPacket -> {
                LOG.fine("Received block request packet!")
                node.getPeerRequestManager().processRequest(pa, packet.nodeFileInfo)
            }
            is FileListPacket -> {
                LOG.fine("Received file list packet!")
                node.getWorkerPool().addWorker(FileEntryWorker(packet.files, node, pa))
            }
            is FileListRequestPacket -> {
                LOG.fine("Received file list request packet!")
                node.getWorkerPool().addWorker(FileListWorker(packet.ignore, pa, node))
            }
            is FileInfoRequestPacket -> {
                LOG.fine("Received a file info request packet!")
                node.getWorkerPool().addWorker(GetFileInfoWorker(pa, node))
            }
            is BlockSitePacket -> {
                LOG.fine("Received a blocksite packet!")
                node.getWorkerPool().addWorker(PutBlockSiteWorker(packet, node))
            }
            is BlockSiteCollectionPacket -> {
                LOG.fine("Received a blocksite collection packet!")
                for (site in packet.sites) {
                    node.getWorkerPool().addWorker(PutBlockSiteWorker(site, node))
                }
            }
            is BlockSitesInfoRequestPacket -> {
                LOG.fine("Received a blocksite info request packet!")
                node.getWorkerPool().addWorker(GetAllBlockSitesWorker(pa, node))
            }
            else -> LOG.fine("UNKNOWN BLOCK RECEIVED FROM " + pa.inetAddress().address.hostAddress)
        }
    }

    companion object {
        private val LOG = Logger.getLogger(NodeIncomingHandler::class.java.name)
    }
}
