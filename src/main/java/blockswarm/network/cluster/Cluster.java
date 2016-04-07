package blockswarm.network.cluster;

import blockswarm.database.entries.FileEntry;
import blockswarm.network.packets.FileListPacket;
import java.util.ArrayList;
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
    }

    public void notifyAllOfNewFiles(ArrayList<FileEntry> files)
    {
        FileListPacket filePacket = new FileListPacket(files);
        for (PeerAddress pa : node.peer.peerBean().peerMap().all())
        {
            node.send(pa, filePacket);
        }
    }
}
