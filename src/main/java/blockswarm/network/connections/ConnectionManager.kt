package blockswarm.network.connections

import blockswarm.network.cluster.PeerAddress
import java.util.HashMap
import java.util.logging.Level
import java.util.logging.Logger
import javax.net.ssl.SSLException

/**
 *
 * @author cal
 */
class ConnectionManager {

    private var connectionMap = HashMap<PeerAddress, Connection>()

    fun getConnection(peerAddress: PeerAddress?): Connection? {
        if (peerAddress == null)
            return null

        return if (connectionMap.containsKey(peerAddress)) {
            connectionMap[peerAddress]
        } else {
            val connection = Connection(peerAddress)
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
