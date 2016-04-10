package blockswarm.workers;

import blockswarm.network.cluster.Node;

/**
 *
 * @author cal
 */
public class DownloadWorker extends Worker implements Runnable
{

    final String filehash;
    Node node;

    public DownloadWorker(String filehash, Node node)
    {
        this.filehash = filehash;
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
            if (node.getDatabase().getDownloads().flaggedForDownload(filehash))
            {
                node.getCluster().downloadFile(filehash);
            } else
            {
                future.cancel(true);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
