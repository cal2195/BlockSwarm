package blockswarm.files;

import blockswarm.files.blocks.Blocker;
import blockswarm.network.cluster.Node;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class FileHandler
{
    Node node;

    public FileHandler(Node node)
    {
        this.node = node;
    }

    public void uploadFile(File file)
    {
        try
        {
            Blocker.insertBlocks(file, node.getDatabase());
        } catch (IOException ex)
        {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
