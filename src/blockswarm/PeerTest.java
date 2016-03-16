package blockswarm;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import net.tomp2p.storage.StorageDisk;

/**
 *
 * @author cal
 */
public class PeerTest
{

    Random random = new Random();

    public PeerTest()
    {
        
        createMaster();
        createPeer();
        //createPeerToCheck();
    }

    public void createMaster()
    {
        try
        {
            Random r = new Random();
            // create a peer with a random peerID, on port 4000, listening
            Peer peer = new PeerMaker(new Number160(r)).setPorts(4000).setStorage(new StorageDisk("heap2/")).makeAndListen();
            System.out.println("Master up!");
        } catch (IOException ex)
        {
            Logger.getLogger(PeerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createPeer()
    {
        try
        {
            Random r = new Random();
            // create a peer with a random peerID, on port 4000, listening
            Peer peer = new PeerMaker(new Number160(r)).setPorts(4001).setStorage(new StorageDisk("heap/")).makeAndListen();
            InetAddress address = Inet4Address.getByName("localhost");
            FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(4000).start();
            futureDiscover.awaitUninterruptibly();
            FutureBootstrap futureBootstrap = peer.bootstrap().setInetAddress(address).setPorts(4000).start();
            futureBootstrap.awaitUninterruptibly();
            System.out.println("Peer looks good!");

            Number160 nr = new Number160(0x123);
            for (int i = 0; i < 100; i++)
            {
                Data data = new Data(new byte[1024*1024]);
                FutureDHT futureDHT = peer.put(nr).setData(new Number160(i), data).start();
                futureDHT.awaitUninterruptibly();
                System.out.println("Added data!");
            }
        } catch (IOException ex)
        {
            Logger.getLogger(PeerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createPeerToCheck()
    {
        try
        {
            Random r = new Random();
            // create a peer with a random peerID, on port 4000, listening
            Peer peer = new PeerMaker(new Number160(r)).setPorts(4002).makeAndListen();
            InetAddress address = Inet4Address.getByName("localhost");
            FutureDiscover futureDiscover = peer.discover().setInetAddress(address).setPorts(4000).start();
            futureDiscover.awaitUninterruptibly();
            FutureBootstrap futureBootstrap = peer.bootstrap().setInetAddress(address).setPorts(4000).start();
            futureBootstrap.awaitUninterruptibly();
            System.out.println("Peer looks good!");
            Number160 nr = new Number160(0x123);
            FutureDHT futureDHT = peer.get(nr).setAll().start();
            futureDHT.awaitUninterruptibly();
            System.out.println("Got data!");
            for (Data data : futureDHT.getDataMap().values())
            {
                System.out.println("" + new String(data.getData()));
            }
        } catch (IOException ex)
        {
            Logger.getLogger(PeerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
