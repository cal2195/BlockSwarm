/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.workers;

import blockswarm.files.FileHandler;
import blockswarm.network.cluster.Node;

/**
 *
 * @author cal
 */
public class FileAssemblyWorker extends Worker implements Runnable
{
    final String filehash;
    Node node;

    public FileAssemblyWorker(String filehash, Node node)
    {
        this.filehash = filehash;
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
        if (node.getDatabase().getFiles().hasFullFile(filehash))
        {
            node.getDatabase().getDownloads().removeDownload(filehash);
            FileHandler fileHandler = new FileHandler(node);
            fileHandler.assembleFile(filehash);
        }
    }
}
