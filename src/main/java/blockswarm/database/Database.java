package blockswarm.database;

import blockswarm.database.handlers.DatabasePool;
import blockswarm.network.cluster.Node;
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
    private CacheDatabase cacheDatabase;
    private FileDatabase fileDatabase;
    private PeerDatabase peerDatabase;
    private DatabasePool databasePool;
    Node node;

    public Database(Node node)
    {
        this.node = node;
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
        fileDatabase = new FileDatabase(conn, node);
        cacheDatabase = new CacheDatabase(conn, node);
        peerDatabase = new PeerDatabase(conn, node);
        databasePool = new DatabasePool();
    }

    public DatabasePool getDatabasePool()
    {
        return databasePool;
    }

    public CacheDatabase getCache()
    {
        return cacheDatabase;
    }

    public FileDatabase getFiles()
    {
        return fileDatabase;
    }

    public PeerDatabase getPeers()
    {
        return peerDatabase;
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
