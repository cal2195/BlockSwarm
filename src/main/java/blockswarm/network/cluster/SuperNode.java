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

    public SuperNode()
    {
        serve();
    }

    public void serve()
    {
        try
        {
            Random r = new Random();
            Peer peer = new PeerBuilder(new Number160(r)).ports(44444).start();
            System.out.println("Listening for connections...");
            peer.objectDataReply(new ObjectDataReply()
            {
                @Override
                public Object reply(PeerAddress sender, Object request) throws Exception
                {
                    System.out.println("request [" + request + "]");
                    return "world!";
                }
            });
            while (true)
            {
                System.out.println("Peers: " + peer.peerBean().peerMap().all() + " unverified: "
                        + peer.peerBean().peerMap().allOverflow());
                Thread.sleep(1000);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
