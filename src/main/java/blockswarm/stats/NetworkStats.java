package blockswarm.stats;

import java.awt.event.ActionEvent;
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
    
    public void blockSent(String hash)
    {
        ArrayList<BlockTimer> blocks = sent.get(hash);
        if (blocks == null)
        {
            blocks = new ArrayList<>();
            sent.put(hash, blocks);
        }
        blocks.add(new BlockTimer(blocks));
    }
    
    public double blocksSent(String hash)
    {
        ArrayList<BlockTimer> blocks = sent.get(hash);
        if (blocks == null)
        {
            blocks = new ArrayList<>();
            sent.put(hash, blocks);
        }
        return (double) blocks.size() / (double) sampleSize;
    }
    
    public void blockReceived(String hash)
    {
        ArrayList<BlockTimer> blocks = received.get(hash);
        if (blocks == null)
        {
            blocks = new ArrayList<>();
            received.put(hash, blocks);
        }
        blocks.add(new BlockTimer(blocks));
    }
    
    public double blocksReceived(String hash)
    {
        ArrayList<BlockTimer> blocks = received.get(hash);
        if (blocks == null)
        {
            blocks = new ArrayList<>();
            received.put(hash, blocks);
        }
        return (double) blocks.size() / (double) sampleSize;
    }
    
    private class BlockTimer
    {
        public BlockTimer(ArrayList<BlockTimer> list)
        {
            new Timer(sampleSize * 1000, (ActionEvent e) ->
            {
                list.remove(BlockTimer.this);
            }).start();
        }
    }
}
