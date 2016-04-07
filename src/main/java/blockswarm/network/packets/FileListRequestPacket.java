package blockswarm.network.packets;

import java.io.Serializable;
import java.util.ArrayList;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class FileListRequestPacket implements Serializable
{
    public final ArrayList<String> ignore;

    public FileListRequestPacket(ArrayList<String> ignore)
    {
        this.ignore = ignore;
    }
}
