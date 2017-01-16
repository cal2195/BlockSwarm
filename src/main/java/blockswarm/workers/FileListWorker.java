/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.workers;

import blockswarm.database.entries.FileEntry;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.PeerAddress;
import blockswarm.network.packets.FileListPacket;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class FileListWorker extends Worker implements Runnable
{
    final ArrayList<String> ignore;
    final PeerAddress requester;
    Node node;

    public FileListWorker(ArrayList<String> ignore, PeerAddress requester, Node node)
    {
        this.ignore = ignore;
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
        ArrayList<FileEntry> files = node.getDatabase().getFiles().getAllFiles(ignore);
        node.send(requester, new FileListPacket(files));
    }
}
