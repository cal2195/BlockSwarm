package blockswarm.network.packets

import blockswarm.info.NodeFileInfo
import java.io.Serializable
import java.util.HashMap

/**
 *
 * @author cal
 */
class FileInfoPacket(val info: HashMap<String, NodeFileInfo>) : Serializable
