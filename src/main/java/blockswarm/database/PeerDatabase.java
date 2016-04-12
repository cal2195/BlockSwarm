package blockswarm.database;

import blockswarm.info.ClusterFileInfo;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class PeerDatabase
{

    private static final Logger LOGGER = Logger.getLogger(PeerDatabase.class.getName());
    private final Connection conn;
    Node node;

    public PeerDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
        setup();
    }

    public boolean putFileInfo(PeerAddress pa, String filehash, NodeFileInfo info)
    {
        String sql = "MERGE into peers (peer_address, file_hash, file_info) KEY(peer_address, file_hash) VALUES (?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setObject(1, pa);
            stmt.setString(2, filehash);
            stmt.setObject(3, info);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            Logger.getLogger(PeerDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public double getAvailability(String filehash)
    {
        HashMap<PeerAddress, NodeFileInfo> nodes = getFileInfo(filehash);
        int totalBlocks = node.getDatabase().getFiles().getTotalBlocks(filehash);
        NodeFileInfo clusterFileInfo = new NodeFileInfo(filehash, totalBlocks);
        if (nodes == null)
        {
            return -1;
        }
        LOGGER.finer("Found " + nodes.size() + " who have this file!");
        for (NodeFileInfo nodeFile : nodes.values())
        {
            LOGGER.finer(nodeFile.blocks.toString());
            clusterFileInfo.blocks.or(nodeFile.blocks);
        }
        LOGGER.log(Level.FINER, "Found {0} out of {1}", new Object[]
        {
            clusterFileInfo.blocks.cardinality(), totalBlocks
        });
        return (double) clusterFileInfo.blocks.cardinality() / (double) totalBlocks;
    }

    public ClusterFileInfo getClusterFileInfo(String filehash)
    {
        int totalBlocks = node.getDatabase().getFiles().getTotalBlocks(filehash);
        HashMap<PeerAddress, NodeFileInfo> nodes = getFileInfo(filehash);
        ClusterFileInfo clusterFileInfo = new ClusterFileInfo(filehash, totalBlocks);
        if (nodes == null)
        {
            return null;
        }
        LOGGER.finer("Found " + nodes.size() + " who have this file!");
        for (NodeFileInfo nodeFile : nodes.values())
        {
            LOGGER.finer(nodeFile.blocks.toString());
            clusterFileInfo.add(nodeFile);
        }
        return clusterFileInfo;
    }

    public HashMap<PeerAddress, NodeFileInfo> getFileInfo(String filehash)
    {
        HashMap<PeerAddress, NodeFileInfo> nodes = new HashMap<>();
        String sql = "SELECT * FROM peers "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                nodes.put((PeerAddress) resultSet.getObject("peer_address"), (NodeFileInfo) resultSet.getObject("file_info"));
            }
            return nodes;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            LOGGER.log(Level.FINER, "Cache miss for peer file {0}!", new Object[]
            {
                filehash
            });
        }
        return null;
    }

    public HashMap<PeerAddress, NodeFileInfo> getDownload(String filehash)
    {
        HashMap<PeerAddress, NodeFileInfo> nodes = getFileInfo(filehash);
        NodeFileInfo found = node.getDatabase().getFiles().getFileInfo(filehash);
        for (PeerAddress peerAddress : nodes.keySet())
        {
            NodeFileInfo peerfile = nodes.get(peerAddress);
            peerfile.blocks.andNot(found.blocks);
            NodeFileInfo ask = new NodeFileInfo(filehash);
            int asked = 0;
            for (int i = peerfile.blocks.nextSetBit(0); i >= 0 && asked++ < 20; i = peerfile.blocks.nextSetBit(i + 1))
            {
                ask.blocks.set(i);
                found.blocks.set(i);
            }
            nodes.put(peerAddress, ask);
        }
        return nodes;
    }

    private void setup()
    {
        try
        {
            LOGGER.info("Creating peer table if needed...");
            if (!tableExists("peers"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE peers "
                            + "(peer_address OTHER,"
                            + " file_hash CHAR(40) not NULL, "
                            + " file_info OTHER,"
                            + " `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                            + " PRIMARY KEY ( id ))";
                    stmt.executeUpdate(sql);
                    sql = "ALTER TABLE peers ADD CONSTRAINT unique_block_peers UNIQUE(peer_address, file_hash)";
                    stmt.executeUpdate(sql);
                }
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean tableExists(String table)
    {
        try (Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM " + table + " LIMIT 1";
            stmt.executeQuery(sql);
            return true;
        } catch (SQLException ex)
        {
            return false;
        }
    }
}
