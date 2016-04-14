package blockswarm.network.cluster;

import blockswarm.info.NodeFileInfo;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;
import net.tomp2p.tracker.FutureTracker;
import net.tomp2p.tracker.PeerBuilderTracker;
import net.tomp2p.tracker.PeerTracker;

/**
 *
 * @author cal
 */
public class DHT
{

    PeerDHT dht;

    public DHT(Peer peer)
    {
        dht = new PeerBuilderDHT(peer).start();
    }

    public HashMap<PeerAddress, NodeFileInfo> getClusterFileInfo(String filehash)
    {
        FutureGet futureGet = dht.get(Number160.createHash(filehash)).start();
        futureGet.awaitUninterruptibly();
        try
        {
            return (HashMap<PeerAddress, NodeFileInfo>) futureGet.data().object();
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DHT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(DHT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void putClusterFileInfo(String filehash, HashMap<PeerAddress, NodeFileInfo> hashmap)
    {
        try
        {
            FuturePut futurePut = dht.put(Number160.createHash(filehash)).data(new Data(hashmap)).start();
            futurePut.awaitUninterruptibly();
        } catch (IOException iOException)
        {
            iOException.printStackTrace();
        }
    }
}
