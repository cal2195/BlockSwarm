package blockswarm.database.handlers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author cal
 */
public class DatabasePool
{
    ExecutorService databasePool = Executors.newFixedThreadPool(5);
    
    public void addBlock(BlockWorker blockWorker)
    {
        databasePool.execute(blockWorker);
    }
}
