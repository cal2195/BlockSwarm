package blockswarm.files.blocks;

import blockswarm.database.Database;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cal
 */
public class Blocker
{

    public static void mergeFiles(List<File> files, File into) throws IOException
    {
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(new FileOutputStream(into)))
        {
            for (File f : files)
            {
                Files.copy(f.toPath(), mergingStream);
            }
        }
    }

    public static List<File> listOfFilesToMerge(File oneOfFiles)
    {
        String tmpName = oneOfFiles.getName();//{name}.{number}
        String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));//remove .{number}
        File[] files = oneOfFiles.getParentFile().listFiles((File dir, String name) -> name.matches(destFileName + "[.]\\d+"));
        Arrays.sort(files);//ensuring order 001, 002, ..., 010, ...
        return Arrays.asList(files);
    }

    public static void insertBlocks(File f, Database database) throws IOException
    {
        int sizeOfFiles = 1024 * 1024; // 1MB
        byte[] buffer = new byte[sizeOfFiles];

        String hash = hashFile(f, "SHA-1");

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f)))
        {   //try-with-resources to ensure closing stream
            String name = f.getName();

            int tmp = 0, block = 0;
            while ((tmp = bis.read(buffer)) > 0)
            {
                //write each chunk of data into separate file with different number in name
//                File newFile = new File("heap/", hash + "." + String.format("%03d", partCounter++));
//                try (FileOutputStream out = new FileOutputStream(newFile))
//                {
//                    out.write(buffer, 0, tmp);//tmp is chunk size
//                }
                database.getCache().putBlock(hash, block++, buffer);
            }
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
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
