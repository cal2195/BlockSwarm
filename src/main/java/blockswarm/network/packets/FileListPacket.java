package blockswarm.network.packets;

import blockswarm.database.entries.FileEntry;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class FileListPacket
{
    final ArrayList<FileEntry> files;

    public FileListPacket(ArrayList<FileEntry> files)
    {
        this.files = files;
    }
}
