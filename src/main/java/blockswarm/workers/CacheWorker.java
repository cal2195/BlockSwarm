package blockswarm.workers;

import blockswarm.network.cluster.Node;

/**
 *
 * @author cal
 */
public class CacheWorker extends Worker implements Runnable
{
    Node node;

    public CacheWorker(Node node)
    {
        this.node = node;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void run()
    {
        try
        {
            for (String hash : node.getDatabase().getFiles().getAllFileHashes())
            {
                node.getCluster().cache(hash);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
