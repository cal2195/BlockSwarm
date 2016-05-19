/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.workers;

import blockswarm.files.FileHandler;
import blockswarm.network.cluster.Node;
import java.io.File;

/**
 *
 * @author cal
 */
public class FileUploadWorker extends Worker implements Runnable
{

    final File toUpload;
    final String tags;
    Node node;

    public FileUploadWorker(File toUpload, String tags, Node node)
    {
        this.toUpload = toUpload;
        this.tags = tags;
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
        FileHandler fileHandler = new FileHandler(node);
        fileHandler.uploadFile(toUpload, tags);
    }
}
