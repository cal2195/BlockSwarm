package blockswarm.database;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class CacheDatabase
{

    private static final Logger LOGGER = Logger.getLogger(CacheDatabase.class.getName());
    private final Connection conn;
    Node node;

    public CacheDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
        setup();
    }

    public boolean putBlock(String filehash, int blockid, byte[] blockdata)
    {
        String sql = "INSERT INTO cache "
                + "(file_hash, block_id, block_data) "
                + "VALUES (?,?,?)";
        LOGGER.log(Level.FINE, "Adding block {0}:{1} to cache!", new Object[]
        {
            filehash, blockid
        });
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            stmt.setInt(2, blockid);
            stmt.setBytes(3, blockdata);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error adding block {0}:{1} to cache!", new Object[]
            {
                filehash, blockid
            });
        }
        return false;
    }

    public byte[] getBlock(String filehash, int blockid)
    {
        String sql = "SELECT block_data FROM cache "
                + "WHERE file_hash = ? AND "
                + "block_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            stmt.setInt(2, blockid);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getBytes("block_data");
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Cache miss for block {0}:{1}!", new Object[]
            {
                filehash, blockid
            });
        }
        return null;
    }

    public NodeFileInfo getFileInfo(String filehash)
    {
        NodeFileInfo fileInfo = new NodeFileInfo(filehash);
        String sql = "SELECT block_id FROM cache "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                fileInfo.blocks.set(resultSet.getInt("block_id"));
            }
            return fileInfo;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Cache miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        return null;
    }
    
    public int cacheSize()
    {
        String sql = "SELECT COUNT(*) FROM cache";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Problem getting cache size!");
        }
        return -1;
    }

    private void setup()
    {
        try
        {
            LOGGER.info("Creating cache table if needed...");
            if (!tableExists("cache"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE cache "
                            + "(file_hash CHAR(40) not NULL, "
                            + " block_id INTEGER not NULL,"
                            + " block_data BLOB,"
                            + " `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                            + " PRIMARY KEY ( id ))";
                    stmt.executeUpdate(sql);
                    sql = "ALTER TABLE cache ADD CONSTRAINT unique_block UNIQUE(file_hash, block_id)";
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
