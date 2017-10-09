package blockswarm.network.packets

import java.io.Serializable

/**
 *
 * @author cal
 */
class BlockSitePacket(val domain: String, val filehash: String, val version: Int, val signature: ByteArray, val publicKey: ByteArray) : Serializable {

    val data: ByteArray
        get() = "$domain:$filehash:$version".toByteArray()
}
