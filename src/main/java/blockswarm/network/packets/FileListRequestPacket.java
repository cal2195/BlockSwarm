package blockswarm.network.packets;

import java.util.ArrayList;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class FileListRequestPacket
{
    public final ArrayList<String> ignore;
    public final PeerAddress requester;

    public FileListRequestPacket(ArrayList<String> ignore, PeerAddress requester)
    {
        this.ignore = ignore;
        this.requester = requester;
    }
}
