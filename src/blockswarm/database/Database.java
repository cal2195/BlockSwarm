package blockswarm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class Database
{
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private Connection conn;

    public Database()
    {

    }

    public void connect()
    {
        LOGGER.info("Connecting to local database...");
        try
        {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:./database/blockswarm.db", "sa", "");
            LOGGER.info("Connected!");
        } catch (ClassNotFoundException | SQLException ex)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect()
    {
        LOGGER.info("Disconnecting from local database...");
        try
        {
            conn.close();
            LOGGER.info("Connection closed!");
        } catch (SQLException ex)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialise()
    {
        setupCache();
    }

    private void setupCache()
    {
        try
        {
            LOGGER.info("Creating cache table if needed...");
            try (Statement stmt = conn.createStatement())
            {
                String sql = "CREATE TABLE cache IF NOT EXISTS"
                        + "(file_hash BINARY not NULL, "
                        + " block_id INTEGER,"
                        + " block_data BLOB,"
                        + " PRIMARY KEY ( id ))";
                stmt.executeUpdate(sql);
                sql = "ALTER TABLE cache ADD CONSTRAINT unique_block UNIQUE(file_hash, block_id)";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
