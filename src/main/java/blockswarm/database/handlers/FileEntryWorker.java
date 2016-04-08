/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database.handlers;

import blockswarm.database.entries.FileEntry;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.packets.BlockRequestPacket;
import blockswarm.network.packets.FileListPacket;
import java.util.ArrayList;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class FileEntryWorker extends Worker implements Runnable
{

    final ArrayList<FileEntry> files;
    Node node;
    PeerAddress pa;

    public FileEntryWorker(ArrayList<FileEntry> files, Node node, PeerAddress pa)
    {
        this.files = files;
        this.node = node;
        this.pa = pa;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void run()
    {
        node.getDatabase().getFiles().putAllFiles(files);
        node.getGui().updateFileList();
//        NodeFileInfo file = node.getDatabase().getFiles().getFileInfo(files.get(0).filehash);
//        file.blocks.flip(0, files.get(0).totalBlocks);
//        node.send(pa, new BlockRequestPacket(file));
    }
}
