package blockswarm.workers;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.ClusterFileInfo;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.PeerRequestKey;
import java.util.ArrayList;
import java.util.Collections;
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

    public HashMap<PeerRequestKey, NodeFileInfo> getCacheRequests()
    {
        HashMap<PeerRequestKey, NodeFileInfo> requests = new HashMap<>();
        ArrayList<FileEntry> files = node.getDatabase().getFiles().getAllFiles();
        int totalBlocks = 0;
        Collections.shuffle(files);
        for (FileEntry file : files)
        {
            ClusterFileInfo clusterInfo = node.getDatabase().getPeers().getClusterFileInfo(file.filehash);
            NodeFileInfo toCache = clusterInfo.getBlocksUnder(2, file.totalBlocks / 4);
            HashMap<PeerRequestKey, NodeFileInfo> toDownload = node.getDatabase().getPeers().getDownload(toCache);
            for (NodeFileInfo info : toDownload.values())
            {
                totalBlocks += info.blocks.cardinality();
            }
            requests.putAll(toDownload);
            if (totalBlocks > 50)
            {
                break;
            }
        }
        return requests;
    }

    @Override
    public void run()
    {
        try
        {
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
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static final Logger LOG = Logger.getLogger(CacheManager.class.getName());
}
