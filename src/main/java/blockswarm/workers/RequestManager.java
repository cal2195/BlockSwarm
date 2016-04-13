package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class RequestManager extends Worker implements Runnable
{

    Node node;

    public RequestManager(Node node)
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
        if (node.getDatabase().getCache().cacheSize() < Integer.parseInt(node.getDatabase().getSettings().get("cacheLimit", "200")))
        {
            for (String hash : node.getDatabase().getFiles().getAllFileHashes())
            {
                node.getCluster().cache(new NodeFileInfo(hash));
            }
        } else
        {
            ArrayList<NodeFileInfo> toDownload = node.getDatabase().getDownloads().getAllDownloads();
            for (NodeFileInfo download : toDownload)
            {
                if (node.getDatabase().getFiles().hasFullFile(download.hash))
                {
                    node.getWorkerPool().addWorker(new FileAssemblyWorker(download.hash, node));
                } else
                {
                    node.getCluster().downloadFile(download.hash);
                }
            }
        }
    }
}
