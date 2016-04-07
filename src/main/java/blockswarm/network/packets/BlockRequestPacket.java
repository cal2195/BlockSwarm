package blockswarm.network.packets;

import blockswarm.info.NodeFileInfo;

/**
 *
 * @author cal
 */
public class BlockRequestPacket
{

    public final NodeFileInfo nodeFileInfo;

    public BlockRequestPacket(NodeFileInfo nodeFileInfo)
    {
        this.nodeFileInfo = nodeFileInfo;
    }
}
