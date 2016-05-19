package blockswarm.network.packets;

import java.io.Serializable;
import java.util.ArrayList;

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
