/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.network.connections;

import blockswarm.network.cluster.PeerAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;

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
            Connection connection = new Connection(peerAddress);
            try
            {
                connection.setupConnection();
            } catch (SSLException ex)
            {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            connectionMap.put(peerAddress, connection);
            return connection;
        }
    }
}
