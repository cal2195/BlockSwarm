package blockswarm.database;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.NodeFileInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

    public PeerDatabase(Connection databaseConnection)
    {
        conn = databaseConnection;
        setup();
    }
    
    public boolean putFileInfo(PeerAddress pa, String filehash, NodeFileInfo info)
    {
        String sql = "REPLACE into peers (peer_address, file_hash, file_info) VALUES (?,?,?)";
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

    public HashMap<PeerAddress, NodeFileInfo> getFileInfo(String filehash)
    {
        HashMap<PeerAddress, NodeFileInfo> nodes = new HashMap<>();
        String sql = "SELECT file_info FROM peers "
                   + "WHERE filehash = ?";
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
            LOGGER.log(Level.FINE, "Cache miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        return null;
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
                            + " file_hash CHAR(40) not NULL UNIQUE, "
                            + " file_info OTHER,"
                            + " `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                            + " PRIMARY KEY ( id ))";
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
