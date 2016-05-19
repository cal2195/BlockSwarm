package blockswarm.database;

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
public class UploadDatabase
{
    private static final Logger LOGGER = Logger.getLogger(UploadDatabase.class.getName());
    private final Connection conn;
    Node node;

    public UploadDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
        setup();
    }

    public boolean queueUpload(String filehash)
    {
        String sql = "INSERT INTO uploads "
                + "(file_hash, date) "
                + "VALUES (?,NOW())";
        LOGGER.log(Level.FINE, "Queueing {0} for upload!", new Object[]
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
            LOGGER.log(Level.FINE, "Error queueing {0} for upload!", new Object[]
            {
                filehash
            });
            //ex.printStackTrace();
        }
        return false;
    }

    public boolean flaggedForUpload(String filehash)
    {
        String sql = "SELECT COUNT(*) FROM uploads "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return (resultSet.getInt(0) == 1);
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Upload miss for file {0}!", new Object[]
            {
                filehash
            });
            return false;
        }
    }

    public boolean removeUpload(String filehash)
    {
        String sql = "DELETE FROM uploads "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error removing uploads {0}!", new Object[]
            {
                filehash
            });
            return false;
        }
    }
    
    public ArrayList<String> getAllUploads()
    {
        ArrayList<String> fileList = new ArrayList<>();
        String sql = "SELECT * FROM uploads";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                fileList.add(resultSet.getString("file_hash"));
            }
            return fileList;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error getting all uploads!!");
        }
        return null;
    }

    private void setup()
    {
        try
        {
            LOGGER.info("Creating uploads table if needed...");
            if (!tableExists("uploads"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE uploads "
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
