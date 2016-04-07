package blockswarm.files;

import blockswarm.database.entries.FileEntry;
import blockswarm.files.blocks.Blocker;
import blockswarm.network.cluster.Node;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class FileHandler
{

    Node node;

    public FileHandler(Node node)
    {
        this.node = node;
    }

    public void uploadFile(File file)
    {
        try
        {
            String hash = hashFile(file, "SHA-1");
            
            int totalBlocks = Blocker.insertBlocks(file, hash, node.getDatabase());
            
            FileEntry fileEntry = new FileEntry(hash, file.getName(), totalBlocks);
            node.getDatabase().getFiles().putFile(fileEntry);
            
        } catch (IOException ex)
        {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String hashFile(File file, String algorithm)
    {
        try (FileInputStream inputStream = new FileInputStream(file))
        {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1)
            {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | IOException ex)
        {
            System.out.println("Could not generate hash from file");
        }
        return "";
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++)
        {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
