package blockswarm.workers;

import blockswarm.network.cluster.Node;

/**
 *
 * @author cal
 */
public class GUIWorker extends Worker implements Runnable
{
    Node node;

    public GUIWorker(Node node)
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
            node.getGui().updateFileList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
