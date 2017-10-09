package blockswarm.network.cluster

import java.io.Serializable
import java.net.InetSocketAddress

/**
 *
 * @author cal
 */
class PeerAddress(internal var address: InetSocketAddress) : Serializable {

    fun inetAddress(): InetSocketAddress {
        return address
    }
}
