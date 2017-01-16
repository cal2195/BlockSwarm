package blockswarm.database;

import blockswarm.info.ClusterFileInfo;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.PeerRequestKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String sql = "MERGE into peers (peer_address, file_hash, file_info, date) KEY(peer_address, file_hash) VALUES (?,?,?,NOW())";
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
        return getClusterFileInfo(filehash, false).getAvailability();
    }

    public ClusterFileInfo[] getLowestAvailability(int limit)
    {
        ArrayList<ClusterFileInfo> files = new ArrayList<>();
        for (String hash : node.getDatabase().getFiles().getAllFileHashes())
        {
            ClusterFileInfo fileInfo = getClusterFileInfo(hash, true);
            if (!Double.isNaN(fileInfo.getAvailability()) && fileInfo.getAvailability() != 0)
            {
                files.add(fileInfo);
                System.out.println("Added " + hash + " with " + fileInfo.getAvailability());
            }
        }
        Collections.sort(files);
        return Arrays.copyOfRange(files.toArray(new ClusterFileInfo[0]), 0, limit);
    }

    public ClusterFileInfo getClusterFileInfo(String filehash, boolean includingMe)
    {
        int totalBlocks = node.getDatabase().getFiles().getTotalBlocks(filehash);
        HashMap<PeerRequestKey, NodeFileInfo> nodes = getFileInfo(filehash);
        ClusterFileInfo clusterFileInfo = new ClusterFileInfo(filehash, totalBlocks);
        if (nodes == null)
        {
            return null;
        }
        for (NodeFileInfo nodeFile : nodes.values())
        {
            LOGGER.finer(nodeFile.blocks.toString());
            clusterFileInfo.add(nodeFile);
        }
        if (includingMe)
        {
            clusterFileInfo.add(node.getDatabase().getCache().getFileInfo(filehash));
        }
        LOGGER.finer(clusterFileInfo.toString());
        return clusterFileInfo;
    }

    public HashMap<PeerRequestKey, NodeFileInfo> getFileInfo(String filehash)
    {
        HashMap<PeerRequestKey, NodeFileInfo> nodes = new HashMap<>();
        String sql = "SELECT * FROM peers "
                + "WHERE file_hash = ? "
                + "AND date > TIMESTAMPADD(minute, -5, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                nodes.put(new PeerRequestKey((PeerAddress) resultSet.getObject("peer_address"), (NodeFileInfo) resultSet.getObject("file_info")), (NodeFileInfo) resultSet.getObject("file_info"));
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

    public HashMap<PeerRequestKey, NodeFileInfo> getDownload(String filehash)
    {
        HashMap<PeerRequestKey, NodeFileInfo> nodes = getFileInfo(filehash);
        NodeFileInfo found = node.getDatabase().getFiles().getFileInfo(filehash);
        List<Map.Entry<PeerRequestKey, NodeFileInfo>> list = new ArrayList<>(nodes.entrySet());
        Collections.shuffle(list);
        for (Map.Entry<PeerRequestKey, NodeFileInfo> entry : list)
        {
            NodeFileInfo peerfile = entry.getValue();
            peerfile.blocks.andNot(found.blocks);
            NodeFileInfo ask = new NodeFileInfo(filehash);
            int asked = 0;
            for (int i = peerfile.blocks.nextSetBit(0); i >= 0 && asked++ < 100; i = peerfile.blocks.nextSetBit(i + 1))
            {
                ask.blocks.set(i);
                found.blocks.set(i);
            }
            nodes.put(entry.getKey(), ask);
        }
        return nodes;
    }

    public HashMap<PeerRequestKey, NodeFileInfo> getDownload(NodeFileInfo toCache)
    {
        HashMap<PeerRequestKey, NodeFileInfo> nodes = getFileInfo(toCache.hash);
        NodeFileInfo have = node.getDatabase().getFiles().getFileInfo(toCache.hash);
        System.out.println("I have this " + have.blocks.toString());
        toCache.blocks.andNot(have.blocks);
        System.out.println("I will ask for " + toCache.blocks.toString());
        toCache.blocks.flip(0, node.getDatabase().getFiles().getTotalBlocks(toCache.hash));
        List<Map.Entry<PeerRequestKey, NodeFileInfo>> list = new ArrayList<>(nodes.entrySet());
        Collections.shuffle(list);
        for (Map.Entry<PeerRequestKey, NodeFileInfo> entry : list)
        {
            NodeFileInfo peerfile = entry.getValue();
            peerfile.blocks.andNot(toCache.blocks);
            NodeFileInfo ask = new NodeFileInfo(toCache.hash);
            int asked = 0;
            for (int i = peerfile.blocks.nextSetBit(0); i >= 0 && asked++ < 20; i = peerfile.blocks.nextSetBit(i + 1))
            {
                ask.blocks.set(i);
                toCache.blocks.set(i);
            }
            nodes.put(entry.getKey(), ask);
        }
        return nodes;
    }

    public void clear()
    {
        try (Statement stmt = conn.createStatement())
        {
            String sql = "DELETE FROM peers";
            stmt.executeUpdate(sql);
        } catch (SQLException ex)
        {
            Logger.getLogger(PeerDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                            + " date TIMESTAMP, "
                            + " `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                            + " PRIMARY KEY ( id ))";
                    stmt.executeUpdate(sql);
                    sql = "ALTER TABLE peers ADD CONSTRAINT unique_block_peers UNIQUE(peer_address, file_hash)";
                    stmt.executeUpdate(sql);
                }
            }
            //For now!
            clear();
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
