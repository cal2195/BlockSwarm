package blockswarm.database;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class DownloadDatabase
{

    private static final Logger LOGGER = Logger.getLogger(DownloadDatabase.class.getName());
    private final Connection conn;
    Node node;

    public DownloadDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
        setup();
    }

    public boolean queueDownload(String filehash)
    {
        String sql = "INSERT INTO downloads "
                + "(file_hash, date) "
                + "VALUES (?,NOW())";
        LOGGER.log(Level.FINE, "Queueing {0} for download!", new Object[]
        {
            filehash
        });
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error queueing {0} for download!", new Object[]
            {
                filehash
            });
            //ex.printStackTrace();
        }
        return false;
    }

    public boolean flaggedForDownload(String filehash)
    {
        String sql = "SELECT COUNT(*) FROM downloads "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return (resultSet.getInt(0) == 1);
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Download miss for file {0}!", new Object[]
            {
                filehash
            });
            return false;
        }
    }

    public boolean removeDownload(String filehash)
    {
        String sql = "DELETE FROM downloads "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error removing download {0}!", new Object[]
            {
                filehash
            });
            return false;
        }
    }
    
    public ArrayList<NodeFileInfo> getAllDownloads()
    {
        ArrayList<NodeFileInfo> fileList = new ArrayList<>();
        String sql = "SELECT * FROM downloads";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                fileList.add(node.getDatabase().getFiles().getFileInfo(resultSet.getString("file_hash")));
            }
            return fileList;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error getting all downloads!!");
        }
        return null;
    }

    private void setup()
    {
        try
        {
            LOGGER.info("Creating downloads table if needed...");
            if (!tableExists("downloads"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE downloads "
                            + "(file_hash CHAR(40) not NULL UNIQUE, "
                            + " date TIMESTAMP, "
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
