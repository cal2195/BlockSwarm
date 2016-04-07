package blockswarm.database.entries;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author cal
 */
public class FileEntry implements Serializable
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

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.filehash);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final FileEntry other = (FileEntry) obj;
        if (!Objects.equals(this.filehash, other.filehash))
        {
            return false;
        }
        return true;
    }
}
