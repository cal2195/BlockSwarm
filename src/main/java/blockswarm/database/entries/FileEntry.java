package blockswarm.database.entries;

/**
 *
 * @author cal
 */
public class FileEntry
{
    public final String filehash, filename;
    public final int totalBlocks;

    public FileEntry(String filehash, String filename, int totalBlocks)
    {
        this.filehash = filehash;
        this.filename = filename;
        this.totalBlocks = totalBlocks;
    }

    @Override
    public String toString()
    {
        return "FileEntry{" + "filehash=" + filehash + ", filename=" + filename + ", totalBlocks=" + totalBlocks + '}';
    }
}
