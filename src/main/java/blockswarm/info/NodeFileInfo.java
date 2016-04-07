package blockswarm.info;

import java.util.BitSet;

/**
 *
 * @author cal
 */
public class NodeFileInfo
{

    public final String hash;
    public final BitSet blocks;

    public NodeFileInfo(String hash)
    {
        this.hash = hash;
        blocks = new BitSet();
    }
}
