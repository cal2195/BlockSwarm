/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database.handlers;

import blockswarm.database.entries.FileEntry;
import blockswarm.network.cluster.Node;
import java.util.ArrayList;

/**
 *
 * @author cal
 */
public class FileEntryWorker extends Worker implements Runnable
{
    final ArrayList<FileEntry> files;
    Node node;

    public FileEntryWorker(ArrayList<FileEntry> files, Node node)
    {
        this.files = files;
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
        node.getDatabase().getFiles().putAllFiles(files);
    }
}
