package blockswarm.workers;

import blockswarm.network.cluster.PeerAddress;
import blockswarm.network.packets.BlockPacket;


/**
 *
 * @author cal
 */
public class SendBlockWorker extends Worker implements Runnable
{

    final String filehash;
    final int blockID;
    final PeerAddress requester;
    final RequestWorker worker;
    int priority = 0;

    public SendBlockWorker(String filehash, int blockID, PeerAddress requester, RequestWorker worker)
    {
        this.filehash = filehash;
        this.blockID = blockID;
        this.requester = requester;
        this.worker = worker;
    }

    @Override
    public void run()
    {
//        if (worker.nodeFileInfo.blocks.get(blockID))
//        {
//            byte[] block = worker.node.getDatabase().getCache().getBlock(filehash, blockID);
//            worker.node.send(requester, new BlockPacket(filehash, blockID, block)).addListener(new BaseFutureAdapter<FutureDirect>() {
//                @Override
//                public void operationComplete(FutureDirect f) throws Exception
//                {
//                    if (f.isSuccess())
//                    {
//                        worker.nodeFileInfo.blocks.clear(blockID);
//                    }
//                }
//            });
//        }
    }
    
    public SendBlockWorker setPriority(int priority)
    {
        this.priority = priority;
        return this;
    }

    @Override
    public int getPriority()
    {
        return priority;
    }
}
