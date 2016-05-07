package blockswarm.blocksites;

import blockswarm.files.FileHandler;
import blockswarm.network.cluster.Node;
import java.io.File;
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
            
            new FileHandler(node).uploadFile(newFile);
            
            node.getDatabase().getSites().addSite(domain, hash);
        } catch (ZipException ex)
        {
            Logger.getLogger(SiteGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
