package blockswarm.network.cluster.supernode

import blockswarm.network.cluster.PeerAddress
import blockswarm.network.packets.*
import blockswarm.workers.*
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.net.InetSocketAddress
import java.util.logging.Logger

/**
 *
 * @author cal
 */
class SuperNodeIncomingHandler(internal var node: SuperNode) : ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, packet: Any) {
        val pa = PeerAddress(ctx.channel().remoteAddress() as InetSocketAddress)

        when (packet) {
            is BlockPacket -> {
                LOG.fine("Received block packet!")
                node.getWorkerPool().addWorker(InsertBlockWorker(packet, node))
            }
            is BlockRequestPacket -> {
                LOG.fine("Received block request packet!")
                node.getWorkerPool().addWorker(RequestWorker(pa, packet.nodeFileInfo, node))
            }
            is FileListPacket -> {
                LOG.fine("Received file list packet!")
                node.getWorkerPool().addWorker(FileEntryWorker(packet.files, node, pa))
            }
            is FileListRequestPacket -> {
                LOG.fine("Received file list request packet!")
                node.getWorkerPool().addWorker(FileListWorker(packet.ignore, pa, node))
            }
            is FileInfoPacket -> {
                LOG.fine("Received a file info packet!")
                node.getWorkerPool().addWorker(PutFileInfoWorker(pa, packet.info, node))
            }
            else -> LOG.fine("UNKNOWN BLOCK RECEIVED FROM " + pa.inetAddress().address.hostAddress)
        }
    }

    companion object {
        private val LOG = Logger.getLogger(SuperNodeIncomingHandler::class.java.name)
    }
}
