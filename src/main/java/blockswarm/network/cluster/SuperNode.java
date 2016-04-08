package blockswarm.network.cluster;

import blockswarm.database.Database;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.ChannelCreator;
import net.tomp2p.connection.DefaultConnectionConfiguration;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

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
            while (true)
            {
                for (PeerAddress pa : peer.peerBean().peerMap().all())
                {
                    System.out.println("PeerAddress: " + pa);
                    FutureChannelCreator fcc = peer.connectionBean().reservation().create(1, 1);
                    fcc.awaitUninterruptibly();

                    ChannelCreator cc = fcc.channelCreator();

                    FutureResponse fr1 = peer.pingRPC().pingTCP(pa, cc, new DefaultConnectionConfiguration());
                    fr1.awaitUninterruptibly();

                    if (fr1.isSuccess())
                    {
                        System.out.println("peer online T:" + pa);
                    } else
                    {
                        System.out.println("offline " + pa);
                    }

                    FutureResponse fr2 = peer.pingRPC().pingUDP(pa, cc, new DefaultConnectionConfiguration());
                    fr2.awaitUninterruptibly();

                    cc.shutdown();

                    if (fr2.isSuccess())
                    {
                        System.out.println("peer online U:" + pa);
                    } else
                    {
                        System.out.println("offline " + pa);
                    }
                }
                for (PeerAddress pa : peer.peerBean().peerMap().allOverflow())
                {
                    System.out.println("PeerAddress: " + pa);
                    FutureChannelCreator fcc = peer.connectionBean().reservation().create(1, 1);
                    fcc.awaitUninterruptibly();

                    ChannelCreator cc = fcc.channelCreator();

                    FutureResponse fr1 = peer.pingRPC().pingTCP(pa, cc, new DefaultConnectionConfiguration());
                    fr1.awaitUninterruptibly();

                    if (fr1.isSuccess())
                    {
                        System.out.println("peer online T:" + pa);
                    } else
                    {
                        System.out.println("offline " + pa);
                    }

                    FutureResponse fr2 = peer.pingRPC().pingUDP(pa, cc, new DefaultConnectionConfiguration());
                    fr2.awaitUninterruptibly();

                    cc.shutdown();

                    if (fr2.isSuccess())
                    {
                        System.out.println("peer online U:" + pa);
                    } else
                    {
                        System.out.println("offline " + pa);
                    }
                }
                try
                {
                    Thread.sleep(10000);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
//            while (true)
//            {
//                System.out.println("Peers: " + peer.peerBean().peerMap().all() + " unverified: "
//                        + peer.peerBean().peerMap().allOverflow());
//                try
//                {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex)
//                {
//                    Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
        } catch (IOException ex)
        {
            Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
