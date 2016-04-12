package blockswarm.workers;

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
    public PriorityBlockingQueue queue = new PriorityBlockingQueue();
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 0, TimeUnit.DAYS, queue);
    public ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(10);
    
    public void addWorker(Worker worker)
    {
        threadPool.execute(worker);
    }
    
    public void addDelayedWorker(Worker worker, long delay)
    {
        worker.setFuture(scheduledThreadPool.schedule(worker, delay, TimeUnit.SECONDS));
    }
    
    public void addRepeatedWorker(Worker worker, long delay)
    {
        worker.setFuture(scheduledThreadPool.scheduleAtFixedRate(worker, 0, delay, TimeUnit.SECONDS));
    }
    
    public void shutdown()
    {
        threadPool.shutdownNow();
        scheduledThreadPool.shutdownNow();
    }
}
