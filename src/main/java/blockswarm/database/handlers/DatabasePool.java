package blockswarm.database.handlers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author cal
 */
public class DatabasePool
{
    PriorityBlockingQueue queue = new PriorityBlockingQueue();
    ThreadPoolExecutor databasePool = new ThreadPoolExecutor(10, 20, 0, TimeUnit.DAYS, queue);
    
    public void addBlock(BlockWorker blockWorker)
    {
        databasePool.execute(blockWorker);
    }
}
