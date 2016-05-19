package blockswarm.database.entries;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author cal
 */
public class FileEntry implements Serializable
{
    public final String filehash, filename, tags;
    public final int totalBlocks;
    public final double availability;

    public FileEntry(String filehash, String filename, String tags, int totalBlocks, double availability)
    {
        this.filehash = filehash;
        this.filename = filename;
        this.tags = tags;
        this.totalBlocks = totalBlocks;
        this.availability = availability;
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
