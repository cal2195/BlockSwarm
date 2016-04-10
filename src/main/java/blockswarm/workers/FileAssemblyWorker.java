/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.workers;

import blockswarm.files.FileHandler;

/**
 *
 * @author cal
 */
public class FileAssemblyWorker extends Worker implements Runnable
{

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void run()
    {
//        if (node.getDatabase().getFiles().hasFullFile(blockPacket.fileHash))
//        {
//            FileHandler fileHandler = new FileHandler(node);
//            fileHandler.assembleFile(blockPacket.fileHash);
//        }
    }
}
