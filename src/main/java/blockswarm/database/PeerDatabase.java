package blockswarm.database;

import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.PeerAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    public boolean updatePeer(PeerAddress pa, int blockCount, boolean supernode)
    {
        String sql = "INSERT INTO peers (peer_address, blocks, super, date) VALUES (?,?,?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setObject(1, pa);
            stmt.setInt(2, blockCount);
            stmt.setBoolean(3, supernode);
            return stmt.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(PeerDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
                            + " blocks INT, "
                            + " super BOOLEAN, "
                            + " date TIMESTAMP, "
                            + " PRIMARY KEY ( peer_address ))";
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
