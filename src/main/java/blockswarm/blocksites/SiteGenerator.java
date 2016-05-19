package blockswarm.blocksites;

import blockswarm.files.FileHandler;
import blockswarm.network.cluster.Node;
import blockswarm.network.packets.BlockSitePacket;
import blockswarm.signatures.SignatureRSA;
import java.io.File;
import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

/**
 *
 * @author cal
 */
public class SiteGenerator
{

    Node node;

    public SiteGenerator(Node node)
    {
        this.node = node;
    }

    public void uploadSite(String rootFolder, String domain)
    {
        try
        {
            new File(".sites").mkdir();
            ZipFile zipFile = new ZipFile(".sites/tmp.zip");
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setIncludeRootFolder(false);
            zipFile.createZipFileFromFolder(rootFolder, zipParameters, false, 0);
            String hash = FileHandler.hashFile(new File(".sites/tmp.zip"), "SHA1");
            File newFile = new File(".sites/" + hash + ".bsite");
            new File(".sites/tmp.zip").renameTo(newFile);

            new FileHandler(node).uploadFile(newFile, "blocksite");

            KeyPair keys = SignatureRSA.loadKeyPair(node.getDatabase().getSettings().get("keyStoreLocation", ".keys"));

            if (keys == null)
            {
                keys = SignatureRSA.generateKeyPair();
                SignatureRSA.saveKeyPair(node.getDatabase().getSettings().get("keyStoreLocation", ".keys"), keys);
            }

            int version = node.getDatabase().getSites().getVersion(domain) + 1;
            byte[] signature = SignatureRSA.sign((domain + ":" + hash + ":" + version).getBytes(), keys.getPrivate().getEncoded());
            
            node.getDatabase().getSites().addSite(domain, hash, version, signature, keys.getPublic().getEncoded());
            broadcastSite(domain);
        } catch (ZipException ex)
        {
            Logger.getLogger(SiteGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void broadcastSite(String domain)
    {
        BlockSitePacket blockSitePacket = node.getDatabase().getSites().getBlockSitePacket(domain);
        node.getCluster().notifyAllOfNewSite(blockSitePacket);
    }
}
