/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.workers;

import blockswarm.network.cluster.Node;

/**
 *
 * @author cal
 */
public class FileListUpdater extends Worker implements Runnable
{
    Node node;

    public FileListUpdater(Node node)
    {
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
        try
        {
            node.getCluster().collectFileLists();
            node.getCluster().collectBlockSites();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
