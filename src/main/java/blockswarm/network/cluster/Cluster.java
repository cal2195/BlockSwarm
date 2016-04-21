package blockswarm.network.cluster;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.ClusterFileInfo;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.FileInfoRequestPacket;
import blockswarm.network.packets.FileListPacket;
import blockswarm.network.packets.FileListRequestPacket;
import blockswarm.workers.DownloadWorker;
import blockswarm.workers.FileAssemblyWorker;
import blockswarm.workers.RequestWorker;
import blockswarm.workers.SendBlockWorker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class Cluster
{

    Random random = new Random();
    Node node;

    public Cluster(Node node)
    {
        this.node = node;
        initialise();
    }

    public void initialise()
    {
        collectFileLists();
    }

    public void downloadFile(String filehash)
    {
        HashMap<PeerRequestKey, NodeFileInfo> peers = node.getDatabase().getPeers().getDownload(filehash);
        for (PeerRequestKey pa : peers.keySet())
        {
            LOG.fine("Asking " + pa.requester.inetAddress().getHostAddress() + " for " + peers.get(pa).blocks.toString());
            node.send(pa.requester, new BlockRequestPacket(peers.get(pa)));
        }
//        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
//        {
//            LOG.log(Level.FINE, "Asking {0} for {1}!", new Object[]
//            {
//                pa, filehash
//            });
//            NodeFileInfo info = node.getDatabase().getFiles().getFileInfo(filehash);
//            info.blocks.flip(0, node.getDatabase().getFiles().getTotalBlocks(filehash));
//            node.send(pa, new BlockRequestPacket(info));
//        }
    }

    public void queueForDownload(String filehash)
    {
        if (node.getDatabase().getFiles().hasFullFile(filehash))
        {
            node.getWorkerPool().addWorker(new FileAssemblyWorker(filehash, node));
        } else
        {
            node.getDatabase().getDownloads().queueDownload(filehash);
            //node.getWorkerPool().addWorker(new DownloadWorker(filehash, node), 60);
        }
    }

    public void download(NodeFileInfo needed)
    {
        if (needed.blocks.cardinality() > 0)
        {
            PeerAddress pa = node.peer.peerBean().peerMap().all().get(random.nextInt(node.peer.peerBean().peerMap().all().size()));
            node.send(pa, new BlockRequestPacket(needed));
        }
    }

    public void cache(NodeFileInfo file)
    {
        ClusterFileInfo clusterInfo = node.getDatabase().getPeers().getClusterFileInfo(file.hash);
        NodeFileInfo toCache = clusterInfo.getBlocksUnder(2);
        NodeFileInfo iHave = node.getDatabase().getCache().getFileInfo(file.hash);
        toCache.blocks.andNot(iHave.blocks);
        LOG.fine("Caching " + toCache.blocks.toString());
        download(toCache);
    }

    public void sendRequests(HashMap<PeerRequestKey, NodeFileInfo> requests)
    {
        for (PeerRequestKey pa : requests.keySet())
        {
            if (requests.get(pa).blocks.cardinality() > 0)
            {
                LOG.fine("Requesting: " + requests.get(pa).blocks.toString());
                node.send(pa.requester, new BlockRequestPacket(requests.get(pa)));
            }
        }
    }

    public void stopAllRequests()
    {
        for (FileEntry file : node.getDatabase().getFiles().getAllFiles())
        {
            NodeFileInfo stop = new NodeFileInfo(file.filehash);
            for (PeerAddress pa : node.peer.peerBean().peerMap().all())
            {
                node.send(pa, new BlockRequestPacket(stop));
            }
        }
    }

    public void superSeed(String filehash)
    {
        List<PeerAddress> peers = node.peer.peerBean().peerMap().all();
        for (int i = 0; i < peers.size(); i++)
        {
            PeerAddress peer = peers.get(i);
            int total = node.getDatabase().getFiles().getTotalBlocks(filehash);
            NodeFileInfo upload = new NodeFileInfo(filehash, total);
            for (int block = 0; block < total; block++)
            {
                if ((block + i) % peers.size() == 0)
                {
                    upload.blocks.set(block);
                }
            }
            node.getWorkerPool().addWorker(new RequestWorker(peer, upload, node));
        }
    }

    public void collectFileLists()
    {
        ArrayList<String> files = node.getDatabase().getFiles().getAllFileHashes();
        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
        {
            LOG.fine("Asking " + pa + " for their file list!");
            node.send(pa, new FileListRequestPacket(files));
        }
    }

    public void notifyAllOfNewFiles(ArrayList<FileEntry> files)
    {
        FileListPacket filePacket = new FileListPacket(files);
        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
        {
            LOG.fine("Telling " + pa + " about new files!");
            node.send(pa, filePacket);
        }
    }
    private static final Logger LOG = Logger.getLogger(Cluster.class.getName());
}
