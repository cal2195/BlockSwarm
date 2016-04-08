package blockswarm.network.packets;

import java.io.Serializable;

/**
 *
 * @author cal
 */
public class FileInfoRequestPacket implements Serializable
{
    public final String filehash;

    public FileInfoRequestPacket(String filehash)
    {
        this.filehash = filehash;
    }
}
