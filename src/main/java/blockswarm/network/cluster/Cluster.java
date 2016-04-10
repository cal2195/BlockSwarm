package blockswarm.network.cluster;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.ClusterFileInfo;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.FileInfoRequestPacket;
import blockswarm.network.packets.FileListPacket;
import blockswarm.network.packets.FileListRequestPacket;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class Cluster
{

    Node node;

    public Cluster(Node node)
    {
        this.node = node;
        initialise();
    }

    public void initialise()
    {
        collectFileLists();
        askAboutAllFiles();
    }

    public void downloadFile(String filehash)
    {
        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
        {
            LOG.log(Level.FINE, "Asking {0} for {1}!", new Object[]
            {
                pa, filehash
            });
            NodeFileInfo info = node.getDatabase().getFiles().getFileInfo(filehash);
            info.blocks.flip(0, node.getDatabase().getFiles().getTotalBlocks(filehash));
            node.send(pa, new BlockRequestPacket(info));
        }
    }
    
    public void download(NodeFileInfo file)
    {
        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
        {
            node.send(pa, new BlockRequestPacket(file));
        }
    }
    
    public void cache(NodeFileInfo file)
    {
        ClusterFileInfo clusterInfo = node.getDatabase().getPeers().getClusterFileInfo(file.hash);
        NodeFileInfo toCache = clusterInfo.getBlocksUnder(2);
        NodeFileInfo iHave = node.getDatabase().getCache().getFileInfo(file.hash);
        toCache.blocks.andNot(iHave.blocks);
        download(toCache);
    }

    public void requestInfoAbout(String filehash)
    {
        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
        {
            LOG.log(Level.FINE, "Asking {0} about {1}!", new Object[]
            {
                pa, filehash
            });
            node.send(pa, new FileInfoRequestPacket(filehash));
        }
    }
    
    public void askAboutAllFiles()
    {
        for (FileEntry file : node.getDatabase().getFiles().getAllFiles())
        {
            requestInfoAbout(file.filehash);
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
