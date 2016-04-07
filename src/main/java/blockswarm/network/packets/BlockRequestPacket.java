package blockswarm.network.packets;

import blockswarm.info.NodeFileInfo;
import java.io.Serializable;

/**
 *
 * @author cal
 */
public class BlockRequestPacket implements Serializable
{

    public final NodeFileInfo nodeFileInfo;

    public BlockRequestPacket(NodeFileInfo nodeFileInfo)
    {
        this.nodeFileInfo = nodeFileInfo;
    }
}
