package blockswarm.database.handlers;

import blockswarm.database.Database;
import blockswarm.info.NodeFileInfo;

/**
 *
 * @author cal
 */
public class RequestWorker extends Worker implements Runnable
{
    final NodeFileInfo nodeFileInfo;
    Database database;

    public RequestWorker(NodeFileInfo nodeFileInfo, Database database)
    {
        this.nodeFileInfo = nodeFileInfo;
        this.database = database;
    }
    
    @Override
    public void run()
    {
        NodeFileInfo myBlocks = database.getCache().getFileInfo(nodeFileInfo.hash);
        myBlocks.blocks.andNot(nodeFileInfo.blocks);
    }

    @Override
    public int getPriority()
    {
        return 0;
    }
}
