package blockswarm.network.packets;

import blockswarm.info.NodeFileInfo;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author cal
 */
public class FileInfoPacket implements Serializable
{
    public final HashMap<String, NodeFileInfo> info;

    public FileInfoPacket(HashMap<String, NodeFileInfo> info)
    {
        this.info = info;
    }
}
