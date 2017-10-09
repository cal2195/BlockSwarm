package blockswarm.network.connections

import blockswarm.network.cluster.Node
import blockswarm.network.cluster.PeerAddress
import java.util.HashMap

/**
 *
 * @author cal
 */
class ConnectionManager(val node: Node) {

    private var connectionMap = HashMap<PeerAddress, Connection>()

    fun getConnection(peerAddress: PeerAddress?): Connection? {
        if (peerAddress == null)
            return null

        return if (connectionMap.containsKey(peerAddress)) {
            connectionMap[peerAddress]
        } else {
            val connection = Connection(peerAddress, node)
            connectionMap.put(peerAddress, connection)
            connection
        }
    }

    fun shutdown() {
        for (con in connectionMap.values) {
            con.shutdown()
        }
    }
}
