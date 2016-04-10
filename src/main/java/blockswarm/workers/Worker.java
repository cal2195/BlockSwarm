package blockswarm.workers;

import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author cal
 */
public abstract class Worker implements Comparable<Worker>, Runnable
{
    ScheduledFuture<?> future;
    public abstract int getPriority();
    
    @Override
    public int compareTo(Worker o)
    {
        return getPriority() - o.getPriority();
    }
    
    public void setFuture(ScheduledFuture<?> future)
    {
        this.future = future;
    }
}
