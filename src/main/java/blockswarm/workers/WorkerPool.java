package blockswarm.workers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author cal
 */
public class WorkerPool
{
    PriorityBlockingQueue queue = new PriorityBlockingQueue();
    ThreadPoolExecutor databasePool = new ThreadPoolExecutor(10, 20, 0, TimeUnit.DAYS, queue);
    
    public void addWorker(Worker worker)
    {
        databasePool.execute(worker);
    }
}
