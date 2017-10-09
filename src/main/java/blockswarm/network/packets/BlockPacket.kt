package blockswarm.network.packets

import java.io.Serializable

/**
 *
 * @author cal
 */
class BlockPacket(val filehash: String, val blockID: Int, val block: ByteArray) : Serializable
