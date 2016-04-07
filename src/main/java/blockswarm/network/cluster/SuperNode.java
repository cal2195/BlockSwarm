package blockswarm.network.cluster;

import blockswarm.database.Database;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.connection.Bindings;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

/**
 *
 * @author cal
 */
public class SuperNode extends Node
{

    private static final Logger LOGGER = Logger.getLogger(SuperNode.class.getName());

    SuperNodeIncomingHandler incomingHandler;

    public SuperNode()
    {

    }

    public void setupSuperNode()
    {
        setupDatabase();
        
        setupIncomingHandler();

        serve();

        setupTracker();
        
        setupCluster();
    }

    @Override
    protected void setupIncomingHandler()
    {
        incomingHandler = new SuperNodeIncomingHandler(this);
    }

    public void serve()
    {
        try
        {
            Random r = new Random();
            Bindings bindings = new Bindings().addInterface("eth0");
            peer = new PeerBuilder(new Number160(r)).ports(44444).bindings(bindings).start();
            System.out.println("address visible to outside is " + peer.peerAddress());
            LOGGER.info("Listening for connections...");
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
