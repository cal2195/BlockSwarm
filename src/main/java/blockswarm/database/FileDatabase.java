package blockswarm.database;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.NodeFileInfo;
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
public class FileDatabase
{

    private static final Logger LOGGER = Logger.getLogger(FileDatabase.class.getName());
    private final Connection conn;

    public FileDatabase(Connection databaseConnection)
    {
        conn = databaseConnection;
        setup();
    }

    public boolean putFile(FileEntry file)
    {
        String sql = "INSERT INTO files "
                + "(file_hash, file_name, total_blocks) "
                + "VALUES (?,?,?)";
        LOGGER.log(Level.FINE, "Adding file {0}({1}) to database!", new Object[]
        {
            file.filename, file.filehash
        });
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setBytes(1, file.filehash.getBytes());
            stmt.setString(2, file.filename);
            stmt.setInt(3, file.totalBlocks);
            stmt.execute();
            return true;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error adding file {0}({1}) to database!", new Object[]
            {
                file.filename, file.filehash
            });
        }
        return false;
    }
    
    public void putAllFiles(ArrayList<FileEntry> files)
    {
        for (FileEntry file : files)
        {
            putFile(file);
        }
    }

    public FileEntry getFile(String filehash)
    {
        String sql = "SELECT * FROM files "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setBytes(1, filehash.getBytes());
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return new FileEntry(new String(resultSet.getBytes("file_hash")), resultSet.getString("file_name"), resultSet.getInt("total_blocks"));
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "File miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        return null;
    }

    public ArrayList<FileEntry> getAllFiles(ArrayList<String> except)
    {
        ArrayList<FileEntry> files = new ArrayList<>();
        String sql = "SELECT * FROM files";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                if (!except.contains(new String(resultSet.getBytes("file_hash"))))
                {
                    files.add(new FileEntry(new String(resultSet.getBytes("file_hash")), resultSet.getString("file_name"), resultSet.getInt("total_blocks")));
                }
            }
            return files;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error generating file list!");
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
            stmt.setBytes(1, filehash.getBytes());
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

    private void setup()
    {
        try
        {
            LOGGER.info("Creating files table if needed...");
            if (!tableExists("files"))
            {
                try (Statement stmt = conn.createStatement())
                {
                    String sql = "CREATE TABLE files "
                            + "(file_hash BINARY(20) not NULL UNIQUE, "
                            + " file_name VARCHAR(255),"
                            + " total_blocks INTEGER not NULL,"
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
