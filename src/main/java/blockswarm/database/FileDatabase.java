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
import java.util.HashMap;
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
    Node node;

    public FileDatabase(Connection databaseConnection, Node node)
    {
        conn = databaseConnection;
        this.node = node;
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
            stmt.setString(1, file.filehash);
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
            //ex.printStackTrace();
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

    public int getTotalBlocks(String filehash)
    {
        String sql = "SELECT total_blocks FROM files "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt("total_blocks");
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "File miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        return -1;
    }

    public boolean hasFullFile(String filehash)
    {
        int totalBlocks = -1, haveBlocks = 0;
        String sql = "SELECT total_blocks FROM files "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            totalBlocks = resultSet.getInt("total_blocks");
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "File miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        sql = "SELECT COUNT(*) FROM cache "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            haveBlocks = resultSet.getInt(1);
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "File miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        return totalBlocks == haveBlocks;
    }

    public FileEntry getFile(String filehash)
    {
        String sql = "SELECT * FROM files "
                + "WHERE file_hash = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, filehash);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return new FileEntry(resultSet.getString("file_hash"), resultSet.getString("file_name"), resultSet.getInt("total_blocks"), (node == null) ? -1 : node.getDatabase().getPeers().getAvailability(resultSet.getString("file_hash")));
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "File miss for file {0}!", new Object[]
            {
                filehash
            });
        }
        return null;
    }
    
    public ArrayList<String> getAllFileHashes()
    {
        ArrayList<String> files = new ArrayList<>();
        String sql = "SELECT * FROM files";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                files.add(resultSet.getString("file_hash"));
            }
            return files;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error generating filehash list!");
        }
        return null;
    }

    public ArrayList<FileEntry> getAllFiles()
    {
        ArrayList<FileEntry> files = new ArrayList<>();
        String sql = "SELECT * FROM files";
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                files.add(new FileEntry(resultSet.getString("file_hash"), resultSet.getString("file_name"), resultSet.getInt("total_blocks"), node.getDatabase().getPeers().getAvailability(resultSet.getString("file_hash"))));
            }
            return files;
        } catch (SQLException ex)
        {
            LOGGER.log(Level.FINE, "Error generating file list!");
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
                if (!except.contains(resultSet.getString("file_hash")))
                {
                    files.add(new FileEntry(resultSet.getString("file_hash"), resultSet.getString("file_name"), resultSet.getInt("total_blocks"), node.getDatabase().getPeers().getAvailability(resultSet.getString("file_hash"))));
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
            stmt.setString(1, filehash);
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
    
    public HashMap<String, NodeFileInfo> getAllFileInfo()
    {
        HashMap<String, NodeFileInfo> fileHashMap = new HashMap<>();
        for (String filehash : getAllFileHashes())
        {
            fileHashMap.put(filehash, getFileInfo(filehash));
        }
        return fileHashMap;
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
                            + "(file_hash CHAR(40) not NULL UNIQUE, "
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
