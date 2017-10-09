package blockswarm.workers;

import blockswarm.network.cluster.Node;
import blockswarm.network.packets.BlockSitePacket;
import blockswarm.signatures.SignatureRSA;

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
        int version = node.getDatabase().getSites().getVersion(packet.getDomain());
        byte[] publicKey = node.getDatabase().getSites().getPublicKey(packet.getDomain());
        System.out.println("Processing " + packet.getDomain());
        if (version < packet.getVersion())
        {
            if (SignatureRSA.verify(packet.getData(), packet.getSignature(), packet.getPublicKey()))
            {
                if (version == -1)
                {
                    node.getDatabase().getSites().addSite(packet.getDomain(), packet.getFilehash(), packet.getVersion(), packet.getSignature(), packet.getPublicKey());
                } else if (SignatureRSA.verify(packet.getData(), packet.getSignature(), publicKey))
                {
                    node.getDatabase().getSites().addSite(packet.getDomain(), packet.getFilehash(), packet.getVersion(), packet.getSignature(), publicKey);
                }
            }
        }
    }
}
