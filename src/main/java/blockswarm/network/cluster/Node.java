package blockswarm.network.cluster;

import blockswarm.blocksites.ProxyServer;
import blockswarm.database.Database;
import blockswarm.gui.FXMLController;
import blockswarm.network.cluster.traffic.TrafficLimiter;
import blockswarm.network.connections.ConnectionManager;
import blockswarm.network.connections.Server;
import blockswarm.stats.NetworkStats;
import blockswarm.workers.GUIWorker;
import blockswarm.workers.PeerRequestManager;
import blockswarm.workers.CacheManager;
import blockswarm.workers.ClusterFileInfoUpdater;
import blockswarm.workers.FileListUpdater;
import blockswarm.workers.WorkerPool;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;

/**
 * @author cal
 */
public class Node {

    private static final Logger LOGGER = Logger.getLogger(Node.class.getName());
    final boolean USING_GUI;

    public WorkerPool workerPool;
    public Server server;
    ConnectionManager connectionManager = new ConnectionManager(this);
    DHT dht;
    Database database;
    public Cluster cluster;
    FXMLController gui;
    PeerRequestManager peerRequestManager;
    NetworkStats networkStats = new NetworkStats();
    ProxyServer proxyServer;
    TrafficLimiter trafficLimiter = new TrafficLimiter();

    int TIMEOUT = 60 * 1000;
    int PORT = 44448;

    public Node() {
        USING_GUI = false;
    }

    public Node(FXMLController gui) {
        USING_GUI = true;
        this.gui = gui;
    }

    public FXMLController getGui() {
        return gui;
    }

    public void setupNode() {
        setupDatabase();

        setupWorkerPool();

        if (USING_GUI) {
            setupGui();
        }

//        setupIncomingHandler();

        loadSettings();

        bootstrap(null);

        setupDHT();

        setupCluster();

        setupProxyServer();
    }

    public void loadSettings() {
//        trafficLimiter.setWriteLimit(getDatabase().getSettings().getInt("uploadLimit", "0"));
//        trafficLimiter.setReadLimit(getDatabase().getSettings().getInt("downloadLimit", "0"));
    }

    public void setupProxyServer() {
        proxyServer = new ProxyServer(this);
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setupWorkerPool() {
        workerPool = new WorkerPool();
    }

    public TrafficLimiter getTrafficLimiter() {
        return trafficLimiter;
    }

    public WorkerPool getWorkerPool() {
        return workerPool;
    }

    protected void setupGui() {
        gui.setNode(this);
        gui.updateFileList();
        workerPool.addRepeatedWorker(new GUIWorker(this), 5);
    }

    protected void setupDHT() {
//        dht = new DHT(peer);
    }

    protected void setupCluster() {
        cluster = new Cluster(this);
        workerPool.addRepeatedWorker(new CacheManager(this), 60);
        peerRequestManager = new PeerRequestManager(this);
        workerPool.addRepeatedWorker(new ClusterFileInfoUpdater(this), 10);
        networkStats = new NetworkStats();
        workerPool.addRepeatedWorker(new FileListUpdater(this), 120);
    }

    protected void setupDatabase() {
        database = new Database(this);
        database.connect();
        database.initialise();
    }

    public NetworkStats getNetworkStats() {
        return networkStats;
    }

    public DHT getDHT() {
        return dht;
    }

    public Database getDatabase() {
        return database;
    }

    public PeerRequestManager getPeerRequestManager() {
        return peerRequestManager;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void bootstrap(PeerAddress supernode) {
        server = new Server(PORT, this);
    }

    public void send(PeerAddress pa, Object o) {
        connectionManager.getConnection(pa).send(o);
        //return peer.sendDirect(pa).connectionTimeoutTCPMillis(TIMEOUT).idleTCPMillis(TIMEOUT).idleUDPMillis(TIMEOUT).object(o).start();
    }
}
