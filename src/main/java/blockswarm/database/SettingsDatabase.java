package blockswarm.database;

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
public class SettingsDatabase
{

    private static final Logger LOGGER = Logger.getLogger(FileDatabase.class.getName());
    private final Connection conn;
    Node node;

    public SettingsDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
        setup();
    }
    
    public void put(String key, String value)
    {
        String sql = "MERGE INTO settings "
                + "(key,value) KEY(key) "
                + "VALUES (?,?)";
        LOGGER.fine("Setting " + key + " to " + value);
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.execute();
        } catch (SQLException ex)
        {
            LOGGER.fine("Error setting " + key + " to " + value);
        }
    }
    
    public String get(String key, String defaultValue)
    {
        String sql = "SELECT value from settings "
                + " WHERE key = ?";
        LOGGER.fine("Getting value for " + key);
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, key);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getString("value");
        } catch (SQLException ex)
        {
            LOGGER.fine("Returning default value " + defaultValue + " for key " + key);
            return defaultValue;
        }
    }

    private void setup()
    {
        try
        {
            LOGGER.info("Creating settings table if needed...");
            if (!tableExists("settings"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE settings "
                            + "(key VARCHAR not NULL UNIQUE, "
                            + " value VARCHAR,"
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
