package blockswarm.network.packets

import java.io.Serializable
import java.util.ArrayList

/**
 *
 * @author cal
 */
class BlockSiteCollectionPacket(val sites: ArrayList<BlockSitePacket>) : Serializable
