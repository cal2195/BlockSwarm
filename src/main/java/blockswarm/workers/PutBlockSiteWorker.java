package blockswarm.workers;

import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.supernode.SuperNode;
import blockswarm.network.packets.BlockSitePacket;
import blockswarm.signatures.SignatureRSA;
import java.util.HashMap;
import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author cal
 */
public class PutBlockSiteWorker extends Worker implements Runnable
{

    Node node;
    BlockSitePacket packet;

    public PutBlockSiteWorker(BlockSitePacket packet, Node node)
    {
        this.packet = packet;
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
        int version = node.getDatabase().getSites().getVersion(packet.domain);
        byte[] publicKey = node.getDatabase().getSites().getPublicKey(packet.domain);
        if (version < packet.version)
        {
            if (SignatureRSA.verify(packet.getData(), packet.signature, packet.publicKey))
            {
                if (version == -1)
                {
                    node.getDatabase().getSites().addSite(packet.domain, packet.filehash, packet.version, packet.signature, packet.publicKey);
                } else if (SignatureRSA.verify(packet.getData(), packet.signature, publicKey))
                {
                    node.getDatabase().getSites().addSite(packet.domain, packet.filehash, packet.version, packet.signature, publicKey);
                }
            }
        }
    }
}
