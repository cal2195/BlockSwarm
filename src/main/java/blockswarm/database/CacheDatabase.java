/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database;

import java.io.IOException;
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

    public CacheDatabase(Connection databaseConnection)
    {
        conn = databaseConnection;
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
            stmt.setBytes(1, filehash.getBytes());
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
            stmt.setBytes(1, filehash.getBytes());
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
