package blockswarm.info;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author cal
 */
public class ClusterFileInfo implements Comparable<ClusterFileInfo>
{

    Random random = new Random();
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

    public int getTotalBlocks()
    {
        return blockCount.length;
    }

    public int getBlockCount(int block)
    {
        return blockCount[block];
    }

    public int getTotalBlocksOf(int count)
    {
        int total = 0;
        for (int i = 0; i < blockCount.length; i++)
        {
            if (blockCount[i] == count)
            {
                total++;
            }
        }
        return total;
    }

    public int getLeastAvailableBlockCount()
    {
        int least = Integer.MAX_VALUE;
        for (int i = 0; i < blockCount.length; i++)
        {
            if (blockCount[i] < least)
            {
                least = blockCount[i];
            }
        }
        return least;
    }

    public double getAvailability()
    {
        int least = getLeastAvailableBlockCount();
        return least + ((double) (blockCount.length - getTotalBlocksOf(least)) / blockCount.length);
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
        if (limit == 0)
        {
            limit = 1;
        }
        BitSet blocks = new BitSet(blockCount.length);
        int found = 0;
        int checked = 0;
        for (int i = random.nextInt(blockCount.length); checked++ < blockCount.length && found < limit; i = (i + 1) % blockCount.length)
        {
            if (blockCount[i] != 0 && blockCount[i] < minimum)
            {
                blocks.set(i);
                found++;
            }
        }
        return new NodeFileInfo(hash, blocks);
    }

    @Override
    public String toString()
    {
        return "ClusterFileInfo{" + "hash=" + hash + ", blockCount=" + Arrays.toString(blockCount) + '}';
    }

    @Override
    public int compareTo(ClusterFileInfo o)
    {
        return (int) (getAvailability() * 100 - o.getAvailability() * 100);
    }
}
