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
public class SiteDatabase
{
    private static final Logger LOGGER = Logger.getLogger(SiteDatabase.class.getName());
    private final Connection conn;
    Node node;

    public SiteDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
        setup();
    }

    public boolean addSite(String domain, String filehash)
    {
        String sql = "INSERT INTO site "
                + "(domain, file_hash) "
                + "VALUES (?,?)";
        LOGGER.log(Level.FINE, "Adding site {0}!", new Object[]
        {
            domain
        });
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, domain);
            stmt.setString(2, filehash);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error adding site {0}!", new Object[]
            {
                domain
            });
            ex.printStackTrace();
        }
        return false;
    }

    public String getHash(String domain)
    {
        String sql = "SELECT file_hash FROM site "
                   + "WHERE domain = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, domain);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getString("file_hash");
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Site miss for {0}!", new Object[]
            {
                domain
            });
        }
        return null;
    }

    private void setup()
    {
        try
        {
            LOGGER.info("Creating site table if needed...");
            if (!tableExists("site"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE site "
                            + "(domain VARCHAR not NULL UNIQUE,"
                            + " file_hash CHAR(40) not NULL UNIQUE, "
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
