package blockswarm.network.packets

import blockswarm.info.NodeFileInfo
import java.io.Serializable

/**
 *
 * @author cal
 */
class BlockRequestPacket(val nodeFileInfo: NodeFileInfo) : Serializable
