package blockswarm.network.cluster;

import blockswarm.database.Database;
import blockswarm.gui.FXMLController;
import blockswarm.workers.CacheWorker;
import blockswarm.workers.GUIWorker;
import blockswarm.workers.PeerRequestManager;
import blockswarm.workers.CacheManager;
import blockswarm.workers.WorkerPool;
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
    final boolean USING_GUI;

    WorkerPool workerPool;
    Peer peer;
    NodeIncomingHandler incomingHandler;
    Tracker tracker;
    Database database;
    Cluster cluster;
    FXMLController gui;
    PeerRequestManager peerRequestManager;

    public Node()
    {
        USING_GUI = false;
    }

    public Node(FXMLController gui)
    {
        USING_GUI = true;
        this.gui = gui;
    }

    public FXMLController getGui()
    {
        return gui;
    }

    public void setupNode()
    {
        setupDatabase();

        setupWorkerPool();

        if (USING_GUI)
        {
            setupGui();
        }

        setupIncomingHandler();

        bootstrap("morebetterengineering.com");

        setupTracker();

        setupCluster();
    }

    public void setupWorkerPool()
    {
        workerPool = new WorkerPool();
    }

    public WorkerPool getWorkerPool()
    {
        return workerPool;
    }

    protected void setupGui()
    {
        gui.setNode(this);
        gui.updateFileList();
        workerPool.addRepeatedWorker(new GUIWorker(this), 5);
    }

    protected void setupIncomingHandler()
    {
        incomingHandler = new NodeIncomingHandler(this);
    }

    protected void setupTracker()
    {
        tracker = new Tracker(peer);
    }

    protected void setupCluster()
    {
        cluster = new Cluster(this);
        workerPool.addRepeatedWorker(new CacheManager(this), 20);
        peerRequestManager = new PeerRequestManager(this);
    }

    protected void setupDatabase()
    {
        database = new Database(this);
        database.connect();
        database.initialise();
    }

    public Database getDatabase()
    {
        return database;
    }

    public Peer getPeer()
    {
        return peer;
    }

    public PeerRequestManager getPeerRequestManager()
    {
        return peerRequestManager;
    }

    public Tracker getTracker()
    {
        return tracker;
    }

    public Cluster getCluster()
    {
        return cluster;
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
            PeerAddress pa = new PeerAddress(Number160.ZERO, address, masterPort, masterPort);

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

    public FutureDirect send(PeerAddress pa, Object o)
    {
        return peer.sendDirect(pa).object(o).start();
    }
}
