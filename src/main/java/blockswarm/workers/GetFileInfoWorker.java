package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.packets.FileInfoPacket;
import java.util.HashMap;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class GetFileInfoWorker extends Worker implements Runnable
{
    final PeerAddress requester;
    Node node;

    public GetFileInfoWorker(PeerAddress requester, Node node)
    {
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
        HashMap<String, NodeFileInfo> all = node.getDatabase().getFiles().getAllFileInfo();
        node.send(requester, new FileInfoPacket(all));
    }
}
