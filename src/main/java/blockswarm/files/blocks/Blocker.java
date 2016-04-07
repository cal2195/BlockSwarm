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

    public static int insertBlocks(File f, String hash, Database database) throws IOException
    {
        int sizeOfFiles = 1024 * 1024; // 1MB
        byte[] buffer = new byte[sizeOfFiles];

        

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f)))
        {   //try-with-resources to ensure closing stream

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
            return block;
        }
    }
}
