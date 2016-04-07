package blockswarm.network.cluster;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

/**
 *
 * @author cal
 */
public class SuperNode
{
    Peer peer;
    SuperNodeIncomingHandler incomingHandler;

    public SuperNode()
    {
        setup();
        serve();
    }
    
    public void setup()
    {
        incomingHandler = new SuperNodeIncomingHandler();
    }

    public void serve()
    {
        try
        {
            Random r = new Random();
            Peer peer = new PeerBuilder(new Number160(r)).ports(44444).start();
            System.out.println("Listening for connections...");
            peer.objectDataReply(incomingHandler);
//            while (true)
//            {
//                System.out.println("Peers: " + peer.peerBean().peerMap().all() + " unverified: "
//                        + peer.peerBean().peerMap().allOverflow());
//                Thread.sleep(1000);
//            }
        } catch (IOException ex)
        {
            Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
