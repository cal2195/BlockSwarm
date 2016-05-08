package blockswarm.network.packets;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class BlockSiteCollectionPacket implements Serializable
{
    public final ArrayList<BlockSitePacket> sites;

    public BlockSiteCollectionPacket(ArrayList<BlockSitePacket> sites)
    {
        this.sites = sites;
    }
}
