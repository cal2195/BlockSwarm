package blockswarm.network.cluster;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
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

    public Node()
    {
        bootstrap("morebetterengineering.com");
    }
    
    public void setup()
    {
        incomingHandler = new NodeIncomingHandler();
    }

    public void bootstrap(String supernode)
    {
        try
        {
            Random r = new Random();
            peer = new PeerBuilder(new Number160(r)).ports(44445).start();
            peer.objectDataReply(incomingHandler);
            
            InetAddress address = Inet4Address.getByName(supernode);
            FutureDiscover futureDiscover = peer.discover().inetAddress(address).ports(44444).start();
            LOGGER.info("Discovering...");
            futureDiscover.awaitUninterruptibly();
            LOGGER.log(Level.INFO, "Found! {0}", futureDiscover.failedReason());
            FutureBootstrap futureBootstrap = peer.bootstrap().inetAddress(address).ports(44444).start();
            LOGGER.info("Bootstrapping...");
            futureBootstrap.awaitUninterruptibly();
            LOGGER.log(Level.INFO, "Bootstrapped!{0}", futureBootstrap.failedReason());
            LOGGER.log(Level.INFO, "Peers: {0} unverified: {1}", new Object[]{peer.peerBean().peerMap().all(), peer.peerBean().peerMap().allOverflow()});
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
