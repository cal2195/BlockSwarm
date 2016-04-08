package blockswarm.network.packets;

import blockswarm.info.NodeFileInfo;
import java.io.Serializable;

/**
 *
 * @author cal
 */
public class FileInfoPacket implements Serializable
{
    public final NodeFileInfo info;

    public FileInfoPacket(NodeFileInfo info)
    {
        this.info = info;
    }
}
