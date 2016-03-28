/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database;

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
public class CacheDatabase
{

    private static final Logger LOGGER = Logger.getLogger(CacheDatabase.class.getName());
    private final Connection conn;

    public CacheDatabase(Connection databaseConnection)
    {
        conn = databaseConnection;
        setup();
    }

    public void putBlock(String filehash, int blockid, byte[] blockdata)
    {
        String sql = "INSERT INTO cache "
                    + "(file_hash, block_id, block_data) "
                    + "VALUES (?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setBytes(1, filehash.getBytes());
            stmt.setInt(2, blockid);
            stmt.setBytes(3, blockdata);
            stmt.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(CacheDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                            + "(file_hash BINARY(20) not NULL, "
                            + " block_id INTEGER not NULL,"
                            + " block_data BLOB,"
                            + " id INTEGER AUTO_INCREMENT,"
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
