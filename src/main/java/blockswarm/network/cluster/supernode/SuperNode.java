package blockswarm.network.cluster.supernode;

import blockswarm.network.cluster.Cluster;
import blockswarm.network.cluster.Node;
import blockswarm.workers.FileListUpdater;
import blockswarm.workers.supernode.ClusterFileInfoWorker;
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
    ClusterFileInfoHandler clusterFileInfoHandler = new ClusterFileInfoHandler();

    public SuperNode()
    {

    }

    public void setupSuperNode()
    {
        setupDatabase();

        setupWorkerPool();

        setupIncomingHandler();

        serve();

        setupTracker();
        
        setupDHT();

        setupCluster();

        setupClusterInfo();
    }

    @Override
    protected void setupIncomingHandler()
    {
        incomingHandler = new SuperNodeIncomingHandler(this);
    }

    public ClusterFileInfoHandler getClusterFileInfoHandler()
    {
        return clusterFileInfoHandler;
    }

    public void setupClusterInfo()
    {
        getWorkerPool().addRepeatedWorker(new ClusterFileInfoWorker(this), 10);
    }
    
    @Override
    protected void setupCluster()
    {
        cluster = new Cluster(this);
        workerPool.addRepeatedWorker(new FileListUpdater(this), 120);
    }

    public void serve()
    {
        try
        {
            Random r = new Random();
            Bindings bindings = new Bindings().addInterface("eth0");
            peer = new PeerBuilder(new Number160(r)).ports(44444).bindings(bindings).start();
            //PeerNAT peerNAT = new PeerBuilderNAT(peer).start();
            System.out.println("address visible to outside is " + peer.peerAddress());
            LOGGER.info("Listening for connections...");
            peer.objectDataReply(incomingHandler);
        } catch (IOException ex)
        {
            Logger.getLogger(SuperNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
