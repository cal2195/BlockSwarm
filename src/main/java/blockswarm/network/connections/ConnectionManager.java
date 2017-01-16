/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.network.connections;

import blockswarm.network.cluster.PeerAddress;
import java.util.HashMap;

/**
 *
 * @author cal
 */
public class ConnectionManager
{

    HashMap<PeerAddress, Connection> connectionMap = new HashMap<>();

    public Connection getConnection(PeerAddress peerAddress)
    {
        if (connectionMap.containsKey(peerAddress))
        {
            return connectionMap.get(peerAddress);
        } else
        {
            Connection connection = new Connection();
            connectionMap.put(peerAddress, connection);
            return connection;
        }
    }
}
