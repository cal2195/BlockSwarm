package blockswarm.network.cluster;

import blockswarm.database.Database;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class Node
{

    private static final Logger LOGGER = Logger.getLogger(Node.class.getName());

    Peer peer;
    NodeIncomingHandler incomingHandler;
    Tracker tracker;
    Database database;

    public Node()
    {

    }

    public void setupNode()
    {
        setupDatabase();
        
        setupIncomingHandler();
        
        bootstrap("morebetterengineering.com");
        
        setupTracker();
    }
    
    public void setupIncomingHandler()
    {
        incomingHandler = new NodeIncomingHandler(this);
    }

    public void setupTracker()
    {
        tracker = new Tracker(peer);
    }
    
    public void setupDatabase()
    {
        database = new Database();
        database.connect();
        database.initialise();
    }

    public Database getDatabase()
    {
        return database;
    }

    public void bootstrap(String supernode)
    {
        try
        {
            Random r = new Random();
            peer = new PeerBuilder(new Number160(r)).ports(44444).start();
            peer.objectDataReply(incomingHandler);
            InetAddress address = Inet4Address.getByName(supernode);

            System.out.println("address visible to outside is " + peer.peerAddress());

            int masterPort = 44444;
            PeerAddress pa = new PeerAddress(Number160.ZERO, address, masterPort, masterPort, masterPort + 1);

            System.out.println("PeerAddress: " + pa);

            // Future Discover
            FutureDiscover futureDiscover = peer.discover().expectManualForwarding().inetAddress(address).ports(masterPort).start();
            futureDiscover.awaitUninterruptibly();

            // Future Bootstrap - slave
            FutureBootstrap futureBootstrap = peer.bootstrap().inetAddress(address).ports(masterPort).start();
            futureBootstrap.awaitUninterruptibly();

            Collection<PeerAddress> addressList = peer.peerBean().peerMap().all();
            System.out.println(addressList.size());

            if (futureDiscover.isSuccess())
            {
                System.out.println("found that my outside address is " + futureDiscover.peerAddress());
            } else
            {
                System.out.println("failed " + futureDiscover.failedReason());
            }
        } catch (IOException ex)
        {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(PeerAddress pa, Object o)
    {
        FutureDirect futureData = peer.sendDirect(pa).object(o).start();
    }
}
