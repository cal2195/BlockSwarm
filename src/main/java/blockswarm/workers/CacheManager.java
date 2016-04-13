package blockswarm.workers;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.ClusterFileInfo;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class CacheManager extends Worker implements Runnable
{

    Node node;

    public CacheManager(Node node)
    {
        this.node = node;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    public HashMap<PeerAddress, NodeFileInfo> getCacheRequests()
    {
        HashMap<PeerAddress, NodeFileInfo> requests = new HashMap<>();
        ArrayList<FileEntry> files = node.getDatabase().getFiles().getAllFiles();
        for (FileEntry file : files)
        {
            ClusterFileInfo clusterInfo = node.getDatabase().getPeers().getClusterFileInfo(file.filehash);
            NodeFileInfo toCache = clusterInfo.getBlocksUnder(2, 5);
            requests.putAll(node.getDatabase().getPeers().getDownload(toCache));
        }
        return requests;
    }

    @Override
    public void run()
    {
        node.getCluster().askAboutAllFiles();
        if (node.getDatabase().getCache().cacheSize() < Integer.parseInt(node.getDatabase().getSettings().get("cacheLimit", "2000")))
        {
            node.getCluster().sendRequests(getCacheRequests());
        }
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
    private static final Logger LOG = Logger.getLogger(CacheManager.class.getName());
}
