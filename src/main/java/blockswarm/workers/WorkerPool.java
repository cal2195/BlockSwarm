package blockswarm.workers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author cal
 */
public class WorkerPool
{
    PriorityBlockingQueue queue = new PriorityBlockingQueue();
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20, 0, TimeUnit.DAYS, queue);
    ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(10);
    
    public void addWorker(Worker worker)
    {
        threadPool.execute(worker);
    }
    
    public void addWorker(Worker worker, long delay)
    {
        scheduledThreadPool.scheduleAtFixedRate(worker, 0, delay, TimeUnit.SECONDS);
    }
}
