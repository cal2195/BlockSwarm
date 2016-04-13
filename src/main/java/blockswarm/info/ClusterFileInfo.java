package blockswarm.info;

import java.util.BitSet;

/**
 *
 * @author cal
 */
public class ClusterFileInfo
{

    public final String hash;
    int[] blockCount;

    public ClusterFileInfo(String hash, int totalBlocks)
    {
        this.hash = hash;
        this.blockCount = new int[totalBlocks];
    }

    public void add(NodeFileInfo info)
    {
        for (int i = info.blocks.nextSetBit(0); i >= 0; i = info.blocks.nextSetBit(i + 1))
        {
            blockCount[i]++;
        }
    }

    public int getBlockCount(int block)
    {
        return blockCount[block];
    }

    public NodeFileInfo getBlocksUnder(int minimum)
    {
        BitSet blocks = new BitSet(blockCount.length);
        for (int i = 0; i < blockCount.length; i++)
        {
            if (blockCount[i] != 0 && blockCount[i] < minimum)
            {
                blocks.set(i);
            }
        }
        return new NodeFileInfo(hash, blocks);
    }

    public NodeFileInfo getBlocksUnder(int minimum, int limit)
    {
        BitSet blocks = new BitSet(blockCount.length);
        int found = 0;
        for (int i = 0; i < blockCount.length && found++ < limit; i++)
        {
            if (blockCount[i] != 0 && blockCount[i] < minimum)
            {
                blocks.set(i);
            }
        }
        return new NodeFileInfo(hash, blocks);
    }
}
