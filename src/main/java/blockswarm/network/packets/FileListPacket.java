package blockswarm.network.packets;

import blockswarm.database.entries.FileEntry;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class FileListPacket implements Serializable
{
    public final ArrayList<FileEntry> files;

    public FileListPacket(ArrayList<FileEntry> files)
    {
        this.files = files;
    }
}
