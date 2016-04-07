package blockswarm.network.cluster;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.tracker.FutureTracker;
import net.tomp2p.tracker.PeerBuilderTracker;
import net.tomp2p.tracker.PeerTracker;

/**
 *
 * @author cal
 */
public class Tracker
{

    PeerTracker tracker;

    public Tracker(Peer peer)
    {
        tracker = new PeerBuilderTracker(peer).verifyPeersOnTracker(true).start();
    }

    public void add(Number160 hash)
    {
        FutureTracker futureTracker = tracker.addTracker(hash).start().awaitUninterruptibly();
        System.out.println("added myself to the tracker with location [" + hash + "]: " + futureTracker.isSuccess() + " I'm: " + tracker.peerAddress());
    }

    public void get(Number160 hash)
    {
        FutureTracker futureTracker = tracker.getTracker(hash).start().awaitUninterruptibly();
        System.out.println("got this: " + futureTracker.trackers());
        System.out.println("currently stored on: " + futureTracker.trackerPeers());
    }
}
