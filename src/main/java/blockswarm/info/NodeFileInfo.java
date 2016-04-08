package blockswarm.info;

import java.io.Serializable;
import java.util.BitSet;

/**
 *
 * @author cal
 */
public class NodeFileInfo implements Serializable
{

    public final String hash;
    public final BitSet blocks;

    public NodeFileInfo(String hash)
    {
        this.hash = hash;
        blocks = new BitSet();
    }
    
    public NodeFileInfo(String hash, int totalBlocks)
    {
        this.hash = hash;
        blocks = new BitSet(totalBlocks);
    }
}
