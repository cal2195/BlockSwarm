package blockswarm.workers;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.packets.FileInfoPacket;
import blockswarm.network.packets.FileListPacket;
import java.util.ArrayList;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class GetFileInfoWorker extends Worker implements Runnable
{
    final String filehash;
    final PeerAddress requester;
    Node node;

    public GetFileInfoWorker(String filehash, PeerAddress requester, Node node)
    {
        this.filehash = filehash;
        this.requester = requester;
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
        NodeFileInfo info = node.getDatabase().getFiles().getFileInfo(filehash);
        node.send(requester, new FileInfoPacket(info));
    }
}
