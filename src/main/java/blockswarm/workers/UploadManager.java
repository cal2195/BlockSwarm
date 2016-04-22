package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;

/**
 *
 * @author cal
 */
public class UploadManager extends Worker implements Runnable
{
    Node node;

    public UploadManager(Node node)
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
            for (String filehash : node.getDatabase().getUploads().getAllUploads())
            {
                if (node.getDatabase().getPeers().getAvailability(filehash) > 1)
                {
                    node.getDatabase().getUploads().removeUpload(filehash);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
