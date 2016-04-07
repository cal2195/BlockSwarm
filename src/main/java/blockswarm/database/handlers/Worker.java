package blockswarm.database.handlers;

/**
 *
 * @author cal
 */
public abstract class Worker implements Comparable<Worker>, Runnable
{
    
    public abstract int getPriority();
    
    @Override
    public int compareTo(Worker o)
    {
        return getPriority() - o.getPriority();
    }
}
