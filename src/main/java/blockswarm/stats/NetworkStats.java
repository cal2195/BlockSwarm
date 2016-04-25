package blockswarm.stats;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;

/**
 *
 * @author cal
 */
public class NetworkStats
{
    int sampleSize = 10;
    HashMap<String, ArrayList<BlockTimer>> sent = new HashMap<>();
    HashMap<String, ArrayList<BlockTimer>> received = new HashMap<>();
    
    public double totalSent()
    {
//        int total = 0;
//        for (ArrayList<BlockTimer> list : sent.values())
//        {
//            total += list.size();
//        }
//        return (double) total / (double) sampleSize;
        return 0;
    }
    
    public double totalReceived()
    {
//        int total = 0;
//        for (ArrayList<BlockTimer> list : received.values())
//        {
//            total += list.size();
//        }
//        return (double) total / (double) sampleSize;
        return 0;
    }
    
    public void blockSent(String hash)
    {
//        ArrayList<BlockTimer> blocks = sent.get(hash);
//        if (blocks == null)
//        {
//            blocks = new ArrayList<>();
//            sent.put(hash, blocks);
//        }
//        blocks.add(new BlockTimer(blocks));
    }
    
    public double blocksSent(String hash)
    {
//        ArrayList<BlockTimer> blocks = sent.get(hash);
//        if (blocks == null)
//        {
//            blocks = new ArrayList<>();
//            sent.put(hash, blocks);
//        }
//        return (double) blocks.size() / (double) sampleSize;
        return 0;
    }
    
    public void blockReceived(String hash)
    {
//        ArrayList<BlockTimer> blocks = received.get(hash);
//        if (blocks == null)
//        {
//            blocks = new ArrayList<>();
//            received.put(hash, blocks);
//        }
//        blocks.add(new BlockTimer(blocks));
    }
    
    public double blocksReceived(String hash)
    {
//        ArrayList<BlockTimer> blocks = received.get(hash);
//        if (blocks == null)
//        {
//            blocks = new ArrayList<>();
//            received.put(hash, blocks);
//        }
//        return (double) blocks.size() / (double) sampleSize;
        return 0;
    }
    
    private class BlockTimer
    {
        public BlockTimer(ArrayList<BlockTimer> list)
        {
            new Timer(sampleSize * 1000, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    list.remove(BlockTimer.this);
                    
                }
            }).start();
        }
    }
}
